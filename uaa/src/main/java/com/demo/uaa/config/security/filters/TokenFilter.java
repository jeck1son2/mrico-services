package com.demo.uaa.config.security.filters;

import com.demo.uaa.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class TokenFilter extends OncePerRequestFilter {

    private final JwtDecoder decoder;

    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        Enumeration<String> aa = request.getHeaderNames();
        //
        String token = request.getHeader("authorization");
        token = token == null ? null : token.replaceFirst("Bearer ","");
        //如果没有继续走下面的过滤器
        if(!StringUtils.hasText(token)){
            filterChain.doFilter(request,response);
            return;
        }
        Jwt to = decoder.decode(token);
        String uJson = (String) to.getClaims().get("user");
        String auJson = (String) to.getClaims().get("authorities");
        User user = objectMapper.readValue(uJson,User.class);

        Set<SimpleGrantedAuthority> authorities = objectMapper.readValue(auJson, new com.fasterxml.jackson.core.type.TypeReference<Set<String>>() {})
                .stream().map(SimpleGrantedAuthority ::new).collect(Collectors.toSet());

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user,null,authorities);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request,response);
    }


    public TokenFilter(JwtDecoder decoder, ObjectMapper objectMapper){
        this.decoder = decoder;
        this.objectMapper = objectMapper;
    }

}
