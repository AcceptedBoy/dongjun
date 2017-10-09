package com.gdut.dongjun.service.webservice.client.service;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.gdut.dongjun.domain.vo.ActiveHighSwitch;
import com.gdut.dongjun.domain.vo.HitchEventVO;

/**
 *	{@code @FormParam}不能和List共用
 */
@Produces({MediaType.APPLICATION_JSON})
public interface WebsiteService {

    /**
     * 当开关状态产生变化时执行回调
     * 0设备分闸，1设备合闸，2设备上线，3设备下线
     */
    @POST
    @Path("/callback_ctx_change")
    @Consumes({MediaType.APPLICATION_JSON})
    public void callbackCtxChange(List<ActiveHighSwitch> data);

    @POST
    @Path("/callback_device_change")
    @Consumes({MediaType.APPLICATION_JSON})
    public void callbackDeviceChange(@FormParam("switchId") String switchId,
                                     @FormParam("type") Integer type);
    
    @POST
    @Path("/callback_hitch_event")
    @Consumes({MediaType.APPLICATION_JSON})
    public void callbackHitchEvent(HitchEventVO event);
    
    /**
     * 实时检测设备报文到达时执行的回调
     * @param switchId
     * @param text
     */
    @POST
    @Path("/callback_text_arrived")
    @Consumes({MediaType.APPLICATION_JSON})
    public void callbackTextArrived(@FormParam("switchId")String switchId, @FormParam("text")String text);

    @POST
    @Path("/callback_ctx_change_voice")
    @Consumes({MediaType.APPLICATION_JSON})
    public void callbackCtxChangeForVoice(@FormParam("switchId") String id, @FormParam("type") int type);
}
