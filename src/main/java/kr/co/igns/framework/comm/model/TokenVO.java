package kr.co.igns.framework.comm.model;

import java.util.Date;
import lombok.*;

@Data
public class TokenVO {
	private String key; //uuid
	private String value; //refreshToken
	private String insertDt;
	private String updateDt;
	private Date expirationDt;
	
	private String accessToken;
	private String refreshToken;
}
