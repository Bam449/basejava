package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.storage.serializer.ObjectStreamSerializer;

public class ObjectPathStorageTest extends AbstractStorageTest{

    public ObjectPathStorageTest() {
        super(new PathStorage(AbstractStorageTest.STORAGE_DIR.getPath(), new ObjectStreamSerializer()));
    }
}
