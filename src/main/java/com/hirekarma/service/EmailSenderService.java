package com.hirekarma.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;



@Service
public class EmailSenderService {
	@Autowired
	public JavaMailSender mailSender;
	
//	sending email function
	@Async
	public void sendSimpleEmail(String toEmail,
			String body,
			String subject) {
		try {
//			Creating mail body
			SimpleMailMessage message = new SimpleMailMessage();
			message.setFrom("springbootemails@gmail.com");
			message.setTo(toEmail);
			message.setText(body);
			message.setSubject(subject);
			
//			sending mail
			mailSender.send(message);
			
			System.out.println("Mail sent");
			
		} catch (Exception e) {
			System.out.println("Somewthing went wrong while sending mail to mailid:"+toEmail+"");
		}
		
	}
}
