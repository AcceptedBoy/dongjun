package com.gdut.dongjun.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.ServletWebRequest;

/**
 * 对RequestContextHolder的一层封装，可以获取当前请求对象
 * 但是对于multipart的请求类型的支持有点问题，详情<a>http://dinguangx.iteye.com/blog/2227049</a>
 * @author Gordan_Deng
 * @date 2017年3月10日
 */
public class RequestHolder {

	public static HttpServletRequest getRequest(){
        HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return req;
    }

    public static HttpServletResponse getResponse(){
        HttpServletResponse resp = ((ServletWebRequest)RequestContextHolder.getRequestAttributes()).getResponse();
        return resp;
    }
}
