package kr.co.igns.framework.comm.model;

import lombok.*;

@Data
public class CmCodeVO {
	private String domainCd;
	private String codeCd;
	private String codeNm;
	private String level;
	private int sort;
	private String prntsCd;
	private String data1;
	private String data2;
	private String data3;
	private String data4;
	private String data5;
	private String note;
	private String useYn;
	private String insertId;
	private String insertDt;
	private String updateId;
	private String updateDt;
	
	private boolean __created__;
	private boolean __modified__;
}
