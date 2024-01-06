package kr.co.igns.framework.comm.model;

import lombok.*;


@Getter
@Setter
@Data
public class DomainVO {
	
	private String domainCd;
	private String domainStatusCd;
	private String domainStatusNm;	
	private String domainNm;
	private String companyNm;
	private String corrorateRegNo;
	private String ownerNm;
	private String postCode;
	private String address;
	private String addressDetail;
	private String addressRef;
	private String addressFull;
	private String phone;
	private String fax;
	private String email;
	private String serviceStartDt;
	private String serviceEndDt;
	private String note;
	private String insertId;
	private String insertDt;
	private String updateId;
	private String updateDt;
	
	private boolean __created__;
	private boolean __modified__;
}
