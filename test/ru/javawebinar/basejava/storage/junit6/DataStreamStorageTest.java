package ru.javawebinar.basejava.storage.junit6;

import ru.javawebinar.basejava.storage.PathStorage;
import ru.javawebinar.basejava.storage.serializer.DataStream;
import ru.javawebinar.basejava.storage.serializer.XmlStream;

class DataStreamStorageTest extends AbstractStorageTest {

    public DataStreamStorageTest() {
        super(new PathStorage(new DataStream(), "basejava"));
    }
}