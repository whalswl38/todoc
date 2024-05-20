package com.todoc.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping
public class TestController {
	
    @GetMapping("/main-page")
    public String test2() {    	
    	return "main/main";
    }

}
