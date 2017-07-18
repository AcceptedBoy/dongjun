package com.gdut.dongjun.service;

import java.util.List;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gdut.dongjun.domain.po.Company;
import com.gdut.dongjun.service.common.CommonSwitch;
import com.gdut.dongjun.service.device.HighVoltageSwitchService;
import com.gdut.dongjun.service.webservice.client.CommonServiceClient;
import com.gdut.dongjun.service.webservice.client.po.InitialParam;

@Component
public class SystemInitalStartup implements InitializingBean {

    @Autowired
    private CommonSwitch commonSwitch;
    @Autowired
    private CommonServiceClient centorServiceClient;
    @Autowired
    private SubstationService substationService;
    @Autowired
    private LineService lineService;
    @Autowired
    private HighVoltageSwitchService hvswitchService;
    @Autowired
    private CompanyService companyService;

    @Override
    public void afterPropertiesSet() throws Exception {

        if(commonSwitch.canService()) {
            systemInital();
//            registerAddress();
        }
    }

    private void systemInital() {

        InitialParam initialParam = new InitialParam();
        initialParam.setSubstationList(substationService.selectByParameters(null));
        initialParam.setLineList(lineService.selectByParameters(null));
        initialParam.setHvswitchList(hvswitchService.selectByParameters(null));
        centorServiceClient.getService().systemInitial(initialParam);
    }

//    private void registerAddress() throws UnknownHostException, SocketException {
//
//        try {
//            centorServiceClient.getService().registerService(NetUtil.inetAton(NetUtil.getRealLocalIp()), NetUtil.getLocalMacAddress());
//        } catch (Exception e) {
//
//        }
//    }
}

