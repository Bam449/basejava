package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.config.Config;
import ru.javawebinar.basejava.storage.serializer.XmlSerializer;


public class XmlStorageTest extends AbstractStorageTest {

    public XmlStorageTest() {
        super(new PathStorage(Config.get().getStorageDir().toPath(), new XmlSerializer()));
    }
}