package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.storage.serializer.Serializer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class PathStorage extends AbstractStorage<Path> {
    private final Path storage;
    private final Serializer serializer;

    public PathStorage(Path storage, Serializer serializer) {
        if (!Files.isDirectory(storage)) throw new StorageException(storage + "is not directory", null);
        this.serializer = serializer;
        this.storage = storage;
    }

    @Override
    protected Resume doGet(Path key) {
        try {
            return serializer.doRead(Files.newInputStream(key));
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected boolean isExist(Path key) {
        return Files.exists(key);
    }

    @Override
    protected void saveKey(Resume resume, Path key) {
        try {
            serializer.doWrite(resume, Files.newOutputStream(key));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected Path getSearchKey(String uuid) {
        return storage.resolve(uuid);
    }

    @Override
    protected void updateKey(Resume resume, Path key) {
        saveKey(resume, key);
    }

    @Override
    protected void deleteKey(Path key) {
        try {
            Files.delete(key);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected List<Resume> getList() {
        List<Resume> list = new ArrayList<>();
        filesList(path -> list.add(doGet(path)));
        return list;
    }

    @Override
    public void clear() {
        filesList(this::deleteKey);
    }

    @Override
    public int size() {
        try (Stream<Path> stream = Files.list(storage)) {
            return (int) stream.count();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void filesList(Consumer<Path> consumer) {
        try (Stream<Path> stream = Files.list(storage)) {
            stream.forEach(consumer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
