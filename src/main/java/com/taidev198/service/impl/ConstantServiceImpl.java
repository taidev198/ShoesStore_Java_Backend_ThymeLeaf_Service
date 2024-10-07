package com.taidev198.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taidev198.model.Constant;
import com.taidev198.repository.ConstantRepository;
import com.taidev198.service.ConstantService;

@Service
public class ConstantServiceImpl implements ConstantService {
    @Autowired
    private ConstantRepository constantRepository;

    @Override
    public List<Constant> getListConstantsByType(String type) {
        return constantRepository.findByType(type);
    }
}
