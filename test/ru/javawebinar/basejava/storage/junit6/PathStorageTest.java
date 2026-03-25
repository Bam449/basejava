package ru.javawebinar.basejava.storage.junit6;

import ru.javawebinar.basejava.storage.PathStorage;
import ru.javawebinar.basejava.storage.serializer.ObjectStream;


class PathStorageTest extends AbstractStorageTest {

    public PathStorageTest() {
        super(new PathStorage(new ObjectStream(), "basejava"));
    }
}