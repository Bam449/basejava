package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.storage.serializer.StreamSerializer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class PathStorage extends AbstractStorage<Path> {

    private final StreamSerializer serializer;

    private final Path directory;

    public PathStorage(StreamSerializer serializer, String dir) {
        Objects.requireNonNull(dir, "directory must not be null");
        this.serializer = serializer;
        this.directory = Paths.get(dir);
        if (!Files.isDirectory(directory) || !Files.isWritable(directory)) {
            throw new IllegalArgumentException(dir + " is not directory or is not writable");
        }
    }

    @Override
    protected Path getIndex(String uuid) {
        return directory.resolve(uuid);
    }

    @Override
    protected boolean isExist(Path path) {
        return Files.exists(path);
    }

    @Override
    protected void insertElement(Resume resume, Path path) {
        try (OutputStream ou = Files.newOutputStream(path)) {
            serializer.doWrite(resume, ou);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected Resume getElement(Path path) {
        try (InputStream is = Files.newInputStream(path)){
            return serializer.doRead(is);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void updateElement(Resume resume, Path path) {
        deleteElement(path);
        insertElement(resume, path);
    }

    @Override
    protected void deleteElement(Path path) {
        try {
            Files.delete(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected List<Resume> getList() {
        List<Resume> list = new ArrayList<>();
        getListFiles().forEach(path -> list.add(getElement(path)));
        return list;
    }

    @Override
    public void clear() {
        getListFiles().forEach(this::deleteElement);
    }

    @Override
    public int size() {
        return (int) getListFiles().count();
    }

    private Stream<Path> getListFiles() {
        try {
            return Files.list(directory);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
