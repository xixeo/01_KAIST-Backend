package kr.co.igns.framework.comm.service;

import lombok.RequiredArgsConstructor;
import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import kr.co.igns.framework.comm.dao.postgre.AuthDao;
import kr.co.igns.framework.comm.model.AuthMenuUserVO;
import kr.co.igns.framework.comm.model.AuthMenuVO;
import kr.co.igns.framework.comm.model.AuthReqDto;
import kr.co.igns.framework.comm.model.AuthUserVO;
import kr.co.igns.framework.comm.model.AuthVO;
import kr.co.igns.framework.comm.model.CmCodeReqDto;
import kr.co.igns.framework.comm.model.CmCodeVO;
import kr.co.igns.framework.utils.etc.IgnsSessionUtils;

@RequiredArgsConstructor
@Service
public class AuthService{

	private final AuthDao authDao;
	private final IgnsSessionUtils ignsSessionUtils;
	
	//권한그룹 조회
	public List<AuthVO> getAuthGroup(AuthReqDto reqDto) {
		List<AuthVO> list = authDao.getAuthGroup(reqDto);
        return list;
    }
	//권한그룹 입력/수정
	public int createAuthGroup(List<AuthVO> voList) {
		String loginId = IgnsSessionUtils.getCurrentLoginUserCd();
		for(AuthVO vo : voList) {
			if(vo.is__created__()) {
				vo.setInsertId(loginId);
				authDao.createAuthGroup(vo);					
			} else if(vo.is__modified__()) {
				vo.setUpdateId(loginId);
				authDao.updateAuthGroup(vo);
			}
		}
		return 0;
    }
	//권한그룹 삭제
	public void deleteAuthGroup(List<AuthVO> voList) {
		for(AuthVO vo : voList) {
			authDao.deleteAuthGroup(vo);
		}
	}
	
	
	//권한유저그룹 조회
	public List<AuthUserVO> getUserAuthGroup(AuthReqDto reqDto) {
		List<AuthUserVO> list = authDao.getUserAuthGroup(reqDto);
        return list;
    }
	//권한유저그룹 입력/수정
	public int createUserAuthGroup(List<AuthUserVO> voList) {
		String loginId = IgnsSessionUtils.getCurrentLoginUserCd();
		for(AuthUserVO vo : voList) {
			if(vo.is__created__()) {
				vo.setInsertId(loginId);
				authDao.createUserAuthGroup(vo);					
			} else if(vo.is__modified__()) {
				vo.setUpdateId(loginId);
				authDao.updateUserAuthGroup(vo);
			}
		}
		return 0;
    }
	//권한유저그룹 삭제
	public void deleteUserAuthGroup(List<AuthUserVO> voList) {
		for(AuthUserVO vo : voList) {
			authDao.deleteUserAuthGroup(vo);
		}
	}
	
	
	//권한메뉴그룹 조회
	public List<AuthMenuVO> getMenuAuthGroup(AuthReqDto reqDto) {
		List<AuthMenuVO> list = authDao.getMenuAuthGroup(reqDto);
        return list;
    }
	//권한메뉴그룹 입력/수정
	public int createMenuAuthGroup(List<AuthMenuVO> voList) {
		String loginId = IgnsSessionUtils.getCurrentLoginUserCd();
		for(AuthMenuVO vo : voList) {
			if(vo.is__created__()) {
				vo.setInsertId(loginId);
				authDao.createMenuAuthGroup(vo);					
			} else if(vo.is__modified__()) {
				vo.setUpdateId(loginId);
				authDao.updateMenuAuthGroup(vo);
			}
		}
		return 0;
    }
	//권한메뉴그룹 삭제
	public void deleteMenuAuthGroup(List<AuthMenuVO> voList) {
		for(AuthMenuVO vo : voList) {
			authDao.deleteMenuAuthGroup(vo);
		}
	}
	
	
	
	
	//사용자 권한메뉴그룹 조회
	public List<AuthMenuUserVO> getMenuUserAuth(AuthReqDto reqDto) {
		List<AuthMenuUserVO> list = authDao.getMenuUserAuth(reqDto);
        return list;
    }
	//사용자 권한메뉴그룹 입력
	public int createMenuUserAuth(List<AuthMenuUserVO> voList) {
		String loginId = IgnsSessionUtils.getCurrentLoginUserCd();
		
		for(AuthMenuUserVO vo : voList) {
			vo.setInsertId(loginId);
			
			if(vo.is__created__()) {
				vo.setInsertId(loginId);
				authDao.createMenuUserAuth(vo);					
			} else if(vo.is__modified__()) {
				vo.setUpdateId(loginId);
				authDao.updateMenuUserAuth(vo);
			}
		}
		return 0;
    }
	//사용자 권한메뉴그룹 삭제
	public void deleteMenuUserAuth(List<AuthMenuUserVO> voList) {
		for(AuthMenuUserVO vo : voList) {
			authDao.deleteMenuUserAuth(vo);
		}
	}
	
	
}
