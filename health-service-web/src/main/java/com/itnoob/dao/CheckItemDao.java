package com.itnoob.dao;

import com.github.pagehelper.Page;
import com.itnoob.pojo.CheckItem;

import java.util.List;

public interface CheckItemDao {

    void add(CheckItem checkItem);

    // mybatis 分页插件
    Page<CheckItem> findPageByCondition(String queryString);

    Long findCountByCheckitemId(Integer id);

    void deleteById(Integer id);

    void edit(CheckItem checkItem);

    CheckItem findById(Integer id);

    List<CheckItem> findAll();

}
