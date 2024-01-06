package kr.co.igns.framework.config.security;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import lombok.extern.log4j.Log4j2;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Log4j2
public class SecurityAccessDeniedHandler implements AccessDeniedHandler {

	@Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException exception) throws IOException {
		// 유저 정보는 있으나 자원에 접근할 수 있는 권한이 없는 경우 SC_FORBIDDEN (403)
		// response.sendError(HttpServletResponse.SC_FORBIDDEN);
		log.info(" =============== AccessDenied =============== ");
        response.sendRedirect("/exception/accessdenied");
    }
}
