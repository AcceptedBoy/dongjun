package com.gdut.dongjun.service.impl;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gdut.dongjun.domain.po.BigGroup;
import com.gdut.dongjun.domain.po.DeviceGroup;
import com.gdut.dongjun.domain.po.DeviceGroupMapping;
import com.gdut.dongjun.domain.po.PlatformGroup;
import com.gdut.dongjun.domain.po.TemperatureDevice;
import com.gdut.dongjun.domain.po.TemperatureSensor;
import com.gdut.dongjun.domain.po.User;
import com.gdut.dongjun.domain.po.UserDeviceMapping;
import com.gdut.dongjun.service.DeviceGroupMappingService;
import com.gdut.dongjun.service.DeviceGroupService;
import com.gdut.dongjun.service.PlatformGroupService;
import com.gdut.dongjun.service.UserDeviceMappingService;
import com.gdut.dongjun.service.UserService;
import com.gdut.dongjun.service.ZTreeNodeService;
import com.gdut.dongjun.service.device.TemperatureDeviceService;
import com.gdut.dongjun.service.device.TemperatureSensorService;
import com.gdut.dongjun.service.manager.UserHolder;
import com.gdut.dongjun.util.MyBatisMapUtil;
 
/**
 * 这个类用来返回ztree插件要用到的数据树。
 * 添加节点的顺序是BigGroup、PlatformGroup、DeviceGroup。
 * 各种设备和DeviceGroup处于同一层次的位置
 * 平台版没有高低压管控
 * @author Sherlock-lee
 * @author Gordan_Deng
 * @date 2017年3月10日
 */
@Component
public class ZTreeNodeServiceImpl implements ZTreeNodeService {

	@Autowired
	private PlatformGroupService platformGroupService;
	@Autowired
	private TemperatureDeviceService deviceService;
	@Autowired
	private DeviceGroupMappingService mappingService;
	@Autowired
	private DeviceGroupService deviceGroupService;
	@Autowired
	private TemperatureSensorService sensorService;
	@Autowired
	private PlatformGroupService pgService;
	@Autowired
	private UserDeviceMappingService UDMappingService;
	@Autowired
	private UserService userService;

	/**
	 * 返回用户的设备树。
	 * 这个方法是根据书上改写的，理论上线程安全。
	 * value保存的相当于一个执行任务，Future运算完成之后只会返回同一个结果。
	 * 之所以value保存的是Future而不是List<ZTreeNode>，是因为要根据Future来控制线程安全，
	 * 而不是用懒汉式加载数据，尽可能防止synchornized的性能损失。
	 * @param userId
	 * @return
	 */
	public List<ZTreeNode> getUserSwitchTree(final String userId) {
		while (true) {
			Future<List<ZTreeNode>> f = UserHolder.getUserSwitchTree(userId);
			if (null == f) {
				//构造FutureTask回调函数
				Callable<List<ZTreeNode>> eval = new Callable<List<ZTreeNode>>() {
					@Override
					public List<ZTreeNode> call() throws Exception {
						return getSwitchTree(userId);
					}
				};
				FutureTask<List<ZTreeNode>> ft = new FutureTask<List<ZTreeNode>>(eval);
				//如果刚好两个线程执行到这里，一个线程执行put方法，返回null；另一个线程执行get方法，
				//返回前一个线程弄进去的value。那最后只会有一个线程启动ft.fun()，另一个线程进入f.get()。
				//因此线程安全。
				f = UserHolder.addUserSwitchTree(userId, ft);
				if (null == f) {
					f = ft;
					ft.run();
				}
			}
			try {
				return f.get();
			} catch (InterruptedException e) {
				//有时候执行方法里面可能会想知道自己的线程是不是被中断了，如果是，在执行方法里面抛出这个方法。
				UserHolder.removeUserSwitchTree(userId);
				e.printStackTrace();
				throw launderThrowable(e.getCause()); 
			} catch (ExecutionException e) {
				UserHolder.removeUserSwitchTree(userId);
				e.printStackTrace();
				throw launderThrowable(e.getCause());
			}
		}
	}
	
	private static RuntimeException launderThrowable(Throwable t) {
		/**
		 * 如果是运行期异常返回
		 */
		if (t instanceof RuntimeException) {
			return (RuntimeException) t;
		}
		/**
		 * 如果是Error直接抛出
		 */
		else if (t instanceof Error) {
			throw (Error) t;
		}
		else {
			throw new IllegalStateException("Not unchecked", t);
		}
	}
	
	
	/**
	 * 超管返回所有大组、公司、设备
	 */
	@Override
	public List<ZTreeNode> getSwitchTree() {
//		List<BigGroup> groupList = groupService.selectByParameters(null);
//		return getAllSwitchTree(groupList);
		return null;
	}
	
	/**
	 * 普通公司返回当前公司、所属设备
	 * @return
	 */
	@Override
	public List<ZTreeNode> getSwitchTree(String userId) {
		User user = userService.selectByPrimaryKey(userId);
		List<PlatformGroup> pgList = pgService.selectByParameters(MyBatisMapUtil.warp("id", user.getCompanyId()));
		return wrap(pgList, null, userId);
	}
	
	private List<ZTreeNode> wrap(List<PlatformGroup> pgList, String parentName, String userId) {
		List<ZTreeNode> pgNodes = new LinkedList<ZTreeNode>();
		for (int j = 0; j < pgList.size(); j++) {
			ZTreeNode n2 = new ZTreeNode();
			if (pgList.get(j) != null) {
				n2.setId(pgList.get(j).getId() + "");
				n2.setName(pgList.get(j).getName());
				if (!(null == parentName || "".equals(parentName))) {
					n2.setParentName(parentName);
				}
				List<ZTreeNode> dgNodes = new LinkedList<ZTreeNode>();
				
				List<DeviceGroup> dgList = 
						deviceGroupService.selectByParameters(MyBatisMapUtil.warp("platform_group_id", pgList.get(j).getId()));
				
				for (DeviceGroup dg : dgList) {
					dgNodes.add(getDeviceGroupNode(pgList.get(j), dg));
				}

				//原本是获取当前Session再获取currentUser，现在改为入参
//				User user  = (User) RequestHolder.getRequest().getSession().getAttribute("currentUser");
				List<UserDeviceMapping> allMappings = UDMappingService.selectMappingEnableToSeeByUserId(userId);
				List<String> lowIds = new ArrayList<String>();
				List<String> highIds = new ArrayList<String>();
				List<String> temIds = new ArrayList<String>();
				for (UserDeviceMapping mapping : allMappings) {
					switch(mapping.getDeviceType()) {
					case 0 : lowIds.add(mapping.getDeviceId()); break;
					case 1 : highIds.add(mapping.getDeviceId()); break;
					case 2 : break;
					case 3 : temIds.add(mapping.getDeviceId()); break;
					default : break;
					}
				}
				
				//低压
//				List<LowVoltageSwitch> lowSwitchs = switchService
//						.selectByParameters(MyBatisMapUtil
//								.warp("group_id", pgList.get(j).getId()));
//				for (int k = 0; k < lowSwitchs.size(); k++) {
//
//					ZTreeNode n3 = new ZTreeNode();
//					if (lowSwitchs.get(k) != null) {
//						n3.setId(lowSwitchs.get(k).getId());
//						n3.setName(lowSwitchs.get(k).getName());
//						n3.setParentName(pgList.get(j).getName());
//						n3.setPlatformGroupId(lowSwitchs.get(k).getGroupId() + "");
//						n3.setAddress(lowSwitchs.get(k).getAddress());
//						n3.setType(0);
//						n3.setShowName(lowSwitchs.get(k).getShowName());
//					}
//					dgNodes.add(n3);
//				}
//				
//				//高压
//				List<HighVoltageSwitch> highSwitches = switchService2
//						.selectByParameters(MyBatisMapUtil
//								.warp("group_id", pgList.get(j)
//										.getId()));// 取到所有的开关
//				// 遍历所有的开关
//				for (int k = 0; k < highSwitches.size(); k++) {
//
//					ZTreeNode n3 = new ZTreeNode();
//					if (highSwitches.get(k) != null) {
//
//						n3.setId(highSwitches.get(k).getId());
//						n3.setName(highSwitches.get(k).getName());
//						n3.setParentName(pgList.get(j).getName());
//						n3.setPlatformGroupId(highSwitches.get(k).getGroupId() + "");
//						n3.setAddress(highSwitches.get(k).getAddress());
//						n3.setType(1);
//						n3.setShowName(highSwitches.get(k).getShowName());
//					}
//					dgNodes.add(n3);
//				}
//				
//				//管控
//				List<ControlMearsureSwitch> controlSwitchs = switchService3
//						.selectByParameters(MyBatisMapUtil
//								.warp("group_id", pgList.get(j)
//										.getId()));// 取到所有的开关
//				// 遍历所有的开关
//				for (int k = 0; k < controlSwitchs.size(); k++) {
//
//					ZTreeNode n3 = new ZTreeNode();
//					if (controlSwitchs.get(k) != null) {
//
//						n3.setId(controlSwitchs.get(k).getId());
//						n3.setName(controlSwitchs.get(k).getName());
//						n3.setParentName(pgList.get(j).getName());
//						n3.setPlatformGroupId(controlSwitchs.get(k).getGroupId() + "");
//						n3.setAddress(controlSwitchs.get(k).getAddress());
//						n3.setType(2);
//						n3.setShowName(controlSwitchs.get(k).getShowName());
//					}
//					dgNodes.add(n3);
//				}
				
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
								n4.setId(sensors.get(l).getTag() + "");
								n4.setName(sensors.get(l).getName());
								n4.setParentName(devices.get(k).getName());
								n4.setType(80);
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
		return pgNodes;
	}
	
	
	public List<ZTreeNode> getAllSwitchTree(List<BigGroup> groupList) {
		List<ZTreeNode> nodes = new LinkedList<ZTreeNode>();
		for (int i = 0; i < groupList.size(); i++) {
			ZTreeNode n1 = new ZTreeNode();
			if (groupList.get(i) != null) {
				n1.setId(groupList.get(i).getId() + "");
				n1.setName(groupList.get(i).getName());
				n1.setParentName(null);
				List<PlatformGroup> pgList = platformGroupService.selectByParameters(MyBatisMapUtil.warp("group_id", groupList.get(i).getId()));
				List<ZTreeNode> pgNodes = wrap(pgList, groupList.get(i).getName(), null);
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
	
	private ZTreeNode getDeviceGroupNode(PlatformGroup group, DeviceGroup dg) {
		List<ZTreeNode> list = new LinkedList<ZTreeNode>();
		ZTreeNode dgNode = new ZTreeNode();
		dgNode.setId(dg.getId() + "");
		dgNode.setName(dg.getName());
		dgNode.setParentName(group.getName());
		
		List<DeviceGroupMapping> mappingList = mappingService.selectByParameters(MyBatisMapUtil.warp("device_group_id", dg.getId()));
		for (DeviceGroupMapping mapping : mappingList) {
			switch(mapping.getType()) {
//			case 0:	//低压
//				LowVoltageSwitch low = switchService.selectByPrimaryKey(mapping.getDeviceId());
//				ZTreeNode node1 = new ZTreeNode();
//				node1.setId(low.getId());
//				node1.setName(low.getName());
//				node1.setParentName(dg.getName());
//				node1.setPlatformGroupId(low.getGroupId() + "");
//				node1.setAddress(low.getAddress());
//				node1.setType(0);
//				node1.setShowName(low.getShowName());
//				list.add(node1);
//				break;
//			case 1:
//				//高压
//				ZTreeNode node2 = new ZTreeNode();
//				HighVoltageSwitch high = switchService2.selectByPrimaryKey(mapping.getDeviceId());
//				node2.setId(high.getId());
//				node2.setName(high.getName());
//				node2.setParentName(dg.getName());
//				node2.setPlatformGroupId(high.getGroupId() + "");
//				node2.setAddress(high.getAddress());
//				node2.setType(1);
//				node2.setShowName(high.getShowName());
//				list.add(node2);
//				break;
//			case 2:
//				//管控
//				ZTreeNode node3 = new ZTreeNode();
//				ControlMearsureSwitch control = switchService3.selectByPrimaryKey(mapping.getDeviceId());
//				node3.setId(control.getId());
//				node3.setName(control.getName());
//				node3.setParentName(dg.getName());
//				node3.setPlatformGroupId(control.getGroupId() + "");
//				node3.setAddress(control.getAddress());
//				node3.setType(2);
//				node3.setShowName(control.getShowName());
//				list.add(node3);
//				break;
			case 3:
				//温度
				ZTreeNode node4 = new ZTreeNode();
				TemperatureDevice device = deviceService.selectByPrimaryKey(mapping.getDeviceId());
				node4.setId(device.getId());
				node4.setName(device.getName());
				node4.setParentName(dg.getName());
				node4.setPlatformGroupId(device.getGroupId() + "");
				node4.setAddress(device.getAddress());
				node4.setType(3);
				node4.setShowName(device.getName());
				
				List<ZTreeNode> sNodes = new LinkedList<ZTreeNode>();
				List<TemperatureSensor> sensors = sensorService.selectByParameters(MyBatisMapUtil.warp("device_id", device.getId()));
				for (TemperatureSensor sensor : sensors) {
					ZTreeNode sNode = new ZTreeNode();
					sNode.setId(sensor.getTag() + "");
					sNode.setName(sensor.getName());
					sNode.setParentName(node4.getName());
					sNode.setType(80);
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
