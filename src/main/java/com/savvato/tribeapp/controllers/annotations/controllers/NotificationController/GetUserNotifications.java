package com.savvato.tribeapp.controllers.annotations.controllers.NotificationController;

import com.savvato.tribeapp.controllers.annotations.responses.Success;
import com.savvato.tribeapp.dto.NotificationDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Operation(
    summary = "Get a user's notifications",
    description =
        "Provided a user ID, get a list of all of the notifications belonging to that user.")
@Success(
    description = "Successfully retrieved all notifications",
    array = @ArraySchema(schema = @Schema(implementation = NotificationDTO.class)))
public @interface GetUserNotifications {}
