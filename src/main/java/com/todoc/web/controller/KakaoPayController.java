package com.todoc.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.todoc.web.security.jwt.JwtAuthorizationFilter;
import com.todoc.web.service.KakaoPayService;
import com.todoc.web.service.UserService;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping
public class KakaoPayController {
	@Autowired
	private KakaoPayService kakaoPayService;

	private final JwtAuthorizationFilter jwtFilter;
	
	public KakaoPayController(JwtAuthorizationFilter jwtFilter){
		this.jwtFilter = jwtFilter;
	}
    
    @PostMapping("/kakaoPay")
    @ResponseBody
    public String kakaoPay() {
        log.info("kakaoPay post............................................");
        
        return kakaoPayService.kakaoPayReady();
 
    }
    
    @GetMapping("/kakaoPaySuccess")
    public String kakaoPaySuccess(@RequestParam("pg_token") String pg_token, Model model) {
        log.info("kakaoPaySuccess get............................................");
        log.info("kakaoPaySuccess pg_token : " + pg_token);

        model.addAttribute("info", kakaoPayService.kakaoPayInfo(pg_token));

        return "pay/kakaoPaySuccess";
    }
    
    @PostMapping("/kakaoPayResult")
    public String kakaoPayResult(@RequestParam("pg_token") String pg_token, Model model) {
        log.info("kakaoPaySuccess get............................................");
        log.info("kakaoPaySuccess pg_token : " + pg_token);

        model.addAttribute("info", kakaoPayService.kakaoPayInfo(pg_token));

        return "pay/kakaoPaySuccess3";
    }
    
	
}
