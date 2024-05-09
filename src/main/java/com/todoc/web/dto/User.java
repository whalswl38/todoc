package com.todoc.web.dto;


import javax.validation.constraints.*;

import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.Data;

@Data
public class User
{
	@NotBlank(message="이메일은 필수 입력 값입니다.")
	@Email(message="이메일 형식이 올바르지 않습니다.")
	private String userEmail;
	
	@NotEmpty(message="비밀번호는 필수 입력 값입니다.")
	@Size(min=4, max=10)
	@Pattern(regexp="^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d\\w\\W]{8,}$", message="비밀번호는 영문 대소문자, 숫자를 포함 8자리이상이어야 합니다.")
	private String userPwd;
	
	@NotEmpty(message="이름은 필수 입력 값입니다.")
	@Size(min=2, message="이름은 2자 이상 입력하셔야합니다.")
	private String userName;
	
	private String userIdentity;
	
	@NotEmpty(message="전화번호는 필수 입력 값입니다.")
	private String userPhone;
	
	private String userPhoneFlag;
	
	private String userRegdate;
	
	private String userStatus;
	
	private String userType;
	
	private String socialType;
	
	private String userRefreshToken;
	
	public User encodePassword(PasswordEncoder passwordEncoder) 
	{
		this.userPwd = passwordEncoder.encode(this.userPwd);
		return this;
	} 
}
