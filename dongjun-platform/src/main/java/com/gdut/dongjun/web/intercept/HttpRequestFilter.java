package com.gdut.dongjun.web.intercept;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gdut.dongjun.service.aspect.HttpRequestContent;

public class HttpRequestFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpRequestContent.setRequest((HttpServletRequest)request);
		HttpRequestContent.setResponse((HttpServletResponse)response);
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {}

}
