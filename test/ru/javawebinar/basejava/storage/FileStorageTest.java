package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.config.Config;
import ru.javawebinar.basejava.storage.serializer.ObjectSerializer;


public class FileStorageTest extends AbstractStorageTest {

    public FileStorageTest() {
        super(new FileStorage(Config.get().getStorageDir(), new ObjectSerializer()));
    }
}