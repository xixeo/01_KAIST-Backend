package kr.co.igns.framework.file.model;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @Data
public class FileVO {
	
	private int fileId;
	private String targetType;
	private String targetId;
	private String fileNm;
	private String saveNm;
	private String fileType;
	private String fileExtension;
	private String fileSize;
	private String delYn;
	private String fileDesc;
	private String sort;
	
	private String insertUserId;
	private String insertDate;
	private String updateUserId;
	private String updatedDate;
	private String editable;		// 등록 수정여부 판단 FLAG (C : 등록 / U : 수정)
}
