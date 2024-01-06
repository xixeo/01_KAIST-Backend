package kr.co.igns.framework.comm.model;

import lombok.*;

@Data
public class UserMenuVO {
	private String domainCd;
	private String menuCd;
	private String menuNm;
	private String icon;
	private String sort;
	private String prntsCd;
	private String useYn;
	private String basicAclc;
	private String basicAclr;
	private String basicAclu;
	private String basicAcld;
}
