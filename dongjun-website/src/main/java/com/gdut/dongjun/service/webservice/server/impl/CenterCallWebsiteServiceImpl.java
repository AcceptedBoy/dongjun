package com.gdut.dongjun.service.webservice.server.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.catalina.core.ApplicationContext;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gdut.dongjun.Const;
import com.gdut.dongjun.domain.po.HighVoltageSwitch;
import com.gdut.dongjun.service.LineService;
import com.gdut.dongjun.service.SubstationService;
import com.gdut.dongjun.service.device.HighVoltageSwitchService;
import com.gdut.dongjun.service.webservice.client.CommonServiceClient;
import com.gdut.dongjun.service.webservice.client.po.HighVoltageSwitchDTO;
import com.gdut.dongjun.service.webservice.client.po.InitialParam;
import com.gdut.dongjun.service.webservice.server.CenterCallWebsiteService;

@Component
public class CenterCallWebsiteServiceImpl implements CenterCallWebsiteService, InitializingBean {

	@Autowired
	private CommonServiceClient client;
	@Autowired
	private SubstationService substationService;
	@Autowired
	private LineService lineService;
	@Autowired
	private HighVoltageSwitchService hvswitchService;
	
	@Override
	public void initCall() {
        InitialParam initialParam = new InitialParam();
        initialParam.setSubstationList(substationService.selectByParameters(null));
        initialParam.setLineList(lineService.selectByParameters(null));
        List<HighVoltageSwitch> switches = hvswitchService.selectByParameters(null);
        List<HighVoltageSwitchDTO> dtos = new ArrayList<>();
        for (HighVoltageSwitch s : switches) {
        	HighVoltageSwitchDTO dto = new HighVoltageSwitchDTO(s);
        	dtos.add(dto);
        }
        initialParam.setHvswitchList(dtos);
        client.getService().systemInitial(initialParam);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		//	系统启动传输系统信息
	
	}

	@Override
	public void confirmInit(int tag) {
		Const.SYSTEM_RUN = true;
	}

}
