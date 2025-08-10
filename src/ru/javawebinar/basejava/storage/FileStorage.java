package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.storage.serializer.Serializer;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class FileStorage extends AbstractStorage<File> {

    private final File directory;
    private final Serializer serializer;

    protected FileStorage(File directory, Serializer serializer) {
        Objects.requireNonNull(directory, "Directory must not be null");
        if (!directory.isDirectory()) {
            throw new IllegalArgumentException(directory.getAbsolutePath() + "is not directory");
        }
        if (!directory.canRead() || !directory.canWrite()) {
            throw new IllegalArgumentException(directory.getAbsolutePath() + "is not readable/writable");
        }
        this.directory = directory;
        this.serializer = serializer;
    }

    @Override
    protected boolean isExist(File file) {
        return file.exists();
    }

    @Override
    protected File getSearchKey(String uuid) {
        return new File(directory, uuid);
    }

    @Override
    protected void insertElement(File file, Resume resume) {
        updateElement(file, resume);
    }

    @Override
    protected void deleteElement(File index) {
        if (!index.delete()) {
            throw new StorageException("File is not delete");
        }
    }

    @Override
    protected Resume getElement(File searchKey) {
        try {
            return serializer.doRead(new BufferedInputStream(new FileInputStream(searchKey)));
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void updateElement(File searchKey, Resume resume) {
        try {
            serializer.doWrite(resume, new BufferedOutputStream(new FileOutputStream(searchKey)));
        } catch (IOException e) {
            throw new StorageException("File not be update", e);
        }
    }

    @Override
    protected List<Resume> getList() {
        List<Resume> list = new ArrayList<>();
        Arrays.stream(Objects.requireNonNull(directory.listFiles())).map(this::getElement).forEach((list::add));
        return list;
    }

    @Override
    public void clear() {
        Arrays.stream(Objects.requireNonNull(directory.listFiles())).forEach(this::deleteElement);
    }

    @Override
    public int size() {
        return Objects.requireNonNull(directory.list()).length;
    }
}
