package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.storage.serializer.ObjectSerializer;

import java.nio.file.Path;

public class PathStorageTest extends AbstractStorageTest{

    public PathStorageTest() {
        super(new PathStorage(Path.of(STORAGE_DIR.getAbsolutePath()), new ObjectSerializer()));
    }
}