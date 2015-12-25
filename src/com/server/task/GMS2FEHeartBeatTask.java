package com.server.task;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.server.GameGM2FE;

@Component
public class GMS2FEHeartBeatTask implements CommonTask{

	@Scheduled(cron="0/5 * *  * * ? ")   //每5秒执行一次
	@Override
	public void doWork() {
		GameGM2FE.getInstance().onTimeSignal();
	}

}
