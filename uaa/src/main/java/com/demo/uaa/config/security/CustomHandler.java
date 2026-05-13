package com.demo.uaa.config.security;

import cn.hutool.json.JSONUtil;
import com.demo.uaa.entity.User;
import com.demo.uaa.web.result.Res;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.util.StringUtils;

import java.util.Set;
import java.util.stream.Collectors;

@Configuration
public class CustomHandler {

    private final JWKSource<SecurityContext> jwkSource;

    private final ObjectMapper objectMapper;

    @Bean
    public LogoutSuccessHandler myLogoutHandler(){
        return (request,response,authentication)->{
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            //如果有需要注销token的逻辑 可以放在这里做


            Res res = Res.builder().code(200).message("success logout").info(authentication).build();
            String m = objectMapper.writeValueAsString(res);
            response.getWriter().write(m);
        };
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler(){
        return (request,response,authentication)->{
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");

            User u = (User) authentication.getPrincipal();
            Set<String> authorities = u.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());;
            String uJson = objectMapper.writeValueAsString(u);
            String aJson = objectMapper.writeValueAsString(authorities);

            JwtClaimsSet claims = JwtClaimsSet.builder()
//                    .issuer("http://localhost:8090").subject("root")
                    .claim("user",uJson)
                    .claim("authorities",aJson)
                    .build();

            JwtEncoderParameters parameters = JwtEncoderParameters.from(claims);
            String token = encoder().encode(parameters).getTokenValue();

            //如果需要管理token，使之能失效etc  例如把token放入redis中统一管理
            //此处可以把token放入reids中

            Res r = Res.builder().code(200).message("登录成功").info(token).build();
            String rJson = objectMapper.writeValueAsString(r);
            response.getWriter().write(rJson);
        };
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler(){
        return (request,response,authenticationException)->{
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");

            Res r = Res.builder().code(401).message("用户名或密码错误").info(authenticationException.getMessage()).build();
            response.getWriter().write(objectMapper.writeValueAsString(r));
        };
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler(){
        return (request,response,accessDeniedException)->{
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");

            Res r = Res.builder().code(403).message("用户权限不足").info(accessDeniedException.getMessage()).build();
            response.getWriter().write(objectMapper.writeValueAsString(r));
        };
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint(){
        return (request,response,authenticationException)->{
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            authenticationException.printStackTrace();
            String token = request.getHeader("authorization");
            if(!StringUtils.hasText(token)){
                Res r = Res.builder().code(401).message("没有token").info(authenticationException.getMessage()).build();
                response.getWriter().write(JSONUtil.toJsonStr(r));
                return;
            }
            Res r = Res.builder().code(401).message("token不正确/已过期").info(authenticationException.getMessage()).build();
            response.getWriter().write(JSONUtil.toJsonStr(r));
        };
    }

    private JwtEncoder encoder(){
        return new NimbusJwtEncoder(jwkSource);
    }

    public CustomHandler(JWKSource<SecurityContext> jwkSource,ObjectMapper objectMapper){
        this.jwkSource = jwkSource;
        this.objectMapper = objectMapper;
    }
}
