package com.savvato.tribeapp.repositories;

import com.savvato.tribeapp.entities.Connection;
import com.savvato.tribeapp.entities.Noun;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConnectionsRepository extends CrudRepository<Connection, Long> {
    List<Connection> findAllByToBeConnectedWithUserId(Long toBeConnectedWithUserId);
    @Query(nativeQuery = true, value = "delete from connections where requesting_user_id=?1 AND to_be_connected_with_user_id=?2")
    void removeConnection(Long requestingUserId, Long connectedWithUserId);
}
