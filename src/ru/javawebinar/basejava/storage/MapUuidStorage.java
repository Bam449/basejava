package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.HashMap;
import java.util.Map;

public class MapUuidStorage extends AbstractStorage{
    Map<String, Resume> storage = new HashMap<>();

    @Override
    protected Resume doGet(Object uuid) {
        return storage.get((String) uuid);
    }

    @Override
    protected boolean isExist(Object key) {
        return storage.containsKey((String) key);
    }

    @Override
    protected void saveKey(Resume resume, Object key) {
        storage.putIfAbsent((String) key, resume);
    }

    @Override
    protected Object getSearchKey(String key) {
        return key;
    }

    @Override
    protected void updateKey(Resume resume, Object key) {
        storage.put((String) key, resume);
    }

    @Override
    protected void deleteKey(Object key) {
        storage.remove((String) key);
    }

    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    public Resume[] getAll() {
        return storage.values().toArray(new Resume[0]);
    }

    @Override
    public int size() {
        return storage.size();
    }
}
