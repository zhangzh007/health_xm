package com.itnoob.service.impl;

import com.itnoob.entity.Result;
import com.itnoob.pojo.Order;

import java.util.Map;

public interface OrderService {

    Result order(Map map) throws Exception;

    Map findById(Integer id);

}
