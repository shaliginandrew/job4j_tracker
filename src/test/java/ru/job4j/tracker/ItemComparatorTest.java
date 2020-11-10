package ru.job4j.tracker;
import org.junit.Test;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import static org.junit.Assert.assertThat;
import static org.hamcrest.core.Is.is;


public class ItemComparatorTest {
    @Test
    public void sort() {
        List<Item> items = Arrays.asList(new Item("b"), new Item("a"), new Item("c"));
        System.out.println(items);
        Collections.sort(items, new ItemComparator());
        List<Item> expected = Arrays.asList(new Item("a"), new Item("b"), new Item("c"));
        System.out.println(items);
        assertThat(items, is(expected));
    }

    @Test
    public void sortReverse() {
        List<Item> items = Arrays.asList(new Item("b"), new Item("a"), new Item("c"));
        System.out.println(items);
        Collections.sort(items, new ItemComparatorReverse());
        List<Item> expected = Arrays.asList(new Item("c"), new Item("b"), new Item("a"));
        System.out.println(items);
        assertThat(items, is(expected));
    }
}
