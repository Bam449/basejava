package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.storage.serializer.ObjectStreamSerializer;

import static ru.javawebinar.basejava.storage.TestData.STORAGE_DIR;

public class ObjectPathStorageTest extends AbstractStorageTest{

    public ObjectPathStorageTest() {
        super(new PathStorage(STORAGE_DIR.getPath(), new ObjectStreamSerializer()));
    }
}
