package com.gdut.dongjun.web.high;

import com.gdut.dongjun.base.BaseMvcMock;
import com.gdut.dongjun.domain.po.HighVoltageSwitch;
import com.gdut.dongjun.domain.po.HighVoltageVoltage;
import com.gdut.dongjun.domain.po.HistoryHighVoltageVoltage;
import com.gdut.dongjun.service.HistoryHighVoltageVoltageService;
import com.gdut.dongjun.service.device.HighVoltageSwitchService;
import com.gdut.dongjun.service.device.voltage.HighVoltageVoltageService;
import com.gdut.dongjun.util.UUIDUtil;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 */
public class AddDB extends BaseMvcMock {

    @Autowired
    private HistoryHighVoltageVoltageService hvVoltageService;

    @Autowired
    private HighVoltageSwitchService hvSwitchService;

    /**
     * 向数据库添加高压电压的数据
     */
    @Test
    public void addVoltageMessage() throws InterruptedException {
        List<HighVoltageSwitch> switchList = hvSwitchService.selectByParameters(null);
        Random random = new Random();
        HistoryHighVoltageVoltage voltage;
        Date date;
        for(HighVoltageSwitch sw : switchList) {
            for(int i = 0; i < 1000; i++) {
                date = new Date();
                voltage = new HistoryHighVoltageVoltage();
                voltage.setId(UUIDUtil.getUUID());
                voltage.setSwitchId(sw.getId());
                voltage.setPhase("A");
                voltage.setValue(22000 + random.nextInt(3000));
                voltage.setTime(date);
                hvVoltageService.insert(voltage);

                voltage = new HistoryHighVoltageVoltage();
                voltage.setId(UUIDUtil.getUUID());
                voltage.setSwitchId(sw.getId());
                voltage.setPhase("B");
                voltage.setValue(0);
                voltage.setTime(date);
                hvVoltageService.insert(voltage);

                voltage = new HistoryHighVoltageVoltage();
                voltage.setId(UUIDUtil.getUUID());
                voltage.setSwitchId(sw.getId());
                voltage.setPhase("C");
                voltage.setValue(0);
                voltage.setTime(date);
                hvVoltageService.insert(voltage);

                Thread.sleep(200);
            }
        }
    }

    @Test
    public void addVoltageMessage2() throws InterruptedException {
        List<HighVoltageSwitch> switchList = hvSwitchService.selectByParameters(null);
        Random random = new Random();
        HistoryHighVoltageVoltage voltage;
        Date date;
        for(HighVoltageSwitch sw : switchList) {
            for(int i = 0; i < 1000; i++) {
                date = new Date();
                voltage = new HistoryHighVoltageVoltage();
                voltage.setId(UUIDUtil.getUUID());
                voltage.setSwitchId(sw.getId());
                voltage.setPhase("A");
                voltage.setValue(22000 + random.nextInt(3000));
                voltage.setTime(date);
                hvVoltageService.insert(voltage);

                voltage = new HistoryHighVoltageVoltage();
                voltage.setId(UUIDUtil.getUUID());
                voltage.setSwitchId(sw.getId());
                voltage.setPhase("B");
                voltage.setValue(0);
                voltage.setTime(date);
                hvVoltageService.insert(voltage);

                voltage = new HistoryHighVoltageVoltage();
                voltage.setId(UUIDUtil.getUUID());
                voltage.setSwitchId(sw.getId());
                voltage.setPhase("C");
                voltage.setValue(0);
                voltage.setTime(date);
                hvVoltageService.insert(voltage);

                Thread.sleep(200);
            }
        }
    }
}
