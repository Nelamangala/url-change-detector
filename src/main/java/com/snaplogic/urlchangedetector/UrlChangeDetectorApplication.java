package com.snaplogic.urlchangedetector;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class UrlChangeDetectorApplication implements CommandLineRunner{

	private Logger logger = Logger.getLogger(UrlChangeDetectorApplication.class);
	
	@Autowired 
	TimedUrlChangeDetector timedUrlChangeDetector;
	
	public static void main(String[] args) {
		SpringApplication.run(UrlChangeDetectorApplication.class, args);
	}
	@Override
	public void run(String... args) throws Exception {
		if(args.length != 3) {
			logger.info("Usage : java -jar <jar> com.snaplogic.urlchangedetector.UrlChangeDetectorApplication <url> <emailNotificationReceipient> <checkIntervalInMillis>");
			return;
		}
		timedUrlChangeDetector.setupTimedUrlChangeDetector(args[0], args[1], Long.valueOf(args[2]));
		
	}
}
