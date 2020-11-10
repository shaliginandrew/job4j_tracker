package ru.job4j.tracker;

import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class SqlTracker implements Store {

    private Connection cn;

    public SqlTracker(Connection cn) {
        this.cn = cn;
    }

    public void init() {
        try (InputStream in = SqlTracker.class.getClassLoader().getResourceAsStream("app.properties")) {
            Properties config = new Properties();
            config.load(in);
            Class.forName(config.getProperty("driver-class-name"));
            cn = DriverManager.getConnection(
                    config.getProperty("url"),
                    config.getProperty("username"),
                    config.getProperty("password")
            );
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void close() throws Exception {
        if (cn != null) {
            cn.close();
        }
    }

    @Override
    public Item add(Item item) throws SQLException {
        try (PreparedStatement ps = cn.prepareStatement("insert into items (name) values (?)",
                Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, item.getName());
            ps.execute();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    item.setId(String.valueOf(keys.getInt(1)));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return item;
    }

    @Override
    public boolean replace(String id, Item item) {
        boolean result = false;
        try (PreparedStatement ps = cn.prepareStatement("update items set name = (?) where id = (?)")) {
            ps.setString(1, item.getName());
            ps.setInt(2, Integer.parseInt(id));
            if (ps.executeUpdate() != 0) {
                result = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public boolean delete(String id) {
        boolean result = false;
        try (PreparedStatement ps = cn.prepareStatement("delete from items where id = (?)")) {
            ps.setInt(1, Integer.parseInt(id));
            if (ps.executeUpdate() != 0) {
                result = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }


    @Override
    public List<Item> findAll() {
        List<Item> items = new ArrayList<>();
        try (PreparedStatement ps = cn.prepareStatement("select *from items")) {
            try (ResultSet rsl = ps.executeQuery()) {
                while (rsl.next()) {
                    Item item = new Item(rsl.getString(2));
                    item.setId(String.valueOf(rsl.getInt(1)));
                    items.add(item);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }

    @Override
    public List<Item> findByName(String key) {
        List<Item> items = new ArrayList<>();
        try (PreparedStatement ps = cn.prepareStatement("select id, name from items where name = (?)")) {
            ps.setString(1, key);
            try (ResultSet rsl = ps.executeQuery()) {
                while (rsl.next()) {
                    Item item = new Item(rsl.getString(2));
                    item.setId(String.valueOf(rsl.getInt(1)));
                    items.add(item);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }

    @Override
    public Item findById(String id) {
        Item item = null;
        try (PreparedStatement ps = cn.prepareStatement("select *from items where id = (?)")) {
            ps.setInt(1, Integer.parseInt(id));
            ResultSet rsl = ps.executeQuery();
            if (rsl.next()) {
                item = new Item(rsl.getString(2));
                item.setId(String.valueOf(rsl.getInt(1)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return item;
    }

    public static void main(String[] args) {
        Input validate = new ValidateInput(
                new ConsoleInput()
        );
        try (Store tracker = new SqlTracker(null)) {
            tracker.init();
            List<UserAction> actions = new ArrayList<>();
            actions.add(new CreateAction());
            actions.add(new FindAllAction());
            actions.add(new ReplaceAction());
            actions.add(new DeleteAction());
            actions.add(new FindByIdAction());
            actions.add(new FindByNameAction());
            actions.add(new ExitAction());
            new StartUI().init(validate, tracker, actions);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}