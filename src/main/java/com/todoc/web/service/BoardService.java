package com.todoc.web.service;

import com.todoc.web.dto.Board;

public interface BoardService 
{
	// 게시글 목록 조회
	public Board read (long reviewSeq) throws Exception;
	
}
