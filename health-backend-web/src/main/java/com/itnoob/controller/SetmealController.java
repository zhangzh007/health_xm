package com.itnoob.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itnoob.constant.MessageConstant;
import com.itnoob.entity.PageResult;
import com.itnoob.entity.QueryPageBean;
import com.itnoob.entity.Result;
import com.itnoob.pojo.CheckGroup;
import com.itnoob.pojo.Setmeal;
import com.itnoob.service.impl.SetMealService;
import com.itnoob.utils.OssUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    @Reference
    private SetMealService setMealService;

    /**
     * 图片上传
     * @param imgFile
     * @return
     */
    @RequestMapping("/upload")
    public Result upload(MultipartFile imgFile){
        // 获取原始文件名
        String filename  = imgFile.getOriginalFilename();
        // 获取上传文件名 UUID生成唯一文件名 避免上传同一图片覆盖
        filename = UUID.randomUUID().toString().replaceAll("-", "") + "_" + filename;
        try {
            // 获取文件在oos服务器路径
            OssUtils ossUtils = new OssUtils();
            String filepath = ossUtils.upload(filename, imgFile.getInputStream());
            return new Result(true, MessageConstant.PIC_UPLOAD_SUCCESS,filepath);
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.PIC_UPLOAD_FAIL);
        }
    }

    /**
     * 添加
     * @param setmeal
     * @param checkgroupIds
     * @return
     */
    @RequestMapping("/add")
    public Result add(@RequestBody Setmeal setmeal, Integer[] checkgroupIds){
        try {
            setMealService.add(setmeal,checkgroupIds);
            return new Result(true,MessageConstant.ADD_SETMEAL_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.ADD_SETMEAL_FAIL);
        }

    }

    /**
     * 分页查询
     * @return
     * QueryPageBean:分页请求类
     */
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        // 判断当前有无查询条件 若有把当前页置为1，否则可能会查不到数据
        String queryString = queryPageBean.getQueryString();
        if(queryString != null && queryString.length() > 0){
            queryPageBean.setCurrentPage(1);
        }
        PageResult page = setMealService.findPage(queryPageBean);
        return page;
    }

    /**
     * 编辑 数据回显
     * @return
     */
    @RequestMapping("/findById")
    public Result findById(Integer id){
        try {
            Setmeal setmeal = setMealService.findById(id);
            return new Result(true,MessageConstant.QUERY_SETMEAL_SUCCESS,setmeal);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_SETMEAL_FAIL);
        }
    }

    /**
     * 编辑 查询当前套餐关联的检查组
     * @param id
     * @return
     */
    @RequestMapping("/findCheckGroupIdsBySetMealId")
    public Result findCheckGroupIdsBySetMealId(Integer id){
        try {
            List<Integer> list = setMealService.findCheckGroupIdsBySetMealId(id);
            return new Result(true,MessageConstant.QUERY_SETMEAL_SUCCESS,list);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_SETMEAL_FAIL);
        }
    }

    /**
     * 编辑
     * @return
     */
    @RequestMapping("/edit")
    public Result edit(@RequestBody Setmeal setmeal,Integer[] checkgroupIds){
        try {
            setMealService.edit(setmeal,checkgroupIds);
            return new Result(true,MessageConstant.EDIT_SETMEAL_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.EDIT_SETMEAL_FAIL);
        }
    }

    /**
     * 删除
     * @param id
     * @return
     */
    @RequestMapping("/delete")
    public Result delete(Integer id){
        try {
            setMealService.delete(id);
            return new Result(true,MessageConstant.DELETE_SETMEAL_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.DELETE_SETMEAL_FAIL);
        }
    }

}
