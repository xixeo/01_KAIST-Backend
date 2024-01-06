package kr.co.igns.iotMonitoring.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class DeviceControlHistoryVO {
	
	private int no;
	private String clientId;
	private int qos;
	private String topic;
	private String payload;
	private String result;
	private String insertDt;
	private String insertId;
	private String cause;
	
	

}
