package com.savvato.tribeapp.services;

import com.plivo.api.models.message.MessageCreateResponse;

import java.util.Optional;

public interface SMSTextMessageService {

  boolean sendSMS(String toPhoneNumber, String msg);

  Optional<MessageCreateResponse> createResponse(String toPhoneNumber, String msg);
}
