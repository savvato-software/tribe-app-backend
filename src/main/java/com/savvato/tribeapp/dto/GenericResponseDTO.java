package com.savvato.tribeapp.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;

@Builder
@JsonDeserialize(builder = GenericResponseDTO.GenericResponseDTOBuilder.class)
public class GenericResponseDTO {
       public String responseMessage;

       public Boolean booleanMessage;

       public Iterable<String> iterableMessage;


}
