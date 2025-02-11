package ru.javawebinar.basejava.storage.serializer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.javawebinar.basejava.exeption.StorageException;
import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.model.Section;
import ru.javawebinar.basejava.util.SectionAdapter;
import ru.javawebinar.basejava.util.LocalDateAdapter;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.time.LocalDate;

public class GsonStreamSerializer implements StreamSerializer {

    Gson gson;

    public GsonStreamSerializer() {
        gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .registerTypeAdapter(Section.class, new SectionAdapter<>())
                .create();
    }

    @Override
    public void doWrite(Resume r, OutputStream outputStream) {

        try (Writer writer = new OutputStreamWriter(outputStream)) {
            gson.toJson(r, writer);
        } catch (IOException e) {
            throw new StorageException(e.getMessage(), null);
        }

    }

    @Override
    public Resume doRead(InputStream inputStream) {
        try (Reader reader = new InputStreamReader(inputStream)) {
            return gson.fromJson(reader, Resume.class);
        } catch (IOException e) {
            throw new StorageException(e.getMessage(), null);
        }
    }
}
