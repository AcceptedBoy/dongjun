package com.gdut.dongjun.service.push;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.directwebremoting.Browser;
import org.directwebremoting.ScriptBuffer;
import org.directwebremoting.ScriptSession;

import com.gdut.dongjun.domain.vo.ActiveHighSwitch;
import com.gdut.dongjun.service.net_server.CtxStore;
import com.gdut.dongjun.service.net_server.SwitchGPRS;
import com.gdut.dongjun.util.JsonUtil;

public class MessagePush {
	
	/*public void readHvStatus(final String id) throws InterruptedException {
		System.out.println(id);
		while(true) {
			Thread.sleep(8000);
			Browser.withAllSessions(new Runnable() {
				private ScriptBuffer script = new ScriptBuffer();
				@Override
				public void run() {
					
					script.appendCall("hvStatusShow", CtxStore.getStatusbyId(id));
					Collection<ScriptSession> sessions = Browser
							.getTargetSessions();
					for (ScriptSession scriptSession : sessions) {
						scriptSession.addScript(script);
					}
				}
			});
		}
	}*/
	private static List<ActiveHighSwitch> oldList = new ArrayList<>();
	private static List<ActiveHighSwitch> newList = new ArrayList<>();
	private static List<ActiveHighSwitch> diff = new ArrayList<>();
	
	private List<ActiveHighSwitch> getFirstActiveHighSwitch() {
		
		List<SwitchGPRS> switchs = CtxStore.getInstance();
		List<ActiveHighSwitch> list = new ArrayList<>();
		for(SwitchGPRS s : switchs) {
			
			list.add(getActiveHighSwitch(s));
		}
		return list;
	}
	
	private ActiveHighSwitch getActiveHighSwitch(SwitchGPRS s) {
		
		ActiveHighSwitch as = new ActiveHighSwitch();
		as.setId(s.getId());
		as.setOpen(s.isOpen());
		as.setStatus(getStatus(s.getId()));
		return as;
	}
	
	private String getStatus(String id) {
		
		return CtxStore.getStatusbyId(id) == null ? "null" : 
			CtxStore.getStatusbyId(id).getStatus();
	}
	/**
	 * @description TODO
	 * 
	 */
	private void diffMessage() {
		/**
		 * 1 == open从true到false
		 * 2 == open从false到true
		 * 3 == status从00到01
		 * 4 == status从01到00
		 */
		diff.clear();
		//List<ActiveHighSwitch> result = new ArrayList<>();
		List<SwitchGPRS> switchs = CtxStore.getInstance();
		ActiveHighSwitch as = null;
		int length = oldList.size();
		
		for(SwitchGPRS s : switchs) {
			int i;
			for(i = length - 1; i >= 0; --i) {
				as = oldList.get(i);
				if(s.getId() != null && as.getId() == s.getId()) {

					if(!as.getStatus().equals(getStatus(s.getId())) 
							|| as.isOpen() != s.isOpen()) {
						
						as = getActiveHighSwitch(s);
						diff.add(as);
					}
					newList.add(as);
					break;
				}
			}
			if(i < 0 && s.getId() != null) {
				as = getActiveHighSwitch(s);
				diff.add(as);
				newList.add(getActiveHighSwitch(s));
			}
		}
		oldList = newList;
	}
	
	/*private boolean isPush() throws InterruptedException {
		
		diffMessage();
		if(diff != null) {
			return true;
		} else {
			return false;
		}
	}*/
	
	public void hitchEventSpy() throws InterruptedException {
		
		while(true) {
			
			Thread.sleep(6000);
			/*if(isPush()) {*/
				Browser.withAllSessions(new Runnable() {
					private ScriptBuffer script = new ScriptBuffer();
					@Override
					public void run() {
						diffMessage();
						script.appendCall("showHitchEvent", JsonUtil.toJsonString(getFirstActiveHighSwitch()));
	
						Collection<ScriptSession> sessions = Browser
								.getTargetSessions();
						for (ScriptSession scriptSession : sessions) {
							scriptSession.addScript(script);
						}
					}
				});
			/*}*/
		}
	}
	
	/*public void send() throws InterruptedException {

		while (true) {
			Thread.sleep(10000);
			Runnable run = new Runnable() {
				private ScriptBuffer script = new ScriptBuffer();
				
				public void run() {
					// 设置要调用的 js及参数
					script.appendCall("alarm", 12);
					// 得到所有ScriptSession
					Collection<ScriptSession> sessions = Browser
							.getTargetSessions();
					// 遍历每一个ScriptSession
					for (ScriptSession scriptSession : sessions) {
						scriptSession.addScript(script);
					}
				}
			};
			// 执行推送
			Browser.withAllSessions(run);
		}
	}*/

	
	/*<script type="text/javascript" src="/dwr/engine.js"></script>
	<script type="text/javascript" src="/dwr/util.js"></script>
	<script type="text/javascript" src="/dwr/interface/alarmPush.js"></script>
	<script type="text/javascript">
		$(document).ready(function() {
			alert('e');
			alarmPush.send();
		});
		
	</script>
	<script type="text/javascript" >
           //这个方法用来启动该页面的ReverseAjax功能
           dwr.engine.setActiveReverseAjax( true);
           //设置在页面关闭时，通知服务端销毁会话
           dwr.engine.setNotifyServerOnPageUnload( true);
           //这个函数是提供给后台推送的时候 调用的
           function alarm(content){
              
           }
        </script>*/
}
