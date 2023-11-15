package com.savvato.tribeapp.services;

import com.plivo.api.PlivoClient;
import com.plivo.api.exceptions.PlivoRestException;
import com.plivo.api.models.message.Message;
import com.plivo.api.models.message.MessageCreateResponse;
import lombok.Generated;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service
@Slf4j
public class SMSTextMessageServiceImpl implements SMSTextMessageService {

    @Autowired
    CacheService cache;

    @Value("${PLIVO_SMS_AUTH_ID}")
    private String SMS_API_AUTH_ID;

    @Value("${PLIVO_SMS_AUTH_TOKEN}")
    private String SMS_API_AUTH_TOKEN;

    @Generated
    @Bean
    public PlivoClient plivoClient() {
        return new PlivoClient(SMS_API_AUTH_ID, SMS_API_AUTH_TOKEN);
    }

    @Autowired
    PlivoClient plivoClient;

    public Optional<MessageCreateResponse> createResponse(String toPhoneNumber, String msg) {
        try {
            MessageCreateResponse response =
                    Message.creator("19342227693", toPhoneNumber, msg).client(plivoClient).create();
            return Optional.of(response);
        } catch (PlivoRestException plivoRestException) {
            log.debug(
                    "PlivoRestException occurred when trying to create response: "
                            + plivoRestException.getMessage());
        } catch (IOException ioException) {
            log.debug("IOException occurred when trying to create response: " + ioException.getMessage());
        }
        return Optional.empty();
    }

    public boolean sendSMS(String toPhoneNumber, String msg) {
        boolean rtn = false;

        if (toPhoneNumber.startsWith("0")) {
            rtn = true; // for use in testing. just act like we did it.

            log.debug("PRETENDED to send SMS to " + toPhoneNumber + " /  msg: [" + msg + "]");
        } else {
            try {
                log.debug(SMS_API_AUTH_ID);
                log.debug(SMS_API_AUTH_TOKEN);
                Optional<MessageCreateResponse> responseOpt = createResponse(toPhoneNumber, msg);
                if (responseOpt.isPresent()) {
                    MessageCreateResponse response = responseOpt.get();

                    log.debug("sms sent to " + toPhoneNumber + " /  msg: [" + msg + "]");
                    log.debug("sms response msg: " + response.getMessage());
                    rtn = true;
                }

            } catch (Exception e) {
                log.info("Caught exception trying to send SMS,  ------ " + e.getMessage());
            }
        }

        return rtn;
    }
}
