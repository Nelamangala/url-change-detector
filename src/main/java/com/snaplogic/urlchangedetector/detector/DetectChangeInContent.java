package com.snaplogic.urlchangedetector.detector;

import java.io.IOException;
import java.util.TimerTask;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;
import com.snaplogic.urlchangedetector.notifier.JavaMailNotifier;

import info.debatty.java.stringsimilarity.Levenshtein;

@Component
public class DetectChangeInContent extends TimerTask implements ApplicationListener<ContextClosedEvent>{

	private Logger logger = Logger.getLogger(DetectChangeInContent.class);
	
	private String archivedXmlContent = null;

	private String url;
	
	private String emailNotificationReceipientAddress;
	
	private Levenshtein levenshteinDistanceCalculator = new Levenshtein();
	
	private int MIN_CHANGE_FOR_NOTIFICATION = 10; // Min changes required to send email notification
	
	@Autowired
	private JavaMailNotifier emailNotifier;

	public void init(String url, String emailNotificationReceipientAddress) {
		this.url = url;
		this.emailNotificationReceipientAddress = emailNotificationReceipientAddress;
	}

	@Override
	public void run() {
		// Hit the web page and get its details
		Document doc = null;
		try {
			doc = Jsoup.connect(url).get();
		} catch (IOException e) {
			logger.error("Jsoup failed to extract web page for given url = " + url + ", error : " + e.getMessage());
			e.printStackTrace();
			return;
		}
		
		Document sanitizedHtml = sanitizeHtml(doc);
		if(sanitizedHtml == null) {
			logger.error("Error sanitizing html, abort!");
			return;
		}
		
		String currentWebPageXmlContent = sanitizedHtml.outerHtml();
		// First run
		if(archivedXmlContent == null) {
			archivedXmlContent = currentWebPageXmlContent;
			return;
		}
		
		compareCurrentVsPrevAndNotify(archivedXmlContent, currentWebPageXmlContent);
	}

	protected void compareCurrentVsPrevAndNotify(String archivedXmlContent, String currentWebPageXmlContent) {
		double distance = levenshteinDistanceCalculator.distance(archivedXmlContent, currentWebPageXmlContent);
		logger.info("distance between the current vs archived = " + distance);
		double percentageChangeInContent = (distance * 100) / archivedXmlContent.length();
		if(percentageChangeInContent > MIN_CHANGE_FOR_NOTIFICATION) {
			emailNotifier.sendSimpleMessage(emailNotificationReceipientAddress, "Change detected",
					String.format("changes detected in url : %s with distance : %f", url, distance));
		}
	}

	protected Document sanitizeHtml(Document doc) {
		// Sanitize html retrieved from url above to get rid of tags like script
		String cleanedHtml = Jsoup.clean(doc.outerHtml(), Whitelist.basic());
		// Parse back cleaned html to validate its contents and balance missing tags
		return Jsoup.parse(cleanedHtml);
	}

	public JavaMailNotifier getEmailNotifier() {
		return emailNotifier;
	}

	public void setEmailNotifier(JavaMailNotifier emailNotifier) {
		this.emailNotifier = emailNotifier;
	}

	@Override
	public void onApplicationEvent(ContextClosedEvent arg0) {
		this.cancel();
	}

}
