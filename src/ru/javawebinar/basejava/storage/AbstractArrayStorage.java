package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

import java.util.Arrays;
import java.util.List;

public abstract class AbstractArrayStorage extends AbstractStorage<Integer> {

    private static final int STORAGE_LIMIT = 10000;
    protected final Resume[] storage = new Resume[STORAGE_LIMIT];
    protected int size = 0;

    protected abstract void insertIndex(Resume resume, int index);

    protected abstract void removeIndex(int index);

    @Override
    protected boolean isExist(Integer index) {
        return index >= 0;
    }

    @Override
    protected void insertElement(Resume resume, Integer index) {
        if (STORAGE_LIMIT == size) throw new StorageException("Stack over flaw", resume.getUuid());
        insertIndex(resume, index);
        size++;
    }

    @Override
    protected Resume getElement(Integer index) {
        return storage[index];
    }

    @Override
    protected void updateElement(Resume resume, Integer index) {
        storage[index] = resume;
    }

    @Override
    protected void deleteElement(Integer index) {
        removeIndex(index);
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
