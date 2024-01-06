package kr.co.igns.framework.comm.dao.postgre;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.igns.framework.comm.model.DomainReqDto;
import kr.co.igns.framework.comm.model.DomainVO;

@Mapper
public interface DomainDao {

	//도메인코드 조회
	public List<DomainVO> getAllDomain(DomainReqDto reqDto);
	public List<DomainVO> getDomain(DomainReqDto reqDto);
	public List<DomainVO> getSubDomain(DomainReqDto reqDto);
	//도메인코드 존재여부 확인
	public int checkDomainCd(String domainCd);
	//도메인코드 등록/수정
	public void createDomainCd(DomainVO vo);
	public void updateDomainCd(DomainVO vo);
	//도메인코드 삭제
	public void deleteDomain(DomainVO vo);
	
}
