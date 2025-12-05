package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.config.Config;
import ru.javawebinar.basejava.storage.serializer.ObjectSerializer;


public class PathStorageTest extends AbstractStorageTest {

    public PathStorageTest() {
        super(new PathStorage(Config.get().getStorageDir().toPath(), new ObjectSerializer()));
    }
}