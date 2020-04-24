package com.itnoob.dao;

import com.github.pagehelper.Page;
import com.itnoob.pojo.CheckGroup;
import com.itnoob.pojo.CheckItem;

import java.util.List;
import java.util.Map;

public interface CheckGroupDao {

    void add(CheckGroup checkGroup);

    void setCheckGroupAndCheckItem(Map map);

    Page<CheckGroup> findPageByCondition(String queryString);

    CheckGroup findById(Integer id);

    List<Integer> findCheckItemIdsByCheckGroupId(Integer id);

    void edit(CheckGroup checkGroup);

    void deleteAssociation(Integer id);

    void deleteById(Integer id);

    List<CheckGroup> findAll();

}
