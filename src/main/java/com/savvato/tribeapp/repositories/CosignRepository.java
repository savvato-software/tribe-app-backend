package com.savvato.tribeapp.repositories;

import com.savvato.tribeapp.entities.Cosign;
import com.savvato.tribeapp.entities.CosignId;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CosignRepository extends CrudRepository<Cosign, CosignId> {
    @Query(nativeQuery = true, value = "select user_id_issuing from cosign where user_id_receiving = ?1 and phrase_id = ?2")
    List<Long> findCosignersByUserIdReceivingAndPhraseId(Long userIdReceiving, Long phraseId);

    @Query(nativeQuery = true, value = "select * from cosign where user_id_receiving = ?")
    List<Cosign> findAllByUserIdReceiving(Long userIdReceiving);
}
