package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exeption.StorageException;
import ru.javawebinar.basejava.model.Resume;

import java.util.Arrays;
import java.util.List;


public abstract class AbstractArrayStorage extends AbstractStorage <Integer> {
    private static final int STORAGE_LIMIT = 10000;
    protected final Resume[] storage = new Resume[STORAGE_LIMIT];
    protected int size;

    @Override
    protected void doSave(Resume r, Integer searchKey) {
        if (STORAGE_LIMIT == size) throw new StorageException("Stack Over Flaw", r.getUuid());
        insertElement(r, searchKey);
        size++;
    }

    public Resume doGet(Integer searchKey) {
        return storage[searchKey];
    }

    public void doUpdate(Resume r, Integer searchKey) {
        storage[searchKey] = r;
    }

    public void doDelete(Integer searchKey) {
        fillDeleteElement(searchKey);
        storage[size - 1] = null;
        size--;
    }

    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    @Override
    protected List<Resume> doCopyAll() {
        return Arrays.asList(Arrays.copyOfRange(storage, 0, size));
    }

    public int size() {
        return size;
    }

    @Override
    protected boolean isExist(Integer searchKey) {
        return searchKey >= 0;
    }

    protected abstract void insertElement(Resume r, int index);

    protected abstract void fillDeleteElement(int index);

}
