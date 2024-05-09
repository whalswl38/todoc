package com.todoc.web.service;

import java.util.HashMap;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.todoc.web.dao.UserDao;
import com.todoc.web.dto.Clinic;
import com.todoc.web.dto.ClinicFile;
import com.todoc.web.dto.Institution;
import com.todoc.web.dto.Pharmacy;
import com.todoc.web.dto.StampFile;
import com.todoc.web.dto.User;

import net.nurigo.java_sdk.api.Message;

@Service
public class UserService 
{
	@Autowired
	private UserDao userDao;
	
	private PasswordEncoder passwordEncoder;
		
	public boolean checkPassword(String rawPassword, String encodedPassword) 
	{
	    return passwordEncoder.matches(rawPassword, encodedPassword);
	}
	
	// 리프레쉬 토큰으로 회원 조회
	public User findByRefreshToken(String userRefreshToken)
	{
		return userDao.findByRefreshToken(userRefreshToken);
	}
	
	// 리프레쉬 토큰 업데이트
	public void refreshTokenUpdate(User user) 
	{
	    userDao.refreshTokenUpdate(user);
	}
	
	// 병원 리프레쉬 토큰 업데이트
	public void clinicRefreshTokenUpdate(Clinic clinic)
	{
		userDao.clinicRefreshTokenUpdate(clinic);
	}
	
	// 약국 리프레쉬 토큰 업데이트
	public void PharmRefreshTokenUpdate(Pharmacy pharmacy)
	{
		userDao.PharmRefreshTokenUpdate(pharmacy);
	}
	
	// 이메일로 회원 확인
	public User findByEmail(String userEmail)
	{
		return userDao.findByEmail(userEmail);
	}
	
	// 이메일로 병원 회원 확인
	public Clinic findClinicEmail(String userEmail)
	{
		return userDao.findClinicEmail(userEmail);
	}
	
	// 이메일로 약국 회원 확인
	public Pharmacy findPharmEmail(String userEmail)
	{
		return userDao.findPharmEmail(userEmail);
	}
	
	// 회원가입
	public int userInsert(User user)
	{
		return userDao.userInsert(user);
	}
	
	// 문자 인증
	public String sendSMS(String to) throws Exception
	{	
		String api_key = "NCSFHBPPFH4YKLGD";
		String api_secret_key = "XUNKCP6IOIIVUWRY18PAWSRZKBXMBXV1";

		Message coolsms = new Message(api_key, api_secret_key);
		
		Random rand = new Random();
		
		String numStr = "";
		
		// 인증번호 랜덩값
		for(int i=0 ; i<6 ; i++)
		{
			String ran = Integer.toString(rand.nextInt(10));
			numStr += ran;
		}
		
		// 문자 서비스
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("to", to); // 수신전화번호
		params.put("from", "010-2580-3668"); // 발신전화번호
		params.put("type", "sms");
		params.put("text", "[todoc] 인증번호 [ "+ numStr + " ] 타인에게 절대 알려주지 마세요.");
		
		coolsms.send(params); // 문자 전송
		
		return numStr;
	}
	
	// 병의원, 약국 회원가입시 요양기관 가입여부 확인
	public Institution findInstitution(String institutionNum)
	{
		return userDao.findInstitution(institutionNum);
	}
	
	// 이메일 중복확인
	public int checkEmail(String userEmail)
	{
		return userDao.checkEmail(userEmail);	
	}
	
	// 병원 사진 등록
	public int insertClinicFile(ClinicFile clinicFile)
	{
		return userDao.insertClinicFile(clinicFile);
	}
	
	// 인감 사진 등록
	public int insertStampFile(StampFile stampFile)
	{
		return userDao.insertStampFile(stampFile);
	}
	
	// 약국 회원가입
	public int insertPharmacy(Pharmacy pharmacy)
	{
		return userDao.insertPharmacy(pharmacy);
	}
	
	// 병원 회원가입
	public int insertClinic(Clinic clinic)
	{
		return userDao.insertClinic(clinic);
	}
}
