package kr.co.igns.framework.config.scheduler;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.stereotype.Component;

import kr.co.igns.framework.comm.service.TokenService;

@Component
public class TaskScheduler {
	
	@Autowired
	private TokenService tokenService;
	
	/*
	@Scheduled(fixedDelay = 2000)
	public void cronJobSch() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		Date now = new Date();
		String strDate = sdf.format(now);
		System.out.println("Java cron job expression:: " + strDate);
		//현재 스레드의 이름을 확인할 수 있음
		System.out.println("Current Thread : {}" + Thread.currentThread().getName());
	}
	*/

	//매일 23시에 만료된 리프레쉬토큰 삭제
	@Scheduled(cron = "0 0 23 * * *")
	public void schDeleteExpirationToken() {
		tokenService.deleteExpirationToken();
	}
}
