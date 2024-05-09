package com.todoc.web.dao;

import org.apache.ibatis.annotations.Mapper;

import com.todoc.web.dto.User;

@Mapper
public interface UserDao {
	// 게시글 목록 조회
	public User read (String userId) throws Exception;
	
	
	
	
}
