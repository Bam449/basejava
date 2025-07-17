package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.Resume;

public abstract class AbstractStorage implements Storage {

    public void save(Resume r) {
        Object searchKey = getNotExistedSearchKey(r.getUuid());
        insertElement(searchKey, r);
    }

    public Resume get(String uuid) {
        Object searchKey = getExistedSearchKey(uuid);
        return getElement(searchKey);
    }

    public void update(Resume resume) {
        Object searchKey = getExistedSearchKey(resume.getUuid());
        updateElement(searchKey, resume);
    }

    public void delete(String uuid) {
        Object searchKey = getExistedSearchKey(uuid);
        deleteElement(searchKey);
    }

    private Object getExistedSearchKey(String uuid) {
        Object searchKey = getSearchKey(uuid);
        if (!isExist(searchKey)) {
            throw new NotExistStorageException(uuid);
        }
        return searchKey;
    }

    private Object getNotExistedSearchKey(String uuid) {
        Object searchKey = getSearchKey(uuid);
        if (isExist(searchKey)) {
            throw new ExistStorageException(uuid);
        }
        return searchKey;
    }

    protected abstract boolean isExist(Object searchKey);

    protected abstract Object getSearchKey(String uuid);

    protected abstract void insertElement(Object index, Resume resume);

    protected abstract void deleteElement(Object index);

    protected abstract Resume getElement(Object searchKey);

    protected abstract void updateElement(Object searchKey, Resume resume);
}
