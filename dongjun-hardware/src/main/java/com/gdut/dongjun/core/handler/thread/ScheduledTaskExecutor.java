package com.gdut.dongjun.core.handler.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

/**
 * TODO 用ROUND_TIME替代固定的时间
 * 这个类维护了一个List<List<ScheduledTask>>，是所有待执行任务的排队地方。
 * 同时这个类开启了能分发任务的线程。未完成，未测试，未上线。
 * @author Gordan_Deng
 * @date 2017年3月29日
 */
@Component
public class ScheduledTaskExecutor implements InitializingBean {
	
	public static final int ROUND_TIME = 3600;

	//执行指针会每秒移到ArrayList下一格，轮询里面的List<ScheduledTask>是否可以执行
	private static final List<List<ScheduledTask>> scheduledTaskList = 
			new ArrayList<List<ScheduledTask>>(ROUND_TIME);
	
	//执行指针
	private static AtomicInteger currentIndex = new AtomicInteger(0);
	
	//线程池
	private static ExecutorService fixedPool = ThreadPoolHolder.fixedPool; 

	private Logger logger = Logger.getLogger(ScheduledTaskExecutor.class);
	
	/**
	 * 添加任务
	 * @param task
	 */
	 public static void submit(ScheduledTask task) {
		 Integer serialNumber = (task.getExecuteTime() - ROUND_TIME * task.getRound() + currentIndex.get()) % ROUND_TIME;
		 task.setSerialNumber(serialNumber);
		 List<ScheduledTask> list = getTaskList(serialNumber);
		 list.add(task);
	 }
	 
	 private static List<ScheduledTask> getTaskList(Integer index) {
		 if (null == index || index < 0 || index >= ROUND_TIME) {
			 return null;
		 }
		 return scheduledTaskList.get(index);
	 }
	 
	 private List<ScheduledTask> getTaskList() {
		 return scheduledTaskList.get(currentIndex.get());
	 }

	@Override
	public void afterPropertiesSet() throws Exception {
		for (int i = 0; i < ROUND_TIME; i++) {
			List<ScheduledTask> list = new ArrayList<ScheduledTask>();
			scheduledTaskList.add(list);
		}
		//开启工作线程
		Thread t = new ExecutorThread();
		t.start();
	}
	
	class ExecutorThread extends Thread {

		@Override
		public void run() {
			while (true) {
				List<ScheduledTask> list = getTaskList();
				for (int i = 0; i < list.size(); i++) {
					ScheduledTask task = list.get(i);
					//删除不可用任务
					if (!task.isAvailable()) {
						list.remove(task);
						i--;
						continue;
					}
					if (0 == task.getRound()) {
						fixedPool.submit(task);
						list.remove(task);
						i--; //一边轮询一边删除，删除之后要将index往后退。
					} else {
						task.setRound(task.getRound() - 1);
					}
				}
				currentIndex.set((currentIndex.get() + 1) % ROUND_TIME);
				try {
					Thread.sleep(1000 * 1);
				} catch (InterruptedException e) {
					e.printStackTrace();
					logger.error(ScheduledTaskExecutor.class.getName() + "被中止，尝试重启线程");
					Thread t = new ExecutorThread();
					t.start();
				}
			}
		}
		
	}
}
