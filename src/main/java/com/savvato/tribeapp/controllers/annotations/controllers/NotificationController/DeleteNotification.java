package com.savvato.tribeapp.controllers.annotations.controllers.NotificationController;

import com.savvato.tribeapp.controllers.annotations.requests.DocumentedRequestBody;
import com.savvato.tribeapp.controllers.annotations.responses.Success;
import io.swagger.v3.oas.annotations.Operation;
import java.lang.annotation.*;

/** Documentation for deleting a notification by its ID. */
@Target({ElementType.METHOD, ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Operation(
    summary = "Delete notification record",
    description = "Provided a notification ID, delete a notification.")
@DocumentedRequestBody(
    description = "The ID of the notification to update",
    example = "1",
    implementation = Integer.class)
@Success(description = "Successfully deleted notification", example = "Notification deleted")
public @interface DeleteNotification {}
