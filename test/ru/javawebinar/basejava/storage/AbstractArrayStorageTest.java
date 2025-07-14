package ru.javawebinar.basejava.storage;

import org.junit.Before;
import org.junit.Test;
import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public abstract class AbstractArrayStorageTest {

    private final Storage storage;
    Resume r1 = new Resume("uuid1");
    Resume r2 = new Resume("uuid2");
    Resume r3 = new Resume("uuid3");
    Resume r4 = new Resume("uuid4");


    public AbstractArrayStorageTest(Storage storage) {
        this.storage = storage;
    }

    @Before
    public void setUp() {
        storage.clear();
        storage.save(r1);
        storage.save(r2);
        storage.save(r3);
    }

    @Test
    public void save() {
        storage.save(r4);
        assertEquals(4, storage.size());
    }

    @Test(expected = StorageException.class)
    public void saveOverFlaw() {
        storage.clear();
        try {
            for (int i = 0; i < 10000; i++) {
                storage.save(new Resume());
            }
        } catch (RuntimeException e) {
            fail("Error");
        }
        storage.save(new Resume());
    }

    @Test(expected = ExistStorageException.class)
    public void saveExist() {
        storage.save(r3);
    }

    @Test
    public void get() {
        assertEquals(r3, storage.get("uuid3"));
    }

    @Test(expected = NotExistStorageException.class)
    public void getNotExist() {
        storage.get("dummy");
    }

    @Test
    public void update() {
        storage.update(r3);
        assertEquals(r3, storage.get("uuid3"));
    }

    @Test(expected = NotExistStorageException.class)
    public void updateNotExist() {
        storage.update(r4);
    }

    @Test
    public void delete() {
        storage.delete("uuid1");
        assertEquals(2, storage.size());
    }

    @Test(expected = NotExistStorageException.class)
    public void deleteNotExist() {
        storage.delete("dummy");
    }

    @Test
    public void clear() {
        storage.clear();
        assertEquals(0, storage.size());
    }

    @Test
    public void getAll() {
        Resume[] resumes = new Resume[]{r1, r2, r3};
        assertArrayEquals(resumes, storage.getAll());
    }

    @Test
    public void size() {
        assertEquals(3, storage.size());
    }
}