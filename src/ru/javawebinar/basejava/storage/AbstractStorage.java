package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.Resume;

import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

public abstract class AbstractStorage <T> implements Storage {

    private static final Logger LOG = Logger.getLogger(AbstractStorage.class.getName());

    protected abstract boolean isExist(T searchKey);

    protected abstract T getSearchKey(String uuid);

    protected abstract void insertElement(T index, Resume resume);

    protected abstract void deleteElement(T index);

    protected abstract Resume getElement(T searchKey);

    protected abstract void updateElement(T searchKey, Resume resume);

    protected abstract List<Resume> getList();

    public void save(Resume r) {
        LOG.info("Save " + r);
        T searchKey = getNotExistedSearchKey(r.getUuid());
        insertElement(searchKey, r);
    }

    public Resume get(String uuid) {
        LOG.info("Get " + uuid);
        T searchKey = getExistedSearchKey(uuid);
        return getElement(searchKey);
    }

    public void update(Resume resume) {
        LOG.info("Update " + resume);
        T searchKey = getExistedSearchKey(resume.getUuid());
        updateElement(searchKey, resume);
    }

    public void delete(String uuid) {
        LOG.info("Delete " + uuid);
        T searchKey = getExistedSearchKey(uuid);
        deleteElement(searchKey);
    }

    private T getExistedSearchKey(String uuid) {
        T searchKey = getSearchKey(uuid);
        if (!isExist(searchKey)) {
            LOG.warning("Resume " + uuid + " not exist");
            throw new NotExistStorageException(uuid);
        }
        return searchKey;
    }

    private T getNotExistedSearchKey(String uuid) {
        T searchKey = getSearchKey(uuid);
        if (isExist(searchKey)) {
            LOG.warning("Resume " + uuid + " already exist");
            throw new ExistStorageException(uuid);
        }
        return searchKey;
    }

    @Override
    public List<Resume> getAllSorted() {
        List <Resume> result = getList();
        Collections.sort(result);
        return result;
    }
}
