package com.demo.security.service.security;

import com.demo.security.entity.User;
import com.demo.security.mapper.UserMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class DefaultUserDetailsService implements UserDetailsService {

    private final UserMapper userMapper;

    public DefaultUserDetailsService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userMapper.selectByLoginName(username);
        if (user == null){
            throw new UsernameNotFoundException("the password is not right or user can not find");
        }
        return user;
    }
}
