package com.savvato.tribeapp.controllers.annotations.controllers.NotificationController;

import com.savvato.tribeapp.controllers.annotations.requests.DocumentedRequestBody;
import com.savvato.tribeapp.controllers.annotations.responses.Success;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import java.lang.annotation.*;

/** Documentation for updating a notification by its ID. */
@Target({ElementType.METHOD, ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Operation(
    summary = "Update notification record",
    description =
        "Provided a notification ID, replace a notification with a new notification containing accurate read status.")
@DocumentedRequestBody(
    description = "The ID of the notification to update",
    example = "1",
    implementation = Integer.class)
@Success(
    description = "Successfully updated notification",
    examples = {
      @ExampleObject(name = "Already read", value = "Notification is already read"),
      @ExampleObject(name = "Updated", value = "Notification status updated")
    })
public @interface UpdateNotification {}
