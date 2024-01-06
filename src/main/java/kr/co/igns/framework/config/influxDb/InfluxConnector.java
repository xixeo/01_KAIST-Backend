package kr.co.igns.framework.config.influxDb;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
@Lazy
public class InfluxConnector {
	
	@Autowired
	private InfluxProperty influxProperty;
	
    private InfluxDB influxDB;
    

	public InfluxDB getInfluxDB() {
		//System.out.println("getInfluxDB- " + influxProperty.getUrl() + " " + influxProperty.getUserName() + " " + influxProperty.getPassword());
		return InfluxDBFactory.connect(influxProperty.getUrl(),influxProperty.getUserName(),influxProperty.getPassword());
	}
	
	public QueryResult getQuery(String query) {
		influxDB = getInfluxDB();
		Query queryObject = new Query(query, influxProperty.getDatabase());
		QueryResult queryResult = influxDB.query(queryObject);
		//System.out.println("#3 " + queryResult.getResults().get(0).getSeries());
		
		return queryResult;
	}

	public void setInfluxDB(InfluxDB influxDB) {
		this.influxDB = influxDB;
	}
}
