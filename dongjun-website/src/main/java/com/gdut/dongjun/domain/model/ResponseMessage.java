package com.gdut.dongjun.domain.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Be used to send message to client or frontend with restful design.
 * @author acceptedboy
 */
public class ResponseMessage {

    public enum Type {
        SUCCESS, WARNING, INFO, DANGER;
    }
    
    private boolean success; //whether success or not
    private Type type;	//the enum
    private Object text; // the json data to client
    private String code; //the appointment between frontend and backstage
    private List<ErrorInfo> errors = new ArrayList<>();

    public ResponseMessage(Type type, Object text, boolean success) {
        this.type = type;
        this.text = text;
        this.success = success;
    }
    
    public ResponseMessage(Type type, Object text, boolean success, List<ErrorInfo> errors) {
        this.type = type;
        this.text = text;
        this.success = success;
        this.errors = errors;
    }

    public ResponseMessage(Type type, String code, Object text, boolean success) {
        this.type = type;
        this.code = code;
        this.text = text;
        this.success = success;
    }
    
    public boolean getSuccess() {
    	return success;
    }

    public Object getText() {
        return text;
    }

    public Type getType() {
        return type;
    }

    public String getCode() {
        return code;
    }
    
    public List<ErrorInfo> getErrors() {
    	return errors;
    }
    
    public static ResponseMessage success(Object text) {
        return new ResponseMessage(Type.SUCCESS, text, true);
    }

    public static ResponseMessage warning(Object text) {
        return new ResponseMessage(Type.WARNING, text, true);
    }

    public static ResponseMessage danger(Object text) {
        return new ResponseMessage(Type.DANGER, text, false);
    }

    public static ResponseMessage info(Object text) {
        return new ResponseMessage(Type.INFO, text, true);
    }
    
    public static ResponseMessage addException(ErrorInfo error) {
    	
    	return setException(Arrays.asList(error));
    }
    
    public static ResponseMessage setException(List<ErrorInfo> errors) {
    	
    	return new ResponseMessage(Type.DANGER, null, false, errors);
    }
    
    public static ResponseMessage generateNullData() {
    	
    	return new ResponseMessage(Type.WARNING, null, true, null);
    }
}
