package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.storage.serializer.GsonStreamSerializer;

import java.nio.file.Paths;


public class GsonStorageTest extends AbstractStorageTest{

    public GsonStorageTest() {
        super(new PathStorage(new GsonStreamSerializer(), Paths.get("./fileStorage")));
    }
}