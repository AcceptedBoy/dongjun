package com.gdut.dongjun.service.webservice.server;

import java.io.Serializable;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Produces({MediaType.APPLICATION_JSON})
public interface CenterCallWebsiteService {
	
	/**
	 * 系统启动，要求子系统发送InitialParam
	 * @param addrs
	 */
	@POST
	@Path("/init_call")
	@Consumes({MediaType.APPLICATION_JSON})
	public void initCall();
	
}