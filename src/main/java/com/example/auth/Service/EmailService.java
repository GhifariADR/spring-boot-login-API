package com.example.auth.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class EmailService {

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private TemplateEngine templateEngine;

	public void sendRegistrationEmail(String to, String username){
		try {
			Context context = new Context();
			context.setVariable("username", username);

			String htmlContent = templateEngine.process("email/registration", context);

			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true);

			helper.setTo(to);
			helper.setSubject("Registrasi Berhasil");
			helper.setText(htmlContent, true);

			mailSender.send(message);

		} catch (MessagingException e){
			throw new RuntimeException("Gagal mengirim email: " + e.getMessage());
		}
	}

}
