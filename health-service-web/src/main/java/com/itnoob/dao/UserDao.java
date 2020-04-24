package com.itnoob.dao;

        import com.itnoob.pojo.User;

public interface UserDao {

    User findByUserName(String username);

}
