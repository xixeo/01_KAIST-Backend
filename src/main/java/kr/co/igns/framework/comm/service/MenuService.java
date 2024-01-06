package kr.co.igns.framework.comm.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Service;

import kr.co.igns.framework.comm.dao.postgre.MenuDao;
import kr.co.igns.framework.comm.model.DomainVO;
import kr.co.igns.framework.comm.model.MenuReqDto;
import kr.co.igns.framework.comm.model.MenuVO;
import kr.co.igns.framework.comm.model.UserMenuVO;
import kr.co.igns.framework.config.security.JwtTokenProvider;
import kr.co.igns.framework.utils.etc.IgnsSessionUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@RequiredArgsConstructor
@Service
public class MenuService{

	private final MenuDao menuDao;
	private final IgnsSessionUtils ignsSessionUtils;
	
	public List<UserMenuVO> getUserMenu(MenuReqDto reqDto) {
		List<UserMenuVO> menu = menuDao.getUserMenu(reqDto);
        return menu;
    }
	
	public List<MenuVO> getMenuById(MenuReqDto reqDto) {
		List<MenuVO> menu = menuDao.getMenuById(reqDto);
        return menu;
    }

	public List<MenuVO> getMenuByDomain(MenuReqDto reqDto) {
		List<MenuVO> menuList = menuDao.getMenuByDomain(reqDto);
		return menuList;
	}
	
	public List<MenuVO> getLowerLevelMenu(MenuReqDto reqDto) {
		List<MenuVO> menu = menuDao.getLowerLevelMenu(reqDto);
        return menu;
    }
	
	public int createMenu(List<MenuVO> dataList) {
		String loginId = IgnsSessionUtils.getCurrentLoginUserCd();
		
		for(MenuVO vo : dataList) {
			if(vo.is__created__()) {
				vo.setInsertId(loginId);
				menuDao.createMenu(vo);					
			} else if(vo.is__modified__()) {
				vo.setUpdateId(loginId);
				menuDao.updateMenu(vo);
			}
		}
		
		return 0;
	}
	
	public void deleteMenu(List<MenuVO> voList) {
		for(MenuVO i : voList){
			menuDao.deleteMenu(i);
		}
	}

}
