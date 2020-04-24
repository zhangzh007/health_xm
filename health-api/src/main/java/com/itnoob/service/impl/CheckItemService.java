package com.itnoob.service.impl;

import com.itnoob.entity.PageResult;
import com.itnoob.entity.QueryPageBean;
import com.itnoob.pojo.CheckItem;
import java.util.List;

/**
 * 检查项服务接口
 */

public interface CheckItemService {

    void add(CheckItem checkItem);

    PageResult findPage(QueryPageBean queryPageBean);

    void deleteById(Integer id);

    void edit(CheckItem checkItem);

    CheckItem findById(Integer id);

    List<CheckItem> findAll();

}
