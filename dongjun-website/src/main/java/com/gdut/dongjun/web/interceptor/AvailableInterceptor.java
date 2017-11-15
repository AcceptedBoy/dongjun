package com.gdut.dongjun.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.gdut.dongjun.Const;

public class AvailableInterceptor extends HandlerInterceptorAdapter {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		if (Const.SYSTEM_RUN) {
			return true;
		} else {
			response.sendRedirect("/error/NotAvailable.html");
		}
		return false;
	}

}
