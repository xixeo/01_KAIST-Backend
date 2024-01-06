package kr.co.igns.framework.config.closet;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import lombok.extern.log4j.Log4j2;

@Configuration
@Profile("prod")
@Log4j2
public class ProdConfiguration {
	@Bean
	public ClosetService devService() {
		log.info("========================================= prod 환경 ========================================");
		return new ClosetService("prod");
	}
}
