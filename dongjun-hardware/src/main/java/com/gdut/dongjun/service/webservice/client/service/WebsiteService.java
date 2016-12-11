package com.gdut.dongjun.service.webservice.client.service;

import com.gdut.dongjun.domain.vo.ActiveHighSwitch;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 *
 */
@Produces({MediaType.APPLICATION_JSON})
public interface WebsiteService {

    /**
     * 当开关状态产生变化时执行回调
     */
    @POST
    @Path("/callback_ctx_change")
    @Consumes({MediaType.APPLICATION_JSON})
    public void callbackCtxChange(List<ActiveHighSwitch> data);

    @POST
    @Path("/callback_device_change")
    public void callbackDeviceChange(@FormParam("switchId") String switchId,
                                     @FormParam("type") Integer type);
}
