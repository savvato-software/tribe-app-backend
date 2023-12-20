package com.savvato.tribeapp.repositories;

import com.savvato.tribeapp.entities.Connection;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConnectionsRepository extends CrudRepository<Connection, Long> {
    @Query(nativeQuery = true, value = "delete from connections where requested_user_id=?1 AND to_be_connected_with_user_id=?2")
    void removeConnection(Long requestingUserId, Long connectedWithUserId);
}
