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
	
	//마이페이지
	@GetMapping("/mypage-page")
	 public String test17(HttpServletRequest request, Model model) 
	 {
		
	    	String token = jwtFilter.extractJwtFromCookie(request);
	    	String userEmail = jwtFilter.getUsernameFromToken(token);
	    	
	    	
	    	int contactTotalCount = contactLogService.contactLogTotal(userEmail);
	    	int reviewTotalCount = reviewService.reviewTotal(userEmail);
	    	
	    	
	    	if(userEmail != null)
	    	{
	    		User user = userService.findByEmail(userEmail);
	    		
	    		if(user != null)
	    		{
			    	if(contactTotalCount >= 0)
			    	{
			    		model.addAttribute("contactTotalCount", contactTotalCount);
			    		model.addAttribute("userEmail", userEmail);
			    	}
			    	
			    	if(reviewTotalCount >= 0)
			    	{
			    		model.addAttribute("reviewTotalCount", reviewTotalCount);
			    	}
	    		}
	    		else
	    		{
	    			return "redirect:/main-page";
	    		}
	    	}
	    	else
	    	{
	    		return "redirect:/login-page";
	    	}
	
	    	return "mypage/mypage";
	   }
	
	//회원정보수정 페이지
	@GetMapping("/infoUpdate-page")
    public String infoUpdate(HttpServletRequest request, Model model) 
	{
		String token = jwtFilter.extractJwtFromCookie(request);
    	String userEmail = jwtFilter.getUsernameFromToken(token);
    	
    	if(userEmail != null)
    	{
    		User user = userService.findByEmail(userEmail);
    	
	    	if(user != null)
	    	{
	    		logger.error("userName : " + user.getUserName());
	    		model.addAttribute("user", user);
	    	}
    	}
    	else
    	{
    		return "redirect:/login-page";
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
    	
    	if(userEmail != null)
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
	
	//진료대기 리스트
	@GetMapping("/reservationList-page")
    public String reservationList(HttpServletRequest request, Model model) {

		List<ReservationContact> list = null;
    	ClinicContact clinic = null;
		
		String token = jwtFilter.extractJwtFromCookie(request);
    	String userEmail = jwtFilter.getUsernameFromToken(token);
    	
    	if(userEmail != null)
    	{
    		clinic = clinicContactService.clinicListView(userEmail);
    		
    		if(clinic != null)
    		{
    			list = clinicContactService.reservationList(clinic.getClinicInstinum());
    			
				model.addAttribute("list", list);
				model.addAttribute("clinic", clinic);
    			
				for(int i =0; i<list.size(); i++)
				{
					logger.error("list : " + list.get(i));
				}
    		}
    	}
    	else
    	{
    		return "redirect:/login-page";
    	}
		
        return "mypage/reservationList";
    }
    
	
	//의사 마이페이지
    @GetMapping("/medical-mypage")
    public String medicalMypage(HttpServletRequest request, Model model) {
    	
    	String token = jwtFilter.extractJwtFromCookie(request);
    	String userEmail = jwtFilter.getUsernameFromToken(token);
    	
    	//예약승인 리스트
    	int listTotalCount = 0;
    	
    	//진료 대기 리스트
    	int contactTotalCount = 0;
    	
    	//이메일로 병원정보 가져오기
    	ClinicContact clinic = null;
    	
    	if(userEmail != null)
    	{
    		clinic = clinicContactService.clinicfindByEmail(userEmail);
    		
    		logger.error("clinic :" + clinic);
    		
    		if(clinic != null)
    		{
    			listTotalCount = clinicContactService.reservationListTotal(clinic.getClinicInstinum());
    			
    			contactTotalCount = clinicContactService.contactListTotal(clinic.getClinicInstinum());
    			
    			if(listTotalCount >= 0)
    			{
    				model.addAttribute("listCount", listTotalCount);
    			}
    			
    			if(contactTotalCount >= 0)
    			{
    				model.addAttribute("contactCount", contactTotalCount);
    			}
    		}
    		else
    		{
    			return "redirect:/main-page";
    		}
    	}
    	else
    	{
    		return "redirect:/login-page";
    	}
    	
        return "mypage/mypageMedical";
    }
    
    //예약 승인 취소 리스트
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
    			
				model.addAttribute("list", list);
				model.addAttribute("clinic", clinic);
    				
    		}
    	}
    	else
    	{
    		return "redirect:/login-page";
    	}
    	
    	
        return "mypage/reservationStatus";
    }
    
    //예약승인
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
    
    //예약취소
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
    
    
    //의사 회원정보수정
    @GetMapping("/medicalUpdate-page")
    public String mypageMedical(Model model)
    {
    	return "mypage/medicalUpdate";
    }
    
}
