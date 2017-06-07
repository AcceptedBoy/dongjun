package com.gdut.dongjun.service.webservice.client.service;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.gdut.dongjun.service.webservice.client.po.ChannelInfo;

/**
 *
 */
@Produces({MediaType.APPLICATION_JSON})
public interface HardwareService {

    /**
     * 产生开闸报文并且发送开闸预置
     */
//    @POST
//    @Path("/generate_open_msg")
//    @Consumes({MediaType.APPLICATION_JSON})
//    public String generateOpenSwitchMessage(@FormParam("address") String address,
//                                            @FormParam("type") Integer type);

//    /**
//     * 通过开关id来获取在线开关的地址
//     */
//    @POST
//    @Path("/online_address")
//    @Consumes({MediaType.APPLICATION_JSON})
//    public String getOnlineAddressById(String id);

    /**
     * 获取在线开关的所有信息
     */
    @POST
    @Path("/all_switchGPRS")
    @Consumes({MediaType.APPLICATION_JSON})
    public List<ChannelInfo> getCtxInstance();

//    /**
//     * 通过开关id来获取在线开关
//     */
//    @POST
//    @Path("/switchGPRS_by_id")
//    @Consumes({MediaType.APPLICATION_JSON})
//    public SwitchGPRS getSwitchGPRS(String id);
    
	/**
	 * 改变内存中温度设备的阈值
	 * @param id
	 */
	@POST
	@Path("/change_temperature_device")
	@Consumes({MediaType.APPLICATION_JSON})
	public void changeTemperatureDevice(String id);
	
	@POST
	@Path("/gprs_module_status")
	@Consumes({MediaType.APPLICATION_JSON})
	public List<Integer> getGPRSModuleStatus(List<String> ids);
	
//	@POST
//	@Path("/test_elec")
//	@Consumes({MediaType.APPLICATION_JSON})
//	public void testElec(String id);
	
	/**
	 * 实时改变ChannelPipeline的ChannelHandler
	 * type是设备的类型
	 * 2为电能表
	 * 3为温度
	 * 4为GPRS
	 * @param monitorId
	 * @param type
	 * @param operate
	 */
	@POST
	@Path("/change_channel_handler")
	@Consumes({MediaType.APPLICATION_JSON})
	public void changeChannelHandler(@FormParam("monitorId") String monitorId, 
			@FormParam("type") Integer type, @FormParam("operate") Integer operate);
	
	/**
	 * TODO 线程不安全
	 * 一旦去除ChannelInfo正在解析的报文可能会出错
	 * 主要的工作是去除ChannelInfo，重新添加的时机是
	 * {@link com.gdut.dongjun.core.handler.msg_decoder.GPRSDataReceiver}接收到登录包或者心跳包
	 */	
	@POST
	@Path("/change_submodule_address")
	@Consumes({MediaType.APPLICATION_JSON})
	public void changeSubmoduleAddress(
			@FormParam("moduleId") String moduleId, 
			@FormParam("type") Integer type);
	
	/**
	 * 测试发送电能表报文
	 * @param m
	 */
	@POST
	@Path("/send_message")
	@Consumes({MediaType.APPLICATION_JSON})
	public void sendMessage(String m);
}
