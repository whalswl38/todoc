package com.todoc.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.todoc.web.dao.BoardDao;
import com.todoc.web.dto.Board;

@Service
public class BoardServiceImpl implements BoardService
{
	@Autowired
	private BoardDao mapper;
	
	public Board read(long reviewSeq) throws Exception {
        return mapper.read(reviewSeq);
    }
	
}
