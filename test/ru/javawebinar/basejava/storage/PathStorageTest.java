package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.storage.serializer.ObjectSerializer;

import java.nio.file.Paths;


public class PathStorageTest extends AbstractStorageTest {

    public PathStorageTest() {
        super(new PathStorage(Paths.get("storage"), new ObjectSerializer()));
    }
}