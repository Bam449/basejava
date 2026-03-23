package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.Resume;

import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;

public abstract class AbstractStorage<T> implements Storage {
    private static final Logger LOG = Logger.getLogger(AbstractStorage.class.getName());

    protected abstract T getIndex(String uuid);

    protected abstract boolean isExist(T index);

    protected abstract void insertElement(Resume resume, T index);

    protected abstract Resume getElement(T index);

    protected abstract void updateElement(Resume resume, T index);

    protected abstract void deleteElement(T index);

    protected abstract List<Resume> getList();

    @Override
    public void save(Resume resume) {
        LOG.info("Save " + resume);
        T index = getNotExistedKey(resume.getUuid());
        insertElement(resume, index);
    }

    @Override
    public Resume get(String uuid) {
        LOG.info("Get " + uuid);
        T index = getExistedKey(uuid);
        return getElement(index);
    }

    @Override
    public void update(Resume resume) {
        LOG.info("Update " + resume);
        T index = getExistedKey(resume.getUuid());
        updateElement(resume, index);
    }

    @Override
    public void delete(String uuid) {
        LOG.info("Delete " + uuid);
        T index = getExistedKey(uuid);
        deleteElement(index);
    }

    @Override
    public List<Resume> getAllSorted() {
        LOG.info("GetAllSorted");
        List<Resume> list = getList();
        list.sort(Comparator.comparing(Resume::getFullName).thenComparing(Resume::getUuid));
        return list;
    }

    private T getNotExistedKey(String uuid) {
        T key = getIndex(uuid);
        if (isExist(key)) {
            throw new ExistStorageException(uuid);
        }
        return key;
    }

    private T getExistedKey(String uuid) {
        T key = getIndex(uuid);
        if (!isExist(key)) {
            throw new NotExistStorageException(uuid);
        }
        return key;
    }
}
