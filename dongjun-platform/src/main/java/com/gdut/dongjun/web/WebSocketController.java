package com.gdut.dongjun.web;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gdut.dongjun.domain.model.ResponseMessage;

/**
 * WebSocket示范类。
 * 配套的js和html是resources/static/test/websocket.js和websocketTest.html
 * 能够满足日常WebSocket开发
 * 教程http://blog.csdn.net/yingxiake/article/details/51224569，这个博主的系列文章能带你入门spring的WebSocket开发
 * @author Gordan_Deng
 * @date 2017年3月20日
 */
@Controller
@RequestMapping("/footest")
@MessageMapping("/foo")
public class WebSocketController {

	/**
	 * 广播
	 * @param name
	 * @return
	 */
	@MessageMapping("handle3")
	@SendTo("/topic/greetings")
	public ResponseMessage handle(String name) {
		System.out.println("websocket handle3 接收到 " + name);
		return ResponseMessage.success("广播，劳资接到啦!!!");
	}
	
	/**
	 * 针对用户的广播，value是发布的端点，broadcast防止用户在多个浏览器登录的时候，其他浏览器能收到登录消息。
	 * @param name
	 * @return
	 */
	@MessageMapping("handle4")
	@SendToUser(value = "/topic/greetings", broadcast = false)
	public ResponseMessage handleToUser(String name) {
		System.out.println("websocket handle3 接收到 " + name);
		return ResponseMessage.success("专门给用户，劳资接到啦!!!");
	}
}
