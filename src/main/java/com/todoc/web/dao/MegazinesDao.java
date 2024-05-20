package com.todoc.web.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.todoc.web.dto.Megazines;

@Mapper
public interface MegazinesDao {

	
	//게시물 리스트
	public List<Megazines> MegazinesList(Megazines megazines);
		
	//게시물 수 count
	public long megazinesListCount(Megazines megazines);
	
	//게시물 상세
	public Megazines megazinesDetail(long newsSeq);
	
	
	
	
	
	
	
	
	
}
