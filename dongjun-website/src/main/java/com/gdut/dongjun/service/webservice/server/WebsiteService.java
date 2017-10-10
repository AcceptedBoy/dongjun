package com.gdut.dongjun.service.webservice.server;

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
//    @Consumes({MediaType.APPLICATION_JSON})
    public void callbackDeviceChange(@FormParam("switchId") String switchId,
                                     @FormParam("type") Integer type);
    
    /**
     * 实时检测设备报文到达时执行的回调
     * @param switchId
     * @param text
     */
    @POST
    @Path("/callback_text_arrived")
//    @Consumes({MediaType.APPLICATION_JSON})
    public void callbackTextArrived(@FormParam("switchId")String switchId, @FormParam("text")String text);

    /**
     * 当开关状态产生变化时执行回调
     */
    @POST
    @Path("/callback_ctx_change_voice")
//    @Consumes({MediaType.APPLICATION_JSON})
    public void callbackCtxChangeForVoice(@FormParam("switchId") String id, @FormParam("type") int type);
}
