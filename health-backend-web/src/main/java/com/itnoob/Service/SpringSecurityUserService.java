package com.itnoob.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itnoob.pojo.Permission;
import com.itnoob.pojo.Role;
import com.itnoob.pojo.User;
import com.itnoob.service.impl.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * security 登录处理
 */

@Component
public class SpringSecurityUserService implements UserDetailsService {

    @Reference
    private UserService userService;

    /**
     * 根据用户名查询用户信息
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userService.findByUserName(username);
        if(user == null){
            // 用户名不存在
            return null;
        }
        // 用户 角色权限集合
        List<GrantedAuthority> list = new ArrayList<>();

        //动态为当前用户授权
        Set<Role> roles = user.getRoles();
        if(roles != null && roles.size() > 0){
            for (Role role : roles) {
                //遍历角色集合，为用户授予角色
                list.add(new SimpleGrantedAuthority(role.getKeyword()));
                Set<Permission> permissions = role.getPermissions();
                if(permissions != null && permissions.size() > 0){
                    for (Permission permission : permissions) {
                        //遍历权限集合，为用户授权
                        list.add(new SimpleGrantedAuthority(permission.getKeyword()));
                    }
                }
            }
        }


        UserDetails userDetails = new org.springframework.security.core.userdetails.User(username,user.getPassword(),list);
        return userDetails;
    }
}
