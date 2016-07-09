package com.gdut.dongjun.web.high;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.Test;

import com.gdut.dongjun.base.BaseMvcMock;

public class HighVoltageSwitchTest extends BaseMvcMock {

	@Test
	public void validatorAddSwitch() throws Exception {
		mockMvc.perform(get("/dongjun/edit_high_voltage_switch")
			.param("id", "")
			.param("name", "1111111111111111111111111111111111122222222222222222222222222222222222222222222222222222")
			).andDo(print())
			.andExpect(status().is4xxClientError());
	}
}
