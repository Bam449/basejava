package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

import java.util.Arrays;

public abstract class AbstractArrayStorage implements Storage{
    private static final int STORAGE_SIZE = 10000;
    protected final Resume[] storage = new Resume[STORAGE_SIZE];
    protected int size;

    public void save(Resume r) {
        if (size == STORAGE_SIZE) {
            throw new StorageException("Storage over flaw");
        }
        int searchKey = getSearchKey(r.getUuid());
        if (searchKey >= 0) {
            throw new ExistStorageException(r.getUuid());
        }
        insertIndex(searchKey, r);
        size++;
    }

    public Resume get(String uuid) {
        int searchKey = getSearchKey(uuid);
        if (searchKey < 0) {
            throw new NotExistStorageException(uuid);
        }
        return storage[searchKey];
    }

    public void update(Resume resume) {
        int searchKey = getSearchKey(resume.getUuid());
        if (searchKey < 0) {
            throw new NotExistStorageException(resume.getUuid());
        }
        storage[searchKey] = resume;
    }

    public void delete(String uuid) {
        int searchKey = getSearchKey(uuid);
        if (searchKey < 0) {
            throw new NotExistStorageException(uuid);
        }
        deleteIndex(searchKey);
        storage[size - 1] = null;
        size--;
    }

    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    public Resume[] getAll() {
        return Arrays.copyOfRange(storage, 0, size);
    }

    public int size() {
        return size;
    }

    protected abstract int getSearchKey(String uuid);
    protected abstract void insertIndex(int index, Resume resume);
    protected abstract void deleteIndex(int index);
}
