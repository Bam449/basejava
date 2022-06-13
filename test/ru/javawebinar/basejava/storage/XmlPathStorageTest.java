package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.storage.serializer.XmlStreamSerializer;

import static ru.javawebinar.basejava.storage.TestData.STORAGE_DIR;

public class XmlPathStorageTest extends AbstractStorageTest{

    public XmlPathStorageTest() {
        super(new PathStorage(STORAGE_DIR.getPath(), new XmlStreamSerializer()));

    }
}
