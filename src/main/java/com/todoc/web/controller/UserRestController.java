package com.todoc.web.controller;

import java.util.ArrayList;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.todoc.web.dto.Clinic;
import com.todoc.web.dto.Institution;
import com.todoc.web.dto.Pharmacy;
import com.todoc.web.dto.User;
import com.todoc.web.security.jwt.JwtTokenProvider;
import com.todoc.web.security.dto.LoginDto;
import com.todoc.web.security.jwt.JwtProperties;
import com.todoc.web.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping
public class UserRestController 
{
	private final UserService userService;
	private final AuthenticationManager authenticationManager;
	private final JwtTokenProvider jwtTokenProvider;
	
	// 이메일 중복 확인
	@ResponseBody
	@PostMapping("/sign/emailCheck")
	public int emailCheck(@RequestParam("userEmail") String userEmail) 
	{
	    if(userEmail == null || userEmail.trim().isEmpty()) 
	    {
	        return 404;
	    } 
	    else 
	    {	        
	        if(userService.checkEmail(userEmail) > 0) return 1;
	        else return 0;
	    }
	}
	
	// 문자 전송
	@GetMapping("/sign/send")
	public @ResponseBody String sendSMS(@RequestParam("to") String to) throws Exception
	{
		return userService.sendSMS(to);
	}
	
	// 로그인 요청
	@PostMapping("/login")
	@ResponseBody
	public int login(@RequestBody LoginDto loginDto, HttpServletResponse response) 
	{
	    String userType = "";

	    if(loginDto.getUsername() == null || loginDto.getPassword() == null) 
	    {
	        return 0;
	    }

	    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken
	    		(
	    			loginDto.getUsername(), loginDto.getPassword(), new ArrayList<>()
	    		);

	    Authentication authentication = authenticationManager.authenticate(authenticationToken);
	    
	    
	    String accessToken = jwtTokenProvider.generateToken(authentication, false);
	    String refreshToken = jwtTokenProvider.generateToken(authentication, true);

	    if("MEDICAL".equals(loginDto.getUserType())) 
	    {
	    	log.info("UserRestController loginDto.getUsername() : " + loginDto.getUsername());
	        Clinic clinic = userService.findClinicEmail(loginDto.getUsername());
	        
	        if(clinic != null && clinic.getClinicStatus().equals("Y")) 
	        {
	        	try
	        	{
		            clinic.setClinicRefreshToken(refreshToken);
		            userService.clinicRefreshTokenUpdate(clinic);
		            userType = "MEDICAL";
	        	}
	        	catch(Exception e)
	        	{
	        		return 0;
	        	}
	        }
	        
	        Pharmacy pharm = userService.findPharmEmail(loginDto.getUsername());

	        if(pharm != null && pharm.getPharmacyStatus().equals("Y")) 
	        {
		       try
		       {
		            pharm.setPharmacyRefreshToken(refreshToken);
		            userService.PharmRefreshTokenUpdate(pharm);
		            userType = "MEDICAL";
	        	}
	        	catch(Exception e)
	        	{
	        		return 0;
	        	}
	        } 
	    } 
	    else 
	    {
	        User user = userService.findByEmail(loginDto.getUsername());
	        
	        if(user == null || user.getUserStatus().equals("N")) 
	        {
	            return 0;
	        }
	        
	        user.setUserRefreshToken(refreshToken);
	        userService.refreshTokenUpdate(user);
	        
	        if(user.getUserType().equals("ADMIN")) userType = "ADMIN";
	        else userType = "USER";
	    }

	    Cookie cookie = new Cookie(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + accessToken);
	    cookie.setHttpOnly(true);
	    cookie.setMaxAge(JwtProperties.EXPIRATION_TIME);
	    cookie.setPath("/");
	    response.addCookie(cookie);
	    
	    log.info("cookie jwt success !");

	    switch (userType) 
	    {
	        case "ADMIN": return 1;
	        case "GUEST": return 2;
	        case "USER": return 3;
	        case "MEDICAL": return 4;
	        default: return 0;
	    }
	}

	
	// 병의원, 약국 회원가입시 요양기관 가입여부 확인
	@ResponseBody
	@GetMapping("/medicalSign/checkInstitution")
	public ResponseEntity<?> checkInstitution(@RequestParam("institutionNum") String institutionNum)
	{				
	    if (institutionNum.isEmpty()) 
	    {
	        return ResponseEntity.badRequest().body("조회하신 값이 올바르지 않습니다.");
	    }
	    
	    Institution institution = userService.findInstitution(institutionNum);
	    
	    if (institution == null) 
	    {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당 요양기관은 가입되지 않았습니다.");
	    }
	    
	    return ResponseEntity.ok(institution);
	}

}
