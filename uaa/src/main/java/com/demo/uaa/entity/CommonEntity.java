package com.demo.uaa.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public abstract class CommonEntity {

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdate = new Date();

}
