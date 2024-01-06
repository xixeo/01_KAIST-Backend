package kr.co.igns.framework.comm.model;

import lombok.*;

@Data
public class AuthVO {
	private String domainCd;
	private String domainNm;
	private String authgrpCd;
	private String authgrpNm;
	private String authgrpType;
	private String authgrpTypeNm;
	private String useYn;
	private String insertId;
	private String insertDt;
	private String updateId;
	private String updateDt;
	
	private boolean __created__;
	private boolean __modified__;
}
