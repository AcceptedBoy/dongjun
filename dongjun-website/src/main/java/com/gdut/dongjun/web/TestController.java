package com.gdut.dongjun.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gdut.dongjun.domain.model.ResponseMessage;
import com.gdut.dongjun.domain.vo.ActiveHighSwitch;
import com.gdut.dongjun.service.webservice.client.HardwareServiceClient;
import com.gdut.dongjun.service.webservice.client.po.SwitchGPRS;

@Controller
public class TestController {
	
	public static String urlOfMapXtremeServlet = "http://localhost:8080/mapxtreme480/servlet/mapxtreme";
	public static int maxZoom = 943781;
	public static int minZoom = 500;
	
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
	
	@Autowired
	private HardwareServiceClient hardwareService;
	
	@RequestMapping("/testSwitchStatus")
	@ResponseBody
	public ResponseMessage testSwtchStatus() {
		StringBuilder sb = new StringBuilder();
		List<ActiveHighSwitch> list = hardwareService.getService().getActiveSwitchStatus();
		List<SwitchGPRS> switchList = hardwareService.getService().getCtxInstance();
		for (SwitchGPRS gprs : switchList) {
			for (ActiveHighSwitch s : list) {
				if (null != gprs.getId() && gprs.getId().equals(s.getId())) {
					sb.append(gprs.getAddress() + "---" + s.getStatus() + "\n");
					break;
				}
			}
		}
		return ResponseMessage.info(sb.toString());
	}

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
//		// 输出地
//		resp.getOutputStream().flush();
//		resp.getOutputStream().close();
//	}
	
//	@Autowired
//	private SimpMessagingTemplate template;
//	@RequestMapping("/ttt")
//	@ResponseBody
//	public String get() throws IOException {
//		byte[] array = VoiceFixUtil.request1("测试设备啦啦啦");
//		byte[] encodeBase64 = Base64.encodeBase64(array); 
//		this.template.convertAndSend("/topic/switch_event", 
//				new String(encodeBase64));
//		return "OK";
//	}
//	
//	@RequestMapping("/ttt1")
//	@ResponseBody
//	public String ttt1() throws IOException {
//		byte[] array = VoiceFixUtil.request1("测试设备啦啦啦");
//		byte[] encodeBase64 = Base64.encodeBase64(array); 
//		this.template.convertAndSend("/topic/switch_event", "asdasd");
//		return "OK";
//	}
	
}
