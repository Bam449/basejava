package ru.javawebinar.basejava.storage.serializer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.util.GsonLocalDateAdapter;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.time.LocalDate;

public class GsonStream implements StreamSerializer {
    private final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(LocalDate.class, new GsonLocalDateAdapter())
            .create();

    @Override
    public Resume doRead(InputStream inputStream) {
        Reader reader = new InputStreamReader(inputStream);
        return gson.fromJson(reader, Resume.class);
    }

    @Override
    public void doWrite(Resume resume, OutputStream outputStream){
        Writer writer = new OutputStreamWriter(outputStream);
        gson.toJson(resume, writer);
    }
}
