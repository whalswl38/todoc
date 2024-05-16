package com.todoc.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.todoc.web.dto.User;
import com.todoc.web.security.jwt.JwtAuthorizationFilter;
import com.todoc.web.service.ContactLogService;
import com.todoc.web.service.ReviewService;
import com.todoc.web.service.UserService;

@Controller
@RequestMapping
public class MypageController {
	
	private static Logger logger = LoggerFactory.getLogger(MypageController.class);
	
	@Autowired
	private ContactLogService contactLogService;
	
	@Autowired
	private ReviewService reviewService;
	
	@Autowired
	private UserService userService;
	
	private final JwtAuthorizationFilter jwtFilter;
	
	public MypageController(JwtAuthorizationFilter jwtFilter)
	{
		this.jwtFilter = jwtFilter;
	}
	
	@GetMapping("/mypage-page")
	 public String test17(HttpServletRequest request, Model model) 
	 {
		
	    	String token = jwtFilter.extractJwtFromCookie(request);
	    	String userEmail = jwtFilter.getUsernameFromToken(token);
	    	
	    	
	    	int contactTotalCount = contactLogService.contactLogTotal(userEmail);
	    	int reviewTotalCount = reviewService.reviewTotal(userEmail);
	    	
	    	
	    	if(contactTotalCount > 0)
	    	{
	    		model.addAttribute("contactTotalCount", contactTotalCount);
	    	}
	    	
	    	if(reviewTotalCount > 0)
	    	{
	    		model.addAttribute("reviewTotalCount", reviewTotalCount);
	    	}
	    	
	
	    	return "/mypage/mypage";
	   }
	
	
	@GetMapping("/infoUpdate-page")
    public String test27(HttpServletRequest request, Model model) 
	{
		String token = jwtFilter.extractJwtFromCookie(request);
    	String userEmail = jwtFilter.getUsernameFromToken(token);
    	
    	if(!userEmail.isEmpty())
    	{
    		User user = userService.findByEmail(userEmail);
    	
	    	if(user != null)
	    	{
	    		logger.error("userName : " + user.getUserName());
	    		model.addAttribute("user", user);
	    	}
    	}
    	
    	
    	
        return "mypage/infoUpdate";
    }
	
	//회원정보수정
	@PostMapping("/update")
	public String update(HttpServletRequest request, @Valid User user, Model model)
	{
		String token = jwtFilter.extractJwtFromCookie(request);
    	String userEmail = jwtFilter.getUsernameFromToken(token);
		
		User updateUser = new User();
		
		if(!userEmail.isEmpty())
		{
			try
			{
				logger.error("222222222222222");
				
				updateUser.setUserName(user.getUserName());
				updateUser.setUserIdentity(user.getUserIdentity());
				updateUser.setUserEmail(userEmail);
				
				logger.error("updateUser" + updateUser);
				
				if(userService.userUpdate(updateUser) > 0)
				{
					logger.error("333333333333333");
					return "mypage/mypage";
				}
				
				model.addAttribute("user", user);
				return "mypage/mypage";
			}
			catch(Exception e)
			{
				logger.error("sign Exception");
				model.addAttribute("errorMessage", e.getMessage());
				
				return "main/main";
			}
		}
		
		return "mypage/mypage";
	}
}
