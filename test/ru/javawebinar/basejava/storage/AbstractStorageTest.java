package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exeption.ExistStorageException;
import ru.javawebinar.basejava.exeption.NotExistStorageException;
import ru.javawebinar.basejava.model.Resume;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static ru.javawebinar.basejava.ResumeTestData.R1;
import static ru.javawebinar.basejava.ResumeTestData.R2;
import static ru.javawebinar.basejava.ResumeTestData.R3;
import static ru.javawebinar.basejava.ResumeTestData.R4;

public abstract class AbstractStorageTest {

    protected final Storage storage;

    protected AbstractStorageTest(Storage storage) {
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
        assertEquals(4, storage.size());
    }

    @org.junit.Test(expected = ExistStorageException.class)
    public void saveExist() {
        storage.save(R3);
    }

    @org.junit.Test
    public void get() {
        assertEquals(R1, storage.get(R1.getUuid()));
    }

    @org.junit.Test(expected = NotExistStorageException.class)
    public void getNotExist() {
        storage.get("Dummy");
    }

    @org.junit.Test
    public void update() {
        Resume testResume = new Resume(R3.getUuid(), "R4");
        storage.update(testResume);
        assertEquals(testResume, storage.get(R3.getUuid()));
        assertSize(3);
    }

    @org.junit.Test(expected = NotExistStorageException.class)
    public void updateNotExist() {
        storage.update(R4);
    }

    @org.junit.Test
    public void delete() {
        storage.delete(R3.getUuid());
        assertSize(2);
    }

    @org.junit.Test(expected = NotExistStorageException.class)
    public void deleteNotExist() {
        storage.delete(R4.getUuid());
    }

    @org.junit.Test
    public void clear() {
        storage.clear();
        assertSize(0);
    }

    @org.junit.Test
    public void getAllSorted() {
        List<Resume> storageTest = new ArrayList<>();
        storageTest.add(R1);
        storageTest.add(R2);
        storageTest.add(R3);
        assertEquals(storageTest, storage.getAllSorted());
    }

    @org.junit.Test
    public void size() {
        assertEquals(3, storage.size());
    }

    private void assertSize(int i) {
        assertEquals(i, storage.size());
    }
}
