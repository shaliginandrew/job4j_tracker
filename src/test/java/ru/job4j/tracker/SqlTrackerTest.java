package ru.job4j.tracker;

import org.hamcrest.Matchers;
import org.junit.Test;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;

public class SqlTrackerTest {

    public Connection init() {
        try (InputStream in = SqlTracker.class.getClassLoader().getResourceAsStream("app.properties")) {
            Properties config = new Properties();
            config.load(in);
            Class.forName(config.getProperty("driver-class-name"));
            return DriverManager.getConnection(
                    config.getProperty("url"),
                    config.getProperty("username"),
                    config.getProperty("password")

            );
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }


    @Test
    public void createItem() throws Exception {
        try (Store tracker = new SqlTracker(ConnectionRollback.create(this.init()))) {
            tracker.add(new Item("Test005"));
            assertThat(tracker.findByName("Test005").size(), is(1));
        }
    }

    @Test
    public void replaceItem() throws Exception {
        try (Store tracker = new SqlTracker(ConnectionRollback.create(this.init()))) {
            Item bug = new Item("Bug");
            tracker.add(bug);
            String id = bug.getId();
            Item bugWithDesc = new Item("Bug with description");
            tracker.replace(id, bugWithDesc);
            assertThat(tracker.findById(id).getName(), is("Bug with description"));
        }
    }

    @Test
    public void deleteItem() throws Exception {
        try (Store tracker = new SqlTracker(ConnectionRollback.create(this.init()))) {
            Item bug = new Item("Bug");
            tracker.add(bug);
            String id = bug.getId();
            tracker.delete(id);
            assertThat(tracker.findById(id), Matchers.is(nullValue()));
        }
    }

    @Test
    public void findeAllItem() throws Exception {
        try (Store tracker = new SqlTracker(ConnectionRollback.create(this.init()))) {
            Item bug1 = new Item("Bug1");
            Item bug2 = new Item("Bug2");
            Item bug3 = new Item("Bug3");
            tracker.add(bug1);
            tracker.add(bug2);
            tracker.add(bug2);
            assertThat(tracker.findAll().size(), Matchers.is(9));
        }
    }

    @Test
    public void findebyName() throws Exception {
        try (Store tracker = new SqlTracker(ConnectionRollback.create(this.init()))) {
            Item bug1 = new Item("BugName");
            Item bug2 = new Item("BugName");
            tracker.add(bug1);
            tracker.add(bug2);
            assertThat(tracker.findByName("BugName").size(), Matchers.is(2));
        }
    }

    @Test
    public void findebyId() throws Exception {
        try (Store tracker = new SqlTracker(ConnectionRollback.create(this.init()))) {
            Item bug = new Item("BugId");
            tracker.add(bug);
            assertThat(tracker.findById(bug.getId()), Matchers.is(bug));
        }
    }
}
