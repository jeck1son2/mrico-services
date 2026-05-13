package com.demo.security.entity;

import lombok.Data;

import java.util.Date;

@Data
public abstract class CommonEntity {

    private Date createdate = new Date();

}
