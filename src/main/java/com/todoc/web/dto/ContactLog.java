package com.todoc.web.dto;

import lombok.Data;

@Data
public class ContactLog {
	private long contactSeq;
    private String userEmail;
    private String clinicName;
    private String status;
    private String contactDate;
    private String reservationSymptom;
    private String clinicInstinum;
    private String userName;
    private String advice;
    
    //병원테이블
    private String clinicDoctor;
    
    //결제내역
    private int payPrice;
    private String payDate;
    
    private long reviewSeq;
}
