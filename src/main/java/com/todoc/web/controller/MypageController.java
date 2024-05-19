package com.todoc.web.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.todoc.web.dto.ClinicContact;
import com.todoc.web.dto.ReservationContact;
import com.todoc.web.dto.User;
import com.todoc.web.security.jwt.JwtAuthorizationFilter;
import com.todoc.web.service.ClinicContactService;
import com.todoc.web.service.ContactLogService;
import com.todoc.web.service.ReviewService;
import com.todoc.web.service.UntactService;
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
	private ClinicContactService clinicContactService;
	
	@Autowired
	private UntactService untactService;
	
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
	    	
	    	
	    	if(userEmail != null)
	    	{
		    	if(contactTotalCount >= 0)
		    	{
		    		model.addAttribute("contactTotalCount", contactTotalCount);
		    	}
		    	
		    	if(reviewTotalCount >= 0)
		    	{
		    		model.addAttribute("reviewTotalCount", reviewTotalCount);
		    	}
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
	@ResponseBody
	public int update(HttpServletRequest request, @RequestBody User user, Model model)
	{
		logger.error("User user : " + user);
		
		String token = jwtFilter.extractJwtFromCookie(request);
    	String userEmail = jwtFilter.getUsernameFromToken(token);
		
    	User updateUser= new User();
    	
    	if(!userEmail.isEmpty())
    	{
    		updateUser = userService.findByEmail(user.getUserEmail());
    		
    		if(updateUser != null)
    		{
    			updateUser.setUserName(user.getUserName());
    			updateUser.setUserIdentity(user.getUserIdentity());
    			
    			if(userService.userUpdate(updateUser) > 0)
    			{
    				return 0;
    			}
    			else
    			{
    				return 1;
    			}
    		}
    		else
    		{
    			return 2;
    		}
    	}
    	else 
    	{
    		return 3;
    	}
		
	}
	
	@GetMapping("/reservationList-page")
    public String test29() {
        return "mypage/reservationList";
    }
    
    @GetMapping("/medical-mypage")
    public String test30() {
        return "mypage/mypageMedical";
    }
    
    @GetMapping("/reservationStatus-page")
    public String clinicContactService(HttpServletRequest request, Model model) 
    {
    	List<ReservationContact> list = null;
    	ClinicContact clinic = null;
    	
    	String token = jwtFilter.extractJwtFromCookie(request);
    	String userEmail = jwtFilter.getUsernameFromToken(token);
    	
    	if(userEmail != null)
    	{
    		clinic = clinicContactService.clinicListView(userEmail);
    		logger.error("clinic : " + clinic);
    		
    		if(clinic != null)
    		{
    			list = clinicContactService.reservationList(clinic.getClinicInstinum());
    			
    			if(list != null)
    			{
    				model.addAttribute("list", list);
    				model.addAttribute("clinic", clinic);
    			}
    		}
    	}
    	
    	
        return "mypage/reservationStatus";
    }
    
    @PostMapping("/mypage/reservationApprove")
    @ResponseBody
    public int reservationApprove(@RequestBody ReservationContact reservationContact, HttpServletRequest request, Model model) 
    {
    	long reservationSeq = reservationContact.getReservationSeq();    	
    	
    	String token = jwtFilter.extractJwtFromCookie(request);
    	String userEmail = jwtFilter.getUsernameFromToken(token);
    	
    	if(userEmail != null && reservationSeq > 0)
    	{
    		if(clinicContactService.reservationApprove(reservationSeq) > 0)
    		{
    			return 0;
    		}
    		else
    		{
    			return 1;
    		}
    	}
    	else
    	{
    		return 2;
    	}
    }
    
    
    @PostMapping("/mypage/reservationCancel")
    @ResponseBody
    public int reservationCancel(@RequestBody ReservationContact reservationContact, HttpServletRequest request, Model model) 
    {
    	long reservationSeq = reservationContact.getReservationSeq();    	
    	
    	String token = jwtFilter.extractJwtFromCookie(request);
    	String userEmail = jwtFilter.getUsernameFromToken(token);
    	
    	if(userEmail != null && reservationSeq > 0)
    	{
    		if(clinicContactService.reservationCancel(reservationSeq) > 0)
    		{
    			return 0;
    		}
    		else
    		{
    			return 1;
    		}
    	}
    	else
    	{
    		return 2;
    	}
    }
    
}
