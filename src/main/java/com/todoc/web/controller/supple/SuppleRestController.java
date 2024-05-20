package com.todoc.web.controller.supple;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.todoc.web.dto.Supple;
import com.todoc.web.service.SuppleService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class SuppleRestController 
{
	@Autowired
	private SuppleService suppleService;
	
	@PostMapping("/nutri/save")
	@ResponseBody
	public ResponseEntity<?> saveSupple(@RequestParam("uploadFile") MultipartFile[] multipartFile
			, @RequestParam("suppleName") String suppleName, @RequestParam("suppleRaw") String suppleRaw, 
			@RequestParam("suppleCaution") String suppleCaution, @RequestParam("suppleFormula") String suppleFormula,
			@RequestParam("suppleCompany") String suppleCompany,@RequestParam("suppleFunction") String suppleFunction,
			@RequestParam("suppleDose") String suppleDose, @RequestParam("suppleLink") String suppleLink)
	{
		if(!suppleDose.isEmpty() && !suppleCaution.isEmpty() && !suppleCompany.isEmpty() && !suppleFormula.isEmpty()
				&& !suppleFunction.isEmpty() && !suppleLink.isEmpty() && !suppleName.isEmpty() && !suppleRaw.isEmpty())
		{
			Supple supple = new Supple();

			supple.setSuppleDose(suppleDose);
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
}
