package com.gdut.dongjun.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gdut.dongjun.domain.po.BigGroup;
import com.gdut.dongjun.domain.po.DataMonitor;
import com.gdut.dongjun.domain.po.DeviceGroup;
import com.gdut.dongjun.domain.po.DeviceGroupMapping;
import com.gdut.dongjun.domain.po.PlatformGroup;
import com.gdut.dongjun.domain.po.User;
import com.gdut.dongjun.service.DeviceGroupMappingService;
import com.gdut.dongjun.service.DeviceGroupService;
import com.gdut.dongjun.service.PlatformGroupService;
import com.gdut.dongjun.service.UserDeviceMappingService;
import com.gdut.dongjun.service.UserService;
import com.gdut.dongjun.service.ZTreeNodeService;
import com.gdut.dongjun.service.device.DataMonitorService;
import com.gdut.dongjun.service.device.TemperatureSensorService;
import com.gdut.dongjun.service.manager.UserHolder;
import com.gdut.dongjun.util.MyBatisMapUtil;

/**
 * 这个类用来返回ztree插件要用到的数据树。 添加节点的顺序是BigGroup、PlatformGroup、DeviceGroup。
 * 各种设备和DeviceGroup处于同一层次的位置 平台版没有高低压管控
 * 
 * @author Sherlock-lee
 * @author Gordan_Deng
 * @date 2017年3月10日
 */
@Component
public class ZTreeNodeServiceImpl implements ZTreeNodeService {

	@Autowired
	private PlatformGroupService platformGroupService;
	@Autowired
	private DeviceGroupMappingService mappingService;
	@Autowired
	private DeviceGroupService deviceGroupService;
	@Autowired
	private TemperatureSensorService sensorService;
	@Autowired
	private PlatformGroupService pgService;
	@Autowired
	private UserDeviceMappingService userMappingService;
	@Autowired
	private UserService userService;
	@Autowired
	private DataMonitorService monitorService;

	/**
	 * 返回用户的设备树。 这个方法是根据书上改写的，理论上线程安全。 value保存的相当于一个执行任务，Future运算完成之后只会返回同一个结果。
	 * 之所以value保存的是Future而不是List<ZTreeNode>，是因为要根据Future来控制线程安全，
	 * 而不是用懒汉式加载数据，尽可能防止synchornized的性能损失。
	 * 
	 * @param userId
	 * @return
	 */
	public List<ZTreeNode> getUserSwitchTree(final String userId) {
		while (true) {
			Future<List<ZTreeNode>> f = UserHolder.getUserSwitchTree(userId);
			if (null == f) {
				// 构造FutureTask回调函数
				Callable<List<ZTreeNode>> eval = new Callable<List<ZTreeNode>>() {
					@Override
					public List<ZTreeNode> call() throws Exception {
						return getSwitchTree(userId);
					}
				};
				FutureTask<List<ZTreeNode>> ft = new FutureTask<List<ZTreeNode>>(eval);
				// 如果刚好两个线程执行到这里，一个线程执行put方法，返回null；另一个线程执行get方法，
				// 返回前一个线程弄进去的value。那最后只会有一个线程启动ft.fun()，另一个线程进入f.get()。
				// 因此线程安全。
				f = UserHolder.addUserSwitchTree(userId, ft);
				if (null == f) {
					f = ft;
					ft.run();
				}
			}
			try {
				return f.get();
			} catch (InterruptedException e) {
				// 有时候执行方法里面可能会想知道自己的线程是不是被中断了，如果是，在执行方法里面抛出这个方法。
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
		} else {
			throw new IllegalStateException("Not unchecked", t);
		}
	}

	/**
	 * 超管返回所有大组、公司、设备
	 */
	@Override
	public List<ZTreeNode> getSwitchTree() {
		// List<BigGroup> groupList = groupService.selectByParameters(null);
		// return getAllSwitchTree(groupList);
		return null;
	}

	/**
	 * 普通公司返回当前公司、所属设备
	 * 
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

				List<DeviceGroup> dgList = deviceGroupService
						.selectByParameters(MyBatisMapUtil.warp("platform_group_id", pgList.get(j).getId()));

				List<String> ids = userMappingService.selectMonitorIdByUserId(userId);
				for (DeviceGroup dg : dgList) {
					dgNodes.add(getDeviceGroupNode(pgList.get(j), dg, ids));
				}

				List<DataMonitor> monitors = monitorService
						.selectByParameters(MyBatisMapUtil.warp("group_id", pgList.get(j).getId()));
				Subject sub = SecurityUtils.getSubject();
				boolean isAdm = false;
				if (sub.hasRole("platform_group_admin")) {
					isAdm = true;
				}
				for (int k = 0; k < monitors.size(); k++) {
					if (isAdm || ids.contains(monitors.get(k).getId())) {
						ZTreeNode n3 = new ZTreeNode();
						if (monitors.get(k) != null) {

							n3.setId(monitors.get(k).getId());
							n3.setName(monitors.get(k).getName());
							n3.setParentName(pgList.get(j).getName());
							n3.setPlatformGroupId(monitors.get(k).getGroupId());
						}
						dgNodes.add(n3);
					}
				}
				if (dgNodes != null && dgNodes.size() != 0) {
					n2.setChildren(dgNodes);
				}
			}
			if (n2 != null && n2.getChildren() != null && !n2.getChildren().isEmpty()) {
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
				List<PlatformGroup> pgList = platformGroupService
						.selectByParameters(MyBatisMapUtil.warp("group_id", groupList.get(i).getId()));
				List<ZTreeNode> pgNodes = wrap(pgList, groupList.get(i).getName(), null);
				if (pgNodes != null && !pgNodes.isEmpty()) {
					n1.setChildren(pgNodes);
				}
			}
			if (n1 != null && n1.getChildren() != null && !n1.getChildren().isEmpty()) {
				nodes.add(n1);
			}
		}
		return nodes;
	}

	private ZTreeNode getDeviceGroupNode(PlatformGroup group, DeviceGroup dg, List<String> ids) {
		List<ZTreeNode> list = new LinkedList<ZTreeNode>();
		ZTreeNode dgNode = new ZTreeNode();
		dgNode.setId(dg.getId() + "");
		dgNode.setName(dg.getName());
		dgNode.setParentName(group.getName());

		List<DeviceGroupMapping> mappingList = mappingService
				.selectByParameters(MyBatisMapUtil.warp("device_group_id", dg.getId()));
		Subject subject = SecurityUtils.getSubject();
		boolean isAdm = false;
		if (subject.hasRole("platform_group_admin")) {
			isAdm = true;
		}
		for (DeviceGroupMapping mapping : mappingList) {
			if (isAdm || ids.contains(mapping.getDeviceId())) {
				ZTreeNode node4 = new ZTreeNode();
				DataMonitor monitor = monitorService.selectByPrimaryKey(mapping.getDeviceId());
				node4.setId(monitor.getId());
				node4.setName(monitor.getName());
				node4.setPlatformGroupId(group.getId());
				node4.setOpen(true);
				list.add(node4);
			}
		}
		dgNode.setChildren(list);
		return dgNode;
	}

}
