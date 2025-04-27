package com.example.Minora.dto;

import jakarta.xml.bind.annotation.*;
import lombok.Data;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class RequestDetailsDto {
    private String id;
    private String acceptDate;
    private String sourceCompany;

}
