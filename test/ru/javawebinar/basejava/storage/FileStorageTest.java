package ru.javawebinar.basejava.storage;


import ru.javawebinar.basejava.storage.serializer.ObjectStreamSerializer;

import java.io.File;

public class FileStorageTest extends AbstractStorageTest{

    public FileStorageTest() {
        super(new FileStorage(new File("./fileStorage"), new ObjectStreamSerializer()) {
        });
    }
}