package ru.javawebinar.basejava.storage.serializer;

import ru.javawebinar.basejava.model.Resume;

import java.io.InputStream;
import java.io.OutputStream;

public class XmlStreamSerializer implements StreamSerializer{
    @Override
    public void doWrite(Resume r, OutputStream outputStream){

    }

    @Override
    public Resume doRead(InputStream inputStream) {
        return null;
    }
}
