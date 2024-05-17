package com.todoc.web.service;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailParseException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.todoc.web.EmailTemplate;
import com.todoc.web.dao.UserDao;
import com.todoc.web.dto.Clinic;
import com.todoc.web.dto.ClinicFile;
import com.todoc.web.dto.Institution;
import com.todoc.web.dto.Pharmacy;
import com.todoc.web.dto.StampFile;
import com.todoc.web.dto.User;

import lombok.extern.slf4j.Slf4j;
import net.nurigo.java_sdk.api.Message;

@Slf4j
@Service
public class UserService 
{
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private JavaMailSender mailSender;
	
	@Autowired
	private EmailTemplate emailTemplate;
		
    // 병원 이미지 파일 경로
    @Value("${clinicFile.upload.dir}")
    private String clinicFileUploadDir;
    
    // 인감 이미지 파일 경로
    @Value("${stampFile.upload.dir}")
    private String stampFileUploadDir; 
	
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
	
	// 랜덤 문자열 생성
	public String rand()
	{
		Random rand = new Random();
		
		String numStr = "";
		
		// 인증번호 랜덩값
		for(int i=0 ; i<6 ; i++)
		{
			String ran = Integer.toString(rand.nextInt(10));
			numStr += ran;
		}
		
		return numStr;
	}
	
	// 문자 인증
	public String sendSMS(String to) throws Exception
	{	
		String api_key = "NCSFHBPPFH4YKLGD";
		String api_secret_key = "XUNKCP6IOIIVUWRY18PAWSRZKBXMBXV1";

		Message coolsms = new Message(api_key, api_secret_key);
		
		String numStr = rand();
		
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
	public int insertClinicFile(MultipartFile clinicFile, Clinic clinic) throws IOException
	{			
		if(clinicFile.isEmpty())
		{
			return 0;
		}
		
		// 원래 파일 이름 추출
		String orgName = clinicFile.getOriginalFilename();
		
		// 파일 이름으로 쓸 uuid 생성
		String uuid = UUID.randomUUID().toString();
		
		// 확장자 추출
		String extension = orgName.substring(orgName.lastIndexOf("."));
		
		// uuid 와 확장자 합침
		String saveName = uuid + extension;
		
		// 파일 크기
		long fileSize = clinicFile.getSize();
		
		// 로컬에 저장
		clinicFile.transferTo(new File(clinicFileUploadDir + saveName));
		
		// 데이터 베이스에 파일 저장
		ClinicFile insertClinicFile = new ClinicFile();
		insertClinicFile.setFileExt(extension);
		insertClinicFile.setFileName(saveName);
		insertClinicFile.setFileOrgName(orgName);
		insertClinicFile.setFileSize(fileSize);
		insertClinicFile.setClinicInstinum(clinic.getClinicInstinum());
		
		try
		{
			return userDao.insertClinicFile(insertClinicFile);
		}
		catch(Exception e)
		{
			log.error("exception : " + e);
			return 0;
		}
	}
	
	// 인감 사진 등록
	public int insertStampFile(MultipartFile stampFile, Clinic clinic) throws IOException
	{			
		if(stampFile.isEmpty())
		{
			return 0;
		}
		// 원래 파일 이름 추출
		String orgName = stampFile.getOriginalFilename();
		
		// 파일 이름으로 쓸 uuid 생성
		String uuid = UUID.randomUUID().toString();
		
		// 확장자 추출
		String extension = orgName.substring(orgName.lastIndexOf("."));
		
		// uuid 와 확장자 합침
		String saveName = uuid + extension;
		
		// 파일 크기
		long fileSize = stampFile.getSize();
		
		// 로컬에 저장
		stampFile.transferTo(new File(stampFileUploadDir + saveName));
		
		// 데이터 베이스에 파일 저장
		StampFile insertStampFile = new StampFile();
		
		insertStampFile.setFileExt(extension);
		insertStampFile.setFileName(saveName);
		insertStampFile.setFileOrgName(orgName);
		insertStampFile.setFileSize(fileSize);
		insertStampFile.setUserEmail(clinic.getUserEmail());
				
		try
		{
			return userDao.insertStampFile(insertStampFile);
		}
		catch(Exception e)
		{
			log.error("exception : " + e);
			return 0;
		}
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
	
	// 일반 유저 아이디 찾기
	public User findUser(User user)
	{
		try
		{
			return userDao.findUser(user);
		}
		catch(Exception e)
		{
			log.error("exception : " + e);
			return null;
		}
	}
	
	// 병원 유저 아이디 찾기
	public String findClinic(Clinic clinic)
	{
		try
		{
			return userDao.findClinic(clinic);
		}
		catch(Exception e)
		{
			log.error("exception : " + e);
			return null;
		}
	}
	
	// 약국 유저 아이디 찾기
	public String findPharmacy(Pharmacy pharmacy)
	{
		try
		{
			return userDao.findPharmacy(pharmacy);
		}
		catch(Exception e)
		{
			log.error("exception : " + e);
			return null;
		}
	}
	
	// 비밀번호 수정 
	public int updateUser(User user)
	{
		return userDao.updateUser(user);
	}
	
	public int updatePharm(Pharmacy pharmacy)
	{
		return userDao.updatePharm(pharmacy);
	}
	
	public int updateClinic(Clinic clinic)
	{
		return userDao.updateClinic(clinic);
	}
	
	// 메일 전송
	public boolean sendTemplateEmail(String userEmail, String randomNum) throws Exception
	{
	    EmailTemplate emailTemplate = new EmailTemplate();

	    // 인증번호 설정
	    emailTemplate.setVerificationCode(randomNum);
	    
		String content = emailTemplate.idFindTemplate();
	
		try
		{
			// 메일 전송을 도와주는 클래스
			MimeMessage mime = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mime, true, "UTF-8");
			
			helper.setFrom("a1a2a4a562@gmail.com", "[todoc]");
			helper.setTo(userEmail); // 수신자
			helper.setSubject("[todoc] 계정 이메일 인증"); // 제목
			
			// 템플릿에 전달할 데이터 설정
			helper.setText(content, true);
			mailSender.send(mime);
			
		}
		catch (MailParseException ex) 
		{			
	        log.error("Sending Mail Exception : {} [failure when parsing the message]", ex.getCause());
	        return false;
	    } 
		catch (MailAuthenticationException ex) 
		{
	    	log.error("Sending Mail Exception : {} [authentication failure]", ex.getCause());
	        return false;
	    } 
		catch (MailSendException ex) 
		{
	        log.error("Sending Mail Exception : {} [failure when sending the message]", ex.getCause());
	        return false;
	    } 
		catch (Exception ex) 
		{
	        log.error("Sending Mail Exception : {} [unknown Exception]", ex.getCause());
	        return false;
	    } 

		return true;
	}
	
	
	//회원정보 수정
	public int userUpdate(User user)
	{
		return userDao.userUpdate(user);
	}
}
