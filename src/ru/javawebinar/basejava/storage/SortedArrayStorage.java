package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.Arrays;
import java.util.Comparator;

public class SortedArrayStorage extends AbstractArrayStorage {

    private final Comparator<Resume> resumeComparator = Comparator.comparing(Resume::getUuid);

    @Override
    protected Object getSearchKey(String uuid) {
        return Arrays.binarySearch(storage, 0, size, new Resume(uuid, "fullName"), resumeComparator);
    }

    @Override
    protected void insertElement(Resume r, int index) {
        System.arraycopy(storage, -index - 1, storage, -index, size + index + 1);
        storage[-index - 1] = r;
    }

    @Override
    protected void fillDeleteElement(int index) {
        System.arraycopy(storage, index + 1, storage, index, size - index - 1);
    }
}
