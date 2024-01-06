package kr.co.igns.business.sample.model;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Data
public class SampleDTO {
	private String userId;
	private String userPw;
	private String userNm;
	private String userType;
	private String chgType;
}
