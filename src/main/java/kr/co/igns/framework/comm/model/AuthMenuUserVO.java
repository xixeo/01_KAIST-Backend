package kr.co.igns.framework.comm.model;

import lombok.*;

@Data
public class AuthMenuUserVO {
	private String domainCd;
	private String userId;
	private String userNm;
	private String menuCd;
	private String menuNm;
	private String menuUrl;
	private String basicAclc;
	private String basicAclr;
	private String basicAclu;
	private String basicAcld;
	private String insertId;
	private String insertDt;
	private String updateId;
	private String updateDt;
	
	private boolean __created__;
	private boolean __modified__;
}

