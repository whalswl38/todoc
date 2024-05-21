package com.todoc.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.todoc.web.dto.Supple;
import com.todoc.web.service.SuppleService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping
public class SuppleController 
{
	@Autowired
	private SuppleService suppleService;
	
	// 리스트 페이지로 이동
    @GetMapping("/nutrients-list-page")
    public String nutriListPage(Model model) 
    {
    	Supple supple = new Supple();
    	model.addAttribute("list", suppleService.suppleList(supple));
    	
        return "nutrients/nutrientsList";
    }
    
    // 글 상세보기
    @GetMapping("/nutrients-detail-page")
    public String nutriDetailPage(@RequestParam("suppleSeq") long suppleSeq, Model model) 
    {
    	Supple supple = suppleService.selectSupple(suppleSeq);
    	log.info(supple.getSuppleCaution());
    	model.addAttribute("supple", supple);
        return "nutrients/nutrientsDetail";
    }
    
    // 글 작성 페이지
    @GetMapping("/nutrients-write")
    public String write()
    {
    	return "nutrients/nutrientsWrite";
    }
    
    // 글 저장
	@PostMapping("/nutri/save")
	@ResponseBody
	public ResponseEntity<?> saveSupple(@RequestParam("uploadFile") MultipartFile[] multipartFile
			, @RequestParam("suppleName") String suppleName, @RequestParam("suppleTitle") String suppleTitle, @RequestParam("suppleRaw") String suppleRaw, 
			@RequestParam("suppleCaution") String suppleCaution, @RequestParam("suppleFormula") String suppleFormula,
			@RequestParam("suppleCompany") String suppleCompany,@RequestParam("suppleFunction") String suppleFunction,
			@RequestParam("suppleDose") String suppleDose, @RequestParam("suppleLink") String suppleLink)
	{
		if(!suppleDose.isEmpty() && !suppleCaution.isEmpty() && !suppleCompany.isEmpty() && !suppleFormula.isEmpty()
				&& !suppleFunction.isEmpty() && !suppleLink.isEmpty() && !suppleName.isEmpty() && !suppleRaw.isEmpty())
		{
			Supple supple = new Supple();

			supple.setSuppleDose(suppleDose);
			supple.setSuppleTitle(suppleTitle);
			supple.setSuppleCaution(suppleCaution);
			supple.setSuppleCompany(suppleCompany);
			supple.setSuppleFormula(suppleFormula);
			supple.setSuppleFunction(suppleFunction);
			supple.setSuppleLink(suppleLink);
			supple.setSuppleName(suppleName);
			supple.setSuppleRaw(suppleRaw);

			int count = suppleService.saveSupple(multipartFile, supple);
			
			return ResponseEntity.ok(count);
		}
		
		return ResponseEntity.ok(404);
	}
	
	// 글 삭제
	@PostMapping("/nutri/delete")
	@ResponseBody
	public ResponseEntity<?> deleteNutri(@RequestParam("suppleSeq") String suppleSeq)
	{
		Supple supple = suppleService.selectSupple(Integer.valueOf(suppleSeq));
		
		if(supple != null)
		{
			if(suppleService.deleteSupple(supple.getSuppleSeq()) > 0)
			{
				return ResponseEntity.ok(1);
			}
			
			return ResponseEntity.ok(500);
		}
		
		return ResponseEntity.ok(404);
	}
}
