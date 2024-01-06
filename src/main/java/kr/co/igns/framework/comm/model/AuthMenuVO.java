package kr.co.igns.framework.comm.model;

import lombok.*;

@Data
public class AuthMenuVO {
	private String domainCd;
	private String menuCd;
	private String authgrpCd;
	private String basicAclc;
	private String basicAclr;
	private String basicAclu;
	private String basicAcld;
	private String insertId;
	private String insertDt;
	private String updateId;
	private String updateDt;
	
	private String authgrpNm;
	private String authgrpType;
	private String authgrpTypeNm;
	private String menuNm;
	private String menuUrl;
	
	private boolean __created__;
	private boolean __modified__;
}
