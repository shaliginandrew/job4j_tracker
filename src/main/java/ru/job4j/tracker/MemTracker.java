package ru.job4j.tracker;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
/**
 * @version $Id$
 * @since 0.1
 */
public class MemTracker implements Store {
    /**
     * Массив для хранение заявок.
     */
    //private final Item[] items = new Item[100];
    private final List<Item> items = new ArrayList<>();

    /**
     * Указатель ячейки для новой заявки.
     */
    //private int position = 0;

    @Override
    public void init() {

    }

    /**
     * Метод реализующий добавление заявки в хранилище
     * @param item новая заявка
     */
    public Item add(Item item) {
        item.setId(generateId());
        //this.items[position++] = item;
        items.add(item);
        return item;
    }

    /**
     * Метод генерирует уникальный ключ для заявки.
     * Так как у заявки нет уникальности полей, имени и описание. Для идентификации нам нужен уникальный ключ.
     * @return Уникальный ключ.
     */
    private String generateId() {
        Random rm = new Random();
        return String.valueOf(Math.abs(rm.nextLong() + System.currentTimeMillis()));
    }


    public List<Item> findAll() {
         return items;
    }

    public List<Item> findByName(String key) {
        List<Item> itemsEqualNames = new ArrayList<>();
        for (int index = 0; index < items.size(); index++) {
            Item item = items.get(index);
            if (item.getName().equals(key)) {
                itemsEqualNames.add(item);
            }
        }
        return itemsEqualNames;
}

    public Item findById(String id) {
        // Находим индекс
        int index = indexOf(id);
        // Если индекс найден возвращаем item, иначе null
        return index != -1 ? items.get(index) : null;
    }

    private int indexOf(String id) {
        int rsl = -1;
        for (int index = 0; index < items.size(); index++) {
            if (items.get(index).getId().equals(id)) {
                rsl = index;
                break;
            }
        }
        return rsl;
    }


    public boolean replace(String id, Item item) {
       boolean result = false;
        int index = indexOf(id);
        if (index != -1) {
            items.set(index, item);
            item.setId(id);
            result = true;
        }
         return result;
    }

    public boolean delete(String id) {
        boolean result = false;
        int findIndex = indexOf(id);
        if (findIndex  != -1) {
            items.remove(findIndex);
            result = true;
        }
        return result;
    }

    @Override
    public void close() throws Exception {

    }
}

