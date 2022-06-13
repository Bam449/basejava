package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.storage.serializer.JsonStreamSerializer;

import static ru.javawebinar.basejava.storage.TestData.STORAGE_DIR;

public class JsonPathStorageTest extends AbstractStorageTest {

    public JsonPathStorageTest() {
        super(new PathStorage(STORAGE_DIR.getAbsolutePath(), new JsonStreamSerializer()));
    }
}
