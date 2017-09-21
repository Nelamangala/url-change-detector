package com.snaplogic.urlchangedetector.detector;

import static org.hamcrest.CoreMatchers.is;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.snaplogic.urlchangedetector.notifier.JavaMailNotifier;

@RunWith(SpringJUnit4ClassRunner.class)
public class DetectChangeInContentTest {

	@InjectMocks
	private DetectChangeInContent changeDetector;
	
	@Mock
	private JavaMailNotifier emailNotifierMock;
	
	@Before
    public void initMocks(){
        MockitoAnnotations.initMocks(this);
    }
	
	@Test
	public void testIfHighChangeTriggersEmail() {
		// Verifies notification is triggered when changes are more than 10%
		String archivedXml = "<html><head>archived content</head></html>";
		String currentXml = "<html><head>adding lot more changes<body></body></head></html>";
		changeDetector.compareCurrentVsPrevAndNotify(archivedXml, currentXml);
		Mockito.verify(emailNotifierMock, Mockito.times(1)).sendSimpleMessage(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
	}
	
	@Test
	public void testBelowThresholdChange() {
		// Verifies notification is not triggered when changes are below 10%
		String archivedXml = "<html><head>archived content</head></html>";
		String currentXml = "<html><head>archived content NEW</head></html>";
		changeDetector.compareCurrentVsPrevAndNotify(archivedXml, currentXml);
		Mockito.verify(emailNotifierMock, Mockito.times(0)).sendSimpleMessage(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
	}
	
	@Test
	public void testWhenNoChangeNoNotificationTriggers() {
		// Verifies notification is not triggered when there are no changes
		String archivedXml = "<html><head>archived content</head></html>";
		String currentXml = "<html><head>archived content</head></html>";
		changeDetector.compareCurrentVsPrevAndNotify(archivedXml, currentXml);
		Mockito.verify(emailNotifierMock, Mockito.times(0)).sendSimpleMessage(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());;
	}
	
	@Test
	public void testSanitizeHtmlInput() throws URISyntaxException, UnsupportedEncodingException, IOException {
		// Verifies if html input is sanitized;
		ClassPathResource classPathResource = new ClassPathResource("inputUnSanitize.html");
        java.nio.file.Path resPath = java.nio.file.Paths.get(classPathResource.getURI());
        String htmlString = new String(java.nio.file.Files.readAllBytes(resPath), "UTF8");
        
        ClassPathResource expectedClassPathResource = new ClassPathResource("expectedSanitizedOutput.html");
        java.nio.file.Path expectedResPath = java.nio.file.Paths.get(expectedClassPathResource.getURI());
        String expectedHtmlString = new String(java.nio.file.Files.readAllBytes(expectedResPath), "UTF8");
        
		Document rawHtml = Jsoup.parse(htmlString);
		Document sanitizedHtml = changeDetector.sanitizeHtml(rawHtml);
		Assert.assertThat(sanitizedHtml.outerHtml(), is(expectedHtmlString));
	}
}
