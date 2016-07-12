package com.gdut.dongjun.web.authc;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gdut.dongjun.domain.model.ResponseMessage;
import com.gdut.dongjun.domain.po.User;
import com.gdut.dongjun.service.authc.ModuleService;

@RestController
@RequestMapping("/dongjun/module")
public class ModuleController {

	@Autowired
	private ModuleService moduleService;
	
	@RequestMapping("/get_user_module")
	public ResponseMessage getUserModule(HttpSession session) {

		User user = (User)session.getAttribute("currentUser");
		if(user != null) {
			return ResponseMessage.success(moduleService.selectUserModule(user.getId()));
		} else {
			return ResponseMessage.success(null);
		}
	}
}
