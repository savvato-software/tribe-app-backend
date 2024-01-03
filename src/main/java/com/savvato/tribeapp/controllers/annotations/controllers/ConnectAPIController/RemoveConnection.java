package com.savvato.tribeapp.controllers.annotations.controllers.ConnectAPIController;


import com.savvato.tribeapp.controllers.annotations.requests.DocumentedRequestBody;
import com.savvato.tribeapp.controllers.annotations.responses.BadRequest;
import com.savvato.tribeapp.controllers.annotations.responses.Success;
import com.savvato.tribeapp.controllers.dto.ConnectionRemovalRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ExampleObject;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Operation(
        summary = "Delete connection between two users",
        description = "Provided a ConnectionRemovalRequest (see schemas), save the connection.")
@DocumentedRequestBody(description = "A request to delete connection", implementation = ConnectionRemovalRequest.class)
@Success(
        description = "Status of attempt to delete connection",
        examples = {
                @ExampleObject(name = "Connection deleted successfully", value = "true"),
                @ExampleObject(name = "Connection could not be deleted", value = "false"),
        })
@BadRequest(description = "Could not delete the connection", noContent = true)
public @interface RemoveConnection {
}
