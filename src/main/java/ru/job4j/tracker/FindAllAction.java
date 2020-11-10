package ru.job4j.tracker;

import java.util.List;

public class FindAllAction implements UserAction {
    @Override
    public String name() {
        return "=== List of all Items ====";
    }

    @Override
    public boolean execute(Input input, Store tracker) {
        List<Item> items = tracker.findAll();
        if (items.size() > 0) {
            for (Item item : tracker.findAll()) {
                System.out.println("id:" + item.getId() + ", name:" + item.getName());

            }
        } else {
            System.out.println("Список пуст");
        }
        return true;
    }
}
