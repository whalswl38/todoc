package com.todoc.web.dao;

import org.apache.ibatis.annotations.Mapper;

import com.todoc.web.dto.Clinic;
import com.todoc.web.dto.ClinicFile;
import com.todoc.web.dto.Institution;
import com.todoc.web.dto.Pharmacy;
import com.todoc.web.dto.StampFile;
import com.todoc.web.dto.User;

@Mapper
<<<<<<< HEAD
public interface UserDao {
	// 게시글 목록 조회
	public User read (String userId) throws Exception;
	
	
	
	
=======
public interface UserDao 
{	
	// 리프레쉬 토큰으로 회원 조회
	User findByRefreshToken(String userRefreshToken);
	
	// 리프레쉬 토큰 업데이트
	void refreshTokenUpdate(User user);
	
	// 병원 리프레쉬 토큰 업데이트
	void clinicRefreshTokenUpdate(Clinic clinic);
	
	// 약국 리프레쉬 토큰 업데이트
	void PharmRefreshTokenUpdate(Pharmacy pharmacy);
	
	// 이메일로 회원 확인
	User findByEmail(String userEmail);
	
	// 이메일로 병원 회원 확인
	Clinic findClinicEmail(String userEmail);
	
	// 이메일로 약국 회원 확인
	Pharmacy findPharmEmail(String userEmail);
	
	// 회원가입
	int userInsert(User user);
	
	// 병의원, 약국 회원가입시 요양기관 가입여부 확인
	Institution findInstitution(String institutionNum);

	// 이메일 중복확인
	int checkEmail(String userEmail);
	
	// 병원 사진 등록
	int insertClinicFile(ClinicFile clinicFile);
	
	// 인감 사진 등록
	int insertStampFile(StampFile stampFile);
	
	// 약국 회원가입
	int insertPharmacy(Pharmacy pharmacy);
	
	// 병원 회원가입
	int insertClinic(Clinic clinic);
>>>>>>> main
}
