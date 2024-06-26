package com.todoc.web.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.todoc.web.dto.ClinicContact;
import com.todoc.web.dto.ReservationContact;


@Mapper
public interface ClinicContactDao {
	/*희주*/
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
	
	/*승준*/
	//예약확인 리스트
	List<ReservationContact> reservationList(String clinicInstinum);

	//예약확인 의사 상세
	ClinicContact clinicListView(String userEmail);
	
	//예약리스트 승인
	int reservationApprove(long reservationSeq);
	
	//예약리스트 취소
	int reservationCancel(long reservationSeq);
	
	//예약 승인리스트 토탈 카운트
	int reservationListTotal(String clinicInstinum);
	
	//진료 대기 리스트 토탈 카운트
	int contactListTotal(String clinicInstinum);
	
	//이메일로 병원정보 불러오기
	ClinicContact clinicfindByEmail(String userEmail);
	
	

	
	

}
