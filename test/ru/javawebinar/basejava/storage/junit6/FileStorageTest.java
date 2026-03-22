package ru.javawebinar.basejava.storage.junit6;

import ru.javawebinar.basejava.storage.FileStorage;
import ru.javawebinar.basejava.storage.serializer.ObjectStream;

class FileStorageTest extends AbstractStorageTest{

    public FileStorageTest() {
        super(new FileStorage(new ObjectStream()));
    }
}