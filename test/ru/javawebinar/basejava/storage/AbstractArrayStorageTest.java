package ru.javawebinar.basejava.storage;

import org.junit.Assert;
import org.junit.Before;
import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public abstract class AbstractArrayStorageTest {
    private final Storage storage;
    private final Resume R1 = new Resume("R1");
    private final Resume R2 = new Resume("R2");
    private final Resume R3 = new Resume("R3");
    private final Resume R4 = new Resume("R4");

    public AbstractArrayStorageTest(Storage storage) {
        this.storage = storage;
    }

    @Before
    public void setUp()  {
        storage.clear();
        storage.save(R1);
        storage.save(R2);
        storage.save(R3);
    }

    @org.junit.Test
    public void save() {
        storage.save(R4);
        assertEquals(R4, storage.get(R4.getUuid()));
        assertSize(4);
    }

    @org.junit.Test(expected = ExistStorageException.class)
    public void saveExist() {
        storage.save(R3);
    }

    @org.junit.Test(expected = StorageException.class)
    public void saveOverFlaw() {
        storage.clear();
        try {
            for (int i = 0; i < 10000; i++) {
                storage.save(new Resume());
            }
        } catch (StorageException e) {
            fail("OverFlaw");
        }
        storage.save(new Resume());
    }

    @org.junit.Test
    public void get() {
        Assert.assertEquals(R3, storage.get(R3.getUuid()));
    }

    @org.junit.Test(expected = NotExistStorageException.class)
    public void getNotExist() {
        storage.get("dummy");
    }

    @org.junit.Test
    public void update() {
        R3.setUuid("R4");
        storage.update(R3);
        assertEquals(R3, storage.get("R4"));
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
        storage.delete("dummy");
    }

    @org.junit.Test
    public void clear() {
        storage.clear();
        assertSize(0);
    }

    @org.junit.Test
    public void size() {
        assertSize(3);
    }

    @org.junit.Test
    public void getAll() {
        Resume[] st1 = new Resume[]{R1, R2, R3};
        assertArrayEquals(st1, storage.getAll());
    }

    private void assertSize(int i) {
        assertEquals(i, storage.size());
    }
}