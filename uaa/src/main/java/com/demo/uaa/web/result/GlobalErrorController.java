package com.demo.uaa.web.result;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@RestController
@RequestMapping
public class GlobalErrorController {

    @GetMapping("/error1")
    public Res globalError(HttpServletRequest request, HttpServletResponse response){
        String aa = request.getRequestURI();
        aa = aa;
        return Res.OK();
    }
}
