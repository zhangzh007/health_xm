package com.itnoob.dao;

import com.github.pagehelper.Page;
import com.itnoob.pojo.CheckGroup;
import com.itnoob.pojo.Setmeal;

import java.util.List;
import java.util.Map;

public interface SetMealDao {

    void add(Setmeal setmeal);

    void setSetmealAndCheckGroup(Map<String, Integer> param);

    Page<Setmeal> findPageByCondition(String queryString);

    Setmeal findById(Integer id);

    Setmeal findDetails(Integer id);

    List<Integer> findCheckGroupIdsBySetMealId(Integer id);

    void deleteAssociation(Integer id);

    void edit(Setmeal setmeal);

    void deleteById(Integer id);

    List<Setmeal> findAll();

    List<Map<String, Object>> findSetmealCount();

}
