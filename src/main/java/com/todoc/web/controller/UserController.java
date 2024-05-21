package com.todoc.web.controller;

import java.io.IOException;

import javax.validation.Valid;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import com.todoc.web.dto.Clinic;
import com.todoc.web.dto.Pharmacy;
import com.todoc.web.dto.User;
import com.todoc.web.security.dto.SignUpDto;
import com.todoc.web.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping
@RequiredArgsConstructor
public class UserController 
{	
	private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    
    // 아이디 찾기 페이지
    @GetMapping("/login/findId")
    public String findIdPage()
    {
    	return "login/findId";
    }
    
    // 비밀번호 찾기 페이지
    @GetMapping("/login/findPwd")
    public String findPwdPage()
    {
    	return "login/findPwd";
    }
    
	// 로그인 페이지
    @GetMapping("/login-page")
    public String loginPage( ) 
    {
        return "login/login";
    }
    
    // 병원, 약국 회원가입 페이지
    @GetMapping("/medical-register-page")
    public String medicalRegisterPage(Model model) 
    {
    	model.addAttribute("signUpDto", new SignUpDto());
    	
        return "login/medicalRegister";
    }
    
    // 병원, 약국 회원 회원가입 기능
    @Transactional
	@PostMapping("/medicalSign")
	public String medicalSign(@Valid SignUpDto signUpDto, BindingResult bindingResult, Model model,@ModelAttribute MultipartFile clinicFile,@ModelAttribute MultipartFile stampFile) throws IOException
	{	
		if(bindingResult.hasErrors())
		{
			model.addAttribute("signUpDto", signUpDto);
			return "login/medicalRegister";
		}
		
		Clinic clinic  = new Clinic();
		Pharmacy pharmacy = new Pharmacy();
		String type = "";
		
		if(signUpDto.getUserType().equals("병의원"))
		{
			try
			{
				clinic.setClinicInstinum(signUpDto.getInstitutionNum());
				clinic.setClinicRegnum(signUpDto.getRegNum());
				clinic.setClinicPhone(signUpDto.getUserPhone());
				clinic.setClinicName(signUpDto.getInstitutionName());
				clinic.setClinicSubject(signUpDto.getSubject());
				clinic.setClinicSymptom(signUpDto.getSymptop());
				clinic.setUserEmail(signUpDto.getUserEmail());
				clinic.setUserPwd(passwordEncoder.encode(signUpDto.getUserPwd()));
				clinic.setClinicDay(signUpDto.getDayOn());
				clinic.setClinicTime(signUpDto.getDayTime());
				clinic.setClinicDayoff(signUpDto.getDayOff());
				clinic.setClinicZipcode(signUpDto.getZipcode());
				clinic.setClinicAddr(signUpDto.getAddr());

				if(signUpDto.getContactType().contains("대면"))
				{
					type = "C";
				}
				else if(signUpDto.getContactType().contains("비대면"))
				{
					type = "U";
				}
				else if(signUpDto.getContactType().contains(","))
				{
					type = "C, U";
				}

				clinic.setUserEmail(signUpDto.getUserEmail());
				clinic.setClinicContactFlag(type);
				clinic.setClinicDetail(signUpDto.getDetail());
				clinic.setClinicDoctor(signUpDto.getUserName());
				clinic.setClinicFax(signUpDto.getFaxNum());
				clinic.setClinicCareer(signUpDto.getCareer());
				clinic.setClinicBreak(signUpDto.getBreakTime());
				clinic.setClinicNight(signUpDto.getNight());
				clinic.setClinicWeekend(signUpDto.getWeekend());

				if(userService.insertClinic(clinic) > 0)
				{
					if(userService.insertClinicFile(clinicFile, clinic) > 0)
					{
						if(userService.insertStampFile(stampFile, clinic) > 0)
						{							
							return "redirect:/login-page";
						}
					}
				}
				
				model.addAttribute("signUpDto", signUpDto);
				return "login/medicalRegister";
			}
			catch(Exception e)
			{
				log.error("sign Exception");
				model.addAttribute("errorMessage", e.getMessage());
				
				return "login/medicalRegister";
			}
		}
		else
		{
			try
			{
				pharmacy.setUserEmail(signUpDto.getUserEmail());
				pharmacy.setPharmacyInstinum(signUpDto.getInstitutionNum());
				pharmacy.setPharmacyRegnum(signUpDto.getRegNum());
				pharmacy.setPharmacyPhone(signUpDto.getInstitutionNum());
				pharmacy.setPharmacyName(signUpDto.getInstitutionName());
				pharmacy.setPharmacistName(signUpDto.getUserName());
				pharmacy.setUserPwd(passwordEncoder.encode(signUpDto.getUserPwd()));
				pharmacy.setPharmacyDayoff(signUpDto.getDayOff());
				pharmacy.setPharmacyTime(signUpDto.getDayTime());
				pharmacy.setPharmacyZipcode(signUpDto.getZipcode());
				pharmacy.setPharmacyAddr(signUpDto.getAddr());
				pharmacy.setPharmacyFax(signUpDto.getFaxNum());
				pharmacy.setPharmacyCareer(signUpDto.getCareer());
				
				if(userService.insertPharmacy(pharmacy) > 0)
				{
					return "redirect:/login-page";
				}
				
				model.addAttribute("signUpDto", signUpDto);
				return "login/medicalRegister";
			}
			catch(Exception e)
			{
				log.error("sign Exception : " + e);
				model.addAttribute("errorMessage", e.getMessage());
				
				return "login/medicalRegister";
			}
		}
	}
    
    // 일반 회원 회원가입 페이지
    @GetMapping("/register-page")
    public String registerPage(Model model) 
    {
        model.addAttribute("user", new User());
        
    	return "login/register";
    }
    
    // 일반 회원 회원가입 기능
	@PostMapping("/sign")
	public String sign(@Valid User user, BindingResult bindingResult, Model model)
	{
		if(bindingResult.hasErrors())
		{
			model.addAttribute("user", user);
			return "login/register";
		}
		
		User signUser = new User();
		
		try
		{
			signUser.setUserEmail(user.getUserEmail());
			signUser.setUserPwd(passwordEncoder.encode(user.getUserPwd()));
			signUser.setUserName(user.getUserName());
			signUser.setUserPhone(user.getUserPhone());
			signUser.setUserIdentity(user.getUserIdentity());
			
			if(userService.userInsert(signUser) > 0)
			{
				return "redirect:/login-page";
			}
			
			model.addAttribute("user", user);
			return "login/register";
		}
		catch(Exception e)
		{
			log.error("sign Exception");
			model.addAttribute("errorMessage", e.getMessage());
			
			return "login/register";
		}
	}	
	
	@PostMapping("/admin/oo")
	public String tesst()
	{
		return "메롱";
	}
	
	@GetMapping("/login/resetPwd")
	public String resetPwd()
	{
		return "login/resetPwd";
	}
	
	//https설정
	@GetMapping("/healthcheck")
	public String healthcheck()
	{
		return "OK";
	}
}
