package kr.co.igns.framework.comm.model;

import lombok.*;

@Getter
@Setter
@Data
public class LoginVO {
	private int uuid;
	private String userId;
	private String domainCd;
	private String asgnCd;
	private String userType;
	private String userNm;
	private String userNmEn;
	private String nickNm;
	private String userPw;
	private String userRole;
	private String nickTempPw;
	private String email;
	private String offiTel;
	private String note;
	private String ip;
}
