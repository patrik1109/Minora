package com.example.Minora.controller;



import com.example.Minora.service.EventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.*;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.file.Path;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EventController.class)
public class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private EventService eventService;

    @Value("${xml.source.path}")
    private String sourcePath;

    private Path source;

    @BeforeEach
    void setUp() {
        source = Path.of(sourcePath);
    }

    @Test
    void testLoad_Success() throws Exception {
        // Нічого не робить, імітуємо успішне зчитування
        doNothing().when(eventService).readXmlAndSaveToDatabase(source);

        mockMvc.perform(get("/events/load"))
                .andExpect(status().isOk())
                .andExpect(content().string("Data loaded successfully"));
    }

    @Test
    void testLoad_Failure() throws Exception {
        // Імітуємо помилку під час зчитування
        doThrow(new RuntimeException("Mocked error")).when(eventService). readXmlAndSaveToDatabase(source);

        mockMvc.perform(get("/events/load"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Failed to process XML file")));
    }
}
