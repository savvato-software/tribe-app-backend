package com.savvato.tribeapp.dto;

import com.savvato.tribeapp.entities.Phrase;
import lombok.Builder;
import java.util.List;

// hold when data is going out
@Builder
public class AttributeDTO {
    public Phrase phrase;

}
