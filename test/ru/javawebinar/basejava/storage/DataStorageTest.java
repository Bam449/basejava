package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.config.Config;
import ru.javawebinar.basejava.storage.serializer.InputStreamSerializer;


public class DataStorageTest extends AbstractStorageTest {

    public DataStorageTest() {
        super(new PathStorage(Config.get().getStorageDir().toPath(), new InputStreamSerializer()));
    }
}