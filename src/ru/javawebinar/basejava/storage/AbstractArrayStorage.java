package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.Arrays;

public abstract class AbstractArrayStorage {
    private static final int STORAGE_SIZE = 10000;
    protected final Resume[] storage = new Resume[STORAGE_SIZE];
    protected int size;

    public void save(Resume r) {
        if (size == STORAGE_SIZE) {
            System.out.println("Error");
            return;
        }
        int searchKey = getSearchKey(r.getUuid());
        if (searchKey >= 0) {
            System.out.println("Error");
            return;
        }
        insertIndex(searchKey, r);
        size++;
    }

    public Resume get(String uuid) {
        int searchKey = getSearchKey(uuid);
        if (searchKey < 0) {
            System.out.println("Error");
            return null;
        }
        return storage[searchKey];
    }

    public void update(Resume resume) {
        int searchKey = getSearchKey(resume.getUuid());
        if (searchKey < 0) {
            System.out.println("Error");
            return;
        }
        storage[searchKey] = resume;
    }

    public void delete(String uuid) {
        int searchKey = getSearchKey(uuid);
        if (searchKey < 0) {
            System.out.println("Error");
            return;
        }
        deleteIndex(searchKey);
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
    protected abstract void insertIndex(int key, Resume resume);
    protected abstract void deleteIndex(int index);
}
