package kr.co.igns.framework.comm.model;

import lombok.*;

@Data
public class TokenDto {
	private String accessToken;
	private String refreshToken;
	private String userId;
	private String domainCd;
}
