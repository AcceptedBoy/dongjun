package com.gdut.dongjun.service.common;

/**
 * 开关配置
 * @author symon
 */
public class CommonSwitch {
    //是否是生产环境
    private static boolean production = false;

    //是否需要中央服务
    private static boolean centerService = false;

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
