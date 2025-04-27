package com.example.Minora.dto;

import jakarta.xml.bind.annotation.*;
import lombok.Data;

import java.util.List;
@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class EventDto {
    private String id;
    private String type;
    private String insuredId;

    @XmlElementWrapper(name = "products")
    @XmlElement(name = "product")
    private List<ProductDto> products;


}
