package io.pivotal.pal.tracker;

import org.springframework.context.annotation.Bean;

import javax.validation.constraints.Null;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class InMemoryTimeEntryRepository implements TimeEntryRepository {

    private List<TimeEntry> timeEntries = new ArrayList<>();
    private int currentId = 0;

    public TimeEntry create(TimeEntry timeEntry) {
        currentId++;

        TimeEntry newTimeEntry = new TimeEntry(currentId, timeEntry.getProjectId(), timeEntry.getUserId(), timeEntry.getDate(), timeEntry.getHours());
        timeEntries.add(newTimeEntry);

        return newTimeEntry;
    }

    public TimeEntry find(long id) {
        for(int i = 0; i < timeEntries.size(); i++){
            if(timeEntries.get(i).getId() == id){
                return timeEntries.get(i);
            }
        }
        return null;
    }

    public List<TimeEntry> list() {

        return timeEntries;
    }

    public TimeEntry update(long id, TimeEntry timeEntry) {
        TimeEntry tm = this.find(id);
        tm.setDate(timeEntry.getDate());
        tm.setHours(timeEntry.getHours());
        tm.setProjectId(timeEntry.getProjectId());
        tm.setUserId(timeEntry.getUserId());
        return tm;
    }

    public TimeEntry delete(long id) {
        TimeEntry deletedTimeEntry = this.find(id);
        timeEntries.remove(deletedTimeEntry);
        return deletedTimeEntry;
    }
}
