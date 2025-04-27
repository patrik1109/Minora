package com.example.Minora.config;

import com.example.Minora.entity.Event;
import com.example.Minora.entity.Product;
import com.example.Minora.entity.Request;
import com.example.Minora.dto.EventDto;
import com.example.Minora.dto.ProductDto;
import com.example.Minora.dto.RootDto;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class Mapper {
    public static Request mapToEntity(RootDto rootDto) {
        Request request = Request.builder()
                .id(rootDto.getRequestDetails().getId())
                .acceptDate(LocalDateTime.parse(rootDto.getRequestDetails().getAcceptDate().replace(" ", "T")))
                .sourceCompany(rootDto.getRequestDetails().getSourceCompany())
                .build();

        List<Event> events = rootDto.getEvents().stream()
                .map(eventDto -> mapEvent(eventDto, request))
                .collect(Collectors.toList());

        request.setEvents(events);

        return request;
    }

    private static Event mapEvent(EventDto dto, Request request) {
        Event event = Event.builder()
                .id(dto.getId())
                .type(dto.getType())
                .insuredId(dto.getInsuredId())
                .request(request)
                .build();

        List<Product> products = dto.getProducts().stream()
                .map(prodDto -> mapProduct(prodDto, event))
                .collect(Collectors.toList());

        event.setProducts(products);

        return event;
    }

    private static Product mapProduct(ProductDto dto, Event event) {
        return Product.builder()
                .type(dto.getType())
                .price(dto.getPrice())
                .startDate(LocalDate.parse(dto.getStartDate().toString()))
                .endDate(LocalDate.parse(dto.getEndDate().toString()))
                .event(event)
                .build();
    }
}
