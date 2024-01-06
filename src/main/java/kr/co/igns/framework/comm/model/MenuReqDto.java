package kr.co.igns.framework.comm.model;
import lombok.*;

@Data
public class MenuReqDto {
	private String userId;
	private String appId;
    private String menuCd;
    private String domainCd;
    private String userIp;
    private String loginType;
}
