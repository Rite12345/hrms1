package com.dizitiveit.hrms.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl {

	
	 @Autowired 
	 public JavaMailSender emailSender;
	 
		

	
		public void sendSimpleMessage(String to, String subject, String text) {
			// TODO Auto-generated method stub
			
			SimpleMailMessage message = new SimpleMailMessage();
			  message.setFrom("dizisales1@gmail.com");
			  message.setTo(to);
			  message.setSubject(subject); 
			  message.setText(text);
			  emailSender.send(message); 
			
		}

	
}
