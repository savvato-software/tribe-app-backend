package com.savvato.tribeapp.controllers.annotations.customizers;

import com.savvato.tribeapp.controllers.annotations.responses.Response;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.responses.ApiResponse;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;

import java.util.HashSet;
import java.util.Set;

import static com.savvato.tribeapp.controllers.annotations.utils.AnnotationsUtils.*;

import static com.savvato.tribeapp.controllers.annotations.utils.AnnotationsUtils.equals;
import static io.swagger.v3.core.util.AnnotationsUtils.getHeaders;

@Component
public class ResponseCustomizer implements OperationCustomizer {
  protected Set<Response> customResponses = new HashSet<>();

  @Override
  public Operation customize(Operation operation, HandlerMethod handlerMethod) {
    customResponses =
        AnnotatedElementUtils.getAllMergedAnnotations(handlerMethod.getMethod(), Response.class);

    for (Response customResponse : customResponses) {
      if (usesAbstraction(customResponse)) {
        ApiResponse apiResponse = new ApiResponse();
        getContent(customResponse).ifPresent(apiResponse::content);
        getHeaders(customResponse.headers(), null).ifPresent(apiResponse::headers);
        getDescription(customResponse).ifPresent(apiResponse::description);
        operation.responses(
            operation.getResponses().addApiResponse(customResponse.responseCode(), apiResponse));
      }
    }

    return operation;
  }

  private boolean usesAbstraction(Response response) {
    Content defaultContent =
        org.springdoc.core.fn.builders.content.Builder.contentBuilder().build();

    return response.content().equals(defaultContent);
  }

  private Set<Response> getCustomResponses() {
    return customResponses;
  }

  private void setCustomResponses(Set<Response> responses) {
    customResponses = responses;
  }
}
