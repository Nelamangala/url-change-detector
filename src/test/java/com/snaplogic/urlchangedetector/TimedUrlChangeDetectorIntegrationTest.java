package com.snaplogic.urlchangedetector;

import java.util.concurrent.TimeUnit;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
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
public class TimedUrlChangeDetectorIntegrationTest {

	@Autowired
	private TimedUrlChangeDetector changeDetector;
	
	@Mock
	private JavaMailNotifier emailNotifierMock;
	
	private static ConfigurableApplicationContext webAppContextForTesting;
	
	@BeforeClass
	public static void setupChangingWebPage() {
		// Sets up a web page at localhost:8080 that adds random content on every refresh
		webAppContextForTesting = SpringApplication.run(WebApp.class, new String[0]);
	}
	
	@Test
	public void testChangeInWeb() throws InterruptedException {
		changeDetector.getChangeDetectorTask().setEmailNotifier(emailNotifierMock);
		changeDetector.setupTimedUrlChangeDetector("http://localhost:8080/", "blahblah@gmail.com", TimeUnit.SECONDS.toMillis(1));
		Thread.sleep(TimeUnit.SECONDS.toMillis(10));
		// Verifies at least 5 times notification was called during the 10 seconds application ran. Note: Interval is set to 1second
		Mockito.verify(emailNotifierMock, Mockito.atLeast(5)).sendSimpleMessage(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
	}
	
	@AfterClass
	public static void tearDownWebApp() throws InterruptedException {
		// Stops the web page loaded at localhost:8080
		SpringApplication.exit(webAppContextForTesting);
	}
	
}
