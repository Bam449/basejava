package ru.javawebinar.basejava.model;

import java.util.Arrays;
import java.util.List;

public class ListSection extends Section{

    private List<String> items;

    public ListSection(List<String> items) {
        this.items = items;
    }

    public ListSection(String ... items) {
        this(Arrays.asList(items));
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        ListSection that = (ListSection) o;
        return items.equals(that.items);
    }

    @Override
    public int hashCode() {
        return items.hashCode();
    }
}
