package com.savvato.tribeapp.repositories;

import com.savvato.tribeapp.entities.Connection;
import com.savvato.tribeapp.entities.Noun;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConnectionsRepository extends CrudRepository<Connection, Long> {
}
