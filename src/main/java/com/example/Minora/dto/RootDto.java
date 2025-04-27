package com.example.Minora.dto;

import jakarta.xml.bind.annotation.*;
import lombok.Data;

import java.util.List;

@Data
@XmlRootElement(name = "root")
@XmlAccessorType(XmlAccessType.FIELD)
public class RootDto {
    private RequestDetailsDto requestDetails;

    @XmlElementWrapper(name = "events")
    @XmlElement(name = "event")
    private List<EventDto> events;

}
