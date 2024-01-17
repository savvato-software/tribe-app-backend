package com.savvato.tribeapp.dto;

import lombok.Builder;

@Builder
public class GenericMessageDTO {
       public String responseMessage;

       public Boolean booleanMessage;

       public Iterable<String> iterableMessage;


}
