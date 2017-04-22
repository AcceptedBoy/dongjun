package com.gdut.dongjun.service.manager;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

import org.springframework.stereotype.Component;

import com.gdut.dongjun.domain.po.User;
import com.gdut.dongjun.service.impl.ZTreeNode;

/**
 * 和用户状态有关的缓存类
 * 
 * @author Gordan_Deng
 * @date 2017年3月28日
 */
@Component
public class UserHolder {

	/*
	 * 用户id和用户实体类的映射
	 */
	private static final Map<String, User> allUserMap = new ConcurrentHashMap<String, User>();
	
	/*
	 * 用户id和设备树的映射
	 */
	private static final ConcurrentHashMap<String, Future<List<ZTreeNode>>> switchTreeCache = new ConcurrentHashMap<String, Future<List<ZTreeNode>>>();
	
	/**
	 * 增加已登录用户
	 * @param user
	 */
	public static void addUser(User user) {
		allUserMap.remove(user.getId());
		allUserMap.put(user.getId(), user);
	}

	/**
	 * 删除登录用户
	 * @param user
	 */
	public static void delUser(User user) {
		allUserMap.remove(user.getId());
	}

	/**
	 * 用户是否已登录
	 * @param id
	 * @return
	 */
	public static boolean isUserLogin(String id) {
		return allUserMap.containsKey(id);
	}
	
	/**
	 * 增加用户设备树
	 * @param userId
	 * @param ft
	 * @return
	 */
	public static Future<List<ZTreeNode>> addUserSwitchTree(String userId, FutureTask<List<ZTreeNode>> ft) {
		return switchTreeCache.putIfAbsent(userId, ft);
	}
	
	/**
	 * 去除用户设备树
	 * @param userId
	 */
	public static void removeUserSwitchTree(String userId) {
		switchTreeCache.remove(userId);
	}
	
	public static Future<List<ZTreeNode>> getUserSwitchTree(String userId) {
		return switchTreeCache.get(userId);
	}
	
//	public static List<ZTreeNode> getUserSwitchTre1(final String userId) {
//		while (true) {
//			Future<List<ZTreeNode>> f = switchTreeCache.get(userId);
//			if (null == f) {
//				//构造FutureTask回调函数
//				Callable<List<ZTreeNode>> eval = new Callable<List<ZTreeNode>>() {
//					@Override
//					public List<ZTreeNode> call() throws Exception {
//						return nodeService.getSwitchTree(userId);
//					}
//				};
//				FutureTask<List<ZTreeNode>> ft = new FutureTask<List<ZTreeNode>>(eval);
//				//如果刚好两个线程执行到这里，一个线程执行put方法，返回null；另一个线程执行get方法，
//				//返回前一个线程弄进去的value。那最后只会有一个线程启动ft.fun()，另一个线程进入f.get()。
//				//因此线程安全。
//				f = switchTreeCache.putIfAbsent(userId, ft);
//				if (null == f) {
//					f = ft;
//					ft.run();
//				}
//			}
//			try {
//				return f.get();
//			} catch (InterruptedException e) {
//				//有时候执行方法里面可能会想知道自己的线程是不是被中断了，如果是，在执行方法里面抛出这个方法。
//				//或者真的由于其他原因，比如OOM啊，SOF啊，中断了线程，也会抛出异常，在外面被捕捉
//				switchTreeCache.remove(userId);
//				e.printStackTrace();
//				throw launderThrowable(e.getCause()); 
//			} catch (ExecutionException e) {
//				switchTreeCache.remove(userId);
//				e.printStackTrace();
//				throw launderThrowable(e.getCause());
//			}
//		}
//	}
//	
//	private static RuntimeException launderThrowable(Throwable t) {
//		if (t instanceof RuntimeException) {
//			return (RuntimeException) t;
//		}
//		else if (t instanceof Error) {
//			throw (Error) t;
//		}
//		else {
//			throw new IllegalStateException("Not unchecked", t);
//		}
//	}
}
