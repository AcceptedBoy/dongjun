package com.gdut.dongjun.service.webservice.client.centor;

import com.gdut.dongjun.service.LineService;
import com.gdut.dongjun.service.SubstationService;
import com.gdut.dongjun.service.device.HighVoltageSwitchService;
import com.gdut.dongjun.service.webservice.client.centor.client.CommonServiceClient;
import com.gdut.dongjun.service.webservice.client.centor.po.Constant;
import com.gdut.dongjun.service.webservice.client.centor.po.InitialParam;
import com.gdut.dongjun.service.webservice.client.centor.util.JaxrsClientUtil;
import com.gdut.dongjun.util.NetUtil;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.SocketException;
import java.net.UnknownHostException;

@Component
public class SystemInitalStartup implements InitializingBean {

    @Autowired
    private Constant constant;

    @Autowired
    private SubstationService substationService;

    @Autowired
    private LineService lineService;

    @Autowired
    private HighVoltageSwitchService hvswitchService;

    @Override
    public void afterPropertiesSet() throws Exception {

        if(constant.isService()) {
            systemInital();
            registerAddress();
        }
    }

    private void systemInital() {
        CommonServiceClient client = (CommonServiceClient)
                new JaxrsClientUtil().getClient(constant.getPreSerivcePath(), CommonServiceClient.class);

        InitialParam initialParam = new InitialParam();
        initialParam.setSubstationList(substationService.selectByParameters(null));
        initialParam.setLineList(lineService.selectByParameters(null));
        initialParam.setHvswitchList(hvswitchService.selectByParameters(null));
        client.systemInitial(initialParam);
    }

    private void registerAddress() throws UnknownHostException, SocketException {

        CommonServiceClient client = (CommonServiceClient)
                new JaxrsClientUtil().getClient(constant.getPreSerivcePath(), CommonServiceClient.class);
        try {
            client.registerService(NetUtil.inetAton(NetUtil.getRealLocalIp()), NetUtil.getLocalMacAddress());
        } catch (Exception e) {

        }
    }
}

