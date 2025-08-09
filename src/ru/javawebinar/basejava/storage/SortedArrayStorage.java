package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.Arrays;


public class SortedArrayStorage extends AbstractArrayStorage {

    @Override
    protected Integer getSearchKey(String uuid) {
        return Arrays.binarySearch(storage, 0, size, new Resume(uuid, "dummy"));
    }

    @Override
    protected void insertIndexElement(Integer index, Resume resume) {
        System.arraycopy(storage, -index - 1, storage, -index, size + index + 1);
        storage[-index - 1] = resume;
    }

    @Override
    protected void deleteIndexElement(Integer index) {
        System.arraycopy(storage, index + 1, storage, index, size - index - 1);
    }
}
