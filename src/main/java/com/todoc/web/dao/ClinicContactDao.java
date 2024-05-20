package com.todoc.web.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.todoc.web.dto.ClinicContact;
import com.todoc.web.dto.ReservationContact;


@Mapper
public interface ClinicContactDao {


	//병원 리스트 조회 
	public List <ClinicContact> clinicList();
	
	//병원 리스트 조회(category)
	public List <ClinicContact> clinicListCategory(ClinicContact search);
	

	//게시물 수
	public long listCount(ClinicContact search);
	
	//병원 리스트 total
	public List<ClinicContact> clinicListTotal(ClinicContact search);
	
	//영업시간 전체 리스트
	public List<ClinicContact> clinicTimeList();
	
	//병원 상세페이지 조회
	public ClinicContact clinicDetail(String clinicInstinum);
	
	//대면 예약
	public int reservationInsert(ReservationContact reservation);
	
	//병원 오늘 영업시간
	public ClinicContact timeOnly(String clinicInstinum);
	
	//해당날짜 이미 예약된 시간대 찾기
	public List<ReservationContact> reservedTime(ReservationContact reservation);



}
