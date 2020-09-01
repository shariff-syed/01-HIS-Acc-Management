package com.ashokit.utils;

import java.security.SecureRandom;

import org.springframework.stereotype.Component;

import com.ashokit.constants.AppConstants;

@Component
public class PwdUtils {
	
	public  String generateRandomPassword(int len)
	{
		final String chars = AppConstants.PAZZWORD_CHARS;
		SecureRandom random = new SecureRandom();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < len; i++) {
			int randomIndex = random.nextInt(chars.length());
			sb.append(chars.charAt(randomIndex));
		}
		return sb.toString();
	}
}
