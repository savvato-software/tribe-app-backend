package com.savvato.tribeapp.controllers.annotations.controllers.NotificationController;

import com.savvato.tribeapp.controllers.annotations.responses.NotFound;
import com.savvato.tribeapp.controllers.annotations.responses.Success;
import com.savvato.tribeapp.dto.NotificationDTO;
import io.swagger.v3.oas.annotations.Operation;
import java.lang.annotation.*;

/** Documentation for getting a notification by its ID. */
@Target({ElementType.METHOD, ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Operation(
    summary = "Get a notification by its ID",
    description = "Provided a notification ID, get the notification if it exists.")
@Success(
    description = "Successfully retrieved notification",
    implementation = NotificationDTO.class)
@NotFound(description = "The notification does not exist")
public @interface GetNotificationById {}
