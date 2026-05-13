package com.demo.uaa.service.security;

import com.demo.uaa.entity.User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class MUserDetailService implements UserDetailsService {

    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User u = User.builder().loginName("root").password(passwordEncoder.encode("root"))
                .id(1L).phone("15810636514").authorities(Set.of(new SimpleGrantedAuthority("user:read"))).build();
        return u;
    }

    public MUserDetailService(PasswordEncoder encoder){
        this.passwordEncoder = encoder;
    }
}
