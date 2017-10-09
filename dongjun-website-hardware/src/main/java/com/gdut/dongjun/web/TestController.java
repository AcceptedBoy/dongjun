package com.gdut.dongjun.web;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class TestController {

	/**
	 * 嗅探应用是否可用的接口
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("/testAvailable")
	public void testAvailable(HttpServletResponse response) throws IOException {
		PrintWriter w = response.getWriter();
		w.write("OK");
		w.flush();
		return ;
	}
	
//	public static void main(String[] args) {
//		String a = "68535368F46C0001C814016C000100010000000000000000000000000000000000000000000001010101010000000000000001000001000000000000000100000000000000000000000000000000000000000000000000B416";
//		String b = a.substring(0, 66) + "01" + a.substring(68, a.length() - 1);
//		System.out.println(b);
//	}
}
