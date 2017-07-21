package com.gdut.dongjun.service.webservice.client.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.gdut.dongjun.domain.po.HighVoltageSwitch;
import com.gdut.dongjun.domain.po.Line;
import com.gdut.dongjun.domain.po.Substation;
import com.gdut.dongjun.domain.vo.AvailableHighVoltageSwitch;
import com.gdut.dongjun.service.impl.ZTreeNode;
import com.gdut.dongjun.service.webservice.client.po.HighVoltageSwitchDTO;
import com.gdut.dongjun.service.webservice.client.po.InitialParam;


@Produces({MediaType.APPLICATION_JSON})
public interface CommonService extends Serializable {

    /**
     * 添加变电站
     * @param substation 变电站
     * @return
     */
    @POST
    @Path("/substation/add")
    @Consumes({MediaType.APPLICATION_JSON})
    public Map<String, Boolean> addSubstation(Substation substation);

    /**
     * 删除变电站
     * @param substationId 变电站
     * @return
     */
    @GET
    @Path("/substation/delete/{substationId}")
    public Map<String, Boolean> deleteSubstation(
            @PathParam("substationId") String substationId);

    /**
     * 更新变电站
     * @param substation 变电站
     * @return
     */
    @POST
    @Path("/substation/update")
    @Consumes({MediaType.APPLICATION_JSON})
    public Map<String, Boolean> updateSubstation(Substation substation);

    /**
     * 添加线路
     * @param line
     * @return
     */
    @POST
    @Path("/line/add")
    @Consumes({MediaType.APPLICATION_JSON})
    public Map<String, Boolean> addLine(Line line);

    /**
     * 删除线路
     * @param lineId
     * @return
     */
    @GET
    @Path("/line/delete/{lineId}")
    public Map<String, Boolean> deleteLine(@PathParam("lineId") String lineId);

    /**
     * 更新线路
     * @param line
     * @return
     */
    @POST
    @Path("/line/update")
    @Consumes({MediaType.APPLICATION_JSON})
    public Map<String, Boolean> updateLine(Line line);

    /**
     * 添加高压开关
     * @param hvSwitch
     * @return
     */
    @POST
    @Path("/hvSwitch/add")
    @Consumes({MediaType.APPLICATION_JSON})
    public Map<String, Boolean> addHighVoltageSwitch(HighVoltageSwitchDTO hvSwitch);

    /**
     * 删除高压开关
     * @param switchId
     * @return
     */
    @GET
    @Path("/hvSwitch/delete/{switchId}")
    public Map<String, Boolean> deleteHighVoltageSwitch(@PathParam("switchId") String switchId);

    /**
     * 更新高压开关
     * @param hvSwitch
     * @return
     */
    @POST
    @Path("/hvSwitch/update")
    @Consumes({MediaType.APPLICATION_JSON})
    public Map<String, Boolean> updateVoltageSwitch(HighVoltageSwitchDTO hvSwitch);


    @POST
    @Path("/system_initial")
    @Consumes({MediaType.APPLICATION_JSON})
    public Map<String, Boolean> systemInitial(InitialParam initialParam);

//    @POST
//    @Path("/register_service")
//    public Center registerService(@FormParam("ipAddr") Long ipAddr,
//                                  @FormParam("macAddr") String macAddr);

    @POST
    @Path("/switch_tree")
    public List<ZTreeNode> getSwitchTree(
                                @FormParam("companyId") String companyId,
                                @FormParam("type") String type);

    @POST
    @Path("/switchs_of_line")
    public List<AvailableHighVoltageSwitch> switchsOfLine(@FormParam("type") Integer type,
                                                          @FormParam("lineId") String lineId);
}