package com.gdut.dongjun.web;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/dongjun")
public class TestController {

	@RequestMapping("/test")
	public String test() {
		return "test";
	}
	
	

	@MessageExceptionHandler
	@SendToUser("/queue/errors")
	public String handleException(Throwable exception) {
		return exception.getMessage();
	}
	
	@Autowired
	private SimpMessagingTemplate template;

    @Autowired
    public TestController(SimpMessagingTemplate template) {
        this.template = template;
    }

    @RequestMapping(value="/greetings")
    @ResponseBody
    public void greet(String greeting) {
        
    	
    }

}
