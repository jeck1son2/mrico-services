package com.demo.security.web.result;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Res {

    private int code;

    private String message;

    private Object info;

    public static Res OK(){
        return Res.builder().code(200).message("success").build();
    }

    public static Res FAIL(){
        return Res.builder().code(500).message("fail").build();
    }
}
