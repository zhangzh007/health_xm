package com.itnoob.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itnoob.dao.SetMealDao;
import com.itnoob.entity.PageResult;
import com.itnoob.entity.QueryPageBean;
import com.itnoob.pojo.CheckGroup;
import com.itnoob.pojo.Setmeal;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = SetMealService.class)
@Transactional
public class SetMealServiceImpl implements SetMealService {

    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    @Value("${out_put_path}")
    private String outputpath;

    @Autowired
    private SetMealDao setMealDao;

    @Override
    public void add(Setmeal setmeal, Integer[] checkgroupIds) {
        setMealDao.add(setmeal);
        if(checkgroupIds.length > 0 && checkgroupIds != null){
            setSetmealAndCheckGroup(setmeal.getId(),checkgroupIds);
        }
        // 新增套餐后需要重新生成静态页面
        generateMobileStaticHtml();
    }

    /**
     * 生成静态页面
     */
    public void generateMobileStaticHtml(){
        // 准备模板文件中所需要的数据
        List<Setmeal> list = setMealDao.findAll();
        // 生成套餐列表静态页面
        generateMobileSetmealListHtml(list);
        // 生成套餐详情静态页面
        generateMobileSetmealDetailHtml(list);
    }

    /**
     * 生成套餐列表静态页面
     */
    public void generateMobileSetmealListHtml(List<Setmeal> list){
        Map<String,Object> dataMap = new HashMap<>();
        dataMap.put("setmealList",list);
        this.generateHtml("mobile_setmeal.ftl","m_setmeal.html",dataMap);
    }

    /**
     * 生成套餐详情静态页面
     */
    public void generateMobileSetmealDetailHtml(List<Setmeal> list){
        for (Setmeal setmeal : list) {
            Map<String,Object> dataMap = new HashMap<>();
            dataMap.put("setmeal",this.findDetails(setmeal.getId())); //查询出套餐全部的信息 检查组+检查项
            this.generateHtml("mobile_setmeal_detail.ftl","m_setmeal_detail_" + setmeal.getId() + ".html",dataMap);
        }
    }

    /**
     * 生成静态页面
     * @param templateName:ftl模板文件
     * @param htmlPageName：生成的html文件
     * @param dataMap:模板数据
     */
    public void  generateHtml(String templateName,String htmlPageName,Map<String, Object> dataMap){
        Configuration configuration = freeMarkerConfigurer.getConfiguration();
        Writer out = null;
        try {
            // 加载模板文件
            Template template = configuration.getTemplate(templateName);
            // 构造输出流
            out = new FileWriter(new File(outputpath + "/" + htmlPageName));
            // 输出文件
            template.process(dataMap,out);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        // 获取查询条件
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();

        // 分页
        PageHelper.startPage(currentPage,pageSize);
        Page<Setmeal> page = setMealDao.findPageByCondition(queryString);
        return new PageResult(page.getTotal(),page.getResult());
    }

    @Override
    public Setmeal findById(Integer id) {
        return setMealDao.findById(id);
    }

    @Override
    public Setmeal findDetails(Integer id) {
        return setMealDao.findDetails(id);
    }

    @Override
    public List<Integer> findCheckGroupIdsBySetMealId(Integer id) {
        return setMealDao.findCheckGroupIdsBySetMealId(id);
    }

    @Override
    public void edit(Setmeal setmeal, Integer[] checkgroupIds) {

        // 1.清除原先的关联关系
        setMealDao.deleteAssociation(setmeal.getId());

        // 2.添加新的关联关系
        setSetmealAndCheckGroup(setmeal.getId(),checkgroupIds);

        // 3.更新基本信息
        setMealDao.edit(setmeal);

        // 编辑套餐后需要重新生成静态页面
        generateMobileStaticHtml();
    }

    @Override
    public void delete(Integer id) {
        List<Integer> list = setMealDao.findCheckGroupIdsBySetMealId(id);
        if(list.size()>0){ //删除关联数据
            setMealDao.deleteAssociation(id);
        }
        setMealDao.deleteById(id);

        // 删除套餐后需要重新生成静态页面
        generateMobileStaticHtml();
    }

    @Override
    public List<Setmeal> findAll() {
        return setMealDao.findAll();
    }

    @Override
    public List<Map<String, Object>> findSetmealCount() {
        return setMealDao.findSetmealCount();
    }

    public void setSetmealAndCheckGroup(Integer id,Integer[] checkgroupIds){
        for(Integer ids : checkgroupIds){
            Map<String,Integer> param = new HashMap<>();
            param.put("SetMealId",id);
            param.put("CheckGroupIds",ids);
            setMealDao.setSetmealAndCheckGroup(param);
        }
    }


}
