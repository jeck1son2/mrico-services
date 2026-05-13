package com.demo.security.config;

import cn.hutool.json.JSONUtil;
import cn.hutool.jwt.JWTUtil;
import com.demo.security.entity.User;
import com.demo.security.web.result.Res;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import java.util.Map;

@Configuration
public class CustomHandler {

    @Bean
    public LogoutSuccessHandler myLogoutHandler(){
        return (request,response,authentication)->{
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            //如果有需要注销token的逻辑 可以放在这里做


            Res res = Res.builder().code(200).message("success logout").info(authentication).build();
            String m = JSONUtil.toJsonStr(res);
            response.getWriter().write(m);
        };
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler(){
        return (request,response,authentication)->{
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");

            User u = (User) authentication.getPrincipal();
            String uJson = JSONUtil.toJsonStr(u);
            // 需要添加过期时间,否则每次token生成都一样
            String token = JWTUtil.createToken(Map.of("user",uJson), Constant.JWT_TOKEN_SECRET.getBytes());

            //如果需要管理token，使之能失效etc  例如把token放入redis中统一管理
            //此处可以把token放入reids中


            Res r = Res.builder().code(200).message("登录成功").info(token).build();
            String rJson = JSONUtil.toJsonStr(r);
            response.getWriter().write(rJson);
        };
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler(){
        return (request,response,authenticationException)->{
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");

            Res r = Res.builder().code(401).message("用户名或密码错误").info(authenticationException.getCause()).build();
            response.getWriter().write(JSONUtil.toJsonStr(r));
        };
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler(){
        return (request,response,accessDeniedException)->{
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");

            Res r = Res.builder().code(403).message("用户权限不足").info(accessDeniedException.getCause()).build();
            response.getWriter().write(JSONUtil.toJsonStr(r));
        };
    }
}
