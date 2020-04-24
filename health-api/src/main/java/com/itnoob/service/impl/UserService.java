package com.itnoob.service.impl;

import com.itnoob.pojo.User;

public interface UserService {

    User findByUserName(String username);
}
