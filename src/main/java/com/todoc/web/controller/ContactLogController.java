package com.todoc.web.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.todoc.web.dao.Paging;
import com.todoc.web.dto.ContactLog;
import com.todoc.web.security.jwt.JwtAuthorizationFilter;
import com.todoc.web.service.ContactLogService;
import com.todoc.web.service.ReviewService;

@Controller
@RequestMapping
public class ContactLogController {
	
	private static Logger logger = LoggerFactory.getLogger(ReviewController.class);
	
	@Autowired
	private ContactLogService contactLogService;
	
	@Autowired
	private ReviewService reviewService;
	
	private final JwtAuthorizationFilter jwtFilter;
	
	public ContactLogController(JwtAuthorizationFilter jwtFilter)
	{
		this.jwtFilter = jwtFilter;
	}
	
	 @GetMapping("/medical-history-page")
     public String contactList(HttpServletRequest request, Model model) {
		 
		 List<ContactLog> list = null;
		 
		 String token = jwtFilter.extractJwtFromCookie(request);
    	 String userEmail = jwtFilter.getUsernameFromToken(token);
	    
    	 
    	 if(!userEmail.isEmpty())
    	 {
    		 list = contactLogService.contactList(userEmail);
    	 }
    	 else
 		 {
 			 return "redirect:login-page";
 		 }
	     
    	 for(int i =0; i< list.size(); i++)
    	 {
    		 logger.error("list++++++++" + list.get(i));
    	 }
    		 
    	 
    	 model.addAttribute("list", list);
	     
		 
        return "mypage/medicalHistory";
     }
	 
	 
	 @GetMapping("/medical-history-detail-page")
	    public String contactViewList(HttpServletRequest request, Model model, @RequestParam(value="contactSeq", defaultValue="0") long contactSeq) 
	 	{
		 	logger.error("contactSeq : " + contactSeq);
		 	 
		 	ContactLog contactLog = null;
		 	Paging paging = null;
		 	
			 String token = jwtFilter.extractJwtFromCookie(request);
	    	 String userEmail = jwtFilter.getUsernameFromToken(token);
		    
	    	 int totalCount = 0;
	    	 
	    	 totalCount = contactLogService.contactLogTotal(userEmail);
	    	 
	    	 
	    	 if(!userEmail.isEmpty())
	    	 {
	    		 if(totalCount > 0 && contactSeq != 0)
	    		 {
	    			 contactLog = contactLogService.contactViewList(contactSeq);
	    			 
	    			 if(contactLog != null)
	    			 {
	    				 model.addAttribute("contactLog", contactLog);
	    			 }
	    		 }
	    	 }
	    	 else
	 		 {
	 			 return "redirect:login-page";
	 		 }
		     
	    	 
		 
	        return "mypage/medicalHistoryDetail";
	    }
	
}
