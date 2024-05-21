package com.todoc.web.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.todoc.web.dao.ContactLogDao;
import com.todoc.web.dto.ContactLog;

@Service
public class ContactLogService {
	@Autowired
	private ContactLogDao contactLogDao;
	
	//진료내역 리스트
	public List<ContactLog> contactList(String userEmail)
	{
		return contactLogDao.contactList(userEmail);
	}
	
	//진료세부내역
	public ContactLog contactViewList(long contactSeq)
	{
		return contactLogDao.contactViewList(contactSeq);
	}
	
	//진료내역 토탈카운트
	public int contactLogTotal(String userEmail)
	{
		return contactLogDao.contactLogTotal(userEmail);
	}
	
	//진료내역 리뷰체크
	public int contactSeqCheck(long contactSeq)
	{
		return contactLogDao.contactSeqCheck(contactSeq);
	}
}
