package ru.job4j.tracker;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StartUI {


    public void init(Input input, Store tracker, List<UserAction> actions) throws SQLException {
        boolean run = true;
        while (run) {
            this.showMenu(actions);
            int select = input.askInt("Select: ", actions.size());
                UserAction action = actions.get(select);
                run = action.execute(input, tracker);
        }
    }

    private void showMenu(List<UserAction> actions) {
        System.out.println("Menu:");
            for (int index = 0; index < actions.size(); index++) {
            System.out.println(index + ". " + actions.get(index).name());
        }
    }

    public static void main(String[] args) throws SQLException {
        List<UserAction> actions = new ArrayList<>();
        Input input = new ConsoleInput();
        Input validate = new ValidateInput(input);
        Store tracker = new MemTracker();
        actions.add(new CreateAction());
        actions.add(new FindAllAction());
        actions.add(new ReplaceAction());
        actions.add(new DeleteAction());
        actions.add(new FindByIdAction());
        actions.add(new FindByNameAction());
        actions.add(new ExitAction());
        new StartUI().init(validate, tracker, actions);
    }
}