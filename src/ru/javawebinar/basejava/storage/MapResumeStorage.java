package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapResumeStorage extends AbstractStorage{
    Map<String, Resume> storage = new HashMap<>();

    @Override
    protected Resume doGet(Object key) {
        return (Resume) key;
    }

    @Override
    protected boolean isExist(Object key) {
        return key != null;
    }

    @Override
    protected void saveKey(Resume resume, Object key) {
        storage.putIfAbsent(resume.getUuid(), resume);
    }

    @Override
    protected Object getSearchKey(String uuid) {
        return storage.get(uuid);
    }

    @Override
    protected void updateKey(Resume resume, Object key) {
        storage.put(resume.getUuid(), resume);
    }

    @Override
    protected void deleteKey(Object key) {
        storage.remove(((Resume)key).getUuid());
    }

    @Override
    protected List<Resume> getList() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    public int size() {
        return storage.size();
    }
}
