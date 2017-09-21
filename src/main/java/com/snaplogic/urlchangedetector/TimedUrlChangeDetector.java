package com.snaplogic.urlchangedetector;

import java.util.Timer;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.snaplogic.urlchangedetector.detector.DetectChangeInContent;

@Component
public class TimedUrlChangeDetector {

	private Logger logger = Logger.getLogger(TimedUrlChangeDetector.class);
	
	@Autowired
	private DetectChangeInContent changeDetectorTask;
	
	public void setupTimedUrlChangeDetector(String url, String emailNotificationReceipient, long checkIntervalInMillis) {
		
		changeDetectorTask.init(url, emailNotificationReceipient);
        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(changeDetectorTask, 0, checkIntervalInMillis);
        logger.info("TimerTask started");
	}

	public DetectChangeInContent getChangeDetectorTask() {
		return changeDetectorTask;
	}
	
}
