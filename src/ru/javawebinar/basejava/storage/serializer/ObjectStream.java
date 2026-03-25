package ru.javawebinar.basejava.storage.serializer;

import ru.javawebinar.basejava.model.Resume;

import java.io.*;

public class ObjectStream implements StreamSerializer {

    @Override
    public Resume doRead(InputStream inputStream) throws IOException {
        try (ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(inputStream))) {
            return (Resume) ois.readObject();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void doWrite(Resume resume, OutputStream outputStream) throws IOException {
        try (ObjectOutputStream ous = new ObjectOutputStream(new BufferedOutputStream(outputStream))) {
            ous.writeObject(resume);
        }
    }
}
