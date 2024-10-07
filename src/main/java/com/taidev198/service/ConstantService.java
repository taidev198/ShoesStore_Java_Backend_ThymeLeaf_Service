package com.taidev198.service;

import java.util.List;

import com.taidev198.model.Constant;

public interface ConstantService {
    List<Constant> getListConstantsByType(String type);
}
