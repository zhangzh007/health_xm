package com.itnoob.service.impl;

import com.itnoob.entity.PageResult;
import com.itnoob.entity.QueryPageBean;
import com.itnoob.pojo.Setmeal;

import java.util.List;
import java.util.Map;

public interface SetMealService {

    void add(Setmeal setmeal, Integer[] checkgroupIds);

    PageResult findPage(QueryPageBean queryPageBean);

    Setmeal findById(Integer id);

    Setmeal findDetails(Integer id);

    List<Integer> findCheckGroupIdsBySetMealId(Integer id);

    void edit(Setmeal setmeal,Integer[] checkgroupIds);

    void delete(Integer id);

    List<Setmeal> findAll();

    List<Map<String,Object>> findSetmealCount();

}
