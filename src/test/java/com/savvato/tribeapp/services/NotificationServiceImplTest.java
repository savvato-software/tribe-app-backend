package com.savvato.tribeapp.services;

import com.savvato.tribeapp.entities.Notification;
import com.savvato.tribeapp.entities.NotificationType;
import com.savvato.tribeapp.repositories.NotificationRepository;
import com.savvato.tribeapp.repositories.NotificationTypeRepository;
import com.savvato.tribeapp.dto.NotificationDTO;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.time.Clock;
import java.time.Instant;

import java.time.ZoneId;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertEquals;


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
	public void testGetNotificationDTOById_NotificationExists() {
		// Mock data
		Long notificationId = 1L;
		NotificationType mockType = new NotificationType();
		mockType.setId(1L);

		Notification mockNotification = new Notification();
		mockNotification.setId(notificationId);
		mockNotification.setType(mockType);
		mockNotification.setDescription("Test Description");
		mockNotification.setBody("Test Body");
		mockNotification.setLastUpdatedDate(LocalDateTime.of(2023, 7, 18, 12, 0)); // Set the lastUpdatedDate to a fixed value for testing

		// Mock repository behavior
		when(notificationRepository.findById(notificationId)).thenReturn(Optional.of(mockNotification));

		// Set a fixed time for testing
		Instant fixedInstant = LocalDateTime.of(2023, 7, 18, 13, 0).atZone(ZoneId.systemDefault()).toInstant();
		Clock fixedClock = Clock.fixed(fixedInstant, ZoneOffset.UTC);

		// Perform the method call with the fixedClock
		NotificationDTO result = notificationService.getNotificationDTOById(notificationId);

		// Verify the repository call
		verify(notificationRepository, times(1)).findById(notificationId);

		// Verify the result
		assertEquals("Test Description", result.description);
		assertEquals("Test Body", result.body);
		assertEquals("3600000", result.lastUpdatedDate); // Check that the formatted lastUpdatedDate is correct (1 hour in milliseconds)
		assertEquals(mockType.getIconUrl(), result.iconUrl);
	}

	@Test
	public void testGetNotificationDTOById_NotificationDoesNotExist() {
		// Mock data
		Long notificationId = 1L;

		// Mock repository behavior
		when(notificationRepository.findById(notificationId)).thenReturn(Optional.empty());

		// Perform the method call
		NotificationDTO result = notificationService.getNotificationDTOById(notificationId);

		// Verify the repository call
		verify(notificationRepository, times(1)).findById(notificationId);

		// Verify the result
		assertEquals(null, result);
	}
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
	@Test
	public void testDeleteNotification() {
		// Mock data
		Long notificationId = 1L;
		Notification mockNotification = new Notification();
		mockNotification.setId(notificationId);

		// Mock repository behavior
		when(notificationRepository.findById(notificationId)).thenReturn(Optional.of(mockNotification));
		doNothing().when(notificationRepository).delete(mockNotification);

		// Perform the method call
		notificationService.deleteNotification(notificationId);

		// Verify the repository calls
		verify(notificationRepository, times(1)).findById(notificationId);
		verify(notificationRepository, times(1)).delete(mockNotification);
	}
	@Test
	public void testCheckNotificationExists_Exists() {
		// Mock data
		Long notificationId = 1L;
		Notification mockNotification = new Notification();
		mockNotification.setId(notificationId);

		// Mock repository behavior
		when(notificationRepository.existsById(notificationId)).thenReturn(true);

		// Perform the method call
		boolean exists = notificationService.checkNotificationExists(notificationId);

		// Verify the repository call
		verify(notificationRepository, times(1)).existsById(notificationId);

		// Verify the result
		assertTrue(exists);
	}
	@Test
	public void testCheckNotificationExists_NotExists() {
		// Mock data
		Long notificationId = 1L;

		// Mock repository behavior
		when(notificationRepository.existsById(notificationId)).thenReturn(false);

		// Perform the method call
		boolean exists = notificationService.checkNotificationExists(notificationId);

		// Verify the repository call
		verify(notificationRepository, times(1)).existsById(notificationId);

		// Verify the result
		assertFalse(exists);
	}
	@Test
	public void testGetNotificationsByUserId() {
		// Mock data
		Long userId = 1L;
		Notification notification1 = new Notification();
		notification1.setId(1L);
		notification1.setUserId(userId);
		// Add more mock notifications as needed

		// Mock the repository behavior
		when(notificationRepository.findByUserId(userId)).thenReturn(Arrays.asList(notification1));
		// You can add more "when" statements for other test cases with different user IDs.

		// Perform the method call
		List<Notification> notifications = notificationService.getNotificationsByUserId(userId);

		// Verify the repository call
		// This ensures that the "findByUserId" method was called with the correct userId parameter
		verify(notificationRepository).findByUserId(userId);

		// Verify the result
		assertEquals(1, notifications.size()); // Change this to match the number of mock notifications
		assertEquals(notification1.getId(), notifications.get(0).getId());
		assertEquals(userId, notifications.get(0).getUserId());
		// Add more assertions for other notification properties as needed
	}
}
