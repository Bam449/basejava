package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.Arrays;

public class SortedArrayStorage extends AbstractArrayStorage {

    @Override
    protected int getIndex(String uuid) {
        Resume testResume = new Resume();
        testResume.setUuid(uuid);
        return Arrays.binarySearch(storage, 0, size, testResume);
    }

    @Override
    protected void insertElement(Resume resume, int index) {
        System.arraycopy(storage, -index - 1, storage, -index, size - index - 1);
        storage[-index - 1] = resume;
    }

    @Override
    protected void deleteElement(int index) {
        System.arraycopy(storage, index + 1, storage, index, size - index - 1);
    }
}
