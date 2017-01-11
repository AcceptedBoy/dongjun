package com.gdut.dongjun.service.webservice.client.service;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.web.bind.annotation.RequestBody;
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
    @POST
    @Path("/generate_open_msg")
    @Consumes({MediaType.APPLICATION_JSON})
    public String generateOpenSwitchMessage(@FormParam("address") String address,
                                            @FormParam("type") Integer type);

    /**
     * 产生合闸报文并发送合闸预置报文
     */
    @POST
    @Path("/generate_close_msg")
    @Consumes({MediaType.APPLICATION_JSON})
    public String generateCloseSwitchMessage(@FormParam("address") String address,
                                             @FormParam("type") Integer type);

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

    /**
     * 通过开关id来获取在线开关的高压状态
     */
    @POST
    @Path("/status_by_id")
    @Consumes({MediaType.APPLICATION_JSON})
    public HighVoltageStatus getStatusbyId(String id);

    /**
     * 通过开关id来改变开关的状态
     */
    @POST
    @Path("/change_ctx_open")
    @Consumes({MediaType.APPLICATION_JSON})
    public boolean changeCtxOpen(String switchId);

    /**
     * 获取所有在线开关的详细状态
     */
    @POST
    @Path("/active_switch_status")
    @Consumes({MediaType.APPLICATION_JSON})
    public List<ActiveHighSwitch> getActiveSwitchStatus();

    /**
     * 这个方法只有在等于true的时候软件客户端才会去发请求向这边请求获取所有在线开关的详细
     * @see {@link #getActiveSwitchStatus()}
     */
    @Deprecated
    @POST
    @Path("/whether_change")
    @Consumes({MediaType.APPLICATION_JSON})
    public boolean whetherChangeInfo();
}
