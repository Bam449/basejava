package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.Resume;

import java.util.Comparator;
import java.util.List;

public abstract class AbstractStorage <T> implements Storage {

    protected abstract Resume doGet(T key);

    protected abstract boolean isExist(T key);

    protected abstract void saveKey(Resume resume, T key);

    protected abstract T getSearchKey(String uuid);

    protected abstract void updateKey(Resume resume, T key);

    protected abstract void deleteKey(T key);

    protected abstract List<Resume> getList();

    @Override
    public void save(Resume resume) {
        T key = getNotExistedKey(resume.getUuid());
        saveKey(resume, key);
    }

    @Override
    public Resume get(String uuid) {
        T key = getExistedKey(uuid);
        return doGet(key);
    }

    @Override
    public void update(Resume resume) {
        T key = getExistedKey(resume.getUuid());
        updateKey(resume, key);
    }

    @Override
    public void delete(String uuid) {
        T key = getExistedKey(uuid);
        deleteKey(key);
    }

    private T getNotExistedKey (String uuid) {
        T key = getSearchKey(uuid);
        if (isExist(key)) {
            throw new ExistStorageException(uuid);
        }
        return key;
    }

    private T getExistedKey (String uuid) {
        T key = getSearchKey(uuid);
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
