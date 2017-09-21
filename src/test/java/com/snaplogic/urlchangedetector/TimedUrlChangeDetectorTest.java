package com.snaplogic.urlchangedetector;

import java.util.concurrent.TimeUnit;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.snaplogic.urlchangedetector.detector.DetectChangeInContent;
import com.snaplogic.urlchangedetector.notifier.JavaMailNotifier;
import com.snaplogic.urlchangedetector.web.WebApp;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {JavaMailNotifier.class, TimedUrlChangeDetector.class, 
		DetectChangeInContent.class, WebApp.class})
public class TimedUrlChangeDetectorTest {

	@Autowired
	private TimedUrlChangeDetector changeDetector;
	
	private ConfigurableApplicationContext webAppContextForTesting;
	
	@BeforeClass
	public void setupChangingWebPage() {
		webAppContextForTesting = SpringApplication.run(WebApp.class, new String[0]);
	}
	
	@Test
	public void testChangeInWeb() throws InterruptedException {
		changeDetector.setupTimedUrlChangeDetector("http://localhost:8080/", "ganesh.sit@gmail.com", TimeUnit.SECONDS.toMillis(5));
		Thread.sleep(100000000);
	}
	
	@AfterClass
	public void tearDownWebApp() {
		SpringApplication.exit(webAppContextForTesting);
	}
	
}
