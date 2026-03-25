package ru.javawebinar.basejava.storage.junit6;

import ru.javawebinar.basejava.storage.PathStorage;
import ru.javawebinar.basejava.storage.serializer.XmlStream;

class XmlStorageTest extends AbstractStorageTest {

    public XmlStorageTest() {
        super(new PathStorage(new XmlStream(), "basejava"));
    }
}