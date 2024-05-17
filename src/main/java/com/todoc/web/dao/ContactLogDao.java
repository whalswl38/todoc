package com.todoc.web.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.todoc.web.dto.ContactLog;

@Mapper
public interface ContactLogDao {
	
	//진료내역 리스트
	List<ContactLog> contactList(String userEmail);
	
	//진료세부내역
	ContactLog contactViewList(long contactSeq);
	
	//진료내역 토탈카운트
	int contactLogTotal(String userEmail);
}
