package kr.co.igns.framework.comm.dao.postgre;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.igns.framework.comm.model.AsgnReqDto;
import kr.co.igns.framework.comm.model.AsgnVO;
import kr.co.igns.framework.comm.model.AuthMenuUserVO;
import kr.co.igns.framework.comm.model.AuthMenuVO;
import kr.co.igns.framework.comm.model.AuthReqDto;
import kr.co.igns.framework.comm.model.AuthUserVO;
import kr.co.igns.framework.comm.model.AuthVO;
import kr.co.igns.framework.comm.model.CmCodeReqDto;
import kr.co.igns.framework.comm.model.CmCodeVO;
import kr.co.igns.framework.comm.model.DomainReqDto;
import kr.co.igns.framework.comm.model.MenuReqDto;
import kr.co.igns.framework.comm.model.MenuVO;

@Mapper
public interface AuthDao {

	List<AuthVO> getAuthGroup(AuthReqDto reqDto);
	int createAuthGroup(AuthVO vo);
	int updateAuthGroup(AuthVO vo);
	int deleteAuthGroup(AuthVO vo);
	
	List<AuthUserVO> getUserAuthGroup(AuthReqDto reqDto);
	int createUserAuthGroup(AuthUserVO vo);
	int updateUserAuthGroup(AuthUserVO vo);
	int deleteUserAuthGroup(AuthUserVO vo);
	
	
	List<AuthMenuVO> getMenuAuthGroup(AuthReqDto reqDto);
	int createMenuAuthGroup(AuthMenuVO vo);
	int updateMenuAuthGroup(AuthMenuVO vo);
	int deleteMenuAuthGroup(AuthMenuVO vo);
	
	
	List<AuthMenuUserVO> getMenuUserAuth(AuthReqDto reqDto);
	int createMenuUserAuth(AuthMenuUserVO vo);
	int updateMenuUserAuth(AuthMenuUserVO vo);
	int deleteMenuUserAuth(AuthMenuUserVO vo);
	
}
