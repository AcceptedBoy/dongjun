package com.gdut.dongjun.service.impl;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gdut.dongjun.domain.po.ControlMearsureSwitch;
import com.gdut.dongjun.domain.po.HighVoltageSwitch;
import com.gdut.dongjun.domain.po.LowVoltageSwitch;
import com.gdut.dongjun.domain.po.PlatformGroup;
import com.gdut.dongjun.domain.po.TemperatureDevice;
import com.gdut.dongjun.domain.po.group;
import com.gdut.dongjun.service.ControlMearsureSwitchService;
import com.gdut.dongjun.service.HighVoltageSwitchService;
import com.gdut.dongjun.service.LowVoltageSwitchService;
import com.gdut.dongjun.service.PlatformGroupService;
import com.gdut.dongjun.service.TemperatureDeviceService;
import com.gdut.dongjun.service.ZTreeNodeService;
import com.gdut.dongjun.service.groupService;
import com.gdut.dongjun.util.MyBatisMapUtil;

@Component
public class ZTreeNodeServiceImpl implements ZTreeNodeService {

	@Autowired
	private PlatformGroupService platformGroupService;
	@Autowired
	private groupService groupService;
	@Autowired
	private LowVoltageSwitchService switchService;
	@Autowired
	private HighVoltageSwitchService switchService2;
	@Autowired
	private ControlMearsureSwitchService switchService3;
	@Autowired
	private TemperatureDeviceService deviceService;
	
	@Override
	public List<ZTreeNode> getSwitchTree(String company_id, String type) {
		List<group> groupList = groupService.selectByParameters(MyBatisMapUtil.warp("company_id", company_id));
		return getSwitchTree(groupList);
	}

	public List<ZTreeNode> getSwitchTree(List<group> groupList) {
		List<ZTreeNode> nodes = new LinkedList<ZTreeNode>();
		for (int i = 0; i < groupList.size(); i++) {
			ZTreeNode n1 = new ZTreeNode();
			if (groupList.get(i) != null) {
				n1.setId(groupList.get(i).getId() + "");
				n1.setName(groupList.get(i).getName());
				n1.setParentName(null);
				List<PlatformGroup> pgList = platformGroupService.selectByParameters(MyBatisMapUtil.warp("group_id", groupList.get(i).getId()));
				List<ZTreeNode> pgNodes = new LinkedList<ZTreeNode>();
				for (int j = 0; j < pgList.size(); j++) {
					ZTreeNode n2 = new ZTreeNode();
					if (pgList.get(j) != null) {
						n2.setId(pgNodes.get(j).getId());
						n2.setName(pgNodes.get(j).getName());
						n2.setParentName(groupList.get(i).getName());
						List<ZTreeNode> switchNodes = new LinkedList<ZTreeNode>();
						
						//低压
						List<LowVoltageSwitch> lowSwitchs = switchService
								.selectByParameters(MyBatisMapUtil
										.warp("group_id", pgList.get(j).getId()));
						for (int k = 0; k < lowSwitchs.size(); k++) {

							ZTreeNode n3 = new ZTreeNode();
							if (lowSwitchs.get(k) != null) {
								n3.setId(lowSwitchs.get(k).getId());
								n3.setName(lowSwitchs.get(k).getName());
								n3.setParentName(pgList.get(j).getName());
								n3.setLongitude(lowSwitchs.get(k)
										.getLongitude().toString());
								n3.setLatitude(lowSwitchs.get(k)
										.getLatitude().toString());
								n3.setLineId(lowSwitchs.get(k).getLineId());
								n3.setAddress(lowSwitchs.get(k).getAddress());
								n3.setType(0);
								n3.setShowName(lowSwitchs.get(k).getShowName());
							}
							switchNodes.add(n3);
						}
						
						//高压
						List<HighVoltageSwitch> highSwitches = switchService2
								.selectByParameters(MyBatisMapUtil
										.warp("group_id", pgList.get(j)
												.getId()));// 取到所有的开关
						// 遍历所有的开关
						for (int k = 0; k < highSwitches.size(); k++) {

							ZTreeNode n3 = new ZTreeNode();
							if (highSwitches.get(k) != null) {

								n3.setId(highSwitches.get(k).getId());
								n3.setName(highSwitches.get(k).getName());
								n3.setParentName(pgList.get(j).getName());
								//n3.setParentName(switchs2.get(k).getName());
								n3.setLongitude(highSwitches.get(k)
										.getLongitude().toString());
								n3.setLatitude(highSwitches.get(k)
										.getLatitude().toString());
								n3.setLineId(highSwitches.get(k).getLineId());
								n3.setAddress(highSwitches.get(k).getAddress());
								n3.setType(1);
								n3.setShowName(highSwitches.get(k).getShowName());
							}
							switchNodes.add(n3);
						}
						
						//管控
						List<ControlMearsureSwitch> controlSwitchs = switchService3
								.selectByParameters(MyBatisMapUtil
										.warp("group_id", pgList.get(j)
												.getId()));// 取到所有的开关
						// 遍历所有的开关
						for (int k = 0; k < controlSwitchs.size(); k++) {

							ZTreeNode n3 = new ZTreeNode();
							if (controlSwitchs.get(k) != null) {

								n3.setId(controlSwitchs.get(k).getId());
								n3.setName(controlSwitchs.get(k).getName());
								n3.setParentName(pgList.get(j).getName());
								n3.setLongitude(controlSwitchs.get(k)
										.getLongitude().toString());
								n3.setLatitude(controlSwitchs.get(k)
										.getLatitude().toString());
								n3.setLineId(controlSwitchs.get(k).getLineId());
								n3.setAddress(controlSwitchs.get(k).getAddress());
								n3.setType(2);
								n3.setShowName(controlSwitchs.get(k).getShowName());
							}
							switchNodes.add(n3);
						}
						
						//温度
						List<TemperatureDevice> devices = 
								deviceService.selectByParameters(MyBatisMapUtil
										.warp("group_id", pgList.get(j).getId()));
						
						for (int k = 0; k < devices.size(); k++) {

							ZTreeNode n3 = new ZTreeNode();
							if (devices.get(k) != null) {

								n3.setId(devices.get(k).getId());
								n3.setName(devices.get(k).getName());
								n3.setParentName(pgList.get(j).getName());
//								n3.setLongitude(devices.get(k)
//										.getLongitude().toString());
//								n3.setLatitude(devices.get(k)
//										.getLatitude().toString());
//								n3.setLineId(devices.get(k).getLineId());
								n3.setAddress(devices.get(k).getAddress());
								n3.setType(3);
//								n3.setShowName(devices.get(k).getShowName());
							}
							switchNodes.add(n3);
						}
						if(switchNodes != null && switchNodes.size() != 0) {
							n2.setChildren(switchNodes);
						}
					}
					if(n2 != null && n2.getChildren() != null && !n2.getChildren().isEmpty()) {
						pgNodes.add(n2);
					}
				}
				if(pgNodes != null && !pgNodes.isEmpty()) {
					n1.setChildren(pgNodes);
				}
			}
			if(n1 != null && n1.getChildren() != null && !n1.getChildren().isEmpty()) {
				nodes.add(n1);
			}
		}
		return nodes;
	}

	@Override
	public List<ZTreeNode> groupTree(String companyId, Integer deviceType) {
		// TODO Auto-generated method stub
		return null;
	}
				
}
