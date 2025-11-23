package ru.javawebinar.basejava.storage.serializer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.model.Section;
import ru.javawebinar.basejava.util.GsonAdapter;
import ru.javawebinar.basejava.util.LocalDateGsonAdapter;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.time.LocalDate;

public class GsonSerializer implements Serializer {
    private final Gson gson;

    public GsonSerializer() {
        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateGsonAdapter())
                .registerTypeAdapter(Section.class, new GsonAdapter<Section>())
                .create();
    }

    @Override
    public Resume doRead(InputStream is) throws IOException {
        try (InputStreamReader isr = new InputStreamReader(is)) {
            return gson.fromJson(isr, Resume.class);
        }
    }

    @Override
    public void doWrite(Resume resume, OutputStream os) throws IOException {
        try (OutputStreamWriter osw = new OutputStreamWriter(os)) {
            gson.toJson(resume, osw);
        }
    }
}
