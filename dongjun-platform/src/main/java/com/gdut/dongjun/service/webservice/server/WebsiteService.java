package com.gdut.dongjun.service.webservice.server;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.gdut.dongjun.domain.dto.ActiveHighSwitch;
import com.gdut.dongjun.domain.dto.HitchEventDTO;
import com.gdut.dongjun.service.webservice.client.po.InfoEventDTO;

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
   // @Consumes({MediaType.APPLICATION_JSON})
    public void callbackDeviceChange(@FormParam("switchId") String switchId,
                                     @FormParam("type") Integer type);
    
    @POST
    @Path("/callback_hitch_event")
    @Consumes({MediaType.APPLICATION_JSON})
    public void callbackHitchEvent(HitchEventDTO event);
    
    @POST
    @Path("/callback_info_event")
    @Consumes({MediaType.APPLICATION_JSON})
    public void callbackInfoEvent(InfoEventDTO event);
}
