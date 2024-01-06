package kr.co.igns.framework.comm.model;

import lombok.*;

@Data
public class AuthUserVO {
	private String domainCd;
	private String authgrpCd;
	private String userId;
	private String userNm;
	private String authgrpAsgCls;
	private String insertId;
	private String insertDt;
	private String updateId;
	private String updateDt;
	
	private boolean __created__;
	private boolean __modified__;
}
