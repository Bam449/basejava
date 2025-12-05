package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.config.Config;
import ru.javawebinar.basejava.storage.serializer.GsonSerializer;


public class GsonStorageTest extends AbstractStorageTest {

    public GsonStorageTest() {
        super(new PathStorage(Config.get().getStorageDir().toPath(), new GsonSerializer()));
    }
}