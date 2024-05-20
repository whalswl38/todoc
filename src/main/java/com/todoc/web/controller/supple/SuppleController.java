package com.todoc.web.controller.supple;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.todoc.web.service.SuppleService;

@Controller
@RequestMapping
public class SuppleController 
{
	private SuppleService suppleService;
	
    @GetMapping("/nutrients-list-page")
    public String nutriListPage() 
    {
        return "nutrients/nutrientsList";
    }
    @GetMapping("/nutrients-detail-page")
    public String nutriDetailPage() 
    {
        return "nutrients/nutrientsDetail";
    }
    
    @GetMapping("/admin/nutrients-write")
    public String write()
    {
    	return "nutrients/nutrientsWrite";
    }
}
