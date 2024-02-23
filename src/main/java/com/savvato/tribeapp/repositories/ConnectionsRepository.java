package com.savvato.tribeapp.repositories;

import com.savvato.tribeapp.entities.Connection;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConnectionsRepository extends CrudRepository<Connection, Long> {
    List<Connection> findAllByToBeConnectedWithUserId(Long toBeConnectedWithUserId);

    @Query(nativeQuery = true, value = "delete from connections where requesting_user_id=?1 AND to_be_connected_with_user_id=?2")
    void removeConnection(Long requestingUserId, Long connectedWithUserId);

    @Query(nativeQuery = true, value = "select * from connections where to_be_connected_with_user_id=?1 and requesting_user_id=?2")
    Optional<Connection> findExistingConnectionWithReversedUserIds(Long requestingUserId, Long toBeConnectedWithUserId);

}
