package com.savvato.tribeapp.services;

import com.plivo.api.Plivo;
import com.plivo.api.models.message.Message;
import com.plivo.api.models.message.MessageCreateResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SMSTextMessageServiceImpl implements SMSTextMessageService {

	@Autowired
	CacheService cache;
	
	@Value("${PLIVO_SMS_AUTH_ID}")
    private String SMS_API_AUTH_ID;

	@Value("${PLIVO_SMS_AUTH_TOKEN}")
    private String SMS_API_AUTH_TOKEN;

	public boolean sendSMS(String toPhoneNumber, String msg) {
		boolean rtn = false;

		if (toPhoneNumber.startsWith("0")) {
			rtn = true; 	// for use in testing. just act like we did it.

			log.debug("PRETENDED to send SMS to " + toPhoneNumber + " /  msg: [" + msg + "]");
		} else {
			try {
				log.debug(SMS_API_AUTH_ID);
				log.debug(SMS_API_AUTH_TOKEN);
				Plivo.init(SMS_API_AUTH_ID, SMS_API_AUTH_TOKEN);
				MessageCreateResponse response = Message.creator("19342227693", toPhoneNumber, msg).create();

				log.debug("sms sent to " + toPhoneNumber + " /  msg: [" + msg + "]");
				log.debug("sms response msg: " + response.getMessage());

				rtn = true;

			} catch (Exception e) {
				log.info("Caught exception trying to send SMS,  ------ " + e.getMessage());
			}
		}

		return rtn;
	}
}
