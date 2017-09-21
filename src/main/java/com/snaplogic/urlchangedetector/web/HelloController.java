package com.snaplogic.urlchangedetector.web;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Random;

import org.apache.commons.lang.RandomStringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HelloController {

	private static final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
	private String changingDisplay = "";
	private Random randomNumGen = new Random();

	@RequestMapping("/")
	public String index(Model model) {
		model.addAttribute("changingDisplay", getChangingDisplay());
		return "index";
	}

	private String getChangingDisplay() {
		String currentTime = dateFormatter.format(new Timestamp(System.currentTimeMillis()));
		String generatedString = RandomStringUtils.randomAlphanumeric(randomNumGen.nextInt(5000) + 100);
		changingDisplay += currentTime + "--" + generatedString + "\n";
		return changingDisplay;
	}
}
