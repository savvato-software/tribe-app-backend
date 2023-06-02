package com.savvato.tribeapp.controllers;

import com.savvato.tribeapp.controllers.dto.SMSChallengeRequest;
import com.savvato.tribeapp.services.SMSChallengeCodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@Slf4j
public class SMSChallengeCodeAPIController {
    @Autowired
    SMSChallengeCodeService smsccs;

    @RequestMapping(value = { "/api/public/sendSMSChallengeCodeToPhoneNumber" }, method=RequestMethod.POST)
    public String sendSMSChallengeCode(@RequestBody @Valid SMSChallengeRequest req) {
        String phoneNumber = req.phoneNumber;  // assume the number we're getting is 10 digits, without the country code

        if (!phoneNumber.startsWith("0"))
            phoneNumber = "1" + phoneNumber;

        String rtn = smsccs.sendSMSChallengeCodeToPhoneNumber(phoneNumber);
        log.debug("Sent challenge code to " + phoneNumber + ". " + rtn);
        return rtn;
    }

    @RequestMapping(value = { "/api/public/clearSMSChallengeCodeToPhoneNumber" }, method=RequestMethod.POST)
    public String clearSMSChallengeCode(HttpServletRequest request, Model model) {
        String phoneNumber = "1" + request.getParameter("phoneNumber");  // assume the number we're getting is 10 digits, without the country code
        smsccs.clearSMSChallengeCodeToPhoneNumber(phoneNumber);
        return "ok";
    }

    @RequestMapping(value = { "/api/public/isAValidSMSChallengeCode" }, method=RequestMethod.POST)
    public boolean isAValidSMSChallengeCode(@RequestBody @Valid SMSChallengeRequest req) {

        if ((req.phoneNumber == null || req.phoneNumber.equals("null")) || (req.code == null || req.code.equals("null"))) {
            throw new IllegalArgumentException("Cannot check for valid SMS challenge code with null phoneNumber or challenge code.");
        }

        return smsccs.isAValidSMSChallengeCode(req.phoneNumber, req.code);
    }

}
