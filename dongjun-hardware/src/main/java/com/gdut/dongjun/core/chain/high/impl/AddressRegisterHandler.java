package com.gdut.dongjun.core.chain.high.impl;

import com.gdut.dongjun.core.CtxStore;
import com.gdut.dongjun.core.SwitchGPRS;
import com.gdut.dongjun.core.chain.AbstractHandler;
import com.gdut.dongjun.core.server.impl.HighVoltageServer;
import com.gdut.dongjun.domain.po.HighVoltageSwitch;
import com.gdut.dongjun.service.HighVoltageSwitchService;
import com.gdut.dongjun.util.HighVoltageDeviceCommandUtil;
import com.gdut.dongjun.util.MyBatisMapUtil;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by AcceptedBoy on 2016/9/16.
 */
@Component
public class AddressRegisterHandler extends AbstractHandler {

    @Autowired
    private HighVoltageSwitchService switchService;

    private static final Logger logger = LoggerFactory.getLogger(AddressRegisterHandler.class);

    @Override
    public boolean handleRequest(ChannelHandlerContext ctx, String data, String functionData) {

        if(!data.startsWith("EB90") && !data.startsWith("eb90")) {
            return false;
        }

        SwitchGPRS gprs = CtxStore.get(ctx);
        /**
         * 当注册的高压开关的地址不为空，说明已经注册过了，不再进行相关操作
         */
        if(gprs != null && gprs.getAddress() != null) {
            ctx.channel().writeAndFlush(data);
            return true;
        }
        String address = data.substring(12, 16);
        gprs.setAddress(address);

        address = new HighVoltageDeviceCommandUtil().reverseString(address);

        if (gprs != null) {
            /**
             * 根据反转后的地址查询得到highvoltageswitch的集合
             */
            List<HighVoltageSwitch> list = switchService
                    .selectByParameters(MyBatisMapUtil.warp("address", Integer.parseInt(address, 16)));
            HighVoltageSwitch s = null;
            if (list != null && list.size() != 0) {

                s = list.get(0);
                String id = s.getId();
                gprs.setId(id);

				/*
				 * 这个地方是开始对bug的一个测试，当开关从跳闸到合闸的时候，无法及时获取，只能
				 * 这样替换ctx才能进行状态更新
				 *
				 * 	if (CtxStore.get(id) != null) {
				 *		CtxStore.remove(id);
				 *		CtxStore.add(gprs);
				 *	}
				 *
				 */
                HighVoltageServer.totalCall(id);
                if(CtxStore.get(id) != null) {
                    CtxStore.remove(id);
                    CtxStore.add(gprs);
                }
            } else {
                logger.error("this device is not registered!!");
            }
        }
        return true;
    }
}
