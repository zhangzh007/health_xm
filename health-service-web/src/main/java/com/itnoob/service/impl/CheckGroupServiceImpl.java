package com.itnoob.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itnoob.dao.CheckGroupDao;
import com.itnoob.entity.PageResult;
import com.itnoob.entity.QueryPageBean;
import com.itnoob.pojo.CheckGroup;
import com.itnoob.pojo.CheckItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = CheckGroupService.class)
@Transactional
public class CheckGroupServiceImpl implements CheckGroupService {

    @Autowired
    private CheckGroupDao checkGroupDao;


    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {

        // 获取查询条件
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();

        // 分页
        PageHelper.startPage(currentPage,pageSize);
        Page<CheckGroup> page = checkGroupDao.findPageByCondition(queryString);

        return new PageResult(page.getTotal(),page.getResult());
    }

    @Override
    public CheckGroup findById(Integer id) {
        return checkGroupDao.findById(id);
    }

    @Override
    public List<Integer> findCheckItemIdsByCheckGroupId(Integer id) {
        return checkGroupDao.findCheckItemIdsByCheckGroupId(id);
    }

    @Override
    public void edit(CheckGroup checkGroup, Integer[] checkitemIds) {

        // 1.清除原先的关联关系
        checkGroupDao.deleteAssociation(checkGroup.getId());

        // 2.添加新的关联关系
        setCheckGroupAndCheckItem(checkGroup.getId(),checkitemIds);

        // 3.更新基本信息
        checkGroupDao.edit(checkGroup);
    }

    @Override
    public void delete(Integer id) {
        List<Integer> list = checkGroupDao.findCheckItemIdsByCheckGroupId(id);
        if(list.size()>0){ //删除关联数据
            checkGroupDao.deleteAssociation(id);
        }
        checkGroupDao.deleteById(id);

    }

    @Override
    public List<CheckGroup> findAll() {
        return checkGroupDao.findAll();
    }

    @Override
    public void add(CheckGroup checkGroup, Integer[] checkitemIds) {
        checkGroupDao.add(checkGroup);
        Integer checkGroupId = checkGroup.getId();
        this.setCheckGroupAndCheckItem(checkGroupId,checkitemIds);
    }

    /**
     * 检查组与检查项 新增 关联
     * @param checkGroupId
     * @param checkitemIds
     */
    public void setCheckGroupAndCheckItem(Integer checkGroupId,Integer[] checkitemIds){
        if(checkitemIds != null && checkitemIds.length > 0){
            for (Integer checkitemId : checkitemIds) {
                Map<String,Integer> map = new HashMap<>();
                map.put("checkgroupId",checkGroupId);
                map.put("checkitemId",checkitemId);
                checkGroupDao.setCheckGroupAndCheckItem(map);
            }
        }
    }

}
