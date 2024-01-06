package kr.co.igns.framework.config.influxDb;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class InfluxProperty {
	
	@Value("${spring.influxdb.url}")
	public String URL;

	@Value("${spring.influxdb.username}")
	public String USER_NAME;

	@Value("${spring.influxdb.password}")
	public String PASSWORD;
	
	@Value("${spring.influxdb.database}")
	public String DATABASE;
	
	@Value("${spring.influxdb.retention-policy}")
	public String R_POLICY;
	
	public String getUrl() {
		return this.URL;
	}
	
	public String getUserName() {
		return this.USER_NAME;
	}
	
	public String getPassword() {
		return this.PASSWORD;
	}
	
	public String getDatabase() {
		return this.DATABASE;
	}
	
	public String getRPolicy() {
		return this.R_POLICY;
	}
	
}
