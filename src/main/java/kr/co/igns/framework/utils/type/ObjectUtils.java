package kr.co.igns.framework.utils.type;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class ObjectUtils {
	
	
	//차트데이터를 위한 타입변경
	public Map<String, Object> convertForChart(List<?> list, String[] keyArray) {
		Map<String, Object> map = new HashMap<>();
		
		for (String key : keyArray) {
			if(key != null) {
				String[] resultArray = this.convertListToArray(list, key);
				map.put(key, resultArray);
			}
		}
		return map;
	}
		
	public static Map convertObjectToMap(Object obj) {
		Map map = new HashMap();
		Field[] fields = obj.getClass().getDeclaredFields();
		for (int i = 0; i < fields.length; ++i) {
			fields[i].setAccessible(true);

			try {
				map.put(fields[i].getName(), fields[i].get(obj));
			} catch (Exception var5) {
				var5.printStackTrace();
			}
		}

		return map;
	}

	public static Object convertMapToObject(Map<String, Object> map, Object obj) {
		String keyAttribute = null;
		String setMethodString = "set";
		String methodString = null;
		Iterator itr = map.keySet().iterator();

		while (itr.hasNext()) {
			keyAttribute = (String) itr.next();
			keyAttribute.substring(0, 1).toUpperCase();
			methodString = keyAttribute.substring(1);
			Method[] methods = obj.getClass().getDeclaredMethods();

			for (int i = 0; i < methods.length; ++i) {
				if (methodString.equals(methods[i].getName())) {
					try {
						methods[i].invoke(obj, map.get(keyAttribute));
					} catch (Exception var9) {
						var9.printStackTrace();
					}
				}
			}
		}

		return obj;
	}
	
	public static String[] convertListToArray(List<?> list, String key) {
		int count= list.size();
//		System.out.println(" @param : " + count + " / " + list + " / " + key);
		String keyAttribute = null;
		String[] array = new String[count];
		for(int i = 0; i < count; i++) {
			Map map = convertObjectToMap(list.get(i));
			Iterator itr = map.keySet().iterator();
			while (itr.hasNext()) {
				keyAttribute = (String) itr.next();
//				System.out.println(" @keyAttribute null check : " + map.get(keyAttribute));
				if (map.get(keyAttribute) != null) {
					if (keyAttribute.equals(key)) {
						array[i] = map.get(keyAttribute).toString();
					}	
				}
			}
		}
		return array;
	}
	
}