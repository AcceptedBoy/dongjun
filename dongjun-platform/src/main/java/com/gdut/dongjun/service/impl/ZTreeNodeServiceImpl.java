package com.gdut.dongjun.service.impl;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gdut.dongjun.domain.po.BigGroup;
import com.gdut.dongjun.domain.po.ControlMearsureSwitch;
import com.gdut.dongjun.domain.po.DeviceGroup;
import com.gdut.dongjun.domain.po.DeviceGroupMapping;
import com.gdut.dongjun.domain.po.HighVoltageSwitch;
import com.gdut.dongjun.domain.po.LowVoltageSwitch;
import com.gdut.dongjun.domain.po.PlatformGroup;
import com.gdut.dongjun.domain.po.TemperatureDevice;
import com.gdut.dongjun.domain.po.TemperatureSensor;
import com.gdut.dongjun.service.BigGroupService;
import com.gdut.dongjun.service.DeviceGroupMappingService;
import com.gdut.dongjun.service.DeviceGroupService;
import com.gdut.dongjun.service.PlatformGroupService;
import com.gdut.dongjun.service.ZTreeNodeService;
import com.gdut.dongjun.service.device.ControlMearsureSwitchService;
import com.gdut.dongjun.service.device.HighVoltageSwitchService;
import com.gdut.dongjun.service.device.LowVoltageSwitchService;
import com.gdut.dongjun.service.device.TemperatureDeviceService;
import com.gdut.dongjun.service.device.TemperatureSensorService;
import com.gdut.dongjun.service.device.temperature.TemperatureMeasureService;
import com.gdut.dongjun.util.MyBatisMapUtil;

/**
 * 这个类用来返回ztree插件要用到的数据树。
 * 添加节点的顺序是BigGroup、PlatformGroup、DeviceGroup。
 * 各种设备和DeviceGroup处于同一层次的位置
 * @author 历代大佬
 * @date 2016年11月26日
 */
@Component
public class ZTreeNodeServiceImpl implements ZTreeNodeService {

	@Autowired
	private PlatformGroupService platformGroupService;
	@Autowired
	private BigGroupService groupService;
	@Autowired
	private LowVoltageSwitchService switchService;
	@Autowired
	private HighVoltageSwitchService switchService2;
	@Autowired
	private ControlMearsureSwitchService switchService3;
	@Autowired
	private TemperatureDeviceService deviceService;
	@Autowired
	private TemperatureMeasureService measureService;
	@Autowired
	private DeviceGroupMappingService mappingService;
	@Autowired
	private DeviceGroupService deviceGroupService;
	@Autowired
	private TemperatureSensorService sensorService;
	
	@Override
	public List<ZTreeNode> getSwitchTree(String company_id, String type) {
		//TODO，以后根据公司来返回数据
		List<BigGroup> groupList = groupService.selectByParameters(null);
		return getSwitchTree(groupList);
	}
	
	
	public List<ZTreeNode> getSwitchTree(List<BigGroup> groupList) {
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
						n2.setId(pgList.get(j).getId() + "");
						n2.setName(pgList.get(j).getName());
						n2.setParentName(groupList.get(i).getName());
						List<ZTreeNode> dgNodes = new LinkedList<ZTreeNode>();
						
						List<DeviceGroup> dgList = 
								deviceGroupService.selectByParameters(MyBatisMapUtil.warp("platform_group_id", pgList.get(j).getId()));
						
						for (DeviceGroup dg : dgList) {
							dgNodes.add(getDeviceGroupNode(pgList.get(j), dg));
						}

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
								n3.setPlatformGroupId(lowSwitchs.get(k).getGroupId() + "");
								n3.setAddress(lowSwitchs.get(k).getAddress());
								n3.setType(0);
								n3.setShowName(lowSwitchs.get(k).getShowName());
							}
							dgNodes.add(n3);
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
								n3.setPlatformGroupId(highSwitches.get(k).getGroupId() + "");
								n3.setAddress(highSwitches.get(k).getAddress());
								n3.setType(1);
								n3.setShowName(highSwitches.get(k).getShowName());
							}
							dgNodes.add(n3);
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
								n3.setPlatformGroupId(controlSwitchs.get(k).getGroupId() + "");
								n3.setAddress(controlSwitchs.get(k).getAddress());
								n3.setType(2);
								n3.setShowName(controlSwitchs.get(k).getShowName());
							}
							dgNodes.add(n3);
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
								n3.setPlatformGroupId(devices.get(k).getGroupId() + "");
								n3.setAddress(devices.get(k).getAddress());
								n3.setType(3);
								n3.setShowName(devices.get(k).getName());
								List<TemperatureSensor> sensors = sensorService.selectByParameters(MyBatisMapUtil.warp("device_id", devices.get(k).getId()));
								List<ZTreeNode> sensorNodes = new LinkedList<ZTreeNode>();
								for (int l = 0; l < sensors.size(); l++) {
									
									ZTreeNode n4 = new ZTreeNode();
									if (sensors.get(l) != null) {
										n4.setId(sensors.get(l).getId());
										n4.setName(sensors.get(l).getName());
										n4.setParentName(devices.get(k).getName());
										n4.setType(10);
									}
									sensorNodes.add(n4);
								}
								if (sensorNodes != null && sensorNodes.size() != 0) {
									n3.setChildren(sensorNodes);
								}
							}
							dgNodes.add(n3);
						}
						if(dgNodes != null && dgNodes.size() != 0) {
							n2.setChildren(dgNodes);
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

	public List<ZTreeNode> groupTree(String companyId, Integer deviceType) {
		// TODO Auto-generated method stub
		return null;
	}
	
//	private List<Integer> getSensor(TemperatureDevice device) {
//		List<TemperatureMeasure> list = measureService.selectByParameters(MyBatisMapUtil.warp("device_id", device.getId()));
//		HashSet<Integer> set = new HashSet<Integer>();
//		for (TemperatureMeasure measure : list) {
//			set.add(measure.getTag());
//		}
//		List<Integer> value = new ArrayList<Integer>();
//		value.addAll(set);
//		return value;
//	}
	
	private ZTreeNode getDeviceGroupNode(PlatformGroup group, DeviceGroup dg) {
		List<ZTreeNode> list = new LinkedList<ZTreeNode>();
		ZTreeNode dgNode = new ZTreeNode();
		dgNode.setId(dg.getId() + "");
		dgNode.setName(dg.getName());
		dgNode.setParentName(group.getName());
		
		List<DeviceGroupMapping> mappingList = mappingService.selectByParameters(MyBatisMapUtil.warp("device_group_id", dg.getId()));
		for (DeviceGroupMapping mapping : mappingList) {
			switch(mapping.getType()) {
			case 0:	//低压
				LowVoltageSwitch low = switchService.selectByPrimaryKey(mapping.getDeviceId());
				ZTreeNode node1 = new ZTreeNode();
				node1.setId(low.getId());
				node1.setName(low.getName());
				node1.setParentName(dg.getName());
				node1.setPlatformGroupId(low.getGroupId() + "");
				node1.setAddress(low.getAddress());
				node1.setType(0);
				node1.setShowName(low.getShowName());
				list.add(node1);
				break;
			case 1:
				//高压
				ZTreeNode node2 = new ZTreeNode();
				HighVoltageSwitch high = switchService2.selectByPrimaryKey(mapping.getDeviceId());
				node2.setId(high.getId());
				node2.setName(high.getName());
				node2.setParentName(dg.getName());
				node2.setPlatformGroupId(high.getGroupId() + "");
				node2.setAddress(high.getAddress());
				node2.setType(0);
				node2.setShowName(high.getShowName());
				list.add(node2);
				break;
			case 2:
				//管控
				ZTreeNode node3 = new ZTreeNode();
				ControlMearsureSwitch control = switchService3.selectByPrimaryKey(mapping.getDeviceId());
				node3.setId(control.getId());
				node3.setName(control.getName());
				node3.setParentName(dg.getName());
				node3.setPlatformGroupId(control.getGroupId() + "");
				node3.setAddress(control.getAddress());
				node3.setType(0);
				node3.setShowName(control.getShowName());
				list.add(node3);
				break;
			case 3:
				//温度
				ZTreeNode node4 = new ZTreeNode();
				TemperatureDevice device = deviceService.selectByPrimaryKey(mapping.getDeviceId());
				node4.setId(device.getId());
				node4.setName(device.getName());
				node4.setParentName(dg.getName());
				node4.setPlatformGroupId(device.getGroupId() + "");
				node4.setAddress(device.getAddress());
				node4.setType(0);
				node4.setShowName(device.getName());
				
				List<ZTreeNode> sNodes = new LinkedList();
				List<TemperatureSensor> sensors = sensorService.selectByParameters(MyBatisMapUtil.warp("device_id", device.getId()));
				for (TemperatureSensor sensor : sensors) {
					ZTreeNode sNode = new ZTreeNode();
					sNode.setId(sensor.getTag() + "");
					sNode.setName(sensor.getName());
					sNode.setParentName(node4.getName());
					sNode.setType(10);
					sNodes.add(sNode);
				}
				node4.setChildren(sNodes);
				
				list.add(node4);
				break;
			default:
				break;
			}
		}
		dgNode.setChildren(list);
		return dgNode;
	}
				
}
