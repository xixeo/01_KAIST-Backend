package kr.co.igns.framework.utils.type;

import org.springframework.web.context.request.RequestContextHolder;

public class RequestUtils {
	public static String getAppId() {
		return (String) RequestContextHolder.getRequestAttributes().getAttribute("app-id", 0);
	}

	public static void setAppId(String object) {
		RequestContextHolder.getRequestAttributes().setAttribute("app-id", object, 0);
	}

	public static String getUserIp() {
		return (String) RequestContextHolder.getRequestAttributes().getAttribute("user-ip", 0);
	}

	public static void setUserIp(String object) {
		RequestContextHolder.getRequestAttributes().setAttribute("user-ip", object, 0);
	}

	public static String getLang() {
		return (String) RequestContextHolder.getRequestAttributes().getAttribute("lang", 0);
	}

	public static void setLang(String object) {
		RequestContextHolder.getRequestAttributes().setAttribute("lang", object, 0);
	}

	public static Object getAttribute(String name) {
		return RequestContextHolder.getRequestAttributes().getAttribute(name, 0);
	}

	public static void setAttribute(String name, Object object) {
		RequestContextHolder.getRequestAttributes().setAttribute(name, object, 0);
	}

	public static String getToken() {
		return (String) RequestContextHolder.getRequestAttributes().getAttribute("X-Auth-Token", 0);
	}

	public static void setToken(String object) {
		RequestContextHolder.getRequestAttributes().setAttribute("X-Auth-Token", object, 0);
	}
}