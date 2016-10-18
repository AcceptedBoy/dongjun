package com.gdut.dongjun.webservice;

/**
 * Created by symon on 16-9-28.
 */
public class Constant {

    private boolean isService;

    private String preSerivcePath;

    public static final String DELETE_SUBSTATION = "/substation/delete";

    public static final String UPDATE_SUBSTATION = "/substation/update";

    public static final String ADD_SUBSTATION = "/substation/add";

    public static final String DELETE_LINE = "/line/delete";

    public static final String UPDATE_LINE = "/line/update";

    public static final String ADD_LINE = "/line/add";

    public static final String DELETE_HVSWITCH = "/hvswitch/delete";

    public static final String UPDATE_HVSWITCH = "/hvswitch/update";

    public static final String ADD_HVSWITCH = "/hvswitch/add";

    public static final String SYSTEM_INITIAL = "/system_initial";

    public boolean isService() {
        return isService;
    }

    public void setIsService(boolean isService) {
        this.isService = isService;
    }

    public String getPreSerivcePath() {
        return preSerivcePath;
    }

    public void setPreSerivcePath(String preSerivcePath) {
        this.preSerivcePath = preSerivcePath;
    }
}
