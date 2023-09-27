package com.savvato.tribeapp.controllers.annotations.config.customizers;

import com.savvato.tribeapp.controllers.annotations.requests.DocumentedRequestBody;
import com.savvato.tribeapp.controllers.annotations.responses.Response;
import com.savvato.tribeapp.controllers.annotations.config.utils.RequestAnnotationUtils;
import com.savvato.tribeapp.controllers.annotations.config.utils.ResponseAnnotationUtils;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.responses.ApiResponse;
import java.util.HashSet;
import java.util.Set;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;

@Component
public class OperationCustomizer implements org.springdoc.core.customizers.OperationCustomizer {
  protected Set<Response> customResponses = new HashSet<>();
  protected DocumentedRequestBody customRequest;

  @Override
  public Operation customize(Operation operation, HandlerMethod handlerMethod) {
    customResponses =
        AnnotatedElementUtils.getAllMergedAnnotations(handlerMethod.getMethod(), Response.class);
    customRequest = handlerMethod.getMethodAnnotation(DocumentedRequestBody.class);
    handleCustomRequestBody(operation);
    handleCustomResponses(operation);
    return operation;
  }

  private void handleCustomRequestBody(Operation operation) {
    if (usesAbstraction(customRequest)) {
      RequestBody requestBody = new RequestBody();
      RequestAnnotationUtils.getContent(customRequest).ifPresent(requestBody::content);
      RequestAnnotationUtils.getDescription(customRequest).ifPresent(requestBody::description);
      operation.requestBody(requestBody);
    }
  }

  private void handleCustomResponses(Operation operation) {
    for (Response customResponse : customResponses) {
      if (usesAbstraction(customResponse)) {
        ApiResponse apiResponse = new ApiResponse();
        ResponseAnnotationUtils.getContent(customResponse).ifPresent(apiResponse::content);
        ResponseAnnotationUtils.getHeaders(customResponse.headers(), null)
            .ifPresent(apiResponse::headers);
        ResponseAnnotationUtils.getDescription(customResponse).ifPresent(apiResponse::description);
        operation.responses(
            operation.getResponses().addApiResponse(customResponse.responseCode(), apiResponse));
      }
    }
  }

  private boolean usesAbstraction(Response response) {
    Content defaultContent =
        org.springdoc.core.fn.builders.content.Builder.contentBuilder().build();

    return response.content().equals(defaultContent);
  }

  private boolean usesAbstraction(DocumentedRequestBody requestBody) {
    if (requestBody == null) return false;
    Content defaultContent =
        org.springdoc.core.fn.builders.content.Builder.contentBuilder().build();

    return requestBody.content().equals(defaultContent);
  }

  private Set<Response> getCustomResponses() {
    return customResponses;
  }

  private void setCustomResponses(Set<Response> responses) {
    customResponses = responses;
  }
}
