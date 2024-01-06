package kr.co.igns.framework.comm.dao.postgre;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.igns.framework.comm.model.CmChildCodeVO;
import kr.co.igns.framework.comm.model.CmCodeReqDto;
import kr.co.igns.framework.comm.model.CmCodeVO;
import kr.co.igns.framework.comm.model.DomainReqDto;
import kr.co.igns.framework.comm.model.MenuReqDto;
import kr.co.igns.framework.comm.model.MenuVO;

@Mapper
public interface CmCodeDao {

	List<CmCodeVO> getAllCmCode(CmCodeReqDto reqDto);
	List<CmCodeVO> getCmCode(CmCodeReqDto reqDto);
	List<CmChildCodeVO> getChildCmCodeList(CmCodeReqDto reqDto);
	
	int createCmCode(CmCodeVO vo);
	int updateCmCode(CmCodeVO vo);
	int deleteCmCode(CmCodeVO vo);
	int deleteDetailCmCode(CmCodeVO vo);
}
