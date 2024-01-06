package kr.co.igns.iotMonitoring.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class DeviceVO {
	
	private String deviceId; 
	private String deviceUniqueId; 
	private String dashUseYN;
	private String deviceGrpId; 
	private String deviceGrpNm;
	private String grpDashUseYN;
	private String deviceType; 
	private String deviceNm; 
	private int    deviceTimeout; 
	private String description; 
	private String topic; 
	private String apiUseYN; 
	private String subUseYN; 
	private String insertDt; 
	private String insertId; 
	private String updateDt; 
    private String updateId;
    private String jsondata;
    //private String errCd;
	
}
