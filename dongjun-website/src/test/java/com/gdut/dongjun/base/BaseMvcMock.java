package com.gdut.dongjun.base;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.gdut.dongjun.annotation.ContextDevTest;

/**
 * 需要在controller层进行mock测试的时候，可以继承该类
 * <p>需要用到{@code GET},{@code POST}等请求时，在测试类中加上
 * {@code import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;}
 * <p>常用的代码块：对请求的返回信息进行相应查看
 * <p>{@code mockMvc.perform(post("/persons"))
 *  .andDo(print())
 *  .andExpect(status().isOk())
 *  .andExpect(model().attributeHasErrors("person"));}
 *
 *
 * @see DemoCase
 * 
 * @author AcceptedBoy
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextDevTest
public class BaseMvcMock {

	@Autowired
	private WebApplicationContext context;
	
	protected MockMvc mockMvc;
	
	@Before
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context).build();
	}
	
	public void defaultUrlTest(String url) throws Exception {
		mockMvc.perform(get(url))  
	            .andDo(MockMvcResultHandlers.print())  
	            .andReturn(); 
	}
	
}
