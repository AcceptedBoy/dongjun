package com.gdut.dongjun.service.webservice.server;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.web.bind.annotation.RequestParam;

import com.gdut.dongjun.core.SwitchGPRS;
import com.gdut.dongjun.domain.HighVoltageStatus;
import com.gdut.dongjun.domain.vo.ActiveHighSwitch;

/**
 *
 */
@Produces({MediaType.APPLICATION_JSON})
public interface HardwareService {
	
	/**
	 * 产生开闸报文并且发送开闸预置
	 */
	/*
	 * 多参数传递、格式为json情况下，不能同时使用{@code @Consumes}和{@code @FormParam}，{@code @FormParam}已经能完成格式转换。
	 * <a> http://stackoverflow.com/questions/9623616/cxf-jaxrs-how-to-post-more-than-one-parameter
	 */
//	@POST
//	@Path("/generate_open_msg")
////	@Consumes({MediaType.APPLICATION_JSON})
//	public String generateOpenSwitchMessage(@FormParam("address") String address,
//											@FormParam("type") Integer type);
	
	/**
	 * 通过开关id来获取在线开关的地址
	 */
	@POST
	@Path("/online_address")
	@Consumes({MediaType.APPLICATION_JSON})
	public String getOnlineAddressById(String id);
    
	/**
	 * 获取在线开关的所有信息
	 */
	@POST
	@Path("/all_switchGPRS")
	@Consumes({MediaType.APPLICATION_JSON})
	public List<SwitchGPRS> getCtxInstance();
	
	/**
	 * 通过开关id来获取在线开关
	 */
	@POST
	@Path("/switchGPRS_by_id")
	@Consumes({MediaType.APPLICATION_JSON})
	public SwitchGPRS getSwitchGPRS(String id);
	
	@POST
	@Path("/change_temperature_device")
	@Consumes({MediaType.APPLICATION_JSON})
	public void changeTemperatureDevice(String id);
	
	@POST
	@Path("/gprs_module_status")
	@Consumes({MediaType.APPLICATION_JSON})
	public List<Integer> getGPRSModuleStatus(List<String> ids);
}
