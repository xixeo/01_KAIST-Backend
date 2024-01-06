package kr.co.igns.framework.comm.dao.postgre;

import org.apache.ibatis.annotations.Mapper;

import kr.co.igns.framework.comm.model.LoginReqDto;
import kr.co.igns.framework.comm.model.LoginVO;

@Mapper
public interface LoginDao {

	LoginVO login(LoginReqDto IgnsLoginDto);
	
}
