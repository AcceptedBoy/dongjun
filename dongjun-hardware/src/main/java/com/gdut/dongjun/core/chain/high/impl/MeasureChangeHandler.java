package com.gdut.dongjun.core.chain.high.impl;

import com.gdut.dongjun.core.CtxStore;
import com.gdut.dongjun.core.chain.AbstractHandler;
import com.gdut.dongjun.domain.po.HighVoltageCurrent;
import com.gdut.dongjun.domain.po.HighVoltageVoltage;
import com.gdut.dongjun.service.HighVoltageCurrentService;
import com.gdut.dongjun.service.HighVoltageVoltageService;
import com.gdut.dongjun.service.HistoryHighVoltageCurrentService;
import com.gdut.dongjun.service.HistoryHighVoltageVoltageService;
import com.gdut.dongjun.util.HighVoltageDeviceCommandUtil;
import com.gdut.dongjun.util.LowVoltageDeviceCommandUtil;
import com.gdut.dongjun.util.MyBatisMapUtil;
import com.gdut.dongjun.util.UUIDUtil;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by AcceptedBoy on 2016/9/16.
 */
@Component
public class MeasureChangeHandler extends AbstractHandler {

    @Autowired
    private HighVoltageCurrentService currentService;

    @Autowired
    private HighVoltageVoltageService voltageService;

    @Autowired
    private HistoryHighVoltageCurrentService historyCurrentService;

    @Autowired
    private HistoryHighVoltageVoltageService historyVoltageService;

    private static final Logger logger = LoggerFactory.getLogger(AddressRegisterHandler.class);

    @Override
    public boolean handleRequest(ChannelHandlerContext ctx, String data, String functionData) {
        if(!functionData.equalsIgnoreCase("09")) {
            return false;
        }

        if(!data.substring(2,4).equals("47")) {
            /**
             * 若data.length=40，测归一值
             */
            logger.info("测归一值-------" + data);
            //680e0e68f4680009010301680008400000001a16
            //68131368f46600090203016600054000000006402700008116
            //68131368f46600090203016600054000000006402700008116
            for(int i = 22; i + 14 < data.length(); i += 10) {
                getMessageAddress(data.substring(i + 4, i + 8), data.substring(22, 26),
                        data.substring(i + 8, i + 14));
            }
            return true;
        }
        logger.info("解析CV---------" + data);
        String address = data.substring(10, 14);
        String id = CtxStore.getIdbyAddress(address);
        if (id != null) {
            saveCV(id, data);
        } else {
            logger.error("there is an error in saving CV!");
        }
        return true;
    }

    private void getMessageAddress(String code, String address, String value) {

        if(code == null || code.length() == 0 || code.length() != 4) {
            return;
        }
        if(code.endsWith("40")) {
            /**
             * 报文反转
             */
            code = LowVoltageDeviceCommandUtil.reverseStringBy2(code);
        }
        if(CtxStore.getIdbyAddress(address) == null) {
            return;
        }

        switch(code) {
            case "4001": saveVoltageForValue(CtxStore.getIdbyAddress(address), "A",
                    HighVoltageDeviceCommandUtil.changToRight(value));break;
            case "4006": saveCurrentForValue(CtxStore.getIdbyAddress(address), "A",
                    HighVoltageDeviceCommandUtil.changToRight(value));break;
            case "4007": saveCurrentForValue(CtxStore.getIdbyAddress(address), "B",
                    HighVoltageDeviceCommandUtil.changToRight(value));break;
            case "4008": saveCurrentForValue(CtxStore.getIdbyAddress(address), "C",
                    HighVoltageDeviceCommandUtil.changToRight(value));break;
            default:break;
        }
    }

    /**
     * 根据开关的id， 和报文信息来获取该开关的全部电压电流值，调用方法保存
     */
    public void saveCV(String switchId, String data) {

        data = data.replace(" ", "");
        String ABVoltage = new HighVoltageDeviceCommandUtil().readABPhaseVoltage(data);
        String BCVoltage = new HighVoltageDeviceCommandUtil().readBCPhaseVoltage(data);
        String ACurrent = new HighVoltageDeviceCommandUtil().readAPhaseCurrent(data);
        String BCurrent = new HighVoltageDeviceCommandUtil().readBPhaseCurrent(data);
        String CCurrent = new HighVoltageDeviceCommandUtil().readCPhaseCurrent(data);

        saveCurrentForValue(switchId, "A", ACurrent);
        saveCurrentForValue(switchId, "B", BCurrent);
        saveCurrentForValue(switchId, "C", CCurrent);
        saveVoltageForValue(switchId, "A", ABVoltage);
        saveVoltageForValue(switchId, "B", BCVoltage);
    }

    /**
     * 保存电压的一个相值
     */
    private void saveVoltageForValue(String switchId, String phase, String value) {

        Date date = new Date();
        Map<String, Object> map = new HashMap<>(3);
        map.put("switch_id", switchId);
        map.put("phase", phase);
        List<HighVoltageVoltage> list = voltageService.selectByParameters(MyBatisMapUtil.warp(map));
        if(list != null && list.size() != 0) {
            HighVoltageVoltage c1 = list.get(0);
            c1.setTime(date);
            c1.setValue(Integer.parseInt(value));
            voltageService.updateByPrimaryKey(c1);
            c1.setId(UUIDUtil.getUUID());
            historyVoltageService.insert(c1.changeToHistory());
        } else {
            HighVoltageVoltage c1 = new HighVoltageVoltage();
            c1.setId(UUIDUtil.getUUID());
            c1.setTime(date);
            c1.setSwitchId(switchId);
            c1.setValue(Integer.parseInt(value));
            c1.setPhase(phase);
            voltageService.insert(c1);
            historyVoltageService.insert(c1.changeToHistory());
        }
    }

    /**
     * 保存电流的一个相值
     */
    private void saveCurrentForValue(String switchId, String phase, String value) {

        Date date = new Date();
        Map<String, Object> map = new HashMap<>(3);
        map.put("switch_id", switchId);
        map.put("phase", phase);
        List<HighVoltageCurrent> list = currentService.selectByParameters(MyBatisMapUtil.warp(map));
        if(list != null && list.size() != 0) {
            HighVoltageCurrent c1 = list.get(0);
            c1.setTime(date);
            c1.setValue(Integer.parseInt(value));
            currentService.updateByPrimaryKey(c1);
            c1.setId(UUIDUtil.getUUID());
            historyCurrentService.insert(c1.changeToHistory());
        } else {
            HighVoltageCurrent c1 = new HighVoltageCurrent();
            c1.setId(UUIDUtil.getUUID());
            c1.setTime(date);
            c1.setSwitchId(switchId);
            c1.setValue(Integer.parseInt(value));
            c1.setPhase(phase);
            currentService.insert(c1);
            historyCurrentService.insert(c1.changeToHistory());
        }
    }
}
