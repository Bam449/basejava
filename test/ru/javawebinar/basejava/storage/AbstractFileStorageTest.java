package ru.javawebinar.basejava.storage;


import ru.javawebinar.basejava.storage.serializer.ObjectStreamSerializer;

import java.io.File;

public class AbstractFileStorageTest extends AbstractStorageTest{

    public AbstractFileStorageTest() {
        super(new AbstractFileStorage(new File("./fileStorage"), new ObjectStreamSerializer()) {
        });
    }
}