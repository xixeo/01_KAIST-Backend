package kr.co.igns.framework.config.security;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import lombok.RequiredArgsConstructor;

/**
 * 
 * @ 프로젝트   	: igns
 * @ 패키지		: com.web.framework.igns.config.security
 * @ 파일명		: WebSecurityConfig.java
 * @ 기능명 		: 스프링 시큐리티 설정
 * @ 작성자 		: 김성준
 * @ 최초생성일 	: 2020. 9. 10.
 */
@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
    public static final String LOGIN_API = "/login";
    public static final String LOGOUT_API = "/logout";
    public static final String ACCESS_DENIED_PAGE = "/jsp/common/not-authorized.jsp?errorCode=401";
    public static final String ROLE = "ASP_ACCESS";
    
	private final JwtTokenProvider jwtTokenProvider;
	
    public static final String[] ignorePages = new String[]{
    		"/com",
    		"/com/**",
            "/resources/**",
            "/axboot.config.js",
            "/assets/**",
            "/jsp/common/**",
            "/jsp/setup/**",
            "/v2/api-docs", "/swagger-resources/**", "/swagger-ui.html", "/webjars/**", "/swagger/**", // swagger
            "/api-docs/**",
            "/setup/**",
            "/h2-console/**",
            "/health",
            "/api/v1/aes/**"
    };
    
	/*
	 * 정적 자원에 대해서는 Security 설정을 적용하지 않음
	 * 스프링 시큐리티 룰을 무시하게 하는 Url 규칙(여기 등록하면 규칙 적용하지 않음)
	 */
    @Override
    public void configure(WebSecurity web) {
//    	System.out.println("web : " + web);
        //web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    	web.ignoring().antMatchers(ignorePages);
    }
    
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /*
     * 시큐리티에 관련된 설정
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
        	.httpBasic().disable() // rest api 이므로 기본설정 사용안함. 기본설정은 비인증시 로그인폼 화면으로 리다이렉트
        	.csrf().disable() // rest api 이므로 csrf 보안이 필요없음
        	.headers().frameOptions().sameOrigin() // 동일도메인에서는 iframe 접근이 가능하도록 설정
        	.and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // JWT TOKEN 으로 인증할것이므로 세션이 필요없음            
            .and()
            	// 다음 리퀘스트에 대한 사용권한 체크            
        		.authorizeRequests()
        			.antMatchers("/com", "/com/**", "/api/menuManage/**", "/signup", "/signup/**", "/dashboard").permitAll() // 가입 및 인증 주소는 누구나 접근 가능
        			.antMatchers("/v2/api-docs", "/swagger-resources/**", "/swagger-ui.html", "/webjars/**", "/swagger/**").permitAll() // swagger
        			.antMatchers("/admin/**").hasRole("ADMIN") // 관리자 요청에 대해서는 ROLE_ADMIN 역할을 가지고 있어야 함 현재는 구현하지 않음.
        			.antMatchers(HttpMethod.GET, "/exception/**").permitAll() // 등록된 GET요청 리소스는 누구나 접근가능
        			//.antMatchers("/api/**").authenticated() // api 관련 호출은 인증이 필요(swagger테스트를 위해 주석처리)
        			.anyRequest().permitAll() // 나머지 요청에 대해 인증 후 접근이 가능하도록 함
            .and()
            	.exceptionHandling().accessDeniedHandler(new SecurityAccessDeniedHandler())
            .and()
            	.exceptionHandling().authenticationEntryPoint(new SecurityAuthenticationEntryPoint())
            .and()
            	// JWT TOKEN 필터를 id/password 인증 필터 전에 넣어라.
            	.addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);
    }
    
    /*
     * 암호화
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

}
