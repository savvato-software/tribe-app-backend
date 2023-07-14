package com.savvato.tribeapp.repositories;

import com.savvato.tribeapp.entities.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationTypeRepository extends JpaRepository<NotificationType, Long> {
}
