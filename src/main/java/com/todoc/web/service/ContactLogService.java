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
	
	public List<ContactLog> contactList(String userEmail)
	{
		return contactLogDao.contactList(userEmail);
	}
	
	public ContactLog contactViewList(long contactSeq)
	{
		return contactLogDao.contactViewList(contactSeq);
	}
	
	public int contactLogTotal(String userEmail)
	{
		return contactLogDao.contactLogTotal(userEmail);
	}
	
	
}
