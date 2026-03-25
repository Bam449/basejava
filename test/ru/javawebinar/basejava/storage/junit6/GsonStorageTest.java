package ru.javawebinar.basejava.storage.junit6;

import ru.javawebinar.basejava.storage.PathStorage;
import ru.javawebinar.basejava.storage.serializer.GsonStream;

class GsonStorageTest extends AbstractStorageTest {

    public GsonStorageTest() {
        super(new PathStorage(new GsonStream(), "basejava"));
    }
}