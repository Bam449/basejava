package ru.javawebinar.basejava.storage;


import org.junit.Assert;
import ru.javawebinar.basejava.exeption.ExistStorageException;
import ru.javawebinar.basejava.exeption.NotExistStorageException;
import ru.javawebinar.basejava.exeption.StorageException;
import ru.javawebinar.basejava.model.Resume;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public abstract class AbstractArrayStorageTest {
    private final Storage storage;
    private final Resume R1 = new Resume("R1");
    private final Resume R2 = new Resume("R2");
    private final Resume R3 = new Resume("R3");
    private final Resume R4 = new Resume("R4");


    protected AbstractArrayStorageTest(Storage storage) {
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

    @org.junit.Test(expected = StorageException.class)
    public void saveOverFlaw() {
        try {
            for (int i = 0; i < 9997; i++) {
                storage.save(new Resume());
            }
        } catch (StorageException e) {
            Assert.fail("Storage overflow any time");
        }
        storage.save(new Resume());
    }

    @org.junit.Test(expected = ExistStorageException.class)
    public void saveExist() {
        storage.save(R3);
    }

    @org.junit.Test
    public void get() {
        assertEquals(R1, storage.get("R1"));
    }

    @org.junit.Test(expected = NotExistStorageException.class)
    public void getNotExist() {
        storage.get("Dummy");
    }

    @org.junit.Test
    public void update() {
        Resume testResume = new Resume("R3");
        storage.update(testResume);
        assertEquals(testResume, storage.get("R3"));
        assertSize(3);
    }

    @org.junit.Test(expected = NotExistStorageException.class)
    public void updateNotExist() {
        storage.update(R4);
    }

    @org.junit.Test
    public void delete() {
        storage.delete("R3");
        assertSize(2);
    }

    @org.junit.Test(expected = NotExistStorageException.class)
    public void deleteNotExist() {
        storage.delete("R4");
    }

    @org.junit.Test
    public void clear() {
        storage.clear();
        assertSize(0);
    }

    @org.junit.Test
    public void getAll() {
        Resume[] storageTest = new Resume[]{R1, R2, R3};
        assertArrayEquals(storageTest, storage.getAll());
    }

    @org.junit.Test
    public void size() {
        assertEquals(3, storage.size());
    }

    private void assertSize(int i) {
        assertEquals(i, storage.size());
    }
}