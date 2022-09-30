package com.savvato.tribeapp.services;

public interface SMSTextMessageService {

	public boolean sendSMS(String toPhoneNumber, String msg);
	
}
