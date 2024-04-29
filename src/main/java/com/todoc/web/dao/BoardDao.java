package com.todoc.web.dao;

import org.apache.ibatis.annotations.Mapper;

import com.todoc.web.dto.Board;

@Mapper
public interface BoardDao {
	// 게시글 목록 조회
	public Board read (long reviewSeq) throws Exception;
}
