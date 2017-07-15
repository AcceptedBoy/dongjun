package com.gdut.dongjun.cxf;


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

import com.gdut.dongjun.cxf.dto.CompanyDTO;
import com.gdut.dongjun.cxf.po.InitialParam;
import com.gdut.dongjun.po.HighVoltageSwitch;
import com.gdut.dongjun.po.Line;
import com.gdut.dongjun.po.Substation;
import com.gdut.dongjun.po.ZTreeNode;
import com.gdut.dongjun.vo.AvailableHighVoltageSwitch;


/**
 * 暴露commonService服务
 * TODO 这里的服务切分得太细了，可以进行门面模式对此进行重构
 * TODO 对变电站，线路，开关的增删改也可以不暴露接口，使用数据库的主从复制
 * Created by symon on 16-9-27.
 */
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
    public Map<String, Boolean> addHighVoltageSwitch(HighVoltageSwitch hvSwitch);

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
    public Map<String, Boolean> updateVoltageSwitch(HighVoltageSwitch hvSwitch);


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
