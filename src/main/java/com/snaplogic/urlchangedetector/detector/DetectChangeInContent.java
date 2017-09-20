package com.snaplogic.urlchangedetector.detector;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.TimerTask;

import org.apache.log4j.Logger;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xml.sax.InputSource;
import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.diff.Diff;
import org.xmlunit.diff.Difference;

import com.snaplogic.urlchangedetector.notifier.JavaMailNotifier;

import info.debatty.java.stringsimilarity.Cosine;

@Component
public class DetectChangeInContent extends TimerTask {

	private Logger logger = Logger.getLogger(DetectChangeInContent.class);
	
	private String archivedXmlContent = null;

	private String url;
	
	private XMLOutputter outputter = new XMLOutputter();
	
	private String emailNotificationReceipientAddress;
	
	private Cosine cosineComparator = new Cosine();
	
	@Autowired
	private JavaMailNotifier emailNotifier;

	public void init(String url, String emailNotificationReceipientAddress) {
		this.url = url;
		this.emailNotificationReceipientAddress = emailNotificationReceipientAddress;
		
        Format newFormat = outputter.getFormat();
        String encoding = "UTF-8";
        newFormat.setEncoding(encoding);
        outputter.setFormat(newFormat);
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
		// Sanitize html retrieved from url above to get rid of tags like script
		String cleanedHtml = Jsoup.clean(doc.outerHtml(), Whitelist.basic());
		// Parse back cleaned html to validate its contents and balance missing tags
		Document parse = Jsoup.parse(cleanedHtml);
		String completeHtml = parse.outerHtml();

		// Convert html to xml to archive and easy comparison
		SAXBuilder saxBuilder = new SAXBuilder("org.cyberneko.html.parsers.SAXParser", false);

		org.jdom2.Document jdomDocument = null;
		try {
			jdomDocument = saxBuilder.build(
					new InputSource(new ByteArrayInputStream(completeHtml.getBytes(StandardCharsets.UTF_8.name()))));
		} catch (JDOMException | IOException e) {
			logger.error("SaxBuilder failed to convert html into xml with error : " + e.getMessage());
			e.printStackTrace();
			return;
		}

		String currentWebPageXmlContent = outputter.outputString(jdomDocument);
		// First run
		if(archivedXmlContent == null) {
			archivedXmlContent = currentWebPageXmlContent;
			return;
		}
		
//		Diff myDiff = DiffBuilder.compare(archivedXmlContent).withTest(currentWebPageXmlContent).build();
//		for(Difference difference : myDiff.getDifferences()) {
//			difference.getResult();
//		}
		double distance = cosineComparator.distance(archivedXmlContent, currentWebPageXmlContent);
		emailNotifier.sendSimpleMessage(emailNotificationReceipientAddress, "change detected", "changes detected");
		System.out.println("distance between the current vs archived = " + distance);
	}

}
