package com.todoc.web.service;

import java.io.File;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.todoc.web.dao.SuppleDao;
import com.todoc.web.dto.Supple;
import com.todoc.web.dto.SuppleFile;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class SuppleService 
{
	@Autowired
	private SuppleDao suppleDao;
	
	// 영양제 첨부파일 경로
	@Value("${suppleFile.upload.dir}")
	private String suppleFileUploadDir;
	
	// 글 모두 조회
	public List<Supple> suppleList(Supple supple)
	{		
		List<Supple> list = suppleDao.suppleList(supple);
		List<SuppleFile> fileList = suppleDao.selectSuppleFile(supple.getSuppleSeq());
		
		return list;
	}
	
	// 글번호로 글 조회
	public Supple selectSupple(long suppleSeq)
	{
		return suppleDao.selectSupple(suppleSeq);
	}
	
	// 글 등록 (다중 첨부파일)
	@Transactional
	public int saveSupple(MultipartFile[] files, Supple supple) 
	{
	    int count = suppleDao.insertSupple(supple);

	    if (files == null || files.length == 0) 
	    { 
	        return count; // 파일이 없는 경우 처리할 내용이 없으므로 바로 반환
	    }
	    
	    if (count > 0 && files.length > 0) 
	    { 
	        for (int i = 0; i < files.length; i++) 
	        {
	            String orgName = files[i].getOriginalFilename();
	            String uuid = UUID.randomUUID().toString();
	            String extension = orgName.substring(orgName.lastIndexOf("."));
	            String saveName = uuid + extension;
	            long fileSize = files[i].getSize();

	            SuppleFile suppleFile = new SuppleFile();
	            suppleFile.setFileOrgName(orgName);
	            suppleFile.setFileExt(extension);
	            suppleFile.setFileName(saveName);
	            suppleFile.setFileSize(fileSize);
	            suppleFile.setSuppleSeq(supple.getSuppleSeq());

	            try 
	            {
	                // 로컬에 저장
	                files[i].transferTo(new File(suppleFileUploadDir + saveName));

	                // 파일 정보 DB에 삽입
	                int result = suppleDao.insertSuppleFile(suppleFile);
	                
	                if (result <= 0) 
	                {
	                    return 0; // 실패 시 처리
	                }
	            }
	            catch (Exception e) 
	            {
	                log.error("[SuppleService] saveSupple Exception ", e);
	                return 500; // 예외 발생 시 처리
	            }
	        }
	    }

	    return count;
	}
	
	// 글 수정
	public int updateSupple(Supple supple)
	{
		return suppleDao.updateSupple(supple);
	}
	
	// 글 삭제
	@Transactional
	public int deleteSupple(long suppleSeq)
	{
		// 파일 삭제
		
		return suppleDao.deleteSupple(suppleSeq);
	}
}
