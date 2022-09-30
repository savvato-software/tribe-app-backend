package com.savvato.tribeapp.services;

public interface SMSChallengeCodeService {

	public String sendSMSChallengeCodeToPhoneNumber(String phoneNumber);
	public void clearSMSChallengeCodeToPhoneNumber(String phoneNumber);
	public boolean isAValidSMSChallengeCode(String phoneNumber, String code);
	
}
