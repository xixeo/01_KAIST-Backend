package kr.co.igns.framework.utils.type;

public class IntegerUtils {
	public static int getNullInt(Object obj) {
		int result = 0;
		if (obj != null) {
			String rst = String.valueOf(obj);
			result = Integer.valueOf(rst);
		}

		return result;
	}

	public static String getNullStr(Object obj) {
		String result = "";
		if (obj != null) {
			result = String.valueOf(obj);
		}

		return result;
	}
}