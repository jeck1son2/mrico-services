package com.demo.uaa.web.result;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 这种方式是重定向指定跳转到这里的，浏览器请求会出问题
 */
@RestController
@RequestMapping("/oauth2")
public class AuthConsentController {

    @GetMapping("/consent")
    public Res sendConsentInfo(HttpServletRequest request,HttpServletResponse response){
        String state = request.getParameter("state");
        String scope = request.getParameter("scope");
        String client = request.getParameter("client_id");
        Map<String,String> keyMap = new HashMap<>();
        keyMap.put("client_id",client);
        keyMap.put("state",state);
        keyMap.put("scope",scope);

        return Res.builder().code(200).message("OK").info(keyMap).build();
    }
}
