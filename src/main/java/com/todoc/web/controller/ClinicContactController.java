package com.todoc.web.controller;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.todoc.web.dto.ClinicContact;
import com.todoc.web.service.ClinicContactService;

@Controller
@RequestMapping
public class ClinicContactController {

	@Autowired
	private ClinicContactService clinicContactService;

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
	public String clinicListCategory(@RequestParam(value = "category", required = false) String category, @RequestParam(value = "searchValue", required = false) String searchValue,@RequestParam(value = "clinicNight", required = false) String clinicNight,@RequestParam(value = "clinicWeekend", required = false) String clinicWeekend, @RequestParam(value = "textSearch", required = false) String textSearch, @RequestParam(value="guValue",required=false) String guValue,  Model model) {
		System.out.println("컨트롤러 시작");
		List<ClinicContact> list = new ArrayList<>();
		ClinicContact search=new ClinicContact();
		search.setCategory(category);
		search.setSearchValue(searchValue);
		search.setClinicNight(clinicNight);
		search.setClinicWeekend(clinicWeekend);
		search.setTextSearch(textSearch);
		search.setGuValue(guValue); // 인덱스
		
		//영업중
		

		
//		
//		List<ClinicContact> timeListAll = clinicContactService.clinicTimeList();
//		int size = timeListAll.size();
//		String[][] timeAndInstinumArray = new String[size][10]; // 10으로 수정
//
//		for (int i = 0; i < size; i++) {
//		    timeAndInstinumArray[i][0] = timeListAll.get(i).getClinicInstinum();
//
//		    // ClinicTime을 쉼표(,)로 분할하여 배열로 만듭니다.
//		    String[] clinicTimes = timeListAll.get(i).getClinicTime().split(",");
//
//		    // ClinicTime 배열의 요소를 timeAndInstinumArray에 할당합니다.
//		    for (int j = 0; j < clinicTimes.length; j++) {
//		        timeAndInstinumArray[i][j + 1] = clinicTimes[j];
//		        System.out.print(timeAndInstinumArray[i][j]+",");
//		    }
//		    System.out.println();
//		}
//		


		
		
	
		
		
//		List<ClinicContact> timeListAll = clinicContactService.clinicList();
//		
//		int size = timeListAll.size();
//		String[][] timeAndInstinumArray = new String[size][8]; // 10으로 수정
//
//		for (int i = 0; i < size; i++) { 
//
//		    // ClinicTime을 쉼표(,)로 분할하여 배열로 만듭니다.
//		    String[] clinicTimes = timeListAll.get(i).getClinicTime().split(",");
//
//		    // ClinicTime 배열의 요소를 timeAndInstinumArray에 할당합니다.
//		    for (int j = 0; j < clinicTimes.length; j++) {
//		        timeAndInstinumArray[i][j] = clinicTimes[j];
//		        System.out.print(timeAndInstinumArray[i][j]+",");
//		    }
//		    System.out.println();
//		}
//		
//		
		

		
		
		
		
		
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
		System.out.println("search.getGuValue()) : " + search.getGuValue());
		System.out.println("///////////////////////////////");

		
		model.addAttribute("search",search);
		model.addAttribute("clinicList", list);
		model.addAttribute("searchValue", searchValue);
		model.addAttribute("category", category);
		model.addAttribute("textSearch", textSearch);
		model.addAttribute("guValue", guValue);

		return "contact/clinicList";
	}
	

	

	@GetMapping("/clinic-contact-detail-page")
	public String clinicDetail(@RequestParam("clinicInstinum") String clinicInstinum, Model model) {
		
		ClinicContact clinicContact = new ClinicContact();
		clinicContact = clinicContactService.clinicDetail(clinicInstinum);

		//요일별 진료시간 (경력사항처럼 바꾸기)
		List<String> clinicTimeList = new ArrayList<>(); 
		String[] clinicTime = clinicContact.getClinicTime().split(",");	
		
		for(int i = 0; i < clinicTime.length ; i++) { 
			clinicTimeList.add(clinicTime[i]); 
		}
		
		//요일 (경력사항처럼 바꾸기)
		List<String> clinicDayList = new ArrayList<>();
		String[] clinicDay = clinicContact.getClinicDay().split(",");
		
		for(int i = 0; i < clinicDay.length; i++) {
			clinicDayList.add(clinicDay[i]);
		}
		
		//진료과목 (경력사항처럼 바꾸기)
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
	
	
	 
	

}
