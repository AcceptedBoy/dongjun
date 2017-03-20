package com.gdut.dongjun;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.gdut.dongjun.web.MySessionConnectedEvent;

@Configuration
public class AppConfig {
	@Bean
    public MySessionConnectedEvent mySessionConnectedEvent(){

        return new MySessionConnectedEvent();
    }
}
