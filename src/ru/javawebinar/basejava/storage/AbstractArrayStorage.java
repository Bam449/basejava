package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

import java.util.Arrays;

public abstract class AbstractArrayStorage extends AbstractStorage{
    private static final int STORAGE_SIZE = 10000;
    protected final Resume[] storage = new Resume[STORAGE_SIZE];
    protected int size;

    @Override
    public void insertElement(Object index, Resume r) {
        if (size == STORAGE_SIZE) {
            throw new StorageException("Storage over flaw");
        }
        insertElement((int)index, r);
        size++;
    }

@Override
    public Resume getElement(Object index) {
        return storage[(int) index];
    }

@Override
    public void updateElement(Object index, Resume resume) {
        storage[(int)index] = resume;
    }

@Override
    public void deleteElement(Object index) {
        deleteElement((int)index);
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

    @Override
    protected boolean isExist(Object searchKey) {
        return (int) searchKey >= 0;
    }

    protected abstract void insertElement(int index, Resume resume);
    protected abstract void deleteElement(int index);
}
