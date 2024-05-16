package com.todoc.web.dto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.todoc.web.util.StringUtil;

import lombok.Data;

@Data
public class Paging implements Serializable{
private static final long serialVersionUID = -6316602959627374690L;
	private String url;         // url
	private String formName;    // 폼 이름
	private long totalCount;    // 총 게시물 수
	private long totalPage;      // 총 페이지 수
	private long startRow;      // 게시물 시작 ROW (ORACLE ROWNUM)
	private long endRow;        // 게시물 끝 ROW (ORACLE ROWNUM)
	private long listCount;      // 한 페이지의 게시물 수
	private long pageCount;      // 페이징 범위 수
	private long curPage;        // 현재 페이지
	
	private long startPage;      // 시작 페이지 번호
	private long endPage;        // 종료 페이지 번호
	
	private long firstPage;      // 첫 번째 페이지 번호
	private long lastPage;       // 마지막 페이지 번호
	
	private long totalBlock;     // 총 블럭 수
	private long curBlock;       // 현재 블럭
	
	private long prevBlockPage;  // 이전 블럭 페이지
	private long nextBlockPage;  // 다음 블럭 페이지
	
	private long startNum;      // 시작 번호 (게시물 번호 적용 DESC)
	
	private String pageTagName; // 페이지번호 태그명
	
	private String scriptFuncName;      // 함수명
	
	private Map<String, Object> param; // 파라미터 맵
	
	
	public Paging(String url, long totalCount, long listCount, long pageCount, long curPage, String pageTagName)
	{
		this(url, null, totalCount, listCount, pageCount, curPage, pageTagName);
	}
	
	public Paging(String url, String formName, long totalCount, long listCount, long pageCount, long curPage, String pageTagName)
	{
		this.url = url;
		this.formName = (StringUtil.isEmpty(formName) ? StringUtil.uniqueValue() : formName);
		this.totalCount = totalCount;
		this.listCount = listCount;
		this.pageCount = pageCount;
		this.curPage = curPage;
		this.pageTagName = pageTagName;
		
		param = new HashMap<String, Object>();
		
		if(totalCount > 0)
		{
			pagingProc();
		}
		
		scriptFuncName = "fn_paging_" + formName;
	}
	
	private void pagingProc()
	{
		// 총 페이지 수를 구한다.
		totalPage = (long)Math.ceil((double)totalCount / listCount);
		// 총 블럭 수를 구한다.
		totalBlock = (long)Math.ceil((double)totalPage / pageCount);
		// 현재 블럭을 구한다.		
		curBlock = (long)Math.ceil((double)curPage / pageCount);
		
		// 시작 페이지 
		startPage = ((curBlock - 1) * pageCount) + 1;
		// 끝 페이지
		endPage = (startPage + pageCount) - 1; 
		
		// 마지막 페이지 보정
		// 총 페이지 보다 끝 페이지가 크다면 총 페이지를 마지막 페이지로 변환한다. 
		if (endPage > totalPage) 
		{
			endPage = totalPage;
		}
		
		// 시작 ROWNUM (ORACLE ROWNUM)
		startRow = ( ( ( curPage - 1 ) * listCount ) + 1 );
		// 끝 ROWNUM (ORACLE ROWNUM)
		endRow = ( ( startRow + listCount ) - 1 );
		
		// 게시물 시작 번호
		startNum = ( totalCount - ( ( curPage - 1 ) * listCount ) );
		
		// 이전 블럭 페이지 번호
		if(curBlock > 1)
		{
			prevBlockPage = ( startPage - 1 );
		}
		
		// 이전 블럭 페이지 번호
		if(curBlock > 1)
		{
			prevBlockPage = ( startPage - 1 );
		}
		
		// 다음 블럭 페이지 번호
		if(curBlock < totalBlock)
		{
			nextBlockPage = endPage + 1;
		}
	}
}
