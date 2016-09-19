package com.gdut.dongjun.core.chain.high.impl;

import com.gdut.dongjun.core.CtxStore;
import com.gdut.dongjun.core.SwitchGPRS;
import com.gdut.dongjun.core.chain.AbstractHandler;
import com.gdut.dongjun.domain.HighVoltageStatus;
import com.gdut.dongjun.domain.po.HighVoltageHitchEvent;
import com.gdut.dongjun.service.HighVoltageHitchEventService;
import com.gdut.dongjun.util.TimeUtil;
import com.gdut.dongjun.util.UUIDUtil;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created by AcceptedBoy on 2016/9/16.
 */
@Component
public class SignalChangeReadHandler extends AbstractHandler {

    @Autowired
    private HighVoltageHitchEventService hitchEventService;

    private static final Logger logger = LoggerFactory.getLogger(AddressRegisterHandler.class);

    @Override
    public boolean handleRequest(ChannelHandlerContext ctx, String data, String functionData) {
        if(!functionData.equalsIgnoreCase("01")) {
            return false;
        }

        String address = data.substring(10, 14);
        String id = CtxStore.getIdbyAddress(address);

        if (id != null && address != null) {

            HighVoltageStatus s = CtxStore.getStatusbyId(id);
            SwitchGPRS gprs = CtxStore.get(id);

            if (s == null) {

                s = new HighVoltageStatus();
                s.setId(id);
                CtxStore.addStatus(s);
            }

            s.setGuo_liu_yi_duan(data.substring(30, 32));
            s.setGuo_liu_er_duan(data.substring(32, 34));
            s.setGuo_liu_san_duan(data.substring(34, 36));

            s.setLing_xu_guo_liu_(data.substring(38, 40));

            if (data.substring(40, 42).equals("01") || data.substring(42, 44).equals("01")
                    || data.substring(44, 46).equals("01")) {
                s.setChong_he_zha("01");
            } else {
                s.setChong_he_zha("00");
            }

            s.setPt1_you_ya(data.substring(48, 50));
            s.setPt2_you_ya(data.substring(50, 52));

            s.setPt1_guo_ya(data.substring(52, 54));
            s.setPt2_guo_ya(data.substring(54, 56));

            String new_status = data.substring(66, 68);

            if ("01".equals(s.getStatus()) && "00".equals(new_status)) {

                gprs.setOpen(true);

                HighVoltageHitchEvent event = new HighVoltageHitchEvent();

                event.setHitchTime(TimeUtil.timeFormat(new Date(), "yyyy-MM-dd HH:mm:ss"));
                event.setHitchPhase("A");
                //event.setHitchReason(hitchEventDesc == null ? "未知报警" : hitchEventDesc);
                event.setHitchReason("控制回路");
                event.setChangeType(0);
                event.setSolveWay("分闸");
                event.setId(UUIDUtil.getUUID());
                event.setSwitchId(id);
                // event.setSolvePeople();
                hitchEventService.insert(event);

                logger.info("-----------跳闸成功");
            } else if ("01".equals(new_status) && "00".equals(s.getStatus())) {

                gprs.setOpen(false);

                HighVoltageHitchEvent event = new HighVoltageHitchEvent();

                event.setHitchTime(TimeUtil.timeFormat(new Date(), "yyyy-MM-dd HH:mm:ss"));
                event.setHitchPhase("A");
                //event.setHitchReason(hitchEventDesc == null ? "未知报警" : hitchEventDesc);
                event.setHitchReason("控制回路");
                event.setChangeType(1);
                event.setSolveWay("合闸");
                event.setId(UUIDUtil.getUUID());
                event.setSwitchId(id);
                hitchEventService.insert(event);
                logger.info("-----------合闸成功");
            }
            s.setStatus(new_status);

            s.setJiao_liu_shi_dian(data.substring(76, 78));

            s.setShou_dong_he_zha(data.substring(78, 80));
            s.setShou_dong_fen_zha(data.substring(80, 82));

            s.setYao_kong_he_zha(data.substring(84, 86));
            s.setYao_kong_fen_zha(data.substring(86, 88));
            s.setYao_kong_fu_gui(data.substring(88, 90));

            logger.info("状态变为-----------" + new_status);

        } else {
            logger.error("there is an error in catching hitch event!");
        }

        return true;
    }
}
