package kr.co.igns.iotMonitoring.model;

import lombok.*;

@Getter
@Setter
@Data
public class InfuxDBQueryDTO {
	private String deviceId;
	private Integer groupId;
	private String changeDiv;
	
	private String startDt;
	/* private String startDtPeriod; */
	private String endDt;
	/* private String baseDt; */
    private int limitCnt;
	
	private String selectData;
	private String groupByData;
	private String orderByData;
	private String fillData;
	
	private String addWhere;
	private String addWhereLast;
}
