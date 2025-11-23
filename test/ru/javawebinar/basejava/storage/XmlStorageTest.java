package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.storage.serializer.XmlSerializer;

import java.nio.file.Paths;


public class XmlStorageTest extends AbstractStorageTest {

    public XmlStorageTest() {
        super(new PathStorage(Paths.get("storage"), new XmlSerializer()));
    }
}