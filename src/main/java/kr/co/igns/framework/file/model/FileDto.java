package kr.co.igns.framework.file.model;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @Data
public class FileDto {
	private int fileId;			// 아이디
	private String fileIdArr;			// 아이디[배열]
	private String targetType; 	// 타겟타입	
	private String targetId;	// 타겟아이디
	
	private String zipName;		// 압축파일 명칭
}
