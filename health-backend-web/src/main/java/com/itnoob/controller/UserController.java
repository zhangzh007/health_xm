package com.itnoob.controller;

import com.itnoob.constant.MessageConstant;
import com.itnoob.entity.Result;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    /**
     * 获取session中的用户信息
     * security将用户信息自动存入session
     * @return
     */
    @RequestMapping("/getUsername")
    public Result getUsername(){
        //当Spring security完成认证后，会将当前用户信息保存到框架提供的上下文对象
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println(user);
        if(user != null){
            String username = user.getUsername();
            return new Result(true, MessageConstant.GET_USERNAME_SUCCESS,username);
        }

        return new Result(false, MessageConstant.GET_USERNAME_FAIL);
    }

}
