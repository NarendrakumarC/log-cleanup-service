package com.core.log_cleanup_service.service;

import com.core.log_cleanup_service.exception.CleanUpException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.FileTime;
import java.time.Instant;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

@Service
public class LogCleanUpService {

    @Value("${cleanup.log-directory}")
    private String logDirectory;

    @Value("${cleanup.days-to-keep}")
    private int daysToKeep;

    public void init(){
        // Initialize any resources if needed
        validateLogDirectory(logDirectory);
    }

    private void validateLogDirectory(String path) {
        if(path == null || path.isBlank()) {
            throw new CleanUpException("Invalid log directory");
        }
        if(path.contains("..")) {
            throw new CleanUpException("Directory traversal not allowed");
        }
        try {
            Files.createDirectories(Paths.get(path));
        } catch (IOException e) {
            throw new CleanUpException("Cannot create directory: " + path, e);
        }
    }

    @Scheduled(cron = "${cleanup.cron-expression}")
    public void cleanOldLogs(){
        Instant cutoff = Instant.now().minus(daysToKeep, java.time.temporal.ChronoUnit.DAYS);
        AtomicInteger count = new AtomicInteger();
        try{
            Stream<Path> files = Files.list(Paths.get(logDirectory));
            files.filter(f -> f.toString().endsWith(".log"))
                    .forEach(f ->{ try{
                        FileTime time =Files.getLastModifiedTime(f);
                        if(time.toInstant().isBefore(cutoff)){
                            Files.delete(f);
                            count.incrementAndGet();
                        }
                    }catch (IOException e) {
                        // Log error and continue
                        throw new CleanUpException("File delete error: ", e);
                    }
                    });

        } catch (IOException e) {
            throw new CleanUpException("Directory read error: ", e);
        }
        createCleanUpLog(count.get(),cutoff);
    }

    private void createCleanUpLog(int deleted, Instant cutoff) {
        String fileName = "cleanup-" + Instant.now().toString().replace(":", "-") + ".log";
        Path file = Paths.get(logDirectory, fileName);
        String log = """
        ======= LOG CLEANUP OPERATION =======
        Timestamp: %s
        Operation: Scheduled Log Cleanup
        Files Deleted: %d
        Cutoff Date: %s
        Retention Policy: Keep logs for %d days
        Status: Success
        ======================================
        """.formatted(Instant.now(), deleted, cutoff, daysToKeep);
        try {
            Files.writeString(file, log,
            StandardOpenOption.CREATE,
            StandardOpenOption.APPEND);
        } catch (IOException e) {
            throw new CleanUpException("Log write failed: ", e);
        }
    }


}
