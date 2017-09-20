package com.snaplogic.urlchangedetector.archive;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class DataArchive {

	Map<String, String> webPageXmlContentByUrl = new HashMap<>();
	
	/**
	 * Gets web page xml for given url if it exists. 
	 * @param url
	 * @return
	 * 		web page xml if exits, NULL otherwise
	 */		
	public String getFromArchive(String url) {
		return webPageXmlContentByUrl.get(url);
	}
	
	/**
	 * Adds url and its corresponding xmlContent into data archive. 
	 * @param url
	 * @param xmlContent
	 */
	public void addToArchive(String url, String xmlContent) {
		webPageXmlContentByUrl.put(url, xmlContent);
	}
}
