package com.savvato.tribeapp.repositories;

import com.savvato.tribeapp.entities.Cosign;
import com.savvato.tribeapp.entities.CosignId;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CosignRepository extends CrudRepository<Cosign, CosignId> {

}
