package com.gdut.dongjun.webservice.client;

import javax.ws.rs.Produces;

import com.gdut.dongjun.domain.po.HighVoltageSwitch;
import com.gdut.dongjun.domain.po.Line;
import com.gdut.dongjun.domain.po.Substation;
import com.gdut.dongjun.service.impl.ZTreeNode;
import com.gdut.dongjun.webservice.po.Center;
import com.gdut.dongjun.webservice.po.InitialParam;


import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.Serializable;

import java.util.List;
import java.util.Map;


/**
 * 暴露commonService服务
 * Created by symon on 16-9-27.
 */
@Produces({MediaType.APPLICATION_JSON})
public interface CommonServiceClient extends Serializable {

    @POST
    @Path("/substation/add")
    @Consumes({MediaType.APPLICATION_JSON})
    public String addSubstation(Substation substation);

    @GET
    @Path("/substation/delete/{substationId}")
    public String deleteSubstation(
            @PathParam("substationId") String substationId);

    @POST
    @Path("/substation/update")
    @Consumes({MediaType.APPLICATION_JSON})
    public String updateSubstation(Substation substation);

    @POST
    @Path("/line/add")
    @Consumes({MediaType.APPLICATION_JSON})
    public String addLine(Line line);

    @GET
    @Path("/line/delete/{lineId}")
    public String deleteLine(@PathParam("lineId") String lineId);

    @POST
    @Path("/line/update")
    @Consumes({MediaType.APPLICATION_JSON})
    public String updateLine(Line line);

    @POST
    @Path("/hvSwitch/add")
    @Consumes({MediaType.APPLICATION_JSON})
    public String addHighVoltageSwitch(HighVoltageSwitch hvSwitch);

    @GET
    @Path("/hvSwitch/delete/{switchId}")
    public String deleteHighVoltageSwitch(@PathParam("switchId") String switchId);

    @POST
    @Path("/hvSwitch/update")
    @Consumes({MediaType.APPLICATION_JSON})
    public String updateVoltageSwitch(HighVoltageSwitch hvSwitch);


    @POST
    @Path("/system_initial")
    @Consumes({MediaType.APPLICATION_JSON})
    public String systemInitial(InitialParam initialParam);

    @POST
    @Path("/register_service")
    public String registerService(@FormParam("ipAddr") Long ipAddr,
                                  @FormParam("macAddr") String macAddr);

    @POST
    @Path("/switch_tree")
    public String getSwitchTree(
            @FormParam("companyId") String company_id,
            @FormParam("type") String type);
}
