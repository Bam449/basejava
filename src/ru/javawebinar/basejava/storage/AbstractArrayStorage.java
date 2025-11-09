package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

import java.util.Arrays;
import java.util.List;

public abstract class AbstractArrayStorage extends AbstractStorage <Integer> {
    private static final int STORAGE_LIMIT = 10000;
    protected final Resume[] storage = new Resume[STORAGE_LIMIT];
    protected int size;

    protected abstract int getIndex(String uuid);
    protected abstract void insertElement(Resume resume, int key);
    protected abstract void deleteElement(int key);

    @Override
    protected boolean isExist(Integer index) {
        return index > -1;
    }

    @Override
    protected Resume doGet(Integer index) {
        return storage[index];
    }

    @Override
    public void saveKey(Resume resume, Integer index) {
        if (size == STORAGE_LIMIT) {
            throw new StorageException("STORAGE OVER FLAW", resume.getUuid());
        }
        insertElement(resume, index);
        size++;
    }

    @Override
    public Integer getSearchKey(String key) {
        return getIndex(key);
    }

    @Override
    public void updateKey(Resume resume, Integer index) {
        storage[index] = resume;
    }

    @Override
    public void deleteKey(Integer index) {
        deleteElement(index);
        storage[size - 1] = null;
        size--;
    }

    @Override
    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    @Override
    public List<Resume> getList() {
        return Arrays.asList(Arrays.copyOfRange(storage, 0, size));
    }

    @Override
    public int size() {
        return size;
    }
}
