package kr.co.igns.framework.config.influxDb;

import java.io.IOException;

import org.influxdb.InfluxDBException;

public class InfluxDBIOException extends InfluxDBException {
	
	public InfluxDBIOException(final IOException cause) {
		super(cause);
	}
		
}

