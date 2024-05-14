package com.todoc.web.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.todoc.web.dto.Review;
import com.todoc.web.security.jwt.JwtAuthorizationFilter;
import com.todoc.web.service.ReviewService;

@Controller
@RequestMapping
public class ReviewController {
	
	private static Logger logger = LoggerFactory.getLogger(ReviewController.class);
	
	@Autowired
	private ReviewService reviewService;
	
	private final JwtAuthorizationFilter jwtFilter;
	
	public ReviewController(JwtAuthorizationFilter jwtFilter)
	{
		this.jwtFilter = jwtFilter;
	}
	
	/*
	@GetMapping("/review-detail-page")
    public String reviewList(HttpServletRequest request, ModelMap model) {
		
		List<Review> list = null;
		Review review = new Review();
		
		String token = jwtFilter.extractJwtFromCookie(request);
    	String userEmail = jwtFilter.getUsernameFromToken(token);
    	
    	list = reviewService.reviewList(userEmail);
    	
	
		if(userEmail != null)
		{
			list = reviewService.reviewList(userEmail);
		}
		else
		{
			return "redirect:login-page";
		}
		
		model.addAttribute("list", list);
		
        return "mypage/reviewDetail";
    }
	*/
	@GetMapping("/review-detail-page")
    public String reviewList(HttpServletRequest request, ModelMap model,  @RequestParam(value="endRow", defaultValue="0") int endRow) {
		
		List<Review> list = null;
		Review review = new Review();
		
		String token = jwtFilter.extractJwtFromCookie(request);
    	String userEmail = jwtFilter.getUsernameFromToken(token);
    	
    	logger.error("endRow : " + endRow);
    	endRow = 3;
    	
		if(userEmail != null)
		{
			review.setUserEmail(userEmail);
			review.setStartRow(0);
			review.setEndRow(3);
			
			list = reviewService.reviewListPlus(review);
			
		}
		else
		{
			return "redirect:login-page";
		}
		
		model.addAttribute("list", list);
		model.addAttribute("endRow", endRow);
		
        return "mypage/reviewDetail";
    }
	
	
	 @GetMapping("/review-page")
	    public String reviewView() {
	        return "mypage/review";
	    }
	
	 
	 @PostMapping("/reviewWrite")
	 @ResponseBody
	 	public int reviewWrite(@RequestBody Review review, HttpServletRequest request, HttpServletResponse response) {
		 	
		 	String token = jwtFilter.extractJwtFromCookie(request);
	    	String userEmail = jwtFilter.getUsernameFromToken(token);
	    	
	    	if(userEmail != null)
	    	{
	    		review.setUserEmail(userEmail);
	    		
	    		if(!review.getReviewTitle().isEmpty() && !review.getReviewContent().isEmpty() )
	    		{
    				if(reviewService.reviewInsert(review) > 0)
	    			{
	    				return 1;
	    			}
    				else
    				{
    					return 0;
    				}
	    		}
	    		else
	    		{
	    			return 0;
	    		}
	    	}
	    	else
	    	{
	    		return 0;
	    	}
		 	
	 	}
}
