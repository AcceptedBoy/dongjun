package com.gdut.dongjun.base;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import com.gdut.dongjun.web.HighVoltageSwitchController;

public class DemoCase extends BaseMvcMock {

	@Autowired
	private HighVoltageSwitchController switchController;
	
	/**
	 * 基于url测试
	 */
	@Test
	public void urlCase() throws Exception {
		@SuppressWarnings("unused")
		MvcResult result = mockMvc.perform(get("/dongjun/get_all_high_voltage_switch"))  
	            .andDo(MockMvcResultHandlers.print())  
	            .andReturn();  
	      
	    //Assert.assertNull(result.getModelAndView().getModel().get("currentUser")); 
	}
	
	/**
	 * 基于controller的注入测试
	 */
	@Test
	public void controllerCase() {
        MockHttpServletRequest request = new MockHttpServletRequest();  
        request.setMethod("POST");  
        System.out.println(switchController.getAllLowVoltage_Switch());
	}
}
