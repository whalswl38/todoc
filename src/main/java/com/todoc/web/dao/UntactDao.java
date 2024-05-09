package com.todoc.web.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.todoc.web.dto.Untact;

@Mapper
public interface UntactDao {
	List subjectList(Untact untact);
	
	Untact selectClinicDetail(Untact untact);
}
