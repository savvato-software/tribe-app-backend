package com.savvato.tribeapp.repositories;

import com.savvato.tribeapp.entities.Connection;
import com.savvato.tribeapp.entities.Noun;
import com.savvato.tribeapp.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConnectionsRepository extends CrudRepository<Connection, Long> {

    // call the repository in the service
    // write unit test for the service
    Connection findByToBeConnectedWithUserId(Long toBeConnectedWithUserId);
}
