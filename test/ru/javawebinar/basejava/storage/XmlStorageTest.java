package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.storage.serializer.XmlStreamSerializer;

import java.nio.file.Paths;


public class XmlStorageTest extends AbstractStorageTest{

    public XmlStorageTest() {
        super(new PathStorage(new XmlStreamSerializer(), Paths.get("./fileStorage")));
    }
}