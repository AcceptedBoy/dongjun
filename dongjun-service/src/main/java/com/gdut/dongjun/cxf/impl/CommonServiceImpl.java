package com.gdut.dongjun.cxf.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.cxf.message.Message;
import org.apache.cxf.phase.PhaseInterceptorChain;
import org.apache.cxf.transport.http.AbstractHTTPDestination;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.gdut.dongjun.cxf.CenterCallServiceClient;
import com.gdut.dongjun.cxf.CommonService;
import com.gdut.dongjun.cxf.po.HighVoltageSwitchDTO;
import com.gdut.dongjun.cxf.po.InitialParam;
import com.gdut.dongjun.po.Company;
import com.gdut.dongjun.po.HighVoltageSwitch;
import com.gdut.dongjun.po.Line;
import com.gdut.dongjun.po.Substation;
import com.gdut.dongjun.po.ZTreeNode;
import com.gdut.dongjun.service.CenterService;
import com.gdut.dongjun.service.CompanyService;
import com.gdut.dongjun.service.HighVoltageSwitchService;
import com.gdut.dongjun.service.LineService;
import com.gdut.dongjun.service.SubstationService;
import com.gdut.dongjun.util.MyBatisMapUtil;
import com.gdut.dongjun.vo.AvailableHighVoltageSwitch;

/**
 * Created by symon on 16-9-27.
 */
@Service
public class CommonServiceImpl implements CommonService {

    @Autowired
    private SubstationService substationService;
    @Autowired
    private LineService lineService;
    @Autowired
    private HighVoltageSwitchService hvSwitchService;
    @Autowired
    private CenterService centerService;
//    @Autowired
//    private ZTreeNodeService treeNodeService;
    @Autowired
    private CompanyService companyService;
    @Autowired
    private CenterCallServiceClient cxfClient;

    private static final Map<String, Boolean> SUCCESS = new HashMap<String, Boolean>(1) {{
        put("success", true);
    }};
    private static Logger logger = Logger.getLogger(CommonServiceImpl.class);

    @Override
    public Map<String, Boolean> addSubstation(Substation substation) {
        substationService.insert(substation);
        return SUCCESS;
    }

    @Override
    public Map<String, Boolean> deleteSubstation(String substationId) {
        substationService.deleteByPrimaryKey(String.valueOf(substationId));
        return SUCCESS;
    }

    @Override
    public Map<String, Boolean> updateSubstation(Substation substation) {
        substationService.updateByPrimaryKeySelective(substation);
        return SUCCESS;
    }

    @Override
    public Map<String, Boolean> addLine(Line line) {
        lineService.insert(line);
        return SUCCESS;
    }

    @Override
    public Map<String, Boolean> deleteLine(String lineId) {
        lineService.deleteByPrimaryKey(lineId);
        return SUCCESS;
    }

    @Override
    public Map<String, Boolean> updateLine(Line line) {
        lineService.updateByPrimaryKeySelective(line);
        return SUCCESS;
    }

    @Override
    public Map<String, Boolean> addHighVoltageSwitch(HighVoltageSwitchDTO hvSwitch) {
    	HighVoltageSwitch s = hvSwitch.getHighVoltageSwitch();
    	s.setAvailable(false);
        hvSwitchService.insert(s);
        return SUCCESS;
    }

    @Override
    public Map<String, Boolean> deleteHighVoltageSwitch(String switchId) {
        hvSwitchService.deleteByPrimaryKey(switchId);
        return SUCCESS;
    }

    @Override
    public Map<String, Boolean> updateVoltageSwitch(HighVoltageSwitchDTO hvSwitch) {
        hvSwitchService.updateByPrimaryKeySelective(hvSwitch.getHighVoltageSwitch());
        return SUCCESS;
    }
    
    

    @Override
    public Map<String, Boolean> systemInitial(InitialParam initialParam) {
        //获取调用方的ip地址，如果数据库没有与此ip地址对应的公司，则无视此次调用。
    	Message message = PhaseInterceptorChain.getCurrentMessage();  
    	HttpServletRequest httprequest = (HttpServletRequest)message.get(AbstractHTTPDestination.HTTP_REQUEST);  
    	String ipAddr = httprequest.getRemoteAddr();
    	logger.info(ipAddr + "系统启动");
    	List<Company> coms = companyService.selectByParameters(MyBatisMapUtil.warp("ip_addr", ipAddr));
    	if (null == coms || 0 == coms.size()) {
    		return null;
    	}
        List<Substation> substationList = initialParam.getSubstationList();
        List<Line> lineList = initialParam.getLineList();
        List<HighVoltageSwitchDTO> hvswitchDTOList = initialParam.getHvswitchList();
        List<HighVoltageSwitch> hvswitchList = new ArrayList<>();
        for (HighVoltageSwitchDTO dto : hvswitchDTOList) {
        	HighVoltageSwitch s = dto.getHighVoltageSwitch();
        	hvswitchList.add(s);
        }
        
        List<String> realSubstationId = new ArrayList<>();
        List<String> realLineId = new ArrayList<>();
        List<String> realHVId = new ArrayList<>();
        
        if (!CollectionUtils.isEmpty(substationList)) {
            for (Substation substation : substationList) {
                substationService.updateByPrimaryKey(substation);
                realSubstationId.add(substation.getId());
            }
        }
        if (!CollectionUtils.isEmpty(lineList)) {
            for (Line line : lineList) {
                lineService.updateByPrimaryKey(line);
                realLineId.add(line.getId());
            }
        }
        if (!CollectionUtils.isEmpty(hvswitchList)) {
            for (HighVoltageSwitch highVoltageSwitch : hvswitchList) {
                hvSwitchService.updateByPrimaryKeySelective(highVoltageSwitch);
                realHVId.add(highVoltageSwitch.getId());
            }
        }
        //删除未更新的记录
        Company c = coms.get(0);
        List<String> storedSubstationId = substationService.selectIdByCompanyId(c.getId());
        List<String> stroredLineId = lineService.selectIdBySubstationIds(storedSubstationId);
        List<String> storedSwitchId = hvSwitchService.selectIdByLineIds(stroredLineId);
        //删除无用配电站信息
        storedSubstationId.removeAll(realSubstationId);
        if (storedSubstationId.size() != 0) {
            substationService.deleteByIds(storedSubstationId);
        }
        //删除无用线路信息
        stroredLineId.removeAll(realLineId);
        if (stroredLineId.size() != 0) {
            lineService.deleteByIds(stroredLineId);
        }
        //删除无用设备信息
        storedSwitchId.removeAll(realHVId);
        if (storedSwitchId.size() != 0) {
            hvSwitchService.deleteByIds(storedSwitchId);
        }
        
        //更新子系统的可用设备地址
        List<String> addrAvailable = hvSwitchService.selectAddrAvailableByCompanyId(c.getId());
        cxfClient.getService().updateSwitchAddressAvailable(addrAvailable, ipAddr);
        //	通知子系统可以正常运行
        cxfClient.getService().confirmInit(ipAddr);
        return SUCCESS;
    }

//    @Override
//    public Center registerService(Long ipAddr, String macAddr) {
//
//        Center center = null;
//
//        if (ipAddr == null || StringUtils.isEmpty(macAddr)) {
//            center.setIpAddr(ipAddr);
//            center.setMacAddr(macAddr);
//            return center;
//        }
//
//        Map<String, Object> check = new HashMap<>();
//        check.put("ip_addr", ipAddr);
//        check.put("mac_addr", macAddr);
//        List<Center> centers = centerService.selectByParameters(check);
//        if (!CollectionUtils.isEmpty(centers) && centers.size() == 1) {
//
//            center = centers.get(0);
//            center.setStartCount(center.getStartCount() + 1);
//            centerService.updateByPrimaryKeySelective(center);
//        } else {
//
//            center = new Center();
//            center.setIpAddr(ipAddr);
//            center.setMacAddr(macAddr);
//            center.setFirstStartTime(new Date());
//            center.setStartCount(1);
//            center.setServiceCount(1);
//            centerService.insert(center);//TODO 以后再来使用serviceCount这个字段
//        }
//
//        center.setLatestStartTime(new Date());
//        return center;
//    }

    @Override
    public List<ZTreeNode> getSwitchTree(String companyId, String type) {
//        return treeNodeService.getSwitchTree(companyId, type);
    	return null;
    }

    @Override
    public List<AvailableHighVoltageSwitch> switchsOfLine(Integer type,
                                 String lineId) {
        switch (type) {
            //高压
            case 1:
                if (lineId != null) {
                    return AvailableHighVoltageSwitch.change2VoList(
                            hvSwitchService
                                    .selectByParameters(MyBatisMapUtil.warp("line_id", lineId)));
                } else {
                    return AvailableHighVoltageSwitch.change2VoList(
                            hvSwitchService
                                    .selectByParameters(MyBatisMapUtil.warp(null)));
                }
        }
        return null;
    }

}
