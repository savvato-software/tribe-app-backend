package com.savvato.tribeapp.services;

public interface CosignService {

    void saveCosign(Long userIdIssuing, Long userIdReceiving, Long phraseId);
}
