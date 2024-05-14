package com.todoc.web.controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.todoc.web.dto.Paging;
import com.todoc.web.dto.Untact;
import com.todoc.web.dto.User;
import com.todoc.web.security.jwt.JwtAuthorizationFilter;
import com.todoc.web.security.jwt.JwtProperties;
import com.todoc.web.service.UntactService;
import com.todoc.web.service.UserService;

import lombok.val;

@Controller
@RequestMapping
public class UntactController {
	@Autowired
	private UntactService untactService;
	private UserService userService;

	//
	private final JwtAuthorizationFilter jwtFilter;
	
	public UntactController(JwtAuthorizationFilter jwtFilter){
		this.jwtFilter = jwtFilter;
	}
	
    @GetMapping("/select-subject-page")
    public String test5( ) {
        return "untact/selectSubject";
    }

    @GetMapping("/select-item-page")
    public String test6( ) {
        return "untact/selectItem";
    }
    
    //test용
    @GetMapping("/select-clinic2-page")
    public String test( ) {
        return "untact/selectClinic2";
    }
    
    
	
	private static final int LIST_COUNT = 10;	//한 페이지의 게시물 수
	private static final int PAGE_COUNT = 5;	//페이징 수 
	

    //비대면-병원리스트
    @GetMapping("/select-clinic-page")
    public String subjectList(Model model,  HttpServletRequest request) {
    	List<Untact> list = new ArrayList<Untact>();
    	Untact untact = new Untact();
    	List<Untact> dtmList = new ArrayList<>();
    	Paging paging = null;
    	LocalDate currentDate = LocalDate.now();
    	LocalTime currentTime = LocalTime.now();
    	String clinicTime = null;
    	int dayOfWeekIndex = currentDate.getDayOfWeek().getValue();

    	String subject = request.getParameter("subject");
    	String symptom = request.getParameter("symptom");
    	String searchWord = request.getParameter("searchWord");
    	/*
    	 * 1. 과목별 눌렀을 때 subject,curPage-> 전체리스트  
    	 * 2. 증상별 눌렀을때 symptom,curPage-> 전체리스트
    	 * 3. 검색했을때 searchWord,curPage-> 전체리스트
    	 */
    	
    	// 현재페이지
    	long curPage = 1; 
    	if(request.getParameter("curPage") != null)
    	 curPage = (long)Integer.parseInt(request.getParameter("curPage"));
    	
    	if(subject != null) 
    	untact.setClinicSubject(subject);
    	
    	if(symptom != null) 
    	untact.setClinicSymptom(symptom);
    	
    	if(searchWord != null)
    	untact.setSearchWord(searchWord);
    	
    	// 정렬 버튼용 파라미터
    	untact.setSortType(request.getParameter("sortType"));
    	
		
		int totalCount = untactService.subjectListCount(untact);
		if (totalCount > 0) {
		paging = new Paging("/select-clinic-page", totalCount, LIST_COUNT, PAGE_COUNT, curPage, "curPage");
		untact.setStartRow(paging.getStartRow()); 
		untact.setEndRow(paging.getEndRow());
    	
    	list = untactService.subjectList(untact);
    	
    	for(Untact val : list) {
    		String[] dtm = val.getClinicTime().split(",");
    		val.setClinicTime(dtm[dayOfWeekIndex-1]);
    		clinicTime = dtm[dayOfWeekIndex-1];
    		if (clinicTime.equals("휴무")) {
    			val.setClinicStatus("N");
    			val.setClinicNight("N");
    		}else {
	    		LocalTime startTime = LocalTime.parse(clinicTime.split("-")[0]);
	    		LocalTime endTime = LocalTime.parse(clinicTime.split("-")[1]);
  
	    		if (endTime.isAfter(LocalTime.parse("18:00"))) {
	                val.setClinicNight("Y");
	            } else {
	                val.setClinicNight("N");
	            }

	    		if(currentTime.isAfter(startTime) && currentTime.isBefore(endTime)) {
	    			val.setClinicStatus("Y");
	    		} else {
	    			val.setClinicStatus("N");
	    		}
    		}
    		dtmList.add(val);
    	}
    	model.addAttribute("subject", dtmList);
		}
		
    	model.addAttribute("curPage", curPage);
		model.addAttribute("paging", paging);
        return "untact/selectClinic";
    }
  
    //비대면-병원상세
    @GetMapping("/select-clinic-detail-page")
    public String test8(ModelMap model, HttpServletRequest request, HttpServletResponse response) {
       String clinicInstinum = request.getParameter("clinicInstinum");
       List<Untact> reviewList2 = new ArrayList<Untact>(); 
       Untact untact = new Untact(); 
       
       untact.setClinicInstinum(clinicInstinum); 
       
       untact = untactService.selectClinicDetail(untact);
       
       reviewList2 = untactService.reviewList(untact); 
       
       String subject = "";
       String career = "";
       String time = "";
       
       if(untact.getClinicSubject() != null) 
       subject = untact.getClinicSubject();
       
       if(untact.getClinicCareer() != null) 
       career = untact.getClinicCareer();
       
       if(untact.getClinicTime() != null) 
       time = untact.getClinicTime();
       
       String[] subjectList = subject.split(",");
       String[] careerList = career.split(",");
       String[] timeList = time.split(",");
       
      model.addAttribute("clinicInstinum", clinicInstinum);
      model.addAttribute("untact", untact);
      model.addAttribute("subjectList", subjectList);
      model.addAttribute("careerList", careerList);
      model.addAttribute("timeList", timeList);
      model.addAttribute("reviewList", reviewList2);
      
       return "untact/selectClinicDetail";
    }
    
    //비대면-병원예약
    @GetMapping("/clinic-reserve-page")
    public String test9(@CookieValue(name = JwtProperties.HEADER_STRING, required = false) String cookieValue, Model model, HttpServletRequest request) {
    	//유저 정보
    	String token = jwtFilter.extractJwtFromCookie(request);
    	String userEmail = jwtFilter.getUsernameFromToken(token);
    	
    	String clinicInstinum = request.getParameter("clinicInstinum");
    	
    	Untact untact = new Untact();   
    	untact.setClinicInstinum(clinicInstinum);
    	
        
    	LocalDate currentDate = LocalDate.now();
    	LocalTime currentTime = LocalTime.now();
    	String clinicTime = null;
    	int dayOfWeekIndex = currentDate.getDayOfWeek().getValue();
    	
    	untact = untactService.selectClinicDetail(untact);
    	
		String[] dtm = untact.getClinicTime().split(",");
		untact.setClinicTime(dtm[dayOfWeekIndex-1]);
		clinicTime = dtm[dayOfWeekIndex-1];
		if (!clinicTime.equals("휴무")) {

			LocalTime startTime = LocalTime.parse(clinicTime.split("-")[0]);
    		LocalTime endTime = LocalTime.parse(clinicTime.split("-")[1]);

    		List<LocalTime> timeSlots = new ArrayList<>();

    		if (currentTime.isAfter(endTime)) {
    		    model.addAttribute("message", "진료 시간이 끝났습니다.");
    		} else {
    		    if (currentTime.isAfter(startTime)) {
    		    	
    		        //timeSlots.add(currentTime);
    		        currentTime = currentTime.plusMinutes(20);
    		    }

    		    while (currentTime.isBefore(endTime)) {
    		    	
    		        if (currentTime.getMinute() % 20 != 0) {
    		            int minutesToAdd = 20 - (currentTime.getMinute() % 20);
    		            currentTime = currentTime.plusMinutes(minutesToAdd);
    		        }
    		        
    		        if (!currentTime.equals(LocalTime.MIDNIGHT)) {
    		            timeSlots.add(currentTime);
    		        }
    		        
    		        currentTime = currentTime.plusMinutes(20);
    		    }
    		}
    		model.addAttribute("timeSlots", timeSlots);
		}
		
        return "untact/clinicReservation";
    }
    
    //비대면-결제
    @GetMapping("/clinic-reserve-payment-page")
    public String test10() {
        return "untact/clinicReservationPayment";
    }

    @GetMapping("/clinic-reserve-user-page")
    public String test11() {
        return "untact/reservationUserView";
    }

    @GetMapping("/clinic-reserve-doctor-page")
    public String test12() {
        return "untact/reservationDoctorView";
    }

}
