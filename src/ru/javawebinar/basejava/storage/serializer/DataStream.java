package ru.javawebinar.basejava.storage.serializer;

import ru.javawebinar.basejava.model.Resume;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DataStream implements StreamSerializer{

    @Override
    public void doWrite(Resume resume, OutputStream outputStream) throws IOException {
        try (DataOutputStream dos = new DataOutputStream(outputStream)){
            dos.writeUTF(resume.getUuid());
            dos.writeUTF(resume.getFullName());
        } catch (IOException e) {
            e.toString();
        }
    }

    @Override
    public Resume doRead(InputStream inputStream) throws IOException {
        return null;
    }
}
