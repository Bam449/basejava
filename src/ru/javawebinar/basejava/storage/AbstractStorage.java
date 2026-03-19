package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.Resume;

public abstract class AbstractStorage implements Storage {

    protected abstract Integer getIndex(String uuid);

    protected abstract boolean isExist(Integer index);

    protected abstract void insertElement(Resume resume, Integer index);

    protected abstract Resume getElement(Integer index);

    protected abstract void updateElement(Resume resume, Integer index);

    protected abstract void deleteElement(Integer index);

    @Override
    public void save(Resume resume) {
        Integer index = getIndex(resume.getUuid());
        if (isExist(index)) {
            throw new ExistStorageException(resume.getUuid());
        }
        insertElement(resume, index);
    }

    @Override
    public Resume get(String uuid) {
        Integer index = getIndex(uuid);
        if (!isExist(index)) throw new NotExistStorageException(uuid);
        return getElement(index);
    }

    @Override
    public void update(Resume resume) {
        Integer index = getIndex(resume.getUuid());
        if (!isExist(index)) throw new NotExistStorageException(resume.getUuid());
        updateElement(resume, index);
    }

    @Override
    public void delete(String uuid) {
        Integer index = getIndex(uuid);
        if (!isExist(index)) throw new NotExistStorageException(uuid);
        deleteElement(index);
    }
}
