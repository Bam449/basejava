package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.storage.serializer.DataStreamSerializer;

public class DataPathStorageTest extends AbstractStorageTest{

    public DataPathStorageTest() {
        super(new PathStorage(AbstractStorageTest.STORAGE_DIR.getPath(), new DataStreamSerializer()));
    }
}
