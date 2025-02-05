package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exeption.ExistStorageException;
import ru.javawebinar.basejava.exeption.NotExistStorageException;
import ru.javawebinar.basejava.model.Resume;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public abstract class AbstractStorageTest {

    protected final Storage storage;
    private final String uuid1 = "uuid1";
    private final String uuid2 = "uuid2";
    private final String uuid3 = "uuid3";
    private final String uuid4 = "uuid4";

    private final String fullName1 = "fullName1";
    private final String fullName2 = "fullName2";
    private final String fullName3 = "fullName3";
    private final String fullName4 = "fullName4";

    private final Resume R1 = new Resume(uuid1, fullName1);
    private final Resume R2 = new Resume(uuid2, fullName2);
    private final Resume R3 = new Resume(uuid3, fullName3);
    private final Resume R4 = new Resume(uuid4, fullName4);

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
        assertEquals(R1, storage.get("uuid1"));
    }

    @org.junit.Test(expected = NotExistStorageException.class)
    public void getNotExist() {
        storage.get("Dummy");
    }

    @org.junit.Test
    public void update() {
        Resume testResume = new Resume(uuid3, "R4");
        storage.update(testResume);
        assertEquals(testResume, storage.get("uuid3"));
        assertSize(3);
    }

    @org.junit.Test(expected = NotExistStorageException.class)
    public void updateNotExist() {
        storage.update(R4);
    }

    @org.junit.Test
    public void delete() {
        storage.delete("uuid3");
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
