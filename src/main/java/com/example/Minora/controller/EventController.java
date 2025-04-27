package com.example.Minora.controller;

import com.example.Minora.dto.CompanyProductsDTO;
import com.example.Minora.entity.Event;
import com.example.Minora.service.EventService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;

@RestController
@RequestMapping("/events")
public class EventController {


    private final EventService eventService;

    @Value("${xml.source.path}")
    private String source; // = Path.of("src/main/resources/Request.xml");
    @Value("${xml.backup.path}")
    private String backup; // = Path.of("src/main/resources/backup");

    public EventController( EventService eventService) {
        this.eventService =eventService;
    }


    @PostMapping ("/load")
    public ResponseEntity<String> loadDataFromXml() {
        try {
            eventService.readXmlAndSaveToDatabase(Path.of(source));
            eventService.moveFileToBackup(Path.of(source),Path.of(backup));
            return ResponseEntity.ok("Data loaded successfully");
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Failed to process XML file: " + e.getMessage());
        }

    }

    @GetMapping("/all")
    public List<Event> getAllEvents() {
        return eventService.getAllEvents();
    }

    @GetMapping("/products/{insuredId}")
    public List<CompanyProductsDTO> getProductsByInsuredId(@PathVariable String insuredId) {
        List<CompanyProductsDTO> result = eventService.getProductsGroupedBySourceCompany(insuredId);
        return result;
    }
}
