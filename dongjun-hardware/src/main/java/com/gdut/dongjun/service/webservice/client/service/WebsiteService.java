package com.gdut.dongjun.service.webservice.client.service;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.gdut.dongjun.domain.vo.DeviceOnlineVO;
import com.gdut.dongjun.domain.vo.HitchEventVO;

/**
 *
 */
@Produces({MediaType.APPLICATION_JSON})
public interface WebsiteService {

    /**
     * 当开关状态产生变化时执行回调
     */
//    @POST
//    @Path("/callback_ctx_change")
//    @Consumes({MediaType.APPLICATION_JSON})
//    public void callbackCtxChange(List<ActiveHighSwitch> data);

    @POST
    @Path("/callback_device_change")
    public void callbackDeviceChange(@FormParam("switchId") String switchId,
                                     @FormParam("type") Integer type);
    
    @POST
    @Path("/callback_hitch_event")
    @Consumes({MediaType.APPLICATION_JSON})
    public void callbackHitchEvent(HitchEventVO event);
    
    @POST
    @Path("/callback_hitch_event")
    @Consumes({MediaType.APPLICATION_JSON})
    public void callbackDeviceOnline(DeviceOnlineVO event);
}
