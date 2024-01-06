package kr.co.igns.framework.utils.etc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import kr.co.igns.framework.user.MDCLoginUser;
import kr.co.igns.framework.comm.model.LoginReqDto;
import kr.co.igns.framework.user.IgnsSessionUser;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;


@Component
public class IgnsSessionUtils {
	
	
    public static UserDetails getCurrentUserDetail() {
        try {
        	//System.out.println("getCurrentUserDetail getContext() : "+ SecurityContextHolder.getContext().getAuthentication());
        	UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return userDetails;
            
        } catch (Exception e) {
            // ignore
        }
        return null;
    }

    public static IgnsSessionUser getCurrentUser() {
        UserDetails userDetails = getCurrentUserDetail();

        if (userDetails != null) {
            if (userDetails instanceof IgnsSessionUser) {
                return (IgnsSessionUser) userDetails;
            }
        }

        return null;
    }

    public static MDCLoginUser getCurrentMdcLoginUser(HttpServletRequest request) {
        UserDetails userDetails = getCurrentUserDetail();

        if (userDetails != null) {
            IgnsSessionUser sessionUser = (IgnsSessionUser) userDetails;
            sessionUser.setUserPs(null);
            MDCLoginUser mdcLoginUser = new MDCLoginUser();
            mdcLoginUser.setSessionUser(sessionUser);
            mdcLoginUser.setUserAgent(AgentUtils.getUserAgent(request));
            mdcLoginUser.setBrowserType(AgentUtils.getBrowserType(request));
            mdcLoginUser.setRenderingEngine(AgentUtils.getRenderingEngine(request));
            mdcLoginUser.setDeviceType(AgentUtils.getDeviceType(request));
            mdcLoginUser.setManufacturer(AgentUtils.getManufacturer(request));
            return mdcLoginUser;
        }

        return null;
    }

    public static boolean isLoggedIn() {
        return getCurrentUser() != null;
    }

    public static String getCurrentLoginUserCd() {
    	
        UserDetails userDetails = getCurrentUserDetail();
        System.out.println("getCurrentLoginUserCd() : " + userDetails);
        return userDetails == null ? "system" : userDetails.getUsername();
    }
    
    public static String getClntId() { 
    	return getCurrentUser().getClntId();
    }
    
    public static String getCompId() { 
    	return getCurrentUser().getCompId();
    }
    
    
    /*
	private String userId = "";
	private String domainCd = "";
	
	public void setLoginInfo(HttpServletRequest req, LoginReqDto loginDto) {
        HttpSession session = req.getSession();
        session.setAttribute("userId", userId);
        session.setAttribute("domainCd", domainCd);
        
        this.userId = loginDto.getUserId();
		this.domainCd = loginDto.getDomainCd();
	}
	
	public String getLoginUserId() {
		return this.userId;
	}
	
	public String getLoginDomainCd() {
		return this.domainCd;
	}
	*/
    
    
}
