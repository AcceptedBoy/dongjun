package com.gdut.dongjun.service.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

/**
 * 这个类维护了一个List<List<ScheduledTask>>，是所有待执行任务的排队地方。
 * 同时这个类开启了能分发任务的线程。未完成，未测试，未上线。
 * @author Gordan_Deng
 * @date 2017年3月29日
 */
//@Component
public class ScheduledTaskExecutor implements InitializingBean {

	//执行指针会每秒移到ArrayList下一格，轮询里面的List<ScheduledTask>是否可以执行
	private List<List<ScheduledTask>> scheduledTaskList = new ArrayList<List<ScheduledTask>>(3600);
	
	//执行指针
	private static AtomicInteger currentIndex = new AtomicInteger(0);
	
	/**
	 * 添加任务
	 * @param task
	 */
	 public void addScheduledTask(ScheduledTask task) {
		 Integer serialNumber = (task.getExecuteTime() - 3600 * task.getRound() + currentIndex.get()) % 3600;
		 task.setSerialNumber(serialNumber);
		 List<ScheduledTask> list = getTaskList(serialNumber);
		 list.add(task);
	 }
	 
	 private List<ScheduledTask> getTaskList(Integer index) {
		 if (null == index || index < 0 || index >= 3600) {
			 return null;
		 }
		 return scheduledTaskList.get(index);
	 }
	 
	 private List<ScheduledTask> getTaskList() {
		 return scheduledTaskList.get(currentIndex.get());
	 }

	@Override
	public void afterPropertiesSet() throws Exception {
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
					if (0 == task.getRound()) {
						//execute TODO
						list.remove(task);
						i--; //一边轮询一边删除，删除之后要将index往后退。
					} else {
						task.setRound(task.getRound() - 1);
					}
				}
				currentIndex.set((currentIndex.get() + 1) % 3600);
				try {
					Thread.sleep(1000 * 1);
				} catch (InterruptedException e) {
					e.printStackTrace();
					//TODO 万一这个线程崩了，能干什么？重新开一个线程？
				}
			}
		}
		
	}
}
