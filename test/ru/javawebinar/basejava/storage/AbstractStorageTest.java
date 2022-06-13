package ru.javawebinar.basejava.storage;

import org.junit.Before;
import org.junit.Test;
import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.Resume;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static ru.javawebinar.basejava.storage.TestData.R1;
import static ru.javawebinar.basejava.storage.TestData.R2;
import static ru.javawebinar.basejava.storage.TestData.R3;
import static ru.javawebinar.basejava.storage.TestData.R4;

public abstract class AbstractStorageTest {
    protected Storage storage;

    protected AbstractStorageTest(Storage storage) {
        this.storage = storage;
    }

    @Before
    public void setUp() throws IOException {
        storage.clear();
        storage.save(R1);
        storage.save(R3);
        storage.save(R2);
    }

    @Test
    public void size() {
        assertSize(3);
    }

    @Test
    public void get() throws IOException {
        assertGet(R1);
    }

    @Test(expected = NotExistStorageException.class)
    public void getNotExist() throws IOException {
        assertGet(new Resume("dummy"));
    }

    @Test
    public void clear() {
        storage.clear();
        assertSize(0);
    }

    @Test
    public void update() throws IOException {
        Resume newResume = new Resume(R1.getUuid(), "New_name1");
        storage.update(newResume);
        assertEquals(3, storage.size());
        assertEquals(newResume, storage.get(R1.getUuid()));
    }

    @Test(expected = NotExistStorageException.class)
    public void updateNotExist() throws IOException {
        storage.update(R4);
    }

    @Test
    public void save() throws IOException {
        storage.save(R4);
        assertSize(4);
        assertGet(R4);
    }

    @Test(expected = ExistStorageException.class)
    public void saveExist() throws IOException {
        storage.save(R3);
    }

    @Test
    public void delete() {
        storage.delete(R1.getUuid());
        assertSize(2);
    }

    @Test(expected = NotExistStorageException.class)
    public void deleteNotExist() {
        storage.delete(R4.getUuid());
    }

    @Test
    public void getAllSorted() throws IOException {
        List<Resume> resumes = new ArrayList<>();
        resumes.add(R1);
        resumes.add(R2);
        resumes.add(R3);
        resumes.sort(Resume::compareTo);
        List<Resume> testResumes = storage.getAllSorted();
        assertEquals(3, testResumes.size());
        assertEquals(resumes, testResumes);
    }

    private void assertSize(int size) {
        assertEquals(size, storage.size());
    }

    private void assertGet(Resume resume) throws IOException {
        assertEquals(resume, storage.get(resume.getUuid()));
    }
}
