package ru.job4j.tracker;

public class ReplaceAction implements UserAction {
    @Override
    public String name() {
        return "=== Edit item ====";
    }

    @Override
    public boolean execute(Input input, Store tracker) {
        String id = input.askStr("Enter id: ");
        String name = input.askStr("Enter name for replace: ");
        Item item = new Item(name);
        if (tracker.replace(id, item)) {
            System.out.println("Замена произведена");
        } else {
            System.out.println("Замена не произведена, заявка с id:" + id + " не найдена");
        }
        return true;
    }
}
