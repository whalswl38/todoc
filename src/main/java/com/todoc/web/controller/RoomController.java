package com.todoc.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.todoc.web.security.jwt.JwtAuthorizationFilter;

@Controller
@RequestMapping
public class RoomController {
	
	private static Logger logger = LoggerFactory.getLogger(ReviewController.class);
	
	private final JwtAuthorizationFilter jwtFilter;
	
	public RoomController(JwtAuthorizationFilter jwtFilter)
	{
		this.jwtFilter = jwtFilter;
	}
	
	 @GetMapping("/room-page")
     public String roomStream(HttpServletRequest request, Model model, @RequestParam(value="userEmail", defaultValue="") String contactUserEmail) 
	 {
		 String token = jwtFilter.extractJwtFromCookie(request);
	     String userEmail = jwtFilter.getUsernameFromToken(token);
	     
	     model.addAttribute("userEmail", userEmail);
	     model.addAttribute("contactUserEmail", contactUserEmail);
	     
	     logger.error("userEmail : " + userEmail);
	     
         return "chat/stream";
     }
	
}
