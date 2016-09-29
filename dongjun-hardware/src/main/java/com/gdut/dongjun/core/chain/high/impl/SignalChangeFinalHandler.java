package com.gdut.dongjun.core.chain.high.impl;

import com.gdut.dongjun.core.CtxStore;
import com.gdut.dongjun.core.chain.AbstractHandler;
import com.gdut.dongjun.domain.HighVoltageStatus;
import com.gdut.dongjun.util.HighVoltageDeviceCommandUtil;
import com.gdut.dongjun.util.LowVoltageDeviceCommandUtil;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Created by AcceptedBoy on 2016/9/16.
 */
@Component
public class SignalChangeFinalHandler extends AbstractHandler {

    private static final Logger logger = LoggerFactory.getLogger(AddressRegisterHandler.class);

    @Override
    public boolean handleRequest(ChannelHandlerContext ctx, String data, String functionData) {
        if(!functionData.equalsIgnoreCase("1f")) {
            return false;
        }
        for(int i = 26, j = Integer.valueOf(data.substring(16, 18)); j > 0; i += 6, --j) {
            changeState(data.substring(22, 26), data.substring(i, i + 4), data.substring(i + 4, i + 6));
        }
        String resu = new HighVoltageDeviceCommandUtil().confirmChangeAffair(data.substring(10, 14));
        logger.info("遥信变位事件确定---------" + resu);
        ctx.writeAndFlush(resu);
        return true;
    }

    private void changeState(String address, String code, String value) {

        if(code == null || code.length() == 0 || code.length() != 4) {
            return;
        }
        if(code.endsWith("00")) {
            /**
             * 报文反转
             */
            code = LowVoltageDeviceCommandUtil.reverseStringBy2(code);
        }
        if(CtxStore.getIdbyAddress(address) == null ||
                CtxStore.getStatusbyId(CtxStore.getIdbyAddress(address)) == null) {
            return;
        }
        if(value.equals("02")) {
            value = "01";
        } else {
            value = "00";
        }
        HighVoltageStatus hvs = CtxStore.getStatusbyId(CtxStore.getIdbyAddress(address));
        switch(code) {
            case "0000": hvs.setGuo_liu_yi_duan(value);break;
            case "0001": hvs.setGuo_liu_er_duan(value);break;
            case "0002": hvs.setGuo_liu_san_duan(value);break;
            case "0004": hvs.setLing_xu_guo_liu_(value);break;
            case "000A": hvs.setPt1_you_ya(value);break;
            case "000B": hvs.setPt1_guo_ya(value);break;
            case "000C": hvs.setPt2_guo_ya(value);break;
            case "000D": hvs.setShou_dong_he_zha(value);break;
            case "000E": hvs.setShou_dong_fen_zha(value);break;
            case "00FB": hvs.setYao_kong_he_zha(value);break;
            case "00FC": hvs.setYao_kong_he_zha(value);break;
            case "00FD": hvs.setYao_kong_fu_gui(value);break;
        }
    }
}
