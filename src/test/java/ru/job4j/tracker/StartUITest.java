package ru.job4j.tracker;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringJoiner;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;

public class StartUITest {
    @Test
    public void whenAddItem() throws SQLException {
        String[] answers = {"Fix PC"};
        Input input = new StubInput(answers);
        Store tracker = (Store) new MemTracker();
        new CreateAction().execute(input, tracker);
        Item created = tracker.findAll().get(0);
        Item expected = new Item("Fix PC");
        assertThat(created.getName(), is(expected.getName()));
    }

    @Test
    public void whenReplaceItem() throws SQLException {
        Store tracker = (Store) new MemTracker();
        Item item = new Item("new item");
        tracker.add(item);
        String[] answers = {
                item.getId(), // id сохраненной заявки в объект tracker.
                "replaced item"
        };
        new ReplaceAction().execute(new StubInput(answers), tracker);
        Item replaced = tracker.findById(item.getId());
        assertThat(replaced.getName(), is("replaced item"));
    }
    @Test
    public void whenDeleteItem() throws SQLException {
        Store tracker = (Store) new MemTracker();
        Item item = new Item("new item");
        tracker.add(item);
        String[] answers = {
                item.getId()
        };
        new DeleteAction().execute(new StubInput(answers), tracker);
        Item deleted = tracker.findById(item.getId());
        assertThat(deleted, is(nullValue()));
    }

    @Test
    public void whenExit() throws SQLException {
        StubInput input = new StubInput(
                new String[] {"0"}
        );
        StubAction action = new StubAction();
        new StartUI().init(input, (Store) new MemTracker(), new ArrayList<UserAction>(Arrays.asList(action)));
        assertThat(action.isCall(), is(true));
    }
    @Test
    public void whenPrtMenu() throws SQLException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream def = System.out;
        System.setOut(new PrintStream(out));
        StubInput input = new StubInput(
                new String[] {"0"}
        );
        StubAction action = new StubAction();
        new StartUI().init(input, (Store) new MemTracker(), new ArrayList<UserAction>(Arrays.asList(action)));
        String expect = new StringJoiner(System.lineSeparator(), "", System.lineSeparator())
                .add("Menu:")
                .add("0. Stub action")
                .toString();
        assertThat(new String(out.toByteArray()), is(expect));
        System.setOut(def);
    }
}