package ru.javawebinar.basejava.storage.serializer;

import ru.javawebinar.basejava.exeption.StorageException;
import ru.javawebinar.basejava.model.Resume;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public class ObjectStreamSerializer implements StreamSerializer {


    @Override
    public void doWrite(Resume r, OutputStream outputStream) throws IOException {
        try (ObjectOutputStream ous = new ObjectOutputStream(outputStream)){
            ous.writeObject(r);
        }
    }

    @Override
    public Resume doRead(InputStream inputStream) {
        try (ObjectInputStream ois = new ObjectInputStream(inputStream)) {
            return (Resume) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new StorageException("Error read Resume", e.toString());
        }
    }
}
