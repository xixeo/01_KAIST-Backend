package kr.co.igns.framework.comm.model;

import lombok.*;

@Data
public class MenuVO {
	private String menuCd;
	private String domainCd;
	private String menuNm;
	private String prntsCd;
	private int sort;
	private String level;
	private String useYn;
	private String note;
	private String expanded;
	private String menuUrl;
	private String icon;
	private String insertId;
	private String insertDt;
	private String updateId;
	private String updateDt;
	
	private String isleaf; //view 페이지 여부
	private boolean __created__;
	private boolean __modified__;
}
