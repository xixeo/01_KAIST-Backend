package kr.co.igns.iotMonitoring.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class DeviceControlHistoryDTO {

	private String startDt;
	private String endDt;
	private String clientId;
	private String topic;
}
