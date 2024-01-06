package kr.co.igns.iotMonitoring.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class GroupDeviceStatusVO {

	 private String groupId;
	 private String groupNm;
	 private int totalDeviceCnt;
	 private int gadongDeviceCnt;
	 private int bigadongDeviceCnt;
}
