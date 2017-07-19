package com.gdut.dongjun.service.webservice.server;

import java.io.Serializable;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Produces({MediaType.APPLICATION_JSON})
public interface CenterCallHardwareService {
	
	/**
	 * 更新允许运作的设备地址
	 * @param addrs
	 */
	@POST
	@Path("/update_switch_address_available")
	@Consumes({MediaType.APPLICATION_JSON})
	public void updateSwitchAddressAvailable(List<String> addrs);
}
