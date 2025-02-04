package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.Arrays;

public abstract class AbstractArrayStorage {
    private static final int STORAGE_LIMIT = 10000;
    protected final Resume[] storage = new Resume[STORAGE_LIMIT];
    protected int size;

    public void save(Resume r) {
        int index = getIndex(r.getUuid());
        if (index > 0 || STORAGE_LIMIT == size) return;
        insertElement(r, index);
        size++;
    }

    public Resume get(String uuid) {
        int index = getIndex(uuid);
        if (index < 0) return null;
        return storage[index];
    }

    public void update(Resume r) {
        int index = getIndex(r.getUuid());
        if (index < 0) return;
        storage[index] = r;
    }

    public void delete(String uuid) {
        int index = getIndex(uuid);
        if (index < 0) return;
        fillDeleteElement(index);
        storage [size - 1] = null;
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


    protected abstract int getIndex(String uuid);
    protected abstract void insertElement(Resume r, int index);
    protected abstract void fillDeleteElement (int index);

}
