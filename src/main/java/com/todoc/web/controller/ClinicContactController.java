package com.todoc.web.controller;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.thymeleaf.util.StringUtils;

import com.todoc.web.dto.ClinicContact;
import com.todoc.web.dto.ReservationContact;
import com.todoc.web.security.jwt.JwtAuthorizationFilter;
import com.todoc.web.security.jwt.JwtProperties;
import com.todoc.web.service.ClinicContactService;


@Controller
@RequestMapping
public class ClinicContactController {

	@Autowired
	private ClinicContactService clinicContactService;
	
	private final JwtAuthorizationFilter jwtFilter;
	
	public ClinicContactController(JwtAuthorizationFilter jwtFilter)
	{
		this.jwtFilter = jwtFilter;
	}

	private static final Logger log = LoggerFactory.getLogger(ClinicContactController.class);
	
	

	//과목선택
	@GetMapping("/clinic-contact-subject-page")
	public String selectSubject() {
		return "contact/selectSubject";
	}
	
	//증상선택
	@GetMapping("/clinic-contact-item-page")
	public String selectItem() {
		return "contact/selectItem";
	}
	
	

	 //리스트 페이지 다이렉트
	 
	 @GetMapping("/clinic-contact-list-page") 
	 public String clinicListDirect(Model model) {
		 System.out.println("3333333333333333333333333333"); 
		 List<ClinicContact> list = new ArrayList<>(); 
		 ClinicContact search=new ClinicContact(); 
		 list = clinicContactService.clinicList();
	 
	 model.addAttribute("clinicList", list); model.addAttribute("search",search);
	 System.out.println("3333333333333333333333333333");
	 
	 return "contact/clinicList"; 
	 }
	 
	
	//리스트 페이지 카테고리
	@GetMapping("/clinic-contact-category-list")
	public String clinicListCategory(
			@RequestParam(value = "category", required = false) String category,
			@RequestParam(value = "searchValue", required = false) String searchValue,
			@RequestParam(value = "clinicNight", required = false) String clinicNight,
			@RequestParam(value = "clinicWeekend", required = false) String clinicWeekend, 
			@RequestParam(value = "textSearch", required = false) String textSearch, 
			@RequestParam(value = "guValue",required=false) Integer guValue,    
			@RequestParam(value = "isOpening", required = false) String isOpening,
			@RequestParam(value = "locationValue", required = false) String locationValue,
			Model model){
		System.out.println("컨트롤러 시작");
		System.out.println("isOpening : " + isOpening);
		List<ClinicContact> list = new ArrayList<>();
		ClinicContact search=new ClinicContact();
		search.setCategory(category);
		search.setSearchValue(searchValue);
		search.setClinicNight(clinicNight);
		search.setClinicWeekend(clinicWeekend);
		search.setTextSearch(textSearch);
		
		//구 지역 검색 인덱스 로 변환(현재위치구/선택구)
		if(locationValue != null) {
			System.out.println("어딜까");
			int locationIndex = search.getGuList().indexOf(locationValue);
			System.out.println("어딜까");

			if (locationIndex != -1) {
			    System.out.println("찾는 값의 인덱스: " + locationIndex);
			    search.setGuValue(locationIndex); 
			} else {
			    System.out.println("해당 값이 리스트에 존재하지 않습니다.");
			}
			
		}else {
			System.out.println("여긴가");
			search.setGuValue(guValue); 
		}
		
			

		
		
	
		
		List runningNumList = new ArrayList<>();
		runningNumList = clinicContactService.clinicRunningList();
		for(int i = 0; i < runningNumList.size();i++) {  
			System.out.println("runningList : " + runningNumList.get(i));
		}
		
		//영업중인 병원 번호 리스트
		if("Y".equals(isOpening)) {
			System.out.println("Y.equals(isOpening) if문");
			if(runningNumList == null || runningNumList.isEmpty()) {
				runningNumList.add(0, "");
				search.setRunningNumList(runningNumList);
			}
			search.setRunningNumList(runningNumList);
		}
		
		
		list = clinicContactService.clinicListCategory(search);
		
		System.out.println("///////////////////////////////");
		System.out.println("clinicNight : " + clinicNight);
		System.out.println("clinicWeekend : " + clinicWeekend);
		System.out.println("category : " + category);
		System.out.println("search.getCategory() : " + search.getCategory());
		System.out.println("searchValue : " + searchValue);
		System.out.println("search.getSearchValue() : " + search.getSearchValue());
		System.out.println("textSearch : " + textSearch);
		System.out.println("search.getTextSearch() : " + search.getTextSearch());
		System.out.println("guValue : " + guValue);
		System.out.println("locationValue : " + locationValue);
		System.out.println("search.getGuValue() : " + search.getGuValue());
		System.out.println("search.getGuName() : " + search.getGuName());
		System.out.println("///////////////////////////////");

		
		model.addAttribute("search",search);
		model.addAttribute("clinicList", list);
		model.addAttribute("searchValue", searchValue);
		model.addAttribute("category", category);
		model.addAttribute("textSearch", textSearch);
		model.addAttribute("guValue", guValue);
		model.addAttribute("isOpening", isOpening);
		model.addAttribute("runningNumList", runningNumList);
		model.addAttribute("locationValue", locationValue);

		
		

		return "contact/clinicList";
	}
	

	

	@GetMapping("/clinic-contact-detail-page")
	public String clinicDetail(@RequestParam("clinicInstinum") String clinicInstinum, Model model) {
		
		ClinicContact clinicContact = new ClinicContact();
		clinicContact = clinicContactService.clinicDetail(clinicInstinum);

		//요일별 진료시간
		List<String> clinicTimeList = new ArrayList<>(); 
		String[] clinicTime = clinicContact.getClinicTime().split(",");	
		
		for(int i = 0; i < clinicTime.length ; i++) { 
			clinicTimeList.add(clinicTime[i]); 
		}
		
		//요일
		List<String> clinicDayList = new ArrayList<>();
		String[] clinicDay = clinicContact.getClinicDay().split(",");
		
		for(int i = 0; i < clinicDay.length; i++) {
			clinicDayList.add(clinicDay[i]);
		}
		
		//진료과목 
		List<String> clinicSubjectList = new ArrayList<>();
		String[] clinicSubject = clinicContact.getClinicSubject().split(",");
		
		for(int i = 0; i < clinicSubject.length; i++) {  
			clinicSubjectList.add(clinicSubject[i]);
		}
		
		//경력사항
		String clinicCareer = clinicContact.getClinicCareer();
		List<String> careerList = new ArrayList<>();

		if (clinicContact.getClinicCareer() != null && !clinicContact.getClinicCareer().trim().isEmpty()) {
		    String[] career = clinicCareer.split(",");
		    for (String careerItem : career) {
		        careerList.add(careerItem);
		    }
		}


		model.addAttribute("clinic", clinicContact);
		model.addAttribute("clinicTimeList", clinicTimeList);
		model.addAttribute("clinicDayList", clinicDayList);
		model.addAttribute("clinicSubjectList", clinicSubjectList);
		model.addAttribute("careerList", careerList);


		return "contact/clinicDetail";
	}
	
	//대면 병원 예약페이지
    @GetMapping("/contact-clinic-reserve-page")
    public String reservation(@RequestParam(value = "clinicInstinum", required = false) String clinicInstinum,
    HttpServletRequest request,Model model) {
    	
    	String token = jwtFilter.extractJwtFromCookie(request);
    	String userEmail = jwtFilter.getUsernameFromToken(token);
    	
    	if(userEmail == null) {
    		return "login/login";
    	}
    	
    	//현재 시점의 최초 timeSlots
    	List<String> timeSlots = clinicContactService.reserveTimebutton(clinicInstinum);    	

        model.addAttribute("timeSlots", timeSlots);
        model.addAttribute("clinicInstinum",clinicInstinum);
    	
        //공휴일플래그
        String holidayFlag = clinicContactService.isHoliday(clinicInstinum);
        System.out.println("holidayFlag : " + holidayFlag);
        model.addAttribute("holidayFlag",holidayFlag);
    	
    	return "contact/clinicReservation";
    }
    
    //예약확인 ajax로 
    @PostMapping("/reservationPost")
    @ResponseBody
	public int reservationPost(HttpServletRequest request, @RequestParam("comments") String comments,@RequestParam("selectedDate") String selectedDate, @RequestParam("selectedTime") String selectedTime, @RequestParam("clinicInstinum") String clinicInstinum ){
    	
    	String token = jwtFilter.extractJwtFromCookie(request);
    	String userEmail = jwtFilter.getUsernameFromToken(token);

    	if(!StringUtils.isEmpty(userEmail)) {
    		
	    		if(!StringUtils.isEmpty(clinicInstinum)){
	    			
	    			ClinicContact clinic = new ClinicContact();
	    			
	    			clinic = clinicContactService.clinicDetail(clinicInstinum);
	    			//해당 병원 요일별 진료시간 
	    			String[] clinicTime = clinic.getClinicTime().split(",");
	    			
			    	if(!StringUtils.isEmpty(comments) && !StringUtils.isEmpty(selectedDate) &&!StringUtils.isEmpty(selectedTime)) {
			    	     
			    		if(clinicContactService.dayCheck(selectedDate,selectedTime,clinicTime)) {
			    	    	 
			    	    	 System.out.println("영업시간맞음");
			    	    	 
			    	    	 ReservationContact reservation = new ReservationContact();
			    	    	 reservation.setClinicInstinum(clinicInstinum);
			    	    	 reservation.setReservationDate(selectedDate);
			    	    	 reservation.setReservationTime(selectedTime);
			    	    	 reservation.setReservationSymptom(comments);
			    	    	 reservation.setUserEmail(userEmail);
			    	    	 
			    	    	 if(clinicContactService.reservationInsert(reservation)>0) {
			    	    		 
			    		   	 // comments 변수에 담긴 값 확인
			    		   	 System.out.println("Comments: " + comments);
			    		   	 System.out.println("selectedDate: " + selectedDate);
			    		   	 System.out.println("selectedTime: " + selectedTime);
			    		   	 
			    			    return 0;	
			    	    	 }else {
			    	    		 return 202;//인서트 실패
			    	    	 }
			    	     }else {
			    	    	 System.out.println("영업시간 아님");
			    	     }
			    			
			    	}else {
			    		
			    	}
	    			
	    		}else {
	    			
	    		}
	    	 
	    }else {
	    	return 101;
	    }
    	return 404;    	

    }

    @ResponseBody 
    @GetMapping("/getAvailableTimeSlots")
    public ResponseEntity<?> getAvailableTimeSlotsY(@RequestParam("selectedDate") String selectedDate, @RequestParam("clinicInstinum") String clinicInstinum, @RequestParam(value = "_isHoliday", required = false) String _isHoliday) {
        // 선택한 날짜에 따른 시간대를 반환하는 로직 구현
        // 예시로 간단히 미리 정의된 시간대를 반환하는 코드
    	List<String> timeSlots = null;
    	System.out.println("clinicInstinum : " + clinicInstinum);
    	System.out.println("selectedDate : " + selectedDate);
    	System.out.println("_isHoliday : " + _isHoliday);
    	//누르는 날짜마다 바뀌는 timeSlots
    	timeSlots = clinicContactService.reserveTimebutton(selectedDate,clinicInstinum,_isHoliday);    
//        if ("2024-05-15".equals(selectedDate)) {
//        	timeSlots = Arrays.asList("09:40", "10:00", "10:20", "10:40");
//            return ResponseEntity.ok(timeSlots);
//        } else {
//        	timeSlots =  Arrays.asList("11:00", "11:20", "11:40", "12:00", "12:20");
//            return ResponseEntity.ok(timeSlots);
//
//        }
    	System.out.println("컨트롤러");
    	System.out.println("timeSlots : " + timeSlots);

    	
    	return ResponseEntity.ok(timeSlots);
    }
    
   


    


}

