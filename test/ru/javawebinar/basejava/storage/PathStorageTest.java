package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.storage.serializer.ObjectStreamSerializer;

import java.nio.file.Paths;


public class PathStorageTest extends AbstractStorageTest{

    public PathStorageTest() {
        super(new PathStorage(new ObjectStreamSerializer(), Paths.get("./fileStorage")));
    }
}