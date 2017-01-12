package com.gdut.dongjun.service.webservice.server.impl;

import com.gdut.dongjun.domain.HighVoltageStatus;
import com.gdut.dongjun.domain.po.User;
import com.gdut.dongjun.domain.vo.ActiveHighSwitch;
import com.gdut.dongjun.service.common.DeviceBinding;
import com.gdut.dongjun.service.device.DeviceCommonService;
import com.gdut.dongjun.service.webservice.client.HardwareServiceClient;
import com.gdut.dongjun.service.webservice.server.WebsiteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 */
@Component
public class WebsiteServiceImpl implements WebsiteService {

    private static final Logger LOG = LoggerFactory.getLogger(WebsiteServiceImpl.class);

    @Autowired
    private SimpMessagingTemplate template;

    @Autowired
    private DeviceCommonService deviceCommonService;

    @Autowired
    private HardwareServiceClient hardwareClient;

    @Autowired
    public WebsiteServiceImpl(SimpMessagingTemplate messagingTemplate) {
        this.template = messagingTemplate;
    }

    @Override
    public void callbackCtxChange(List<ActiveHighSwitch> data) {
        LOG.info("hardware端ctx变化，进行页面推送");
        this.template.convertAndSend("/topic/get_active_switch_status", data);
    }

    @Override
    public void callbackDeviceChange(String switchId, Integer type) {
    	LOG.info("设备状态发生变化" + switchId + "    type为" + type);
        List<User> userList = DeviceBinding.getListenUser(switchId);
        if(!CollectionUtils.isEmpty(userList)) {
            for(User user : userList) {

                template.convertAndSendToUser(user.getName(), "/queue/read_current",
                        deviceCommonService.getCurrentService(Integer.valueOf(type)).readCurrent(switchId));

                template.convertAndSendToUser(user.getName(), "/queue/read_voltage",
                        deviceCommonService.getVoltageService(Integer.valueOf(type)).getVoltage(switchId));

                HighVoltageStatus status = hardwareClient.getService().getStatusbyId(switchId);
                if (status != null) {
                    template.convertAndSendToUser(user.getName(),
                            "/queue/read_hv_status", status);
                }
            }
        }
    }
}
