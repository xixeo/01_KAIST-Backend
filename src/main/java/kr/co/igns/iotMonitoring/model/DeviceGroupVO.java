package kr.co.igns.iotMonitoring.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class DeviceGroupVO {

	private String deviceGrpId;
	private String deviceGrpNm;
	private String description;
	private int devCnt;
	private int devCntForDash;
	private String dashUseYN;
	private String useYN;
	private String insertDt;
	private String insertId;
	private String updateDt;
	private String updateId;
	//private String errCd;
	
}
