package kr.co.igns.business.sample.model;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Data
public class SampleVO {
	private String userId;
	private String domainCd;
	private String userPw;
	private String userNm;
	private String userType;
	private String chgType;
	private String errCd;
	private boolean __created__;
	private boolean __modified__;
	
}
