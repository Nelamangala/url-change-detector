package com.snaplogic.urlchangedetector.notifier;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {JavaMailNotifier.class})
public class JavaMailNotifierTest {

	@Autowired
	private JavaMailNotifier emailNotifier;
	
	@Test
	public void testSendEmail() {
		try {
			emailNotifier.sendSimpleMessage("gnelamangala@gmail.com", "helloo", "test successful");
		}catch(Exception failedToSendEmail) {
			Assert.fail("Failed to send email, error : " + failedToSendEmail.getMessage());
			failedToSendEmail.printStackTrace();
		}
		
	}
	
}
