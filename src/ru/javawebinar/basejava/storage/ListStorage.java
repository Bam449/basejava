package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.ArrayList;
import java.util.List;

public class ListStorage extends AbstractStorage {
    List<Resume> storage = new ArrayList<>();

    @Override
    protected Object getSearchKey(String uuid)  {
        for (int i = 0; i < storage.size(); i++) {
            if (storage.get(i).getUuid().equals(uuid)) {
                return i;
            }
        }
        return null;
    }

    @Override
    protected boolean isExist(Object key) {
        return key != null;
    }

    @Override
    protected void saveKey(Resume resume, Object key) {
        storage.add(resume);
    }

    @Override
    protected Resume doGet(Object key) {
        return storage.get((int)key);
    }

    @Override
    protected void updateKey(Resume resume, Object key) {
        storage.add((int)key, resume);
    }

    @Override
    protected void deleteKey(Object key) {
        storage.remove((int)key);
    }

    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    public List <Resume> getList() {
        return new ArrayList<>(storage);
    }

    @Override
    public int size() {
        return storage.size();
    }
}
