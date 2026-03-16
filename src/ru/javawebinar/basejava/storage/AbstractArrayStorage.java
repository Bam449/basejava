package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

import java.util.Arrays;

public abstract class AbstractArrayStorage implements Storage {

    private static final int STORAGE_LIMIT = 10000;
    protected final Resume[] storage = new Resume[STORAGE_LIMIT];
    protected int size = 0;

    protected abstract int getIndex(String uuid);

    protected abstract void insertIndex(Resume resume, int index);

    protected abstract void removeIndex(int index);

    @Override
    public void save(Resume resume) {
        if (STORAGE_LIMIT == size) throw new StorageException("Stack over flaw", resume.getUuid());
        int index = getIndex(resume.getUuid());
        if (index < 0) {
            insertIndex(resume, index);
            size++;
            return;
        }
        throw new ExistStorageException(resume.getUuid());
    }

    @Override
    public Resume get(String uuid) {
        int index = getIndex(uuid);
        if (index < 0) throw new NotExistStorageException(uuid);
        return storage[index];
    }

    @Override
    public void update(Resume resume) {
        int index = getIndex(resume.getUuid());
        if (index < 0) throw new NotExistStorageException(resume.getUuid());
        storage[index] = resume;
    }

    @Override
    public void delete(String uuid) {
        int index = getIndex(uuid);
        if (index < 0) throw new NotExistStorageException(uuid);
        removeIndex(index);
        size--;
    }

    @Override
    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    @Override
    public Resume[] getAll() {
        return Arrays.copyOfRange(storage, 0, size);
    }

    @Override
    public int size() {
        return size;
    }

}
