package com.gdut.dongjun.core.handler.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Created by AcceptedBoy on 2016/9/16.
 */
@Component
public class MsgCenterLogger {

    private Logger logger = LoggerFactory.getLogger(MsgCenterLogger.class);

    public void info(String msg) {
        if(logger.isInfoEnabled()) {
            logger.info(msg);
        }
    }

    public void debug(String msg) {
        if(logger.isErrorEnabled()) {
            logger.debug(msg);
        }
    }

    public void error(String msg) {
        if(logger.isErrorEnabled()) {
            logger.error(msg);
        }
    }

    public void warn(String msg) {
        if(logger.isWarnEnabled()) {
            logger.warn(msg);
        }
    }

    public void trace(String msg) {
        if(logger.isTraceEnabled()) {
            logger.trace(msg);
        }
    }
}
