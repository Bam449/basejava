package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.storage.serializer.InputStreamSerializer;

import java.nio.file.Paths;


public class DataStorageTest extends AbstractStorageTest {

    public DataStorageTest() {
        super(new PathStorage(Paths.get("storage"), new InputStreamSerializer()));
    }
}