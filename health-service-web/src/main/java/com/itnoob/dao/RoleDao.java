package com.itnoob.dao;

import com.itnoob.pojo.Role;

import java.util.Set;

public interface RoleDao {

    Set<Role> findByUserId(Integer userId);

}
