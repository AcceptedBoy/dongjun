package com.gdut.dongjun.service.webservice.client.service;

import java.io.Serializable;

import javax.sound.sampled.Line;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.gdut.dongjun.service.webservice.client.po.InitialParam;

/**
 * TODO
 * @author Gordan_Deng
 * @date 2017年4月11日
 */
@Produces({MediaType.APPLICATION_JSON})
public interface CenterService extends Serializable {

//    @POST
//    @Path("/substation/add")
//    @Consumes({MediaType.APPLICATION_JSON})
//    public String addSubstation(Substation substation);

    @GET
    @Path("/substation/delete/{substationId}")
    public String deleteSubstation(
            @PathParam("substationId") String substationId);

//    @POST
//    @Path("/substation/update")
//    @Consumes({MediaType.APPLICATION_JSON})
//    public String updateSubstation(Substation substation);

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

//    @POST
//    @Path("/hvSwitch/add")
//    @Consumes({MediaType.APPLICATION_JSON})
//    public String addHighVoltageSwitch(HighVoltageSwitch hvSwitch);

    @GET
    @Path("/hvSwitch/delete/{switchId}")
    public String deleteHighVoltageSwitch(@PathParam("switchId") String switchId);

//    @POST
//    @Path("/hvSwitch/update")
//    @Consumes({MediaType.APPLICATION_JSON})
//    public String updateVoltageSwitch(HighVoltageSwitch hvSwitch);


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

//    @POST
//    @Path("/switchs_of_line")
//    public List<AvailableHighVoltageSwitch> switchsOfLine(@FormParam("type") Integer type,
//                                                          @FormParam("lineId") String lineId);
}
