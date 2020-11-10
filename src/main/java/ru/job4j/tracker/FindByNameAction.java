package ru.job4j.tracker;

import java.util.List;

public class FindByNameAction implements UserAction {
    @Override
    public String name() {
        return "=== Find items by name ====";
    }

    @Override
    public boolean execute(Input input, Store tracker) {
        String name = input.askStr("Enter name: ");
        List<Item> items = tracker.findByName(name);
        if (items.size() > 0) {
            for (Item item : tracker.findByName(name)) {
                System.out.println("id:" + item.getId() + ", name:" + item.getName());
            }
        } else {
            System.out.println("=== Заявка не найдена ===");
        }
        return true;
    }
}
