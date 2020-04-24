package com.itnoob.dao;

import com.itnoob.pojo.Permission;

import java.util.Set;

public interface PermissionDao {

    Set<Permission> findByRoleId(Integer roleId);

}
