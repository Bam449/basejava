package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exeption.StorageException;
import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.storage.serializer.StreamSerializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public abstract class AbstractFileStorage extends AbstractStorage<File> {

    private final StreamSerializer serializer;
    private final File storage;

    protected AbstractFileStorage(File storage, StreamSerializer serializer){
        Objects.requireNonNull(storage, "Directory must not be null");
        this.serializer = serializer;
        if (!storage.isDirectory()) throw new IllegalArgumentException(storage.getAbsolutePath() + " is not directory");
        if (!storage.canRead() || !storage.canWrite()) {
            throw new IllegalArgumentException(storage.getAbsolutePath() + " is not readable/writable");
        }
        this.storage = storage;
    }

    @Override
    protected File getSearchKey(String uuid) {
        return new File(storage, uuid);
    }

    @Override
    protected boolean isExist(File searchKey) {
        return searchKey.exists();
    }

    @Override
    protected void doSave(Resume r, File searchKey) {
        try {
            serializer.doWrite(r, new FileOutputStream(searchKey));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected Resume doGet(File searchKey) {
        try {
            return serializer.doRead(new FileInputStream(searchKey));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doUpdate(Resume r, File searchKey) {
        doDelete(searchKey);
        doSave(r, searchKey);
    }

    @Override
    protected void doDelete(File searchKey) {
        if (!searchKey.delete()) {
            throw new StorageException("File delete error", searchKey.getName());
        }
    }

    @Override
    protected List<Resume> doCopyAll() {
        List<Resume> list = new ArrayList<>();
        listFilesFunction(file -> list.add(doGet(file)));
        return list;
    }

    @Override
    public void clear() {
        listFilesFunction(file -> {
            doDelete(file);
            return null;
        });
    }

    @Override
    public int size() {
        String[] files = storage.list();
        if (files == null) throw new StorageException("Directory read error", null);
        return files.length;
    }

    private <T> void listFilesFunction(Function<File, T> function) {
        for (File t : Objects.requireNonNull(storage.listFiles())) {
            function.apply(t);
        }
    }
}
