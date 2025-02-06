package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exeption.ExistStorageException;
import ru.javawebinar.basejava.exeption.NotExistStorageException;
import ru.javawebinar.basejava.model.Resume;

import java.util.Collections;
import java.util.List;

public abstract class AbstractStorage <SK> implements Storage {

    protected abstract SK getSearchKey(String uuid);

    protected abstract boolean isExist(SK searchKey);

    protected abstract void doSave(Resume r, SK searchKey);

    protected abstract Resume doGet(SK searchKey);

    protected abstract void doUpdate(Resume r, SK searchKey);

    protected abstract void doDelete(SK searchKey);

    protected abstract List<Resume> doCopyAll();


    public void save(Resume r) {
        SK searchKey = getSearchKey(r.getUuid());
        if (isExist(searchKey)) throw new ExistStorageException(r.getUuid());
        doSave(r, searchKey);
    }

    public Resume get(String uuid) {
        SK searchKey = getSearchKey(uuid);
        if (!isExist(searchKey)) throw new NotExistStorageException(uuid);
        return doGet(searchKey);
    }

    public void update(Resume r) {
        SK searchKey = getSearchKey(r.getUuid());
        if (!isExist(searchKey)) throw new NotExistStorageException(r.getUuid());
        doUpdate(r, searchKey);
    }

    public void delete(String uuid) {
        SK searchKey = getSearchKey(uuid);
        if (!isExist(searchKey)) throw new NotExistStorageException(uuid);
        doDelete(searchKey);
    }

    @Override
    public List<Resume> getAllSorted() {
        List<Resume> list = doCopyAll();
        Collections.sort(list);
        return list;
    }
}
