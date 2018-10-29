package com.maybe.work.service.impl;

import com.maybe.work.model.Furlough;
import com.maybe.work.repository.FurloughRepository;
import com.maybe.work.service.IFurloughService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FurloughServiceImpl implements IFurloughService {

    @Autowired
    private FurloughRepository furloughRepository;

    @Transactional
    public void save() {
        Furlough furlough = new Furlough();
        furloughRepository.save(furlough);
    }
}
