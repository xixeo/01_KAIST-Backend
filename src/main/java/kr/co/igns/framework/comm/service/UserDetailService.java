package kr.co.igns.framework.comm.service;

import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import kr.co.igns.framework.comm.dao.postgre.UserDao;
import kr.co.igns.framework.comm.model.UserCertVO;
import kr.co.igns.framework.comm.model.UserDetailVO;
import kr.co.igns.framework.utils.etc.GlobalConstants;

@RequiredArgsConstructor
@Service
public class UserDetailService implements UserDetailsService {

	private static String secretKey = GlobalConstants.ADMIN_AUTH_TOKEN_KEY;
	
	@Autowired
	private UserDao userDao;
    
    @Override
	public UserDetails loadUserByUsername(String uuid) throws UsernameNotFoundException {

        //DB로부터 회원 정보를 가져온다.
    	
		ArrayList<UserCertVO> userAuthes = userDao.findByUserId(Integer.parseInt(uuid));
		if(userAuthes.size() == 0) {
			throw new UsernameNotFoundException("User "+ uuid+" Not Found!");
		}
		System.out.println("loadUserByUsername --> " + userAuthes);
		return new UserDetailVO(userAuthes); //UserDetails 클래스를 상속받은 UserPrincipalVO 리턴한다.
	}

}
