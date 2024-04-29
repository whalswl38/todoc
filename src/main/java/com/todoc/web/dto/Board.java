package com.todoc.web.dto;

import lombok.Data;

@Data
public class Board {
	private long reviewSeq;
	private String userId;
	private String userName;
	private String reviewTitle;
	private String reviewContent;
	private String reviewDelFlag;
	private int reviewReadCnt;
	private int reviewLikeCnt;
	private String regdate;
}

