package kr.co.igns.framework.comm.model;

import lombok.*;

@Data
public class AsgnVO {
	private String domainCd;
	private String asgnCd;
	private String asgnNm;
	private String level;
	private int sort;
	private String prntsCd;
	private String note;
	private String insertId;
	private String insertDt;
	private String updateId;
	private String updateDt;
	private String asgnType;
	private boolean __created__;
	private boolean __modified__;
}
