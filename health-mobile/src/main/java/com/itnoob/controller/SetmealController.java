package com.itnoob.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itnoob.constant.MessageConstant;
import com.itnoob.entity.PageResult;
import com.itnoob.entity.QueryPageBean;
import com.itnoob.entity.Result;
import com.itnoob.pojo.Setmeal;
import com.itnoob.service.impl.SetMealService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 套餐管理
 */

@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    @Reference
    private SetMealService setmealService;

    //查询所有套餐
    @RequestMapping("/getAllSetmeal")
    public Result getAllSetmeal(){
        try{
            List<Setmeal> list = setmealService.findAll();
            return new Result(true, MessageConstant.GET_SETMEAL_LIST_SUCCESS,list);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_SETMEAL_LIST_FAIL);
        }
    }

    //根据套餐ID查询套餐详情（套餐基本信息、套餐对应的检查组信息、检查组对应的检查项信息）
    @RequestMapping("/findDetails")
    public Result findById(int id){
        try{
            Setmeal setmeal = setmealService.findDetails(id);
            return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS,setmeal);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_SETMEAL_FAIL);
        }
    }

    /**
     * 查询套餐信息 预约页面中展示
     * @param
     * @return
     */
    @RequestMapping("/findById")
    public Result findById(Integer id){
        try {
            Setmeal setmeal = setmealService.findById(id);
            return new Result(true,MessageConstant.QUERY_SETMEAL_SUCCESS,setmeal);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_SETMEAL_FAIL);
        }
    }
}
