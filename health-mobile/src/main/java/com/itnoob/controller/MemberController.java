package com.itnoob.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.itnoob.constant.MessageConstant;
import com.itnoob.constant.RedisMessageConstant;
import com.itnoob.entity.Result;
import com.itnoob.pojo.Member;
import com.itnoob.service.impl.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/login")
public class MemberController {

    @Autowired
    private JedisPool jedisPool;

    @Reference
    private MemberService memberService;

    /**
     * 用户登录
     * @param map
     * @return
     */
    @RequestMapping("/check")
    public Result check(HttpServletResponse response, @RequestBody Map map){

        // 验证 验证码输入是否正确
        String validateCode = (String) map.get("validateCode");
        String telephone = (String) map.get("telephone");
        String codeInRedis = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_LOGIN);
        if(validateCode != null && codeInRedis != null && validateCode.equals(codeInRedis)){ //验证码输入正确
            //判断当前登录用户是否已经注册
            Member member = memberService.findByTelephone(telephone);
            if(member == null){ //未注册 自动注册
                member.setRegTime(new Date()); // 此处时间为代码执行时间 并不是数据添加到数据库的时间
                member.setPhoneNumber(telephone);
                memberService.add(member);
            }else{
                // 登录信息存入cookie中
                Cookie cookie = new Cookie("login_member_telephone",telephone);
                cookie.setPath("/");
                cookie.setMaxAge(60*60*24*30);
                response.addCookie(cookie);
                //将会员信息保存到Redis fastjson将对象转为json数据
                String json = JSON.toJSON(member).toString();
                jedisPool.getResource().setex(telephone,60*30,json);
            }
            return new Result(true,MessageConstant.LOGIN_SUCCESS);
        }else {
            //验证码输入错误
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }
    }

}
