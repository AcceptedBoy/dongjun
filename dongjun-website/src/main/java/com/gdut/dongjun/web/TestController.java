package com.gdut.dongjun.web;

import java.awt.Color;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/test")
public class TestController {
	
	public static String urlOfMapXtremeServlet = "http://localhost:8080/mapxtreme480/servlet/mapxtreme";
	public static int maxZoom = 943781;
	public static int minZoom = 500;
	

//	@RequestMapping("/electronic_map_info")
//	public void getMapInfo(HttpServletRequest req, HttpServletResponse resp) throws Exception {
//		MapJ mapj = new MapJ();
//		mapj.setDeviceBounds(new DoubleRect(0, 0, 1050, 600));
//		// 第一个参数是文件名字，第二个名字是文件所在路径
//		mapj.loadGeoset("d:\\test.gst", "d:\\", null);
//		ServletOutputStream so = resp.getOutputStream();
//		// mapj.setDeviceBounds(new DoubleRect(0, 0, 800, 600));
//
//		// Create an ImageRequestComposer（官方文档）
//		ImageRequestComposer irc = ImageRequestComposer.create(mapj, ImageRequestComposer.MAX_COLORS_TRUECOLOR,
//				Color.WHITE, "image/gif");
//
//		// Create a MapXtremeImageRenderer（官方文档）
//		MapXtremeImageRenderer mir = new MapXtremeImageRenderer(urlOfMapXtremeServlet);
//
//		mir.render(irc);
//
//		mir.toStream(so);
//
//		mir.dispose();
//		so.flush();
//		so.close();
//		
//		req.getSession().setAttribute("mapjtab", mapj);
//		// 输出地图
//		resp.getOutputStream().flush();
//		resp.getOutputStream().close();
//	}
}
