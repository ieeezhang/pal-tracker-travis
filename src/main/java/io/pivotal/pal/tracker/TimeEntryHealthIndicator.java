package io.pivotal.pal.tracker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TimeEntryHealthIndicator implements HealthIndicator {

    @Autowired
    TimeEntryRepository repository;

    @Override
    public Health health() {
        int errorCode = check(); // perform some specific health check
        if (errorCode != 0) {
            return Health.down().withDetail("Error Code", errorCode).build();
        }
        return Health.up().build();
    }

    private int check() {
        List<TimeEntry> timeEntryList = repository.list();
        return timeEntryList == null || timeEntryList.size() < 5 ? 0 : -1;
    }

}
