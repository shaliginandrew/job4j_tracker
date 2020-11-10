package ru.job4j.tracker;

import java.sql.SQLException;

public class CreateAction implements UserAction {
    @Override
    public String name() {
        return "=== Create a new Item ====";
    }

    @Override
    public boolean execute(Input input, Store tracker) throws SQLException {
        String name = input.askStr("Enter name: ");
        Item item = new Item(name);
        tracker.add(item);
        System.out.println("Новая заявка создана");
        return true;
    }
}
