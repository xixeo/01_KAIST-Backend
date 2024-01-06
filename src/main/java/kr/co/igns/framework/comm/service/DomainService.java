package kr.co.igns.framework.comm.service;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.stereotype.Service;

import kr.co.igns.framework.comm.dao.postgre.DomainDao;
import kr.co.igns.framework.comm.model.DomainReqDto;
import kr.co.igns.framework.comm.model.DomainVO;
import kr.co.igns.framework.utils.etc.IgnsSessionUtils;

@RequiredArgsConstructor
@Service
public class DomainService{

	private final DomainDao systemDao;
	private final IgnsSessionUtils ignsSessionUtils;
	
	
	//도메인코드 조회
	public List<DomainVO> getAllDomain(DomainReqDto reqDto) {
		List<DomainVO> list = systemDao.getAllDomain(reqDto);
        return list;
    }
		
	//도메인코드 조회
	public List<DomainVO> getDomain(DomainReqDto reqDto) {
		List<DomainVO> list = systemDao.getDomain(reqDto);
        return list;
    }
	
	//서브도메인코드 조회
	public List<DomainVO> getSubDomain(DomainReqDto reqDto) {
		List<DomainVO> list = systemDao.getSubDomain(reqDto);
        return list;
    }
	
	//도메인코드 입력
	public int createDomain(List<DomainVO> voList) {
		String loginId = IgnsSessionUtils.getCurrentLoginUserCd();
		
		for(DomainVO vo : voList) {
			if(vo.is__created__()) {
				vo.setInsertId(loginId);
				systemDao.createDomainCd(vo);					
			} else if(vo.is__modified__()) {
				vo.setUpdateId(loginId);
				systemDao.updateDomainCd(vo);
			}
		}
		return 0;
    }
	
	//도메인코드 삭제
	public void deleteDomain(List<DomainVO> voList) {
		for(DomainVO vo : voList) {
			systemDao.deleteDomain(vo);
		}
	}
	
}
