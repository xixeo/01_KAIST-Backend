package kr.co.igns.framework.comm.dao.postgre;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.igns.framework.comm.model.CmCodeVO;
import kr.co.igns.framework.comm.model.UserCertVO;
import kr.co.igns.framework.comm.model.UserReqDto;
import kr.co.igns.framework.comm.model.UserVO;

@Mapper
public interface UserDao {

	List<UserVO> getUser(UserReqDto reqDto);
	ArrayList<UserCertVO> findByUserId(int uuid);
	
	int createUser(UserVO vo);
	int updateUser(UserVO vo);
	int updateUserPass(UserVO vo);
	int deleteUser(UserVO vo);
}
