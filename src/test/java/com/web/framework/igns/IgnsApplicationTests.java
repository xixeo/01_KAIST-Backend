package com.web.framework.igns;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.test.context.SpringBootTest;

import kr.co.igns.IgnsApplication;

@SpringBootTest
class IgnsApplicationTests {

	@Test
	SpringApplicationBuilder contextLoads(SpringApplicationBuilder builder) {
		return builder.sources(IgnsApplicationTests.class);
	}

	
}
