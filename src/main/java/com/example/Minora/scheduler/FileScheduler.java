package com.example.Minora.scheduler;

import com.example.Minora.service.EventService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.nio.file.Paths;
@Component
public class FileScheduler {
    private final EventService eventService;

    @Value("${xml.source.path}")
    private String source;
    @Value("${xml.backup.path}")
    private String backup;

    @Value("${scheduler.fixedDelay}")
    private long fixedDelay;

    public FileScheduler(EventService eventService) {
        this.eventService = eventService;
    }

    // Issue will be started every 10 minutes
    //@Scheduled(fixedRate = 60000)  // 600000 milliseconds = 10 minutes
    //@Scheduled(fixedDelay = 60000)
    @Scheduled(fixedDelayString = "${scheduler.fixedDelay}")
    public void loadDataFromXmlPeriodically() {
        System.out.println(" +++++++++++ Sheduler start to work!!! +++++++++++++");
        try {
            Path sourcePath = Paths.get(source);
            eventService.readXmlAndSaveToDatabase(sourcePath);
            Path backupPath = Paths.get(backup);
            eventService.moveFileToBackup(sourcePath, backupPath);

            System.out.println("Data loaded successfully at " + System.currentTimeMillis());
        } catch (Exception e) {
            System.err.println("Error during scheduled file processing: " + e.getMessage());
        }
    }
}
