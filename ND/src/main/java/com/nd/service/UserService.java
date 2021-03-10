package com.nd.service;

import com.nd.mapper.UserMapper;
import com.nd.vo.User;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserService {

    @Resource
    private UserMapper userMapper;

    public User getByUname(String uname) {
        return userMapper.getByUname(uname);
    }

    public void save(User user) {
        userMapper.save(user);
    }

}
