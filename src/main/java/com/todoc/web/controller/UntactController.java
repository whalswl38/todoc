package com.todoc.web.controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.todoc.web.dto.Paging;
import com.todoc.web.dto.Presc;
import com.todoc.web.dto.Reserve;
import com.todoc.web.dto.Untact;
import com.todoc.web.dto.User;
import com.todoc.web.security.jwt.JwtAuthorizationFilter;
import com.todoc.web.security.jwt.JwtProperties;
import com.todoc.web.service.UntactService;
import com.todoc.web.service.UserService;
import com.todoc.web.util.StringUtil;

import lombok.val;

@Controller
@RequestMapping
public class UntactController {
	@Autowired
	private UntactService untactService;
	private UserService userService;
	private final JwtAuthorizationFilter jwtFilter;
	
	public UntactController(JwtAuthorizationFilter jwtFilter){
		this.jwtFilter = jwtFilter;
	}
	
	private static final int LIST_COUNT = 10;	//한 페이지의 게시물 수
	private static final int PAGE_COUNT = 5;	//페이징 수 
	
	//비대면-과목선택
    @GetMapping("/select-subject-page")
    public String selectSubject( ) {
        return "untact/selectSubject";
    }

    //비대면-증상선택
    @GetMapping("/select-item-page")
    public String selectItem( ) {
        return "untact/selectItem";
    }
	
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
    	String sortType = request.getParameter("sortType");
    	String location = request.getParameter("location");  	
    	String status = request.getParameter("status");  	
    	
    	
    	long curPage = 1; 
    	if(request.getParameter("curPage") != null)
    	 curPage = (long)Integer.parseInt(request.getParameter("curPage"));
    	
    	if(subject != null) 
    	untact.setClinicSubject(subject);
    	
    	if(symptom != null) 
    	untact.setClinicSymptom(symptom);
    	
    	if(searchWord != null)
    	untact.setSearchWord(searchWord);
    	
    	if(sortType != null)
    	untact.setSortType(sortType);

    	if(location != null)
    	untact.setLocation(location);
    	
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
	    		} else {
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
	    	
	    	List statusList = new ArrayList();
	    	//진료중 클릭했을때,
	    	if(!StringUtil.isEmpty(status)) {
	    		//기존 N ,Y 데이터 모두 가져와서 Y 데이터만 다시 리스트 넣어서 뿌릴거
	    		for(Untact val : dtmList) {
	    			if(val.getClinicStatus().equals("Y")) {
	    				statusList.add(val);
	    			}
	    		}
	    		dtmList = statusList;
	    	}
	    	model.addAttribute("subject", dtmList);
		}
		
		model.addAttribute("untact", untact);
    	model.addAttribute("curPage", curPage);
		model.addAttribute("paging", paging);
        return "untact/selectClinic";
    }
  
    //비대면-병원상세
    @GetMapping("/select-clinic-detail-page")
    public String clinicDetail(ModelMap model, HttpServletRequest request, HttpServletResponse response) {
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
    @GetMapping("/clinic-reserve-application-page")
    public String clinicReserveApp(Model model, HttpServletRequest request) {
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
    		model.addAttribute("clinic", untact);
    		model.addAttribute("timeSlots", timeSlots);
		}
		
		//untact = untactService.selectClinicDetail(untact);
        return "untact/clinicReservation";
    }
    
    //비대면-진료예약
    @PostMapping("/clinic-reserve-page")
    @ResponseBody
    public String clinicReserve(HttpServletRequest request, HttpServletResponse response) {
    	String token = jwtFilter.extractJwtFromCookie(request);
    	String userEmail = jwtFilter.getUsernameFromToken(token);
    	
    	String clinicInstinum = request.getParameter("clinicInstinum");
    	
    	String date= request.getParameter("date");
    	String time= request.getParameter("time");
    	String symptoms= request.getParameter("symptoms");
    	
    	Reserve rsve = new Reserve();
    	
    	if(!StringUtil.isEmpty(userEmail)) {
    		rsve.setUserEmail(userEmail);
    	}
    	
    	if(!StringUtil.isEmpty(clinicInstinum)) {
    		rsve.setClinicInstinum(clinicInstinum);
    	}
    	
    	if(!StringUtil.isEmpty(symptoms)) {
    		rsve.setReservationSymptom(symptoms);
    	}
    	
    	if(!StringUtil.isEmpty(time)) {
    		rsve.setReservationTime(time);
    	}
    	
    	if(!StringUtil.isEmpty(date)) {
    		if(date.equals("오늘")){
    			rsve.setReservationDate(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")));
    		} else {
    			rsve.setReservationDate(LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("yyyy/MM/dd")));
    		}
    	}
    	
    	int res = 0;
    	if((res = untactService.insertReservation(rsve)) > 0 ) {
    		response.addIntHeader("code", 250);
    	} else {
    		response.addIntHeader("code", 404);
    	}
    	response.addIntHeader("res", res);
    	return  Integer.toString(res);
    }

    //비대면-결제
    @GetMapping("/clinic-reserve-payment-page")
    public String clinicReservePay(HttpServletRequest request, HttpServletResponse response) {
    	String token = jwtFilter.extractJwtFromCookie(request);
    	String userEmail = jwtFilter.getUsernameFromToken(token);
    	
    	return "untact/clinicReservationPayment";
    }
    
    
    @GetMapping("/clinic-reserve-user-page")
    public String clinicReserveUser(Model model, HttpServletRequest request, HttpServletResponse response) {
    	String token = jwtFilter.extractJwtFromCookie(request);
    	String userEmail = jwtFilter.getUsernameFromToken(token);
    	
    	Reserve rsve = new Reserve();
    	
    	if(!StringUtil.isEmpty(userEmail)) {
    		rsve.setUserEmail(userEmail);
    	}
    	
    	rsve = untactService.reserveCheck(rsve);
    	
    	model.addAttribute("rsve", rsve);
    	
    	return "untact/reservationUserView";
    }
    
    //안쓸예정
    @GetMapping("/clinic-reserve-doctor-page")
    public String test13() {
        return "untact/reservationDoctorView";
    }
    
    //약국리스트
    @GetMapping("/select-pharm-page")
    public String pharmList() {
    	return "untact/pharmacyList";
    }
    
    @GetMapping("/pdf-page")
    public String test30() {
        return "untact/pdf";
    }
    
    @PostMapping("/prescriptionInsert")
    public String prescriptionInsert(Model model, HttpServletRequest request, HttpServletResponse response) {
    	String reservationSeq =request.getParameter("reservationSeq");
    	String clinicInstinum = request.getParameter("clinicInstinum");
    	String dose = request.getParameter("dose");
    	Presc presc = new Presc();
    	int res= 0;
    	
    	List<String> list = new ArrayList<String>();
    	for(int i = 1; i<=6; i++) {
    		if(!(StringUtil.isEmpty(request.getParameter("medi"+i)) || request.getParameter("medi"+i).equals("")))
    			list.add(request.getParameter("medi"+i));
    	}
		int idx = 1;
		String prescriptionSeq = String.valueOf(untactService.getprescriptionSeq());
    	for(Object var  : list) {
    		presc.setPrescriptionSeq(prescriptionSeq);
    		presc.setMedi((String)var);
    		presc.setClinicInstinum(clinicInstinum);
    		presc.setDose(dose);
    		presc.setReservationSeq(Integer.parseInt(reservationSeq));
    		presc.setOrder(String.valueOf(idx++));
    		res += untactService.prescriptionInsert(presc);
    	}
    	//insert 결과값
    	if(res > 0) { //성공했을때,
    		return "untact/prescriptionList";
    	} else { //실패했을때,
    		return "untact/prescriptionList";
    	}
    	
    	
    	
    }
    
    @GetMapping("/medicine-page")
    public String test31(Model model, HttpServletRequest request, HttpServletResponse response) {
    	String token = jwtFilter.extractJwtFromCookie(request);
    	String userEmail = jwtFilter.getUsernameFromToken(token);
    	
    	Untact untact = new Untact(); 
    	Reserve rsve = new Reserve();
    	
    	untact.setUserEmail(userEmail);
    	//todo: 예약에서 시퀀스 받아오기
    	untact.setReservationSeq(16);
    	untact = untactService.precriptionWrite(untact);
    	model.addAttribute("untact", untact);
    	
    	return "untact/prescription";
    }


}
