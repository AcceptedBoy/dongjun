package com.gdut.dongjun.cxf;

import java.io.Serializable;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Produces({MediaType.APPLICATION_JSON})
public interface CenterCallService extends Serializable {
	
	@POST
	@Path("/update_switch_address_available")
	@Consumes({MediaType.APPLICATION_JSON})
	public void updateSwitchAddressAvailable(List<String> addrs);
}
