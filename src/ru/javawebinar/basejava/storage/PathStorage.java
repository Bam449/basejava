package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.storage.serializer.Serializer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class PathStorage extends AbstractStorage<Path> {

    private final Path directory;
    private final Serializer serializer;

    protected PathStorage(Path directory, Serializer serializer) {
        Objects.requireNonNull(directory, "Directory must not be null");
        if (!Files.isDirectory(directory)) {
            throw new IllegalArgumentException(directory.getFileName().toString() + "is not directory");
        }
        if (!Files.isReadable(directory) || !Files.isWritable(directory)) {
            throw new IllegalArgumentException(directory.getFileName().toString() + "is not readable/writable");
        }
        this.directory = directory;
        this.serializer = serializer;
    }

    @Override
    protected boolean isExist(Path searchKey) {
        return Files.exists(searchKey);
    }

    @Override
    protected Path getSearchKey(String uuid) {
        return directory.resolve(uuid);
    }

    @Override
    protected void insertElement(Path index, Resume resume) {
        updateElement(index, resume);
    }

    @Override
    protected void deleteElement(Path index) {
        try {
            Files.delete(index);
        } catch (IOException e) {
            throw new StorageException("File is not delete");
        }
    }

    @Override
    protected Resume getElement(Path searchKey) {
        try {
            return serializer.doRead(Files.newInputStream(searchKey));
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void updateElement(Path searchKey, Resume resume) {
        try {
            serializer.doWrite(resume, Files.newOutputStream(searchKey));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected List<Resume> getList() {
        List<Resume> list = new ArrayList<>();
            getFilesList().map(this::getElement).forEach((list::add));
        return list;
    }

    @Override
    public void clear() {
            getFilesList().forEach(this::deleteElement);
    }

    @Override
    public int size() {
        return (int) getFilesList().count();
    }

    private Stream<Path> getFilesList() {
        Stream<Path> stream;
        try {
            stream = Files.list(directory);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return stream;
    }
}
