package ru.javawebinar.basejava.storage;


import org.junit.Assert;
import ru.javawebinar.basejava.exeption.StorageException;
import ru.javawebinar.basejava.model.Resume;

public abstract class AbstractArrayStorageTest extends AbstractStorageTest {

    protected AbstractArrayStorageTest(Storage storage) {
        super(storage);
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
}