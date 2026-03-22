package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.storage.serializer.StreamSerializer;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FileStorage extends AbstractStorage<File> {

    private final StreamSerializer serializer;

    private final File directory = new File("filestorage");

    public FileStorage(StreamSerializer serializer) {
        this.serializer = serializer;
    }

    @Override
    protected File getIndex(String uuid) {
        return new File (directory, uuid);
    }

    @Override
    protected boolean isExist(File file) {
        return file.exists();
    }

    @Override
    protected void insertElement(Resume resume, File file) {
        try {
            serializer.doWrite(resume, new FileOutputStream(file));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected Resume getElement(File file) {
        try {
            return serializer.doRead(new FileInputStream(file));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void updateElement(Resume resume, File file) {
        deleteElement(file);
        insertElement(resume, file);
    }

    @Override
    protected void deleteElement(File file) {
        if (!file.delete()) {
            throw new StorageException("File not delete", file.toString());
        }
    }

    @Override
    protected List<Resume> getList() {
        List<Resume> list = new ArrayList<>();
        for (File file : getListFiles()) {
            try {
                serializer.doRead(new FileInputStream(file));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return list;
    }

    @Override
    public void clear() {
        for (File file : getListFiles()) {
            deleteElement(file);
        }
    }

    @Override
    public int size() {
        return getListFiles().length;
    }

    private File[] getListFiles() {
        return Objects.requireNonNull(directory.listFiles());
    }
}
