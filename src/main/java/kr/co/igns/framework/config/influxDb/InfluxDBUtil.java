package kr.co.igns.framework.config.influxDb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.influxdb.dto.QueryResult;
import org.springframework.stereotype.Component;

@Component
public class InfluxDBUtil {
	

	//결과값 : [{time=2021-05-25T08:35:07.92815364Z, book_stand=1.0}, {time=2021-05-25T08:35:17.933861546Z, book_stand=1.0}....]
	public List<Map<String, Object>> convertType(QueryResult result) {
		List<Map<String, Object>> dataMapList = new ArrayList<>();
		List<QueryResult.Result> resultList = result.getResults();

		if(resultList.get(0).getSeries() != null) {
			List<String> columnList = resultList.get(0).getSeries().get(0).getColumns();
			
			for (int z = 0; z < resultList.get(0).getSeries().size(); z++) {
				resultList.get(0).getSeries().get(z).getValues().stream().forEach(row -> {
					int i = 0;
					Map<String, Object> map = new HashMap<>();
					for (Object o : row) {
						map.put(String.valueOf(columnList.get(i)), 
								o);
								//String.valueOf(o));
						++i;
					}
					dataMapList.add(map);
				});
			}
		}
		return dataMapList;
	}
	
	//결과값 : [{time=2021-05-25T08:35:07.92815364Z, book_stand=1.0}, {time=2021-05-25T08:35:17.933861546Z, book_stand=1.0}....]
	public List<Object> convertType(QueryResult result, String field) {
		List<QueryResult.Result> resultList = result.getResults();
		List<String> columnList = resultList.get(0).getSeries().get(0).getColumns();
		List<Object> dataMapList = new ArrayList<>();
		
		resultList.get(0).getSeries().get(0).getValues().stream().forEach(row -> {
			int i = 0;
			Map<String, Object> map = new HashMap<>();
			for (Object o : row) {
				if(field.equals(String.valueOf(columnList.get(i)))) {
					dataMapList.add(o);
				}
				++i;
			}
		});
		return dataMapList;
	}
}

