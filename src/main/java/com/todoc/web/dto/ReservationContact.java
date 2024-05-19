package com.todoc.web.dto;

import lombok.Data;

@Data
public class ReservationContact {

	private long reservationSeq;
	private String userEmail;
	private String clinicInstinum;
	private String reservationDate;
	private String reservationTime;
	private String reservationSymptom;
	private String reservationFlag;
	private String regdate;
	private String status;
	
	//예약확인 리스트
	private String userName;
	private String clinicDoctor;
	private String clinicName;
	
	
	
	


	
	
	
	
	
	
	
}
