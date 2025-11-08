package ru.javawebinar.basejava.storage;


import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public abstract class AbstractArrayStorageTest {

    private final Storage storage;

    private final String uuid1 = "uuid1";
    private final String uuid2 = "uuid2";
    private final String uuid3 = "uuid3";
    private final String uuid4 = "uuid4";
    private final Resume resume1 = new Resume(uuid1);
    private final Resume resume2 = new Resume(uuid2);
    private final Resume resume3 = new Resume(uuid3);
    private final Resume resume4 = new Resume(uuid4);


    public AbstractArrayStorageTest(Storage storage) {
        this.storage = storage;
    }

    @org.junit.Before
    public void setUp() {
        storage.clear();
        storage.save(resume1);
        storage.save(resume2);
        storage.save(resume3);
    }

    @org.junit.Test
    public void save() {
        storage.save(resume4);
        assertEquals(resume4, storage.get(uuid4));
        assertSize(4);
    }

    @org.junit.Test(expected = StorageException.class)
    public void saveOverFlaw() {
        storage.clear();
        try {
            for (int i = 0; i < 10000; i++) {
                storage.save(new Resume());
            }
        } catch (Exception e) {
            fail(e.getMessage());
        }
        storage.save(resume4);
    }

    @org.junit.Test(expected = ExistStorageException.class)
    public void saveExist() {
        storage.save(resume1);
    }

    @org.junit.Test
    public void get() {
        assertEquals(resume3, storage.get(uuid3));
    }

    @org.junit.Test(expected = NotExistStorageException.class)
    public void getNotExist() {
        storage.get(uuid4);
    }

    @org.junit.Test
    public void update() {
        Resume testResume = new Resume("uuid3");
        storage.update(testResume);
        assertEquals(resume3, storage.get(uuid3));
    }

    @org.junit.Test(expected = NotExistStorageException.class)
    public void updateNotExist() {
        storage.update(resume4);
    }

    @org.junit.Test
    public void delete() {
        storage.delete(uuid2);
        assertSize(2);
    }

    @org.junit.Test(expected = NotExistStorageException.class)
    public void deleteNotExist() {
        storage.delete(uuid4);
    }

    @org.junit.Test
    public void clear() {
        storage.clear();
        assertSize(0);
    }

    @org.junit.Test
    public void getAll() {
        Resume[] arrayStorage = new Resume[]{resume1, resume2, resume3};
        assertArrayEquals(arrayStorage, storage.getAll());
    }

    @org.junit.Test
    public void size() {
        assertSize(3);
    }

    private void assertSize(int size) {
        assertEquals(size, storage.size());
    }
}