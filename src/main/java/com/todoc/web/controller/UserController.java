package com.todoc.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.todoc.web.dto.User;
import com.todoc.web.service.UserService;

@Controller
@RequestMapping
public class UserController 
{
	@Autowired
	private UserService userService;
	
	private static final Logger log = LoggerFactory.getLogger(UserController.class);
	
	@GetMapping("/login")
	public String userRead(ModelMap model, String userId) throws Exception{				
		
		userId = "test1";
		User user = userService.read(userId);
		
		
		model.addAttribute("user", user);
		
		return "login/login";	
	}
	
	@GetMapping("/test")
	public String userRead() throws Exception{				
		
		return "/test";	
	}
	
}
