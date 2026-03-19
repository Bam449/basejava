package ru.javawebinar.basejava.storage.junit6;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.storage.Storage;

import static org.junit.jupiter.api.Assertions.*;


public abstract class AbstractStorageTest {

    protected final Storage storage;

    private final String uuid1 = "uuid1";
    private final String uuid2 = "uuid2";
    private final String uuid3 = "uuid3";
    private final String uuid4 = "uuid4";

    private final Resume resume1 = new Resume(uuid1);
    private final Resume resume2 = new Resume(uuid2);
    private final Resume resume3 = new Resume(uuid3);
    private final Resume resume4 = new Resume(uuid4);

    public AbstractStorageTest(Storage storage) {
        this.storage = storage;
    }

    @BeforeEach
    public void setUp() {
        storage.clear();
        storage.save(resume1);
        storage.save(resume2);
        storage.save(resume3);
    }

    @Test
    public void save() {
        storage.save(resume4);
        assertSize(4);
    }

    @Test
    public void saveExist() {
        assertThrows(ExistStorageException.class, () -> storage.save(resume3));
    }

    @Test
    public void get() {
        assertEquals(resume1, storage.get(uuid1));
    }

    @Test
    public void getNotExist() {
        assertThrows(NotExistStorageException.class, () -> storage.get("dummy"));
    }

    @Test
    public void update() {
        Resume testResume = new Resume(uuid3);
        storage.update(testResume);
        assertEquals(testResume, storage.get(uuid3));
    }

    @Test
    public void updateNotExist() {
        assertThrows(NotExistStorageException.class, () -> storage.update(new Resume("dummy")));
    }

    @Test
    public void delete() {
        storage.delete(uuid1);
        assertSize(2);
    }

    @Test
    public void deleteNotExist() {
        assertThrows(NotExistStorageException.class, () -> storage.delete("dummy"));
    }

    @Test
    public void clear() {
        storage.clear();
        assertSize(0);
    }

    @Test
    public void getAll() {
        Resume[] testStorage = new Resume[]{resume1, resume2, resume3};
        assertArrayEquals(storage.getAll(), testStorage);
    }

    @Test
    public void size() {
        assertSize(3);
    }

    private void assertSize(int size) {
        assertEquals(storage.size(), size);
    }
}