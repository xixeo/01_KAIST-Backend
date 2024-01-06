package kr.co.igns.framework.comm.service;

import lombok.RequiredArgsConstructor;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import kr.co.igns.framework.comm.dao.postgre.UserDao;
import kr.co.igns.framework.comm.model.CmCodeVO;
import kr.co.igns.framework.comm.model.UserCertVO;
import kr.co.igns.framework.comm.model.UserReqDto;
import kr.co.igns.framework.comm.model.UserVO;
import kr.co.igns.framework.config.exception.CEmailSigninFailedException;
import kr.co.igns.framework.config.exception.CUserDefinedException;
import kr.co.igns.framework.config.exception.CUserNotFoundException;
import kr.co.igns.framework.utils.etc.IgnsSessionUtils;


@RequiredArgsConstructor
@Service
public class UserService{

	private final UserDao userDao;
	private final PasswordEncoder passwordEncoder;
	private final IgnsSessionUtils ignsSessionUtils;
	
	public List<UserVO> getUser(UserReqDto reqDto) {
		System.out.println("getUser");
		List<UserVO> userInfo = userDao.getUser(reqDto);
        return userInfo;
    }
	
	public List<UserCertVO> findByUserId(UserReqDto reqDto) {
		System.out.println("findByUserId");
		List<UserCertVO> userInfo = userDao.findByUserId(Integer.parseInt(reqDto.getUuid()));
        return userInfo;
    }
	
	public boolean checkAdminPw(UserReqDto reqDto) {
		//System.out.println("checkAdminPw");
		if(reqDto.getDomainCd().equals("") || reqDto.getUserId().equals("")
				|| reqDto.getDomainCd() == null || reqDto.getUserId() == null) {
			throw new CUserDefinedException("필수 입력값이 없습니다.");
		}
		List<UserVO> userInfo = userDao.getUser(reqDto);
		if(userInfo.size() > 0) {
			//if(!reqDto.getUserPw().equals(userInfo.get(0).getUserPw())) {
			if (!passwordEncoder.matches(reqDto.getUserPw(), userInfo.get(0).getUserPw())) { //pw 디코딩
				return false; 
	     	}
		} else {
			throw new CUserDefinedException("해당도메인에 계정이 없습니다.");
		}
		
		
        return true;
    }

	//유저정보 입력/수정
	public int createUser(List<UserVO> voList) {
		String loginId = IgnsSessionUtils.getCurrentLoginUserCd();
		
		for(UserVO vo : voList) {
			if(vo.is__created__()) {
				vo.setInsertId(loginId);
				userDao.createUser(vo);					
			} else if(vo.is__modified__()) {
				vo.setUpdateId(loginId);
				//vo.setUserPw(passwordEncoder.encode(vo.getUserPw()));
				userDao.updateUser(vo);
			}
		}
		return 0;
    }
	
	//유저정보 비밀번호 수정
	public int updateUserPass(UserVO vo) {
		String loginId = IgnsSessionUtils.getCurrentLoginUserCd();
		vo.setUpdateId(loginId);
		vo.setUserPw(passwordEncoder.encode(vo.getUserPw()));
		userDao.updateUserPass(vo);
		return 0;
    }
	
	//유저정보 삭제
	public void deleteUser(List<UserVO> voList) {
		for(UserVO vo : voList) {
			userDao.deleteUser(vo);
		}
	}
}
