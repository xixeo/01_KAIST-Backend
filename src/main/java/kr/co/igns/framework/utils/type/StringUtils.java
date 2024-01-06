package kr.co.igns.framework.utils.type;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class StringUtils extends org.apache.commons.lang3.StringUtils {
	public static String getRuleFileKeyName(URI uri) {
		String keyName = "";
		String temp = uri.toString().substring(uri.toString().lastIndexOf("/"));
		String replace1 = uri.toString().replaceFirst(temp, temp.substring(1));
		keyName = replace1.substring(replace1.lastIndexOf("/") + 1);
		return keyName;
	}

	public static String getGUID32() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}

	public static String getRandomStr(int nSize) {
		StringBuffer temp = new StringBuffer();
		Random rnd = new Random();

		for (int i = 0; i < nSize; ++i) {
			int rIndex = rnd.nextInt(3);
			switch (rIndex) {
				case 0 :
					temp.append((char) (rnd.nextInt(26) + 97));
					break;
				case 1 :
					temp.append((char) (rnd.nextInt(26) + 65));
					break;
				case 2 :
					temp.append(rnd.nextInt(10));
			}
		}

		return temp.toString();
	}

	public static String ifNullToEmpty(String obj) {
		String result = "";
		return obj != null ? obj : result;
	}

	public static String objectIfNullToEmpty(Object obj) {
		String result = "";
		if (obj != null) {
			result = String.valueOf(obj);
			if ("null".equals(result)) {
				result = "";
			}

			return result;
		} else {
			return result;
		}
	}

	public static boolean isEmptyChk(Object s) {
		if (s == null) {
			return true;
		} else if (s instanceof String && ((String) s).trim().length() == 0) {
			return true;
		} else if (s instanceof Map) {
			return ((Map) s).isEmpty();
		} else if (s instanceof List) {
			return ((List) s).isEmpty();
		} else if (s instanceof Object[]) {
			return ((Object[]) s).length == 0;
		} else {
			return false;
		}
	}

	public static String nullToStr(String s) {
		return isEmptyChk(s) ? "" : s.trim();
	}

	public static String nullToStr(String org, String converted) {
		return isEmptyChk(org) ? converted : org.trim();
	}

	public static String nullToStr(Object org, String converted) {
		return isEmptyChk(org) ? converted : org.toString();
	}

	public static int nullToStr(String org, int converted) {
		return isEmptyChk(org) ? converted : Integer.parseInt(org);
	}

	public static int nullToStr(Object org, int converted) {
		return isEmptyChk(org) ? converted : Integer.parseInt(org.toString());
	}
}