package com.todoc.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.todoc.web.dao.UserDao;
import com.todoc.web.dto.User;

@Service
public class UserService 
{
	@Autowired
	private UserDao usermapper;
	
	public User read (String userId) throws Exception
	{
		return usermapper.read(userId);
	}
}
