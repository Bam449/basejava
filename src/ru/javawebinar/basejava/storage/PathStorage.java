package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.storage.serializer.StreamSerializer;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class PathStorage extends AbstractStorage<Path> {

    private final Path storage;
    private final StreamSerializer serializer;

    public PathStorage(StreamSerializer serializer, Path storage) {
        Objects.requireNonNull(storage, "Directory must not be null");
        this.serializer = serializer;
        if (!Files.isDirectory(storage))
            throw new IllegalArgumentException(storage.toAbsolutePath() + " is not directory");
        if (!Files.isWritable(storage) || !Files.isReadable(storage))
            throw new IllegalArgumentException(storage.toAbsolutePath() + " is not readable/writable");
        this.storage = storage;
    }


    @Override
    protected Path getSearchKey(String uuid) {
        return Paths.get(storage.toAbsolutePath().toString(), uuid);
    }

    @Override
    protected boolean isExist(Path searchKey) {
        return Files.exists(searchKey);
    }

    @Override
    protected void doSave(Resume r, Path searchKey) {
        try {
            serializer.doWrite(r, new BufferedOutputStream(Files.newOutputStream(searchKey)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected Resume doGet(Path searchKey) {
        try {
            return serializer.doRead(new BufferedInputStream(Files.newInputStream(searchKey)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doUpdate(Resume r, Path searchKey) {
        doSave(r, searchKey);
    }

    @Override
    protected void doDelete(Path searchKey) {
        try {
            Files.delete(searchKey);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected List<Resume> doCopyAll() {
        return new ArrayList<>(getStreamDirectory().map(this::doGet).toList());
    }

    @Override
    public void clear() {
        getStreamDirectory().forEach(this::doDelete);
    }

    @Override
    public int size() {
        return (int) getStreamDirectory().count();
    }

    private Stream<Path> getStreamDirectory() {
        try {
            return Files.list(storage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
