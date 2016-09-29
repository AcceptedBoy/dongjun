package com.symon.cxf;


import com.symon.cxf.po.InitialParam;
import com.symon.po.*;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import org.aspectj.lang.annotation.Pointcut;

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
public interface CommonService extends Serializable {

    @POST
    @Path("/substation/add")
    @Consumes({MediaType.APPLICATION_JSON})
    public Map<String, Boolean> addSubstation(Substation substation);

    @GET
    @Path("/substation/delete/{substationId}")
    public Map<String, Boolean> deleteSubstation(
            @PathParam("substationId") String substationId);

    @POST
    @Path("/substation/update")
    @Consumes({MediaType.APPLICATION_JSON})
    public Map<String, Boolean> updateSubstation(Substation substation);

    @POST
    @Path("/line/add")
    @Consumes({MediaType.APPLICATION_JSON})
    public Map<String, Boolean> addLine(Line line);

    @GET
    @Path("/line/delete/{lineId}")
    public Map<String, Boolean> deleteLine(@PathParam("lineId") String lineId);

    @POST
    @Path("/line/update")
    @Consumes({MediaType.APPLICATION_JSON})
    public Map<String, Boolean> updateLine(Line line);

    @POST
    @Path("/hvSwitch/add")
    @Consumes({MediaType.APPLICATION_JSON})
    public Map<String, Boolean> addHighVoltageSwitch(HighVoltageSwitch hvSwitch);

    @GET
    @Path("/hvSwitch/delete/{switchId}")
    public Map<String, Boolean> deleteHighVoltageSwitch(@PathParam("switchId") String switchId);

    @POST
    @Path("/hvSwitch/update")
    @Consumes({MediaType.APPLICATION_JSON})
    public Map<String, Boolean> updateVoltageSwitch(HighVoltageSwitch hvSwitch);


    @POST
    @Path("/system_initial")
    @Consumes({MediaType.APPLICATION_JSON})
    public Map<String, Boolean> systemInitial(InitialParam initialParam);

    @POST
    @Path("/register_service")
    public Center registerService(@FormParam("ipAddr") Long ipAddr,
                                  @FormParam("macAddr") String macAddr);

    @POST
    @Path("/switch_tree")
    public List<ZTreeNode> getSwitchTree(
                                @FormParam("companyId") String companyId,
                                @FormParam("type") String type);
}
