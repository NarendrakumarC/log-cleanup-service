package com.core.log_cleanup_service.controller;

import com.core.log_cleanup_service.service.LogCleanUpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cleanup")
public class CleanUpController {
    @Autowired
    private final LogCleanUpService logCleanUpService;

    public CleanUpController(LogCleanUpService logCleanUpService) {
        this.logCleanUpService = logCleanUpService;
    }

    @PostMapping("/run")
    public ResponseEntity<Void> triggerCleanUp() {
        logCleanUpService.cleanOldLogs();
        return ResponseEntity.noContent().build();
    }
}
