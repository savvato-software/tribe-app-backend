package com.savvato.tribeapp.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;

@Builder
@JsonDeserialize(builder = GenericMessageDTO.GenericMessageDTOBuilder.class)
public class GenericMessageDTO {
       public String responseMessage;

       @JsonPOJOBuilder(withPrefix = "")
       public static class GenericMessageDTOBuilder {
              // Lombok will automatically implement the builder
       }
}
