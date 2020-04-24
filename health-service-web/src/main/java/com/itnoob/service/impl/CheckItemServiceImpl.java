package com.itnoob.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itnoob.dao.CheckItemDao;
import com.itnoob.entity.PageResult;
import com.itnoob.entity.QueryPageBean;
import com.itnoob.pojo.CheckItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service(interfaceClass = CheckItemService.class)
@Transactional
public class CheckItemServiceImpl implements CheckItemService {

    @Autowired
    private CheckItemDao checkItemDao;

    @Override
    public void add(CheckItem checkItem) {
        checkItemDao.add(checkItem);
    }

    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {

        // 获取查询条件
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();

        // 分页
        PageHelper.startPage(currentPage,pageSize);
        Page<CheckItem> page = checkItemDao.findPageByCondition(queryString);

        return new PageResult(page.getTotal(),page.getResult());
    }


    @Override
    public void deleteById(Integer id) {
        // 判断当前检查项有无关联检查组，若有则不能删除
        Long countByCheckitemId = checkItemDao.findCountByCheckitemId(id);
        if(countByCheckitemId>0){
            throw new RuntimeException();
        }
        checkItemDao.deleteById(id);
    }

    @Override
    public void edit(CheckItem checkItem) {
        checkItemDao.edit(checkItem);
    }

    @Override
    public CheckItem findById(Integer id) {
        return checkItemDao.findById(id);
    }

    @Override
    public List<CheckItem> findAll() {
        return checkItemDao.findAll();
    }
}
