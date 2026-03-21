package ru.javawebinar.basejava.storage.junit6;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.storage.Storage;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


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

    private final Resume resume1 = new Resume(uuid1, fullName1);
    private final Resume resume2 = new Resume(uuid2, fullName2);
    private final Resume resume3 = new Resume(uuid3, fullName3);
    private final Resume resume4 = new Resume(uuid4, fullName4);

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
        Resume testResume = new Resume(uuid3, fullName4);
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
        List<Resume> list = new ArrayList<>();
        list.add(resume1);
        list.add(resume2);
        list.add(resume3);
        assertEquals(storage.getAllSorted(), list);
    }

    @Test
    public void size() {
        assertSize(3);
    }

    private void assertSize(int size) {
        assertEquals(storage.size(), size);
    }
}