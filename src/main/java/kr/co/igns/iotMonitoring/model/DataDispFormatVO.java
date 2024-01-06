package kr.co.igns.iotMonitoring.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class DataDispFormatVO {

	private String dispGubn;
	private String dispGubnCode;
	private String displayCol;
	private String displayColNm;
	private String description;
	private String useYN;
	private String insertDt;
	private String insertId;
	private String updateDt;
	private String updateId;
	private String fieldtype;
	private String format;
	private String alignment;
	private String width;
	private String max;
	private boolean required;
	private boolean fixed;
	private int displayOrder;
	
}
