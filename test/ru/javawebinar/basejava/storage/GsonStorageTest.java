package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.storage.serializer.GsonSerializer;

import java.nio.file.Paths;


public class GsonStorageTest extends AbstractStorageTest {

    public GsonStorageTest() {
        super(new PathStorage(Paths.get("storage"), new GsonSerializer()));
    }
}