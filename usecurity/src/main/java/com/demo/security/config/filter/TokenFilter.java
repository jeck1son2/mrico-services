package com.demo.security.config.filter;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.jwt.JWTUtil;
import com.demo.security.config.Constant;
import com.demo.security.entity.User;
import com.demo.security.web.result.Res;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class TokenFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");

        //
        String token = request.getHeader("authorization");
        //如果没有继续走下面的过滤器
        if(!StringUtils.hasText(token)){
            filterChain.doFilter(request,response);
            return;
        }

        //校验JWT token，放入SecurityContextHolder上下文中
        boolean verify = JWTUtil.verify(token, Constant.JWT_TOKEN_SECRET.getBytes());
        if (!verify){
            Res r = Res.builder().code(401).message("token已失效").build();
            response.getWriter().write(JSONUtil.toJsonStr(r));
            return;
        }

        //如果要对token进行管理，在这里添加逻辑
        JSONObject payloads = JWTUtil.parseToken(token).getPayloads();
        String userJSON = payloads.get("user", String.class);
        User user = JSONUtil.toBean(userJSON, User.class);

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user,null,user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request,response);
    }
}
