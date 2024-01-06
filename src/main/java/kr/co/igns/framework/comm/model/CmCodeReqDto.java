package kr.co.igns.framework.comm.model;
import lombok.*;

@Data
public class CmCodeReqDto {
	private String domainCd;
	private String codeCd;
    private String codeNm;
    private String level;
    private String prntsCd;
    private String useYn;
}
