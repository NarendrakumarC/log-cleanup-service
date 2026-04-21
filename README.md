# log-cleanup-service
Implement a service that automatically deletes log files older than 7 days.
The cleanup should run automatically on a daily schedule (configured to run at 2:00AM).
The service must handle file system operations, including listing, checking modification dates, and deleting files.


Implement the following :

Configuration setup(application.yml): You must update the application.yml file for logging and cleanup service configurations. 

Cleanup configuration

cleanup:
  log-directory: /path/to/log/files
  retention-days: 7
  schedule: "0 0 2 * * ?" # Cron expression for daily at 2:00 AM

Logging configuration

logging:
  level: INFO
  pattern : "%d{yyyy-MM-dd HH:mm:ss} - %msg%n"
  file: "%d{yyyy-MM-dd HH:mm:ss}[%thread] %-5level %logger{36}-%msg%n"

File
  name: ./logs/app.log
  max-size: 10MB
  max-history: 30

logback:
  rolling policy:
    max-file-size: 10MB
    max-history: 30

You are required to implement LogCleanupService.java

Required Annotations:

@Service
@Scheduled(cron = "0 0 2 * * ?") // Schedule to run daily at 2:00 AM

Required Methods:

1. cleanOldLogs() 
2. Inject configuration properties using @Value for cleanup.log-directory and cleanup.days-to-keep
3. Calculate the cutoff date by subtracting the retention days from the current time.
      Instant.now().minus(retentionDays, ChronoUnit.DAYS);
4. Scan the log directory and find all files with a .log extension.
5. For each log file, check its last modified time is before the cutoff date using Files.getLastModifiedTime(file.toPath()).toInstant().isBefore(cutoffDate)
6. Delete files that are older than the cutoff date
7. Count the number of files deleted
8. Call createCleanupLogEntry(deletedFilesCount, cutoff) after cleanup opertaion
9. createCleanupLogEntry(int deletedFilesCount, Instant cutoff)
10. Create a log entry with the current date in the filename : cleanup-YYYY-MM-DD.log

Write the following exact content to the file:

****** LOG CLEANUP OPERATION ********

Timestamp: [current date and time]
Operation: Scheduled Log Cleanup
Files Deleted: [number of files deleted]
Cutoff Date: [cutoff date and time]
Retention Policy: [days-to-keep] days
Status : SUCCESS

************** END ********************
