package com.todoc.web.service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.todoc.web.dao.ClinicContactDao;
import com.todoc.web.dto.ClinicContact;



@Service
public class ClinicContactService {
	private static Logger logger = LoggerFactory.getLogger(ClinicContactService.class);
	
	@Autowired
	private ClinicContactDao clinicContactDao;
	
	//대면 병원 리스트(total로 바꿔보기)
	public List<ClinicContact> clinicList(){ 
		List<ClinicContact> list =null;
		
		try {
			list = clinicContactDao.clinicList(); 	
		}catch(Exception e) {
			logger.error("[ClinicContactService] clinicList Exception",e);
		}

		return list;
		
	}
	
	//병원 리스트 total
	public List<ClinicContact> clinicListTotal(ClinicContact search){
		List<ClinicContact> list =null;
		
		try {
			list = clinicContactDao.clinicListTotal(search); 	
		}catch(Exception e) {
			logger.error("[ClinicContactService] clinicListTotal Exception",e);
		}

		return list;
		
	}
	
	//병원 리스트 조회(category)
	public List <ClinicContact> clinicListCategory(ClinicContact search){
		List<ClinicContact> list =null;
		String searchValueCode = search.getSearchValue();
		String searchValue=null;
		
		if(searchValueCode != null) {
		
			switch(searchValueCode) { 
				// 진료과목 검색
				case "1": searchValue = "피부과";
					break;
				case "2": searchValue = "산부인과";
					break;
				case "3": searchValue = "이비인후과";
					break;
				case "4": searchValue = "내과";
					break;
				case "5": searchValue = "안과";
					break;
				case "6": searchValue = "가정의학과";
					break;
				case "7": searchValue = "소아과";
					break;
				case "8": searchValue = "정형외과";
					break;
				case "9": searchValue = "정신건강의학과";
					break;
				case "10": searchValue = "비뇨기과";
					break;
				case "11": searchValue = "치과";
					break;
				case "12": searchValue = "신경외과";
					break;
				// 증상 검색
				case "21": searchValue = "감기/몸살";
					break;	
				case "22": searchValue = "소화불량";
					break;		
				case "23": searchValue = "소아과";
					break;		
				case "24": searchValue = "비염";
					break;		
				case "25": searchValue = "여드름";
					break;		
				case "26": searchValue = "탈모";
					break;		
				case "27": searchValue = "다이어트";
					break;		
				case "28": searchValue = "인공눈물";
					break;		
				case "29": searchValue = "위염";
					break;		
				case "30": searchValue = "장염";
					break;		
				case "31": searchValue = "당뇨";
					break;		
				case "32": searchValue = "고혈압";
					break;		
				case "33": searchValue = "여성질환";
					break;		
				case "34": searchValue = "만성질환";
					break;		
				case "35": searchValue = "복통";
					break;		
				case "36": searchValue = "두통";
					break;	
				case "37": searchValue = "주의력저하";
					break;
				case "38": searchValue = "우울증";
					break;
				case "39": searchValue = "치아";
					break;
				case "40": searchValue = "기력저하";
					break;
				
				
			}
		}
		if(search.getGuValue() != null) {
		int guValueIndex = Integer.parseInt(search.getGuValue());
		search.setGuValue(search.getGuList().get(guValueIndex));
		}
		
		search.setSearchValue(searchValue);
		list =clinicContactDao.clinicListCategory(search);
		
	
		return list;
		
		
	}
	
	//영업시간 전체 리스트
	public List<ClinicContact> clinicTimeList(){   
		List<ClinicContact> list =null;
		try {
			list = clinicContactDao.clinicTimeList();
		} catch (Exception e) {
			logger.error("[ClinicContactService] clinicTimeList Exception",e);
		}
		
		return list;
	}
	
	//진료중 구하기
	public List clinicRunningList(){  
		
		List<ClinicContact> timeListAll = clinicContactDao.clinicTimeList();
        int size = timeListAll.size();
        String[][] timeAndInstinumArray = new String[size][10]; // 10으로 수정

        for (int i = 0; i < size; i++) {
            timeAndInstinumArray[i][0] = timeListAll.get(i).getClinicInstinum();

            // ClinicTime을 쉼표(,)로 분할하여 배열로 만듭니다.
            String[] clinicTimes = timeListAll.get(i).getClinicTime().split(",");

            // ClinicTime 배열의 요소를 timeAndInstinumArray에 할당합니다.
            for (int j = 0; j < clinicTimes.length; j++) {
                timeAndInstinumArray[i][j + 1] = clinicTimes[j];
            }
        }
        
        // 현재 요일과 시간을 가져옵니다.
        DayOfWeek currentDayOfWeek = DayOfWeek.from(LocalDateTime.now());
        LocalTime currentTime = LocalTime.now();
        
        // 영업중인 ClinicInstinum 목록을 반환합니다.
        List<String> openClinics = new ArrayList<>();
        int dayIndex = currentDayOfWeek.getValue() - 1;
        
        for (int i = 0; i < timeAndInstinumArray.length; i++) {
            String businessHour = timeAndInstinumArray[i][dayIndex + 1];
            if (businessHour != null && isBusinessHour(businessHour)) {
                openClinics.add(timeAndInstinumArray[i][0]);
            }
        }
        
        System.out.println(openClinics);
        return openClinics;
        
	}
	private boolean isBusinessHour(String businessHour) {
        LocalTime currentTime = LocalTime.now();
        String[] hours = businessHour.split("-");
        LocalTime startTime = LocalTime.parse(hours[0]);
        LocalTime endTime = LocalTime.parse(hours[1]);

        return !currentTime.isBefore(startTime) && !currentTime.isAfter(endTime);
    }
	
	//병원 상세페이지 조회
	public ClinicContact clinicDetail(String clinicInstinum) {
		ClinicContact clinicContact = null;
		try {
			clinicContact = clinicContactDao.clinicDetail(clinicInstinum);
		} catch (Exception e) {
			logger.error("[ClinicContactService] clinicDetail Exception",e);
		}
		
		return clinicContact;
	}
	

	
	
	
	
	
}


