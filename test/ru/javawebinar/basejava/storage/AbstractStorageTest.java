package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.Resume;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static ru.javawebinar.basejava.TestData.R1;
import static ru.javawebinar.basejava.TestData.R2;
import static ru.javawebinar.basejava.TestData.R3;
import static ru.javawebinar.basejava.TestData.R4;
import static ru.javawebinar.basejava.TestData.UUID_2;
import static ru.javawebinar.basejava.TestData.UUID_3;
import static ru.javawebinar.basejava.TestData.UUID_4;

public abstract class AbstractStorageTest {
    protected final Storage storage;

    public AbstractStorageTest(Storage storage) {
        this.storage = storage;
    }

    @org.junit.Before
    public void setUp() {
        storage.clear();
        storage.save(R1);
        storage.save(R2);
        storage.save(R3);
    }

    @org.junit.Test
    public void save() {
        storage.save(R4);
        assertEquals(R4, storage.get(UUID_4));
        assertSize(4);
    }

    @org.junit.Test(expected = ExistStorageException.class)
    public void saveExist() {
        storage.save(R1);
    }

    @org.junit.Test
    public void get() {
        assertEquals(R3, storage.get(UUID_3));
    }

    @org.junit.Test(expected = NotExistStorageException.class)
    public void getNotExist() {
        storage.get(UUID_4);
    }

    @org.junit.Test
    public void update() {
        Resume testResume = new Resume(UUID_3, R4.getFullName());
        storage.update(testResume);
        assertEquals(testResume, storage.get(UUID_3));
    }

    @org.junit.Test(expected = NotExistStorageException.class)
    public void updateNotExist() {
        storage.update(R4);
    }

    @org.junit.Test
    public void delete() {
        storage.delete(UUID_2);
        assertSize(2);
    }

    @org.junit.Test(expected = NotExistStorageException.class)
    public void deleteNotExist() {
        storage.delete(UUID_4);
    }

    @org.junit.Test
    public void clear() {
        storage.clear();
        assertSize(0);
    }

    @org.junit.Test
    public void getAllSorted() {
        List<Resume> list = new ArrayList<>();
        list.add(R1);
        list.add(R2);
        list.add(R3);
        list.sort(Comparator.comparing(Resume::getUuid).thenComparing(Resume::getFullName));
        assertEquals(list, storage.getAllSorted());
    }

    @org.junit.Test
    public void size() {
        assertSize(3);
    }

    private void assertSize(int size) {
        assertEquals(size, storage.size());
    }
}
