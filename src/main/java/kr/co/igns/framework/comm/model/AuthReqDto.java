package kr.co.igns.framework.comm.model;
import lombok.*;

@Data
public class AuthReqDto {
	private String domainCd;
	private String authgrpCd;
    private String authgrpNm;
    
    private String userId;
    private String userNm;
    private String authgrpAsgCls;
    
}
