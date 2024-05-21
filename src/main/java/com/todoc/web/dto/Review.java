package com.todoc.web.dto;

import lombok.Data;

@Data
public class Review {
	
	private String reviewRegdate;
	
	private String reviewContent;
	
	private String reviewTitle;
	
	private long reviewSeq;
	
	private String userEmail;
	
	private int reviewGrade;
	
	private String userName;
	
	private String clinicName;
	
	private String clinicDoctor;
	
	private String clinicInstinum;
	
	private int startRow;
	private int endRow;
	
	private long contactSeq;
}

