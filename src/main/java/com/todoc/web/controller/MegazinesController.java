package com.todoc.web.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.todoc.web.dto.Megazines;
import com.todoc.web.dto.Paging;
import com.todoc.web.security.jwt.JwtAuthorizationFilter;
import com.todoc.web.service.MegazinesService;
import com.todoc.web.service.UserService;



@Controller
@RequestMapping
public class MegazinesController {
	
	@Autowired
	private MegazinesService megazinesService;
	
	@Autowired
	private UserService userService;
	
	private final JwtAuthorizationFilter jwtFilter;
	
	public MegazinesController(JwtAuthorizationFilter jwtFilter){
		this.jwtFilter = jwtFilter;
	}
	
	private static final Logger log = LoggerFactory.getLogger(ClinicContactController.class);
	
	private static final int LIST_COUNT = 10;
	private static final int PAGE_COUNT = 5;
	
		
	   //매거진 리스트
	   @GetMapping("/megazine-list-page")
	   public String list(Model model, HttpServletRequest request) {
		   
		 //정렬필터타입(1:최신순, 2: 조회, 3: 추천)
		 String newsFilter = request.getParameter("newsFilter");
	     //검색 (진료과,증상,제목,내용)
	     String searchValue = request.getParameter("searchValue");
		 
	      
	     //현재페이지
	     long curPage = 1;
	     
	     System.out.println("newsFilter : " + newsFilter);
	     System.out.println("searchValue : " + searchValue);
	
	     //게시물 리스트
	     List<Megazines> list = null;
	     //파일(사진첨부파일)리스트
	     //List<ReviewFile> fileList = null;
	
	     //조회객체
	     Megazines search = new Megazines();
	     //페이징 객체
	     Paging paging = null;
	     //총 게시물
	     long totalCount = 0;
	     
	     
	    	 search.setNewsFilter(newsFilter);
	    	 search.setSearchValue(searchValue);
	     
	     
	     totalCount = megazinesService.megazinesListCount(search);	
	     
		if(totalCount > 0) {  
		 	if(request.getParameter("curPage") != null)
		    	 curPage = (long)Integer.parseInt(request.getParameter("curPage"));
			paging = new Paging("/megazine-list-page",totalCount,LIST_COUNT,PAGE_COUNT ,curPage,"curPage");
		
			search.setStartRow(paging.getStartRow());
			search.setEndRow(paging.getEndRow());

		}
		
		list = megazinesService.MegazinesList(search);
		System.out.println("totalCount : " + totalCount);
		System.out.println("list : " + list);
		 
		model.addAttribute("search",search);
		model.addAttribute("list", list);
		model.addAttribute("searchValue", searchValue);
		model.addAttribute("curPage",curPage); 
		model.addAttribute("paging",paging);
		model.addAttribute("newsFilter",newsFilter);

		   
	       return "megazines/megazineList";
	   }
	   
	   //매거진 글쓰기 페이지
	   @GetMapping("/megazine-write-page")
	   public String write(HttpServletRequest request) {
			String token = jwtFilter.extractJwtFromCookie(request);
	    	String userEmail = jwtFilter.getUsernameFromToken(token);
		   
	    	if(userEmail == null) {
	    		return "login/login";
	    	}else {
	    		if(!"ADMIN".equals(userService.findByEmail(userEmail))) {
	    			return "redirect:/logout";
	    		}
	    	}

	    	
	       return "megazines/megazineWrite";
	   }
	   
	   //글 등록
	   public String writeProc(MultipartHttpServletRequest request, HttpServletResponse response) {
		   
		   
		   
		    return "megazines/megazineList";
	   }
	   
	   
	   
	   
	   //매거진 글 상세페이지
	   @GetMapping("/megazine-detail-page")
	   public String detail() {
		   
		   
		   
		   
		   
		   
		   
	       return "megazines/megazineDetail";
	   }
	
	
	
}
