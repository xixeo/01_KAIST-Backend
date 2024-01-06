package kr.co.igns.framework.comm.service;

import lombok.RequiredArgsConstructor;
import java.util.List;
import org.springframework.stereotype.Service;
import kr.co.igns.framework.comm.dao.postgre.AsgnDao;
import kr.co.igns.framework.comm.model.AsgnReqDto;
import kr.co.igns.framework.comm.model.AsgnVO;
import kr.co.igns.framework.comm.model.CmCodeReqDto;
import kr.co.igns.framework.comm.model.CmCodeVO;
import kr.co.igns.framework.utils.etc.IgnsSessionUtils;

@RequiredArgsConstructor
@Service
public class AsgnService{

	private final AsgnDao asgnDao;
	private final IgnsSessionUtils ignsSessionUtils;
	
	//조직코드 조회
	public List<AsgnVO> getAsgn(AsgnReqDto reqDto) {
		List<AsgnVO> list = asgnDao.getAsgn(reqDto);
        return list;
    }
		
	//조직코드 중복체크
	public int checkAsgnCd(AsgnReqDto reqDto) {
		int count = asgnDao.checkAsgnCd(reqDto);
        return count;
    }
	//조직코드 입력/수정
	public int createAsgn(List<AsgnVO> voList) {
		String loginId = IgnsSessionUtils.getCurrentLoginUserCd();
		
		for(AsgnVO vo : voList) {
			if(vo.is__created__()) {
				vo.setInsertId(loginId);
				asgnDao.createAsgn(vo);					
			} else if(vo.is__modified__()) {
				vo.setUpdateId(loginId);
				asgnDao.updateAsgn(vo);
			}
		}
		return 0;
    }
	

	//조직코드 삭제
	public void deleteAsgn(List<AsgnVO> voList) {
		for(AsgnVO vo : voList) {
			asgnDao.deleteAsgn(vo);
		}
	}

	
}
