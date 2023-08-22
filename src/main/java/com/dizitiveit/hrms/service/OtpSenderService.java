package com.dizitiveit.hrms.service;

import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.dizitiveit.hrms.model.OtpSender;
import com.google.common.cache.CacheBuilder;

@Service
public class OtpSenderService {

	@Autowired
	private OtpSender otpsender;

	private static final Integer EXPIRE_MIN = 2;
	private LoadingCache<String, Integer> otpCache;

	@Value("${sms.sever}")
	private String SMSSERVER;

	@Value("${sms.port}")
	private int SMSPORT;

	@Value("${sms.username}")
	private String SMSUSERNAME;

	@Value("${sms.password}")
	private String SMSPASSWORD;

	@Value("${sms.source}")
	private String SMSSOURCE;

	@Value("${sms.dlr}")
	private String SMSDLR;

	@Value("${sms.type}")
	private String SMSTYPE;

	private String otpNumber;

	public String sendOtp(String mobile, String msg) {

		otpNumber = String.valueOf(OTP(4));
		String message = "<#> Your HRMS OTP Code is"+otpNumber+" "+msg;

		otpsender.setServer(SMSSERVER);
		otpsender.setPort(SMSPORT);
		otpsender.setUsername(SMSUSERNAME);
		otpsender.setPassword(SMSPASSWORD);
		otpsender.setMessage(convertToUnicode(message).toString());
		otpsender.setDlr(SMSDLR);
		otpsender.setType(SMSTYPE);
		otpsender.setDestination(mobile);
		otpsender.setSource(SMSSOURCE);

		System.out.println("SMS DETAILS: " + otpsender.toString());
		otpsender.submitMessage();
		int i = Integer.parseInt(otpNumber);
		otpCache.put(mobile, i);
		try {
			System.out.println("cache is" + otpCache.get(mobile));
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return otpNumber;

	}

	private static StringBuffer convertToUnicode(String regText) {
		char[] chars = regText.toCharArray();
		StringBuffer hexString = new StringBuffer();
		for (int i = 0; i < chars.length; i++) {
			String iniHexString = Integer.toHexString((int) chars[i]);
			if (iniHexString.length() == 1) {
				iniHexString = "000" + iniHexString;
			} else if (iniHexString.length() == 2) {
				iniHexString = "00" + iniHexString;
			} else if (iniHexString.length() == 3) {
				iniHexString = "0" + iniHexString;
			}
			hexString.append(iniHexString);
		}
		System.out.println(hexString);
		return hexString;
	}

	static char[] OTP(int len) {

		String numbers = "0123456789";

		// Using random method
		Random rndm_method = new Random();
		char[] otp = new char[len];

		for (int i = 0; i < len; i++) {
			// use of charAt() method : to get character value
			// use of nextInt() as it is scanning the value as int
			otp[i] = numbers.charAt(rndm_method.nextInt(numbers.length()));
		}
		return otp;
	}

	public OtpSenderService() {
		super();
		otpCache = CacheBuilder.newBuilder().expireAfterWrite(EXPIRE_MIN, TimeUnit.MINUTES)
				.build(new CacheLoader<String, Integer>() {
					public Integer load(String key) {
						return 0;
					}
				});
	}

	public int getOtp(String key) {
		try {
			return otpCache.get(key);
		} catch (Exception e) {
			return 0;
		}
	}

	public void clearOTP(String key) {
		otpCache.invalidate(key);
	}
	
	public String sendSms(String mobile, String msg) {

		otpsender.setServer(SMSSERVER);
		otpsender.setPort(SMSPORT);
		otpsender.setUsername(SMSUSERNAME);
		otpsender.setPassword(SMSPASSWORD);
		otpsender.setMessage(convertToUnicode(msg).toString());
		otpsender.setDlr(SMSDLR);
		otpsender.setType(SMSTYPE);
		otpsender.setDestination(mobile);
		otpsender.setSource(SMSSOURCE);

		
		otpsender.submitMessage();
		

		return "Sent";

	}
}
