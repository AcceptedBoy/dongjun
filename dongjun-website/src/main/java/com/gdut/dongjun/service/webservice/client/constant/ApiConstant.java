package com.gdut.dongjun.service.webservice.client.constant;


/**
 *
 * 服务路径
 */
public class ApiConstant {

    /**
     * 产生开闸报文并且发送开闸预置
     */
    public static final String GENETATE_OPEN_MSG = "/hardware/generate_open_msg";

    /**
     * 产生合闸报文并发送合闸预置报文
     */
    public static final String GENERATE_CLOSE_MSG = "/hardware/generate_close_msg";

    /**
     * 通过开关id来获取在线开关的地址
     */
    public static final String ONLINE_ADDRESS = "/hardware/online_address";

    /**
     * 获取在线开关的所有信息
     */
    public static final String ALL_SWITCHGPRS = "/hardware/all_switchGPRS";

    /**
     * 通过开关id来获取在线开关
     */
    public static final String SWITCHGPRS_BY_ID = "/hardware/switchGPRS_by_id";

    /**
     * 通过开关id来获取在线开关的高压状态
     */
    public static final String STATUS_BY_ID = "/hardware/status_by_id";

    /**
     * 通过开关id来改变开关的状态
     */
    public static final String CHANGE_CTX_OPEN = "/hardware/change_ctx_open";

    /**
     * 获取所有在线开关的详细状态
     */
    public static final String ACTIVE_SWITCH_STATUS = "/hardware/active_switch_status";

    /**
     * 这个方法只有在等于true的时候软件客户端才会去发请求向这边请求获取所有在线开关的详细
     */
    @Deprecated
    public static final String WHETHER_CHANGE = "/hardware/whether_change";
}
