package com.gdut.dongjun.service.webservice.client.centor.client;

import com.gdut.dongjun.domain.po.HighVoltageSwitch;
import com.gdut.dongjun.domain.po.Line;
import com.gdut.dongjun.domain.po.Substation;
import com.gdut.dongjun.domain.vo.AvailableHighVoltageSwitch;
import com.gdut.dongjun.service.webservice.client.centor.po.InitialParam;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.Serializable;
import java.util.List;


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

    @POST
    @Path("/switchs_of_line")
    public List<AvailableHighVoltageSwitch> switchsOfLine(@FormParam("type") Integer type,
                                                          @FormParam("lineId") String lineId);
}
