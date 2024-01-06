package kr.co.igns.framework.comm.model;
import lombok.*;

@Getter
@Setter
@Data
public class UserReqDto {
	private String domainCd;
	private String userId;
	private String userNm;
	private String userPw;
	private String appId;
    private String userIp;
    private String loginType;
    
    private String uuid;
}
