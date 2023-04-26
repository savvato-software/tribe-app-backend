package com.savvato.tribeapp.services;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;
import org.slf4j.Logger;

@Service
public class SMSChallengeCodeServiceImpl implements SMSChallengeCodeService {

	private Logger logger;
	
	@Autowired
	CacheService cache;
	
	@Autowired
	SMSTextMessageService smss;
	
	public String sendSMSChallengeCodeToPhoneNumber(String phoneNumber) {
		String rtn = "";
		String challengeCode = getRandomStringOfNDigits(6);
		logger.debug("SMS Challenge Code is:" + challengeCode);
		if (smss.sendSMS(phoneNumber, challengeCode + " <--- Your Tribe App Challenge Code")) {
			cache.put("SMSChallengeCodesByPhoneNumber", phoneNumber, challengeCode);
            rtn = challengeCode;
		} else {
            rtn = "error sending sms challenge to " + phoneNumber; 
        }
        
        return rtn;
	}
	
	public void clearSMSChallengeCodeToPhoneNumber(String phoneNumber) {
		cache.remove("SMSChallengeCodesByPhoneNumber", phoneNumber);
	}
	
	public boolean isAValidSMSChallengeCode(String phoneNumber, String code) {
		boolean rtn = false;
		String sentCode = cache.get("SMSChallengeCodesByPhoneNumber", phoneNumber);
		rtn = sentCode != null && sentCode.equals(code);
		
		logger.debug("isAValidSMSChallengeCode(), for ph# " + phoneNumber + " and user-supplied code " + code + " returns " + rtn);
		
		return rtn;
	}
	
	private String getRandomStringOfNDigits(int digitCount) {
		String rtn = "";
		
		while (digitCount > 0) {
			Random rand = new Random();
			int value = rand.nextInt(10);
			
			rtn += value;
			digitCount--;
		}
		
		return rtn;
	}
}
