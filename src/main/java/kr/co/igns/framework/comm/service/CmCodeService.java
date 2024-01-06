package kr.co.igns.framework.comm.service;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.stereotype.Service;

import kr.co.igns.framework.comm.dao.postgre.CmCodeDao;
import kr.co.igns.framework.comm.dao.postgre.DomainDao;
import kr.co.igns.framework.comm.model.CmChildCodeVO;
import kr.co.igns.framework.comm.model.CmCodeReqDto;
import kr.co.igns.framework.comm.model.CmCodeVO;
import kr.co.igns.framework.comm.model.DomainReqDto;
import kr.co.igns.framework.comm.model.DomainVO;
import kr.co.igns.framework.utils.etc.IgnsSessionUtils;

@RequiredArgsConstructor
@Service
public class CmCodeService{

	private final CmCodeDao cmCodeDao;
	private final IgnsSessionUtils ignsSessionUtils;
	
	//부모공통코드 조회
	public List<CmCodeVO> getAllCmCode(CmCodeReqDto reqDto) {
		List<CmCodeVO> list = cmCodeDao.getAllCmCode(reqDto);
        return list;
    }
		
	//공통코드 조회
	public List<CmCodeVO> getCmCode(CmCodeReqDto reqDto) {
		List<CmCodeVO> list = cmCodeDao.getCmCode(reqDto);
        return list;
    }
	
	//공통코드 조회
	public List<CmChildCodeVO> getChildCmCodeList(CmCodeReqDto reqDto) {
		List<CmChildCodeVO> list = cmCodeDao.getChildCmCodeList(reqDto);
        return list;
    }
	
	//공통코드 입력/수정
	public int createCmCode(List<CmCodeVO> voList) {
		String loginId = IgnsSessionUtils.getCurrentLoginUserCd();
		
		for(CmCodeVO vo : voList) {
			if(vo.is__created__()) {
				vo.setInsertId(loginId);
				cmCodeDao.createCmCode(vo);					
			} else if(vo.is__modified__()) {
				vo.setUpdateId(loginId);
				cmCodeDao.updateCmCode(vo);
			}
		}
		return 0;
    }
	
	//공통코드 삭제
	public void deleteCmCode(List<CmCodeVO> voList) {
		for(CmCodeVO vo : voList) {
			cmCodeDao.deleteCmCode(vo);
			if(vo.getLevel().equals("0")) {
				cmCodeDao.deleteDetailCmCode(vo);
			}
		}
	}
	
	
}
