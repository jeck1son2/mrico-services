package com.demo.uaa.config.security;

import cn.hutool.json.JSONUtil;
import com.demo.uaa.config.security.filters.TokenFilter;
import com.demo.uaa.web.result.Res;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StringUtils;

import java.util.Set;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfig {

    private final AuthenticationSuccessHandler authenticationSuccessHandler;

    private final AccessDeniedHandler accessDeniedHandler;

    private final AuthenticationEntryPoint authenticationEntryPoint;

    private final JwtDecoder decoder;

    private final ObjectMapper objectMapper;

    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors()
                .configurationSource(new CRCorsConfigurationSource())
                .and()
                .csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS).permitAll()
                .antMatchers("/api/v1/login").permitAll()
                .antMatchers("/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(new TokenFilter(decoder,objectMapper), UsernamePasswordAuthenticationFilter.class)
                .formLogin()
                .loginProcessingUrl("/api/v1/login")
                .successHandler(authenticationSuccessHandler)
                .failureHandler((request,response,authenticationException)->{
                    response.setContentType("application/json");
                    response.setCharacterEncoding("utf-8");
                    Res r = Res.builder().code(401).message("用户名或密码错误").info(authenticationException.getCause()).build();
                    response.getWriter().write(JSONUtil.toJsonStr(r));
                })
                .and()
                .exceptionHandling()
                // 403 权限异常自定义处理
                .accessDeniedHandler(accessDeniedHandler)
                // 401 登录失败自定义处理
                .authenticationEntryPoint(authenticationEntryPoint);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    public SecurityConfig(AuthenticationSuccessHandler authenticationSuccessHandler,
                          AccessDeniedHandler accessDeniedHandler, AuthenticationEntryPoint authenticationEntryPoint,
                          JwtDecoder decoder, ObjectMapper objectMapper){
        this.authenticationSuccessHandler = authenticationSuccessHandler;
        this.accessDeniedHandler = accessDeniedHandler;
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.decoder = decoder;
        this.objectMapper = objectMapper;
    }
}
