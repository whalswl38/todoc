package com.todoc.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
public class AdminController {

	//관리자페이지
    @GetMapping("/adminPage")
    public String adminPage( ) {
        return "admin/adminPage";
    }
}
