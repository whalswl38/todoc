package com.todoc.web.controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.todoc.web.dto.Untact;
import com.todoc.web.service.UntactService;

import lombok.val;

@Controller
@RequestMapping
public class UntactController {
	@Autowired
	private UntactService untactService;
	
    @GetMapping("/select-subject-page")
    public String test5( ) {
        return "untact/selectSubject";
    }

    @GetMapping("/select-item-page")
    public String test6( ) {
        return "untact/selectItem";
    }

    @GetMapping("/subject-list-page")
    public String subjectList(Model model,  HttpServletRequest request) {
    	List<Untact> list = new ArrayList<>();
    	Untact untact = new Untact();
    	List<Untact> dtmList = new ArrayList<>();
        
    	LocalDate currentDate = LocalDate.now();
        int dayOfWeekIndex = currentDate.getDayOfWeek().getValue();

        
    	//진료과목
    	String subject = request.getParameter("subject");
    	
    	//증상
    	String symptom = request.getParameter("symptom");
    	
    	//운영타입
    	//String CLINIC_Type = request.getParameter("Type");
    	
    	//정렬타입
    	//String sortType = request.getParameter("sortType");
 //   	String CLINIC_SUBJECT = subject;

    	untact.setClinicSubject(subject);
    	untact.setClinicSymptom(symptom);
    	list = untactService.subjectList(untact);
    	
    	
    	for(Untact val : list) {
    		String[] dtm = val.getClinicTime().split(",");
    		val.setClinicTime(dtm[dayOfWeekIndex-1]);
    		dtmList.add(val);
    	}
    	
    	
        model.addAttribute("subject", dtmList);
        
        return "untact/selectClinic";
    }
    
    @GetMapping("/select-clinic-page")
    public String test7(Model model) {
    	List<Untact> list = new ArrayList<>();
    	Untact untact = new Untact();
    	List<Untact> dtmList = new ArrayList<>();
        
    	LocalDate currentDate = LocalDate.now();
    	LocalTime currentTime = LocalTime.now();
    	String clinicTime = null;

        int dayOfWeekIndex = currentDate.getDayOfWeek().getValue();

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
        
        return "untact/selectClinic";
    }

    @GetMapping("/select-clinic-detail-page")
    public String test8(ModelMap model, HttpServletRequest request, HttpServletResponse response) {
    	String clinicInstinum = request.getParameter("clinicInstinum");
    	Untact untact = new Untact();
    	untact.setClinicInstinum(clinicInstinum);
    	
    	untact = untactService.selectClinicDetail(untact);
    	String subject = untact.getClinicSubject();
    	String career = untact.getClinicCareer();
    	String time = untact.getClinicTime();
    	String[] subjectList = subject.split(",");
    	String[] careerList = career.split(",");
    	String[] timeList = time.split(",");
    	
		model.addAttribute("clinicInstinum", clinicInstinum);
		model.addAttribute("untact", untact);
		model.addAttribute("subjectList", subjectList);
		model.addAttribute("careerList", careerList);
		model.addAttribute("timeList", timeList);
    	return "untact/selectClinicDetail";
    }
    
    @GetMapping("/clinic-reserve-page")
    public String test9() {
        return "untact/clinicReservation";
    }
    
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
