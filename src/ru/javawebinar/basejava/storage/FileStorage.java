package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.storage.serializer.Serializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class FileStorage extends AbstractStorage <File>{
    private final File directory;
    private final Serializer serializer;

    public FileStorage(File directory, Serializer serializer) {
        if (!directory.isDirectory()) throw new StorageException("is not directory", null);
        this.directory = directory;
        this.serializer = serializer;
    }

    @Override
    protected Resume doGet(File key) {
        try {
            return serializer.doRead(new FileInputStream(key));
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected boolean isExist(File key) {
        return key.exists();
    }

    @Override
    protected void saveKey(Resume resume, File key) {
        try {
            serializer.doWrite(resume, new FileOutputStream(key));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected File getSearchKey(String uuid) {
        return new File(directory, uuid);
    }

    @Override
    protected void updateKey(Resume resume, File key) {
        saveKey(resume, key);
    }

    @Override
    protected void deleteKey(File key) {
        if (!key.delete()) {
            throw new StorageException("file not delete " + key, null);
        }
    }

    @Override
    protected List<Resume> getList() {
        List<Resume> list = new ArrayList<>();
        filesList(file -> list.add(doGet(file)));
        return list;
    }

    @Override
    public void clear() {
        filesList(this::deleteKey);
    }

    @Override
    public int size() {
        return Objects.requireNonNull(directory.listFiles()).length;
    }

    private void filesList(Consumer <File> t) {
        for (File file : Objects.requireNonNull(directory.listFiles())) {
            t.accept(file);
        }
    }
}
