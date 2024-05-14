package com.todoc.web.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.todoc.web.dao.ReviewDao;
import com.todoc.web.dto.Review;

@Service
public class ReviewService {
	@Autowired
	private ReviewDao reviewDao;
	
	public List<Review> reviewList(String userEmail)
	{
		return reviewDao.reviewList(userEmail);
	}
	
	//후기 리스트 더보기
	public List<Review> reviewListPlus(Review review)
	{
		return reviewDao.reviewListPlus(review);
	}
}
