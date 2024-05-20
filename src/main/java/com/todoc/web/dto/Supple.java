package com.todoc.web.dto;
import java.util.List;


import lombok.Data;

@Data
public class Supple 
{
	private long suppleSeq;
	private String suppleName;
	private String suppleRaw;
	private String suppleCaution;
	private String suppleFormula;
	private String suppleCompany;
	private String suppleFunction;
	private String suppleDose;
	private String suppleRegdate;
	private String suppleLink;
	private String suppleStatus;
	private String suppleTitle;
	
	private List<SuppleFile> suppleFile;
}
