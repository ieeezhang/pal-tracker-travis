package io.pivotal.pal.tracker;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class JdbcTimeEntryRepository implements TimeEntryRepository {

    private final JdbcTemplate template;

    public JdbcTimeEntryRepository(DataSource ds) {
        this.template = new JdbcTemplate(ds);
    }

    @Override
    public TimeEntry create(TimeEntry timeEntry) {
        String sql = "insert into time_entries (project_id, user_id, date, hours) values (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        template.update(
                connection -> {
                    PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
                    ps.setLong(1, timeEntry.getProjectId());
                    ps.setLong(2, timeEntry.getUserId());
                    ps.setString(3, timeEntry.getDate().toString());
                    ps.setInt(4, timeEntry.getHours());
                    return ps;
                }, keyHolder);

        Number key = keyHolder.getKey();
        timeEntry.setId(key.longValue());
        return timeEntry;
    }

    @Override
    public TimeEntry find(long id) {
        RowMapper<TimeEntry> timeEntryRowMapper = new RowMapper() {
            @Override
            public TimeEntry mapRow(ResultSet rs, int rowNum) throws SQLException {
                TimeEntry rowTimeEntry = new TimeEntry();
                rowTimeEntry.setId(rs.getLong("id"));
                rowTimeEntry.setUserId(rs.getLong("user_id"));
                rowTimeEntry.setProjectId(rs.getLong("project_id"));
                rowTimeEntry.setDate(LocalDate.parse(rs.getString("date")));
                rowTimeEntry.setHours(rs.getInt("hours"));
                return rowTimeEntry;
            }
        };

        List<TimeEntry> timeEntryList = template.query("select * from time_entries where id=?", new Object[]{id}, timeEntryRowMapper);
        if (timeEntryList.size() < 1) {
            return null;
        }
        return timeEntryList.get(0);
    }

    @Override
    public List<TimeEntry> list() {
        RowMapper<TimeEntry> timeEntryRowMapper = new RowMapper() {
            @Override
            public TimeEntry mapRow(ResultSet rs, int rowNum) throws SQLException {
                TimeEntry rowTimeEntry = new TimeEntry();
                rowTimeEntry.setId(rs.getLong("id"));
                rowTimeEntry.setUserId(rs.getLong("user_id"));
                rowTimeEntry.setProjectId(rs.getLong("project_id"));
                rowTimeEntry.setDate(LocalDate.parse(rs.getString("date")));
                rowTimeEntry.setHours(rs.getInt("hours"));
                return rowTimeEntry;
            }
        };

        List<TimeEntry> timeEntryList = template.query("select * from time_entries", timeEntryRowMapper);

        return timeEntryList;
    }

    @Override
    public TimeEntry update(long id, TimeEntry timeEntry) {
        String sql = "update time_entries set project_id=?, user_id=?, date=?, hours=? where id=?";


        int count = template.update(
                connection -> {
                    PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
                    ps.setLong(1, timeEntry.getProjectId());
                    ps.setLong(2, timeEntry.getUserId());
                    ps.setString(3, timeEntry.getDate().toString());
                    ps.setInt(4, timeEntry.getHours());
                    ps.setLong(5, id);
                    return ps;
                });

        return find(id);
    }

    @Override
    public TimeEntry delete(long id) {
        String sql = "delete from time_entries where id=?";

        int count = template.update(
                connection -> {
                    PreparedStatement ps = connection.prepareStatement(sql);
                    ps.setLong(1, id);
                    return ps;
                });

        TimeEntry timeEntry = find(id);
        return timeEntry;
    }
}
