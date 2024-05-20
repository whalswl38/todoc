package com.todoc.web.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.todoc.web.dao.MegazinesDao;
import com.todoc.web.dto.Megazines;

@Service
public class MegazinesService {
	private static Logger logger = LoggerFactory.getLogger(MegazinesService.class);

	@Autowired
	private MegazinesDao megazinesDao;
	

	
	//게시물 리스트
	public List<Megazines> MegazinesList(Megazines megazines){
		List<Megazines> list = null;
		try {
			list =megazinesDao.MegazinesList(megazines);
		} catch (Exception e) {
			logger.error("[MegazinesService] MegazinesList Exception", e);
		}
		return list;
	}
		
	//게시물 수 count
	public long megazinesListCount(Megazines megazines) {
		long count = 0;
		try {
			count =megazinesDao.megazinesListCount(megazines);
		} catch (Exception e) {
			logger.error("[MegazinesService] megazinesListCount Exception", e);
		}
		
		
		return count;
	}
	
	//게시물 상세
	public Megazines megazinesDetail(long newsSeq) {
		Megazines megazines = null;
		try {
			megazines = megazinesDao.megazinesDetail(newsSeq);

			//조회수 증가 포함하기
//			if(review != null) {  
//				reviewDao.boardReadCntPlus(reviewSeq);
//				
//				
//			}
		} catch (Exception e) {
			logger.error("[MegazinesService] megazinesDetail Exception", e);
		}
		
		
		
		return megazines;
		
		
		
		
	}
	
	
}
