package com.savvato.tribeapp.services;

import com.savvato.tribeapp.entities.Notification;
import com.savvato.tribeapp.entities.NotificationType;
import com.savvato.tribeapp.repositories.NotificationRepository;
import com.savvato.tribeapp.repositories.NotificationTypeRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith({SpringExtension.class})
public class NotificationServiceImplTest {

	@TestConfiguration
	static class NotificationServiceTestContextConfiguration {

		@Bean
		public NotificationService notificationService() {
			return new NotificationServiceImpl();
		}
	}
	@Autowired
	NotificationService notificationService;

	@MockBean
	NotificationTypeRepository notificationTypeRepository;

	@MockBean
	NotificationRepository notificationRepository;

	@Test
	public void testCreateNotification() {
		// Mock data
		NotificationType mockType = new NotificationType();
		mockType.setId(1L);

		Notification mockNotification = new Notification();
		mockNotification.setId(1L);
		mockNotification.setType(mockType);
		mockNotification.setUserId(1L);
		mockNotification.setDescription("Test Description");
		mockNotification.setBody("Test Body");
		mockNotification.setRead(false);
		mockNotification.setCreatedDate(LocalDateTime.now());
		mockNotification.setLastUpdatedDate(LocalDateTime.now());

		// Mock repository behavior
		when(notificationTypeRepository.findById(1L)).thenReturn(Optional.of(mockType));
		when(notificationRepository.save(any(Notification.class))).thenReturn(mockNotification);

		// Test data
		NotificationType type = new NotificationType();
		type.setId(1L);

		Long userId = 1L;
		String description = "Test Description";
		String body = "Test Body";

		// Perform the method call
		Notification result = notificationService.createNotification(type, userId, description, body);

		// Verify the repository calls
		verify(notificationTypeRepository, times(1)).findById(1L);
		verify(notificationRepository, times(1)).save(any(Notification.class));

		// Verify the result
		assertNotNull(result);
		assertEquals(mockNotification.getId(), result.getId());
		assertEquals(mockNotification.getType(), result.getType());
		assertEquals(mockNotification.getUserId(), result.getUserId());
		assertEquals(mockNotification.getDescription(), result.getDescription());
		assertEquals(mockNotification.getBody(), result.getBody());
		assertEquals(mockNotification.isRead(), result.isRead());
		assertEquals(mockNotification.getCreatedDate(), result.getCreatedDate());
		assertEquals(mockNotification.getLastUpdatedDate(), result.getLastUpdatedDate());
	}
}