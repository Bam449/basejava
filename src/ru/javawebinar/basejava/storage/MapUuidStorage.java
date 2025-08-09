package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapUuidStorage extends AbstractStorage<String> {

    private final Map<String, Resume> storage = new HashMap<>();

    @Override
    protected boolean isExist(String searchKey) {
        return storage.containsKey(searchKey);
    }

    @Override
    protected String getSearchKey(String uuid) {
        return uuid;
    }

    @Override
    protected void insertElement(String searchKey, Resume resume) {
        storage.putIfAbsent(searchKey, resume);
    }

    @Override
    protected void deleteElement(String uuid) {
        storage.remove(uuid);
    }

    @Override
    protected Resume getElement(String searchKey) {
        return storage.get(searchKey);
    }

    @Override
    protected void updateElement(String searchKey, Resume resume) {
        storage.put(searchKey, resume);
    }

    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    public List <Resume> getList() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public int size() {
        return storage.size();
    }
}
