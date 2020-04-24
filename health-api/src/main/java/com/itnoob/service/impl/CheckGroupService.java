package com.itnoob.service.impl;

import com.itnoob.entity.PageResult;
import com.itnoob.entity.QueryPageBean;
import com.itnoob.pojo.CheckGroup;
import com.itnoob.pojo.CheckItem;

import java.util.List;

public interface CheckGroupService {

    void add(CheckGroup checkGroup, Integer[] checkitemIds);

    PageResult findPage(QueryPageBean queryPageBean);

    CheckGroup findById(Integer id);

    List<Integer> findCheckItemIdsByCheckGroupId(Integer id);

    void edit(CheckGroup checkGroup,Integer[] checkitemIds);

    void delete(Integer id);

    List<CheckGroup> findAll();

}
