package com.gdut.dongjun.core.chain.high.factory;

import com.gdut.dongjun.core.chain.AbstractHandler;
import com.gdut.dongjun.core.chain.high.impl.*;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by AcceptedBoy on 2016/9/16.
 */
@Component
public final class HighVoltageChainFactory implements InitializingBean {

    public static final CopyOnWriteArrayList<AbstractHandler> instance = new CopyOnWriteArrayList<>();

    @Autowired
    private AddressRegisterHandler addressRegisterHandler;

    @Autowired
    private SignalChangeInitialHandler signalChangeInitialHandler;

    @Autowired
    private SignalChangeFinalHandler signalChangeFinalHandler;

    @Autowired
    private SignalChangeReadHandler signalChangeReadHandler;

    @Autowired
    private MeasureChangeHandler measureChangeHandler;

    @Autowired
    private CircleAllGetHandler circleAllGetHandler;

    @Autowired
    private UnSolveHandler unSolveHandler;

    private void customizeChain() {
        instance.add(addressRegisterHandler);
        instance.add(signalChangeInitialHandler);
        instance.add(signalChangeFinalHandler);
        instance.add(signalChangeReadHandler);
        instance.add(measureChangeHandler);
        instance.add(circleAllGetHandler);
        instance.add(unSolveHandler);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        customizeChain();
    }

    public CopyOnWriteArrayList<AbstractHandler> getInstance() {
        return instance;
    }
}
