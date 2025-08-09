package ru.javawebinar.basejava.storage;


import ru.javawebinar.basejava.model.Resume;

public class ArrayStorage extends AbstractArrayStorage {

    @Override
    protected Integer getSearchKey(String uuid) {
        for (int i = 0; i < size; i++) {
            if (storage[i].getUuid().equals(uuid)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    protected void insertIndexElement(Integer index, Resume resume) {
        storage[size] = resume;
    }

    @Override
    protected void deleteIndexElement(Integer index) {
        storage[index] = storage[size - 1];
    }

}
