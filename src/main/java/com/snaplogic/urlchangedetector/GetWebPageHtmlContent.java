package com.snaplogic.urlchangedetector;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

import org.cyberneko.html.parsers.DOMParser;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class GetWebPageHtmlContent {

	public static void main(String args[]) throws InterruptedException, SAXException, JDOMException {

		try {
//			Document doc = Jsoup.connect("https://www.amazon.com/Gerber-Three-Piece-Cardigan-Diaper-Watermelon/dp/B018T3OL04/ref=br_asw_pdt-2?pf_rd_m=ATVPDKIKX0DER&pf_rd_s=&pf_rd_r=GYFCB6ENF9QXJH3YT0TY&pf_rd_t=36701&pf_rd_p=42d0df3a-0efe-4b59-9b69-87c0d382942d&pf_rd_i=desktop").get();
			Document doc = Jsoup.connect("https://raw.githubusercontent.com/olivierlacan/keep-a-changelog/master/CHANGELOG.md").get();
			String cleanedHtml = Jsoup.clean(doc.outerHtml(), Whitelist.basic());
			Document parse = Jsoup.parse(cleanedHtml);
			String outerHtml = parse.outerHtml();
			DOMParser parser = new DOMParser();
			parser.parse(new InputSource(new ByteArrayInputStream(outerHtml.getBytes(StandardCharsets.UTF_8.name()))));
			
			SAXBuilder saxBuilder = new SAXBuilder("org.cyberneko.html.parsers.SAXParser", false);
			org.jdom2.Document jdomDocument = saxBuilder.build(new InputSource(new ByteArrayInputStream(outerHtml.getBytes(StandardCharsets.UTF_8.name()))));
			
			
			XMLOutputter outputter = new XMLOutputter();
	        Format newFormat = outputter.getFormat();
	        String encoding = "UTF-8";
	        newFormat.setEncoding(encoding);
	        outputter.setFormat(newFormat);
			

			//save to this filename
			String fileName = "/Users/Ganesh/url-change-detector/test.xml";
			File file = new File(fileName);

			if (!file.exists()) {
				file.createNewFile();
			}

			//use FileWriter to write file
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			String outputString = outputter.outputString(jdomDocument); 
			System.out.flush();
			bw.write(outputString);
			bw.close();

			System.out.println("Done");

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
