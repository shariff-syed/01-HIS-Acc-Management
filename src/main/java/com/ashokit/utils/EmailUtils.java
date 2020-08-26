package com.ashokit.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.ashokit.constants.AppConstants;
import com.ashokit.entity.HISEmployeeAccountsEntity;
import com.ashokit.exception.EmailException;

@Component
public class EmailUtils {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private JavaMailSender mailSender;

	public boolean sendUserAccUnlockEmail(HISEmployeeAccountsEntity employee) {
		logger.debug(AppConstants.METHOD_STARTED);
		try {
			MimeMessage mimeMessage = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
			helper.setTo(employee.getEmail());
			helper.setSubject(AppConstants.UNLOCCK_MAIL_SUB);
			helper.setText(getUnlockEmailBody(employee), true);
			mailSender.send(mimeMessage);
		} catch (Exception e) {
			logger.error(AppConstants.EXCE_OCCUR, e.getMessage());
			throw new EmailException(AppConstants.EMAIL_SENT_FAILED);
		}
		logger.debug(AppConstants.METHOD_ENDED);
		return true;
	}

	private String getUnlockEmailBody(HISEmployeeAccountsEntity employee) throws IOException {

		StringBuilder sb = new StringBuilder("");
		String mailBody = null;
		FileReader fr = new FileReader(AppConstants.UNLOCCK_MAIL_TXT);
		try (BufferedReader br = new BufferedReader(fr)) {
			String line = br.readLine();
			while (line != null) {
				sb.append(line);
				line = br.readLine();
			}
			mailBody = sb.toString();
			mailBody = mailBody.replace(AppConstants.MAIL_BIND_FNAME, employee.getFirstName());
			mailBody = mailBody.replace(AppConstants.MAIL_BIND_LNAME, employee.getLastName());
			mailBody = mailBody.replace(AppConstants.MAIL_BIND_EMAIL, employee.getEmail());
			mailBody = mailBody.replace(AppConstants.MAIL_BIND_PWD, employee.getPazzword());
		} catch (Exception e) {
			logger.error(AppConstants.EXCE_OCCUR, e.getMessage());
			throw new EmailException(AppConstants.EMAIL_SENT_FAILED);
		}
		logger.debug(AppConstants.METHOD_ENDED);
		return mailBody;
	}
}
