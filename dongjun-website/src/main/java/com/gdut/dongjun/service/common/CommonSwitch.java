package com.gdut.dongjun.service.common;

/**
 * 开关配置
 * @author symon
 */
public class CommonSwitch {

    private static boolean production = false;

    private static boolean centerService = true;

    /**
     * 是否需要中央服务
     */
    public boolean canService() {
        return centerService;
    }

    public boolean isProduction() {
        return production;
    }
}
