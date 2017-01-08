package com.gdut.dongjun.webservice;

import com.gdut.dongjun.service.LineService;
import com.gdut.dongjun.service.SubstationService;
import com.gdut.dongjun.service.device.HighVoltageSwitchService;
import com.gdut.dongjun.util.NetUtil;
import com.gdut.dongjun.webservice.client.CommonServiceClient;
import com.gdut.dongjun.webservice.po.InitialParam;
import com.gdut.dongjun.webservice.util.JaxrsClientUtil;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.*;


/**
 * Created by symon on 16-9-29.
 */
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

