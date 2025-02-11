package ru.javawebinar.basejava.storage.serializer;

import ru.javawebinar.basejava.exeption.StorageException;
import ru.javawebinar.basejava.model.Resume;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.function.Consumer;

public class DataStreamSerializer implements StreamSerializer {


    @Override
    public void doWrite(Resume r, OutputStream outputStream) throws IOException {
        try (DataOutputStream dos = new DataOutputStream(outputStream)){
            dos.writeUTF(r.getUuid());
            dos.writeUTF(r.getFullName());

            int size = r.getContacts().size();
            dos.writeInt(size);
            for (int i = 0; i < size; i++) {

            }
        }
    }

    @Override
    public Resume doRead(InputStream inputStream) {
        try (DataInputStream dis = new DataInputStream(inputStream)) {
            return (Resume) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new StorageException("Error read Resume", e.toString());
        }
    }

    private T void saveMap(Map<K,V> map, );

    private <T> void MyConsumer(Consumer<T> consumer, T t) {
        consumer.accept(t);
    }
}
