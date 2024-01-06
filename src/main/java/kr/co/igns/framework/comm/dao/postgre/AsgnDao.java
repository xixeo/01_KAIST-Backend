package kr.co.igns.framework.comm.dao.postgre;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.igns.framework.comm.model.AsgnReqDto;
import kr.co.igns.framework.comm.model.AsgnVO;
import kr.co.igns.framework.comm.model.CmCodeReqDto;
import kr.co.igns.framework.comm.model.CmCodeVO;
import kr.co.igns.framework.comm.model.DomainReqDto;
import kr.co.igns.framework.comm.model.MenuReqDto;
import kr.co.igns.framework.comm.model.MenuVO;

@Mapper
public interface AsgnDao {

	List<AsgnVO> getAsgn(AsgnReqDto reqDto);
	int checkAsgnCd(AsgnReqDto reqDto);
	
	int createAsgn(AsgnVO vo);
	int updateAsgn(AsgnVO vo);
	int deleteAsgn(AsgnVO vo);
}
