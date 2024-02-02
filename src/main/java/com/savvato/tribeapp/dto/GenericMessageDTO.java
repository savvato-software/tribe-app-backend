package com.savvato.tribeapp.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;

@Builder
@JsonDeserialize(builder = GenericMessageDTO.GenericMessageDTOBuilder.class)
public class GenericMessageDTO {
       public String responseMessage;

       public Boolean booleanMessage;

       public Iterable<String> iterableMessage;


}
