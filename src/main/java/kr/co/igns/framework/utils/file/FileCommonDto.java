package kr.co.igns.framework.utils.file;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter @Setter
public class FileCommonDto {
	private String groupCd;	
	
	private String tableName;
	private String tableCol;
	private int value;
}
