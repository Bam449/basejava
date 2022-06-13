package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.storage.serializer.ObjectStreamSerializer;

import static ru.javawebinar.basejava.storage.TestData.STORAGE_DIR;

public class ObjectFileStorageTest extends AbstractStorageTest{

    public ObjectFileStorageTest() {
        super(new FileStorage(STORAGE_DIR, new ObjectStreamSerializer()));
    }
}
