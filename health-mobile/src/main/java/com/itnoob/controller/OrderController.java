package com.itnoob.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itnoob.constant.MessageConstant;
import com.itnoob.constant.RedisMessageConstant;
import com.itnoob.entity.Result;
import com.itnoob.pojo.Order;
import com.itnoob.service.impl.OrderService;
import com.itnoob.service.impl.SetMealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.util.Map;

/**
 * 预约服务
 */

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private JedisPool jedisPool;

    @Reference
    private OrderService orderService;

    @Reference
    private SetMealService setMealService;

    /**
     *
     * @param map:接收预约信息
     * @return
     */
    @RequestMapping("/submit")
    public Result submit(@RequestBody Map map){

        // 对比输入验证码与redis中的验证码是否相等
        String telephone = (String) map.get("telephone");
        String validateCode = (String) map.get("validateCode");
        String code = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_ORDER);
        if(validateCode == null || code == null || !validateCode.equals(code)){
            // 验证码输入错误
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }

        // 远程调用体检预约服务
        Result result = new Result(false,MessageConstant.ORDER_FAIL);
        try {
            map.put("orderType", Order.ORDERTYPE_WEIXIN);
            result = orderService.order(map);
        } catch (Exception e) {
            e.printStackTrace();
            return result;
        }

        // 预约成功，发送短信通知
//        if(result.isFlag()){
//            // 查询当前预约套餐名称
//            String setmealId = map.get("setmealId").toString();
//            int id = Integer.parseInt(setmealId);
//            Setmeal setmeal = setMealService.findById(id);
//            String setmealName = setmeal.getName();
//
//            // 预约时间
//            String orderDate = (String) map.get("orderDate");
//            String param = setmealName + ",预约时间" + orderDate;
//
//            try {
//                SMSUtils.sendShortMessage(SMSUtils.ORDER_NOTICE,telephone,param);
//            } catch (ClientException e) {
//                e.printStackTrace();
//                //return new Result(false,MessageConstant.SEND_VALIDATECODE_FAIL);
//            }
//        }

        return result;
    }

    /**
     * 根据id查询预约信息
     * @param id
     * @return
     */
    @RequestMapping("/findById")
    public Result findById(Integer id){
        try {
            Map map = orderService.findById(id);
            return new Result(true,MessageConstant.QUERY_ORDER_SUCCESS,map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_ORDER_FAIL);
        }
    }

}
