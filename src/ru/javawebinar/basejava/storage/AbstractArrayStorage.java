package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

import java.util.Arrays;
import java.util.List;

public abstract class AbstractArrayStorage extends AbstractStorage <Integer>{
    private static final int STORAGE_SIZE = 10000;
    protected final Resume[] storage = new Resume[STORAGE_SIZE];
    protected int size;

    @Override
    public void insertElement(Integer index, Resume r) {
        if (size == STORAGE_SIZE) {
            throw new StorageException("Storage over flaw");
        }
        insertIndexElement(index, r);
        size++;
    }

@Override
    public Resume getElement(Integer index) {
        return storage[index];
    }

@Override
    public void updateElement(Integer index, Resume resume) {
        storage[index] = resume;
    }

@Override
    public void deleteElement(Integer index) {
        deleteIndexElement(index);
        storage[size - 1] = null;
        size--;
    }

    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    @Override
    public List<Resume> getList() {
        return Arrays.asList(Arrays.copyOfRange(storage, 0, size));
    }

    public int size() {
        return size;
    }

    @Override
    protected boolean isExist(Integer searchKey) {
        return searchKey >= 0;
    }

    protected abstract void insertIndexElement(Integer index, Resume resume);
    protected abstract void deleteIndexElement(Integer index);
}
