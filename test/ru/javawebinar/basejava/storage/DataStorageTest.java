package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.storage.serializer.DataStreamSerializer;

import java.nio.file.Paths;


public class DataStorageTest extends AbstractStorageTest{

    public DataStorageTest() {
        super(new PathStorage(new DataStreamSerializer(), Paths.get("./fileStorage")));
    }
}