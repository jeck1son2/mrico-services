package com.demo.security.config;

import cn.hutool.json.JSONUtil;
import com.demo.security.config.filter.TokenFilter;
import com.demo.security.mapper.UserMapper;
import com.demo.security.web.result.Res;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.util.StringUtils;

@Configuration
// 开启方法上的权限校验   hasAuthority('clue:list') and hasAuthority('clue:edit')
// @PostAuthorize()    @PreAuthorize(value = "hasRole('admin')")  对应的是Role_admin
// 模块名:功能名(clue:list)   或者   项目名:模块名:功能名(dlyk:clue:list)
@EnableMethodSecurity
public class SecurityConfig {

    private final LogoutSuccessHandler logoutSuccessHandler;

    private final AuthenticationSuccessHandler authSuccessHandler;

    private final AuthenticationFailureHandler authFailHandler;

    private final AccessDeniedHandler accessDeniedHandler;

    private final TokenFilter tokenFilter;

    private final Cache corsCache;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors()
                .configurationSource(new CRCorsConfigurationSource(corsCache))
                .and()
//                防会话固定共计，session_stateless 不使用session的情况下，可以关闭
                .csrf().disable()
//                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
//                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                // 添加tokenfilter校验
                .addFilterBefore(tokenFilter, UsernamePasswordAuthenticationFilter.class)
                //登出
                .logout()
                .logoutUrl("/auth/logout")
                .logoutSuccessHandler(logoutSuccessHandler)
                .and()
                //设置登录接口
                .formLogin()
                .loginProcessingUrl("/auth/login")
                //处理登录成功/失败的handler
                .successHandler(authSuccessHandler)
                .failureHandler(authFailHandler)
                .and()
                .exceptionHandling()
                // 403 权限异常自定义处理
                .accessDeniedHandler(accessDeniedHandler)
                // 401 登录失败自定义处理
                .authenticationEntryPoint((request,response,authenticationException)->{
                    response.setContentType("application/json");
                    response.setCharacterEncoding("utf-8");

                    String token = request.getHeader("Bearer");
                    if(!StringUtils.hasText(token)){
                        Res r = Res.builder().code(401).message("没有token").info(authenticationException.getCause()).build();
                        response.getWriter().write(JSONUtil.toJsonStr(r));
                        return;
                    }

                    Res r = Res.builder().code(401).message("token不正确/已过期").info(authenticationException.getCause()).build();
                    response.getWriter().write(JSONUtil.toJsonStr(r));
                })
                .and()
                .authorizeRequests()
//                这里是否可以动态加载权限控制
//                FilterSecurityInterceptor -> SecurityMetadataSource
//                FilterSecurityInterceptor 是 FilterChainProxy 最后一条过滤器
                .antMatchers(HttpMethod.OPTIONS).permitAll()
                .antMatchers("/auth").permitAll()
//                静态控制权限api
//                .antMatchers(HttpMethod.GET,"").hasRole("Admin")
                .anyRequest().authenticated();
        return http.build();
    }


    public SecurityConfig(LogoutSuccessHandler outHandler, AuthenticationSuccessHandler authSuccessHandler,
                          AuthenticationFailureHandler authFailHandler, AccessDeniedHandler accessDeniedHandler,
                          TokenFilter filter,
                          Cache corsCache){
        this.logoutSuccessHandler = outHandler;
        this.authSuccessHandler = authSuccessHandler;
        this.authFailHandler = authFailHandler;
        this.accessDeniedHandler = accessDeniedHandler;
        this.tokenFilter = filter;
        this.corsCache = corsCache;
    }
}
