package ru.javawebinar.basejava.storage.junit6;

import org.junit.jupiter.api.Test;
import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.storage.Storage;

import static org.junit.jupiter.api.Assertions.*;


public abstract class AbstractArrayStorageTest extends AbstractStorageTest{

    public AbstractArrayStorageTest(Storage storage) {
        super(storage);
    }

    @Test
    public void saveOverFlaw() {
        storage.clear();
        assertThrows(StorageException.class, () -> {
                    try {
                        for (int i = 0; i < 10000; i++) {
                            storage.save(new Resume("test"));
                        }
                    } catch (Exception e) {
                        fail(e.getMessage());
                    }
                    storage.save(new Resume("test"));
                }
        );
    }

}