package io.pivotal.pal.tracker;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Null;
import java.util.List;

@RestController
public class TimeEntryController {
    private TimeEntryRepository timeEntryRepository;

    public TimeEntryController(TimeEntryRepository timeEntryRepository) {
        this.timeEntryRepository = timeEntryRepository;
    }


    @PostMapping("/time-entries")
    public ResponseEntity create(@RequestBody TimeEntry timeEntryToCreate) {
        TimeEntry createdTimeEntry = timeEntryRepository.create(timeEntryToCreate);
        return new ResponseEntity(createdTimeEntry, HttpStatus.CREATED);
    }

    @GetMapping("/time-entries/{id}")
    public ResponseEntity<TimeEntry> read(@PathVariable long id) {
        TimeEntry foundTimeEntry = timeEntryRepository.find(id);
        if (foundTimeEntry == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(foundTimeEntry, HttpStatus.OK);
    }

    @GetMapping("/time-entries")
    public ResponseEntity<List<TimeEntry>> list() {
        List<TimeEntry> listTimeEntry = timeEntryRepository.list();

        return new ResponseEntity<>(listTimeEntry, HttpStatus.OK);
    }

    @PutMapping("/time-entries/{id}")
    public ResponseEntity<TimeEntry> update(@PathVariable long id, @RequestBody TimeEntry timeEntryToUpdate) {
        TimeEntry updatedTimeEntry = timeEntryRepository.update(id, timeEntryToUpdate);
        if(updatedTimeEntry == null ){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }else {
            return new ResponseEntity<>(updatedTimeEntry, HttpStatus.OK);
        }
    }

    @DeleteMapping("/time-entries/{id}")
    public ResponseEntity<TimeEntry> delete(@PathVariable long id) {
        timeEntryRepository.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
