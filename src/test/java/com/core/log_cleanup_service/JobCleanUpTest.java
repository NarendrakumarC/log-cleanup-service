package com.core.log_cleanup_service;

import com.core.log_cleanup_service.service.LogCleanUpService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class JobCleanUpTest {
    @Autowired
    private LogCleanUpService service;

    @Test
    void testCleanUpExecution(){
        service.cleanOldLogs();
    }
}
