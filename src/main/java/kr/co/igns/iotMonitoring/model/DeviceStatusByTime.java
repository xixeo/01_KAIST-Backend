package kr.co.igns.iotMonitoring.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class DeviceStatusByTime {
	
	   private String time;
	   private String count; //device count
	   private String totalCount;  //total data count
}
