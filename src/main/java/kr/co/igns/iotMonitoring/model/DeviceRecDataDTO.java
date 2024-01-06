package kr.co.igns.iotMonitoring.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class DeviceRecDataDTO {

    private String groupId;
    private String deviceId;
	
	private String startDt;
	/* private String startDtPeriod; */
	private String endDt;
	private int limitCnt;
	/* private String baseDt; */
	
	/* private String selectData; */
	
	/* private String deviceType; */
	
}
