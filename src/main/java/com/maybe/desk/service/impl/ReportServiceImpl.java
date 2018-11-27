package com.maybe.desk.service.impl;

import com.maybe.desk.common.dto.WeeklyLevelDto;
import com.maybe.desk.common.param.WeeklyParam;
import com.maybe.desk.dao.WeeklyMapper;
import com.maybe.desk.model.Weekly;
import com.maybe.desk.service.IReportService;
import com.maybe.sys.common.util.LevelUtil;
import com.maybe.sys.common.util.SessionLocal;
import com.maybe.sys.common.util.SixUtil;
import com.maybe.sys.dao.SysUserMapper;
import com.maybe.sys.model.SysUser;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class ReportServiceImpl implements IReportService {

    @Autowired
    private WeeklyMapper weeklyMapper;
    @Autowired
    private SysUserMapper sysUserMapper;

    @Override
    public Weekly selectBefore() {
        Calendar cal = Calendar.getInstance();
        return weeklyMapper.selectByOperateIdAndWeekAndYear(SessionLocal.getUser().getId(), cal.get(cal.WEEK_OF_YEAR) - 1, cal.get(cal.YEAR));
    }

    @Override
    public Weekly selectWeekly() {
        Calendar cal = Calendar.getInstance();
        return weeklyMapper.selectByOperateIdAndWeekAndYear(SessionLocal.getUser().getId(), cal.get(cal.WEEK_OF_YEAR), cal.get(cal.YEAR));
    }

    @Override
    public void insertWeekly(WeeklyParam param) {
        Calendar cal = Calendar.getInstance();
        Weekly weekly = weeklyMapper.selectByOperateIdAndWeekAndYear(SessionLocal.getUser().getId(), cal.get(cal.WEEK_OF_YEAR), cal.get(cal.YEAR));
        if (ObjectUtils.isEmpty(weekly)) {
            weekly = new Weekly();
        }
        BeanUtils.copyProperties(param, weekly);
        weekly.setYear(cal.get(cal.YEAR));
        weekly.setWeek(cal.get(cal.WEEK_OF_YEAR));
        SixUtil.setOperate(weekly);
        if (ObjectUtils.isEmpty(weekly.getId())) {
            weeklyMapper.insert(weekly);
        } else {
            weeklyMapper.updateByPrimaryKey(weekly);
        }
    }

    @Override
    public List<WeeklyLevelDto> mergeWeekly(Date date) {
        List<WeeklyLevelDto> rootList = new ArrayList<>();
        SysUser lead = sysUserMapper.selectByPrimaryKey(SessionLocal.getUser().getId());
        if (ObjectUtils.isEmpty(lead.getLevel())) {
            return rootList;
        }
        String rootLevel = LevelUtil.calculateLevel(lead.getLevel(), lead.getId());
        Calendar cal = Calendar.getInstance();
        if (!ObjectUtils.isEmpty(date)) {
            cal.setTime(date);
        }
        List<Weekly> weeklyList = weeklyMapper.selectByWeekAndYearAndLevelLike(cal.get(cal.WEEK_OF_YEAR), cal.get(cal.YEAR), lead.getId());
        LinkedMultiValueMap<String, WeeklyLevelDto> levelWeeklyMap = new LinkedMultiValueMap<>();
        for (Weekly weekly : weeklyList) {
            WeeklyLevelDto dto = WeeklyLevelDto.adapt(weekly);
            if (rootLevel.equals(dto.getLevel())) {
                rootList.add(dto);
            } else {
                levelWeeklyMap.add(dto.getLevel(), dto);
            }
        }
        transformWeeklyTree(rootList, rootLevel, levelWeeklyMap);
        return rootList;
    }

    private void transformWeeklyTree(List<WeeklyLevelDto> weeklyLevelList, String level, LinkedMultiValueMap<String,WeeklyLevelDto> levelWeeklyMap) {
        for (WeeklyLevelDto weeklyLevelDto: weeklyLevelList) {
            String nextLevel = LevelUtil.calculateLevel(level, weeklyLevelDto.getOperateId());
            List<WeeklyLevelDto> tempWeeklyList = levelWeeklyMap.get(nextLevel);
            if (!CollectionUtils.isEmpty(tempWeeklyList)) {
                // 设置子部门数据
                weeklyLevelDto.setChildren(tempWeeklyList);
                // 处理子部门
                transformWeeklyTree(tempWeeklyList, nextLevel, levelWeeklyMap);
            }
        }
    }
}
