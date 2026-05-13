package com.demo.security.service;

import com.demo.security.entity.User;
import com.demo.security.mapper.UserMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserMapper userMapper;

    private final PasswordEncoder encoder;

    public User getUserById(long id){
        return userMapper.selectByPrimaryKey(id);
    }

    public int insertUser(User user){
        user.setPassword(encoder.encode(user.getPassword()));
        int i = userMapper.insert(user);
        return i;
    }

    public UserService(UserMapper userMapper, PasswordEncoder encoder){
        this.userMapper = userMapper;
        this.encoder = encoder;
    }
}
