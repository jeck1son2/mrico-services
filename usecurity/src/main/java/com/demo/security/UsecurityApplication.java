package com.demo.security;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

@SpringBootApplication
@MapperScan(value = "com.demo.security.mapper")
public class UsecurityApplication {
	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(UsecurityApplication.class);
		Environment env = app.run(args).getEnvironment();
		System.out.println("===========================当前应用为:"+env.getProperty("spring.application.name","unknown"));
		for (String profile : env.getActiveProfiles()){
			System.out.println("=============================当前激活的profile为:"+profile);
		}
	}
}
