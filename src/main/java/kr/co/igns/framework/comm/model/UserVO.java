package kr.co.igns.framework.comm.model;

import lombok.*;

@Data
public class UserVO {
	private int uuid; //추가
	private String userId;
	private String domainCd;
	private String domainNm;
	private String asgnCd;
	private String asgnNm;
	private String userType;
	private String userTypeNm;
	private String userRole; //추가
	private String userNm;
	private String userNmEn;
	private String nickNm;
	private String userPw;
	private String userTempPw;
	private String email;
	private String offiTel;
	private String note;
	private String useYn;
	private String insertId;
	private String insertDt;
	private String updateId;
	private String updateDt;
	
	//userDetail 객체
	private String password;
	private String name;
	private String roleName;
	
	private boolean __created__;
	private boolean __modified__;

}
