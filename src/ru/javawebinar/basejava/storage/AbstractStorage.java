package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.Resume;

import java.util.Comparator;
import java.util.List;

public abstract class AbstractStorage implements Storage {

    protected abstract Resume doGet(Object key);

    protected abstract boolean isExist(Object key);

    protected abstract void saveKey(Resume resume, Object key);

    protected abstract Object getSearchKey(String uuid);

    protected abstract void updateKey(Resume resume, Object key);

    protected abstract void deleteKey(Object key);

    protected abstract List<Resume> getList();

    @Override
    public void save(Resume resume) {
        Object key = getNotExistedKey(resume.getUuid());
        saveKey(resume, key);
    }

    @Override
    public Resume get(String uuid) {
        Object key = getExistedKey(uuid);
        return doGet(key);
    }

    @Override
    public void update(Resume resume) {
        Object key = getExistedKey(resume.getUuid());
        updateKey(resume, key);
    }

    @Override
    public void delete(String uuid) {
        Object key = getExistedKey(uuid);
        deleteKey(key);
    }

    private Object getNotExistedKey (String uuid) {
        Object key = getSearchKey(uuid);
        if (isExist(key)) {
            throw new ExistStorageException(uuid);
        }
        return key;
    }

    private Object getExistedKey (String uuid) {
        Object key = getSearchKey(uuid);
        if (!isExist(key)) {
            throw new NotExistStorageException(uuid);
        }
        return key;
    }

    @Override
    public List<Resume> getAllSorted() {
        List<Resume> list = getList();
        list.sort(Comparator.comparing(Resume::getUuid).thenComparing(Resume::getFullName));
        return list;
    }
}
