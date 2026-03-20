package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.Resume;

import java.util.Collections;
import java.util.List;

public abstract class AbstractStorage<T> implements Storage {

    protected abstract T getIndex(String uuid);

    protected abstract boolean isExist(T index);

    protected abstract void insertElement(Resume resume, T index);

    protected abstract Resume getElement(T index);

    protected abstract void updateElement(Resume resume, T index);

    protected abstract void deleteElement(T index);

    protected abstract List<Resume> getList();

    @Override
    public void save(Resume resume) {
        T index = getIndex(resume.getUuid());
        if (isExist(index)) {
            throw new ExistStorageException(resume.getUuid());
        }
        insertElement(resume, index);
    }

    @Override
    public Resume get(String uuid) {
        T index = getIndex(uuid);
        if (!isExist(index)) throw new NotExistStorageException(uuid);
        return getElement(index);
    }

    @Override
    public void update(Resume resume) {
        T index = getIndex(resume.getUuid());
        if (!isExist(index)) throw new NotExistStorageException(resume.getUuid());
        updateElement(resume, index);
    }

    @Override
    public void delete(String uuid) {
        T index = getIndex(uuid);
        if (!isExist(index)) throw new NotExistStorageException(uuid);
        deleteElement(index);
    }

    @Override
    public List<Resume> getAllSorted() {
        List<Resume> list = getList();
        Collections.sort(list);
        return list;
    }
}
