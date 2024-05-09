package com.todoc.web.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.todoc.web.dto.ClinicContact;


@Mapper
public interface ClinicContactDao {
	//병원 리스트 조회 (total 쿼리로 바꿔보기)
	public List <ClinicContact> clinicList();
	
	//병원 리스트 조회(category)
	public List <ClinicContact> clinicListCategory(ClinicContact search);
	
	//병원 리스트 total
	public List<ClinicContact> clinicListTotal(ClinicContact search);
	
	//영업시간 전체 리스트
	public List<ClinicContact> clinicTimeList();
	
	//병원 상세페이지 조회
	public ClinicContact clinicDetail(String clinicInstinum);
	

	
	

}
