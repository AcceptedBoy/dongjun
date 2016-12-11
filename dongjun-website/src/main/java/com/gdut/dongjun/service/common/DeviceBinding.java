package com.gdut.dongjun.service.common;

import com.gdut.dongjun.domain.po.User;
import org.apache.commons.collections.ListUtils;
import org.apache.commons.lang.StringUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 设备的监听绑定，如果在前端页面打开了设备的信息，那么就开始绑定信息
 */
public class DeviceBinding {

    /**
     * 进行绑定，userName为键，正在打开弹窗监听的设备id为值
     */
    private static Map<User, String> listenerBind = new ConcurrentHashMap<>();

    public static void binding(User user, String switchId) {
        listenerBind.put(user, switchId);
    }

    public static void unbinding(User user) {
        listenerBind.remove(user);
    }

    public static List<User> getListenUser(String switchId) {
        if(StringUtils.isEmpty(switchId)) {
            return ListUtils.EMPTY_LIST;
        }
        List<User> listenUser = new LinkedList<>();
        for(Map.Entry<User, String> entry : listenerBind.entrySet()) {
            if(entry.getValue().equals(switchId)) {
                listenUser.add(entry.getKey());
            }
        }
        return listenUser;
    }
}
