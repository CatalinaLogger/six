package com.maybe.flow.service.impl;

import com.maybe.flow.common.param.FlowParam;
import com.maybe.flow.dao.FlowDefineMapper;
import com.maybe.flow.dao.FlowMineMapper;
import com.maybe.flow.dao.FlowTaskMapper;
import com.maybe.flow.model.FlowDefine;
import com.maybe.flow.model.FlowMine;
import com.maybe.flow.model.FlowTask;
import com.maybe.flow.service.IFlowService;
import com.maybe.sys.common.config.ResultEnum;
import com.maybe.sys.common.dto.PageDto;
import com.maybe.sys.common.exception.SixException;
import com.maybe.sys.common.param.PageParam;
import com.maybe.sys.common.util.SessionLocal;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.RepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipInputStream;

@Slf4j
@Service
public class FlowServiceImpl implements IFlowService {

    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private FlowDefineMapper flowDefineMapper;
    @Autowired
    private FlowTaskMapper flowTaskMapper;
    @Autowired
    private FlowMineMapper flowMineMapper;
    @Autowired
    private AsyncServiceNotice serviceNotice;
    @Resource
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 部署流程
     * @param name 流程名称
     * @param category 流程分类
     * @param file 流程定义文件
     */
    @Override
    @Transactional
    public void deploy(String name, String category, MultipartFile file) {
        if(file.isEmpty()) {
            throw new SixException(ResultEnum.ERROR_PARAM.getCode(), "流程定义文件不能为空");
        } else {
            if(ObjectUtils.isEmpty(name)) {
                throw new SixException(ResultEnum.ERROR_PARAM.getCode(), "流程名称不能为空");
            }
            if(ObjectUtils.isEmpty(category)) {
                throw new SixException(ResultEnum.ERROR_PARAM.getCode(), "流程分类不能为空");
            }
            String filename = file.getOriginalFilename();
            System.out.println(filename);
            String type = filename.indexOf(".") != -1 ? filename.substring(filename.lastIndexOf(".") + 1, filename.length()) : null;
            if (type != null) {
                try {
                    if (type.endsWith("zip")) {
                        ZipInputStream zs=new ZipInputStream(file.getInputStream());
                        repositoryService
                                .createDeployment()
                                .name(name)
                                .category(category)
                                .addZipInputStream(zs)
                                .deploy();

                    } else if(type.endsWith("bpmn20") || type.endsWith("bpmn") || type.endsWith("xml")) {
                        InputStream inputStream=file.getInputStream();
                        repositoryService.createDeployment()
                                .name(name)
                                .category(category)
                                .addInputStream(filename, inputStream)
                                .deploy();
                    } else {
                        throw new SixException(ResultEnum.ERROR_PARAM.getCode(), "流程定义文件格式出错");
                    }
                } catch (Exception e) {
                    log.error(e.getMessage());
                    throw new SixException(ResultEnum.EXCEPTION.getCode(), "数据库接受的数据包过大，请调整max_allowed_packet值的大小");
                }
            }
        }
    }

    @Override
    public List<FlowDefine> flowListByNameOrCategory(String name, String category, Boolean last) {
        if (ObjectUtils.isEmpty(name)) {
            name = null;
        } else {
            name = "%" + name + "%";
        }
        if (ObjectUtils.isEmpty(category)) {
            category = null;
        }
        List<FlowDefine> list = flowDefineMapper.defineListByNameAndCategory(name, category);
        if (last) {
            /*
             * Map<String,ProcessDefinition>
             * map集合的key:流程定义的key
             * map集合的value:流程定义的对象
             * 特点：当map集合key值相同的情况下，后一次的值将替换前一次的值
             */
            Map<String,FlowDefine> map = new LinkedHashMap<>();
            if(list != null && list.size() >0){
                for(FlowDefine pd : list){
                    map.put(pd.getKey(), pd);
                }
            }
            return new ArrayList<>(map.values());
        }
        return list;
    }

    @Override
    public void activate(String processId) {
        repositoryService.activateProcessDefinitionById(processId);
    }

    @Override
    public void suspend(String processId) {
        repositoryService.suspendProcessDefinitionById(processId);
    }

    @Override
    public List<FlowTask> taskListByProcessId(String processId) {
        return flowTaskMapper.taskListByProcessId(processId);
    }

    @Override
    public PageDto<FlowMine> minePage(FlowParam param, PageParam page) {
        int total = flowMineMapper.mineCountByUser(SessionLocal.getUser().getUsername(), param, page);
        if (total > 0) {
            List<FlowMine> list = flowMineMapper.minePageByUser(SessionLocal.getUser().getUsername(), param, page);
            return new PageDto<>(page.getPage(), page.getSize(), total, list);
        }
        return new PageDto<>(page.getPage(), page.getSize(), total, null);
    }

    @Override
    public FlowDefine selectByDefineId(String defineId) {
        return flowDefineMapper.selectByDefineId(defineId);
    }

    @Override
    public void pressFlow(String processId) {
        // log.info("用户登录，缓存用户信息 >> " + loginKey);
        String pressKey = SessionLocal.getUser().getId() + "-" + processId;
        if (redisTemplate.hasKey(pressKey)) {
            throw new SixException(ResultEnum.ERROR_PARAM.getCode(), "您已经催办过该流程了，请耐心等候！");
        } else {
            redisTemplate.opsForValue().set(pressKey, processId, 1, TimeUnit.HOURS);
            serviceNotice.sendNoticePress(SessionLocal.getUser().getName(), processId);
        }
    }

}
