package ru.javawebinar.basejava.storage.serializer;

import jakarta.xml.bind.JAXBContext;
import ru.javawebinar.basejava.model.Resume;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class XmlStream implements StreamSerializer{
    JAXBContext context;

    @Override
    public Resume doRead(InputStream inputStream) throws IOException {
        return null;
    }

    @Override
    public void doWrite(Resume resume, OutputStream outputStream) throws IOException {

    }
}
