package kr.co.igns.framework.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import kr.co.igns.framework.comm.controller.LoginController;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;


@Configuration
@Log4j2
public class webConfig implements WebMvcConfigurer {

    @Bean
    public WebMvcConfigurer webMvcConfigurer() {
    	
        return new WebMvcConfigurer() {
        	@Override
			public void addCorsMappings(CorsRegistry registry) {
        		log.info("WebMvcConfigurer...");
				registry.addMapping("/**")
					.allowedOrigins("*");
			}
        };
    }

    
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**");
    }
//  => webflux 사용시 작성법
//
//    @Bean
//    public WebFluxConfigurer webFluxConfigurer() {
//        return new WebFluxConfigurerComposite() {
//            @Override
//            public void addCorsMappings(CorsRegistry registry) {
//                registry.addMapping("/message/**")
//                        .allowedOrigins("*")
//                        .allowedMethods(HttpMethod.POST.name())
//                        .allowCredentials(false)
//                        .maxAge(3600);
//            }
//        };
//    }
}