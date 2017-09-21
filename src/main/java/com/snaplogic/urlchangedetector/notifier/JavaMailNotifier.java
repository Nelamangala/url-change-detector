package com.snaplogic.urlchangedetector.notifier;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

@Service
@PropertySource("classpath:config.properties") 
public class JavaMailNotifier {

	@Autowired
	public JavaMailSender emailSender;
	
	private String smtpHost;
	private int mailPort;
	private String mailUserName;	
	private String mailPassword;
	
	@Autowired
	public JavaMailNotifier(@Value( "${app.mail.host}" )String smtpHost, 
			@Value( "${app.mail.port}" )int mailPort, 
			@Value( "${app.mail.username}" )String mailUserName, 
			@Value( "${app.mail.password}" )String mailPassword) {
		this.smtpHost = smtpHost;
		this.mailPort = mailPort;
		this.mailUserName = mailUserName;
		this.mailPassword = mailPassword;
	}
	public void sendSimpleMessage(String to, String subject, String text) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(to);
		message.setSubject(subject);
		message.setText(text);
		emailSender.send(message);
	}
	
	@Bean
	public JavaMailSender getJavaMailSender() {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
	    mailSender.setHost(smtpHost);
	    mailSender.setPort(mailPort);
	     
	    mailSender.setUsername(mailUserName);
	    mailSender.setPassword(mailPassword);
	     
	    Properties props = mailSender.getJavaMailProperties();
	    props.put("mail.transport.protocol", "smtp");
	    props.put("mail.smtp.auth", "true");
	    props.put("mail.smtp.starttls.enable", "true");
	    props.put("mail.debug", "true");
	     
	    return mailSender;
	}

	public String getSmtpHost() {
		return smtpHost;
	}

	public void setSmtpHost(String smtpHost) {
		this.smtpHost = smtpHost;
	}

	public int getMailPort() {
		return mailPort;
	}

	public void setMailPort(int mailPort) {
		this.mailPort = mailPort;
	}

	public String getMailUserName() {
		return mailUserName;
	}

	public void setMailUserName(String mailUserName) {
		this.mailUserName = mailUserName;
	}

	public String getMailPassword() {
		return mailPassword;
	}

	public void setMailPassword(String mailPassword) {
		this.mailPassword = mailPassword;
	}
}
