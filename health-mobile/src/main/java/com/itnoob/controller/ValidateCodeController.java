package com.itnoob.controller;

import com.aliyuncs.exceptions.ClientException;
import com.itnoob.constant.MessageConstant;
import com.itnoob.constant.RedisMessageConstant;
import com.itnoob.entity.Result;
import com.itnoob.utils.SMSUtils;
import com.itnoob.utils.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

/**
 * 短信服务
 */

@RestController
@RequestMapping("/validateCode")
public class ValidateCodeController {

    @Autowired
    private JedisPool JedisPool;

    /**
     * 体检预约短信服务
     * @return
     */
    @RequestMapping("/send4Order")
    public Result send4Order(String telephone){
        // 生成随机4位验证码
        Integer codeSMS = ValidateCodeUtils.generateValidateCode(4);
        // 发送短信
        try {
            SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE,telephone,codeSMS.toString());
        } catch (ClientException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
        System.out.println("发送的手机验证码为：" + codeSMS);
        // 验证码放入redis缓存
        JedisPool.getResource().setex( telephone + RedisMessageConstant.SENDTYPE_ORDER,5 * 60,codeSMS.toString());
        return new Result(true,MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }

    /**
     * 用户登录短信服务
     * @param telephone
     * @return
     */
    @RequestMapping("/send4Login")
    public Result send4Login(String telephone){
        // 生成随机4位验证码
        Integer codeSMS = ValidateCodeUtils.generateValidateCode(6);
        // 发送短信
        try {
            SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE,telephone,codeSMS.toString());
        } catch (ClientException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
        System.out.println("发送的手机验证码为：" + codeSMS);
        // 验证码放入redis缓存
        JedisPool.getResource().setex( telephone + RedisMessageConstant.SENDTYPE_LOGIN,5 * 60,codeSMS.toString());
        return new Result(true,MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }

}
