package kr.co.igns.iotMonitoring.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class DashboardLastHistoryDTO {

	private String devGrpID;
	private String devID;
}
