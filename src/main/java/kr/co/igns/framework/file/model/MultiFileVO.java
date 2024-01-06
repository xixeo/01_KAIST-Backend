package kr.co.igns.framework.file.model;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @Data
public class MultiFileVO {
	
	private int fileId;
	private String targetType;
	private String targetId;
	private String fileNm;
	private String fileSize;
	private String fileDesc;
	
}
