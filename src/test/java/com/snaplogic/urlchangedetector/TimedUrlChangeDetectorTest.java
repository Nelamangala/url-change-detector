package com.snaplogic.urlchangedetector;

import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.snaplogic.urlchangedetector.detector.DetectChangeInContent;
import com.snaplogic.urlchangedetector.notifier.JavaMailNotifier;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {JavaMailNotifier.class, TimedUrlChangeDetector.class, DetectChangeInContent.class})
public class TimedUrlChangeDetectorTest {

	@Autowired
	private TimedUrlChangeDetector changeDetector;
	
	@Test
	public void testChangeInWeb() {
		changeDetector.setupTimedUrlChangeDetector("https://news.google.com/news/?ned=us&hl=en#0", "ganesh.sit@gmail.com", TimeUnit.SECONDS.toMillis(5));
	}
}
