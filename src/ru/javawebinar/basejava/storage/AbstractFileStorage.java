package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public abstract class AbstractFileStorage extends AbstractStorage<File> {

    private final File directory;

    protected AbstractFileStorage(File directory) {
        Objects.requireNonNull(directory, "Directory must not be null");
        if (!directory.isDirectory()) {
            throw new IllegalArgumentException(directory.getAbsolutePath() + "is not directory");
        }
        if (!directory.canRead() || !directory.canWrite()) {
            throw new IllegalArgumentException(directory.getAbsolutePath() + "is not readable/writable");
        }
        this.directory = directory;
    }

    @Override
    protected boolean isExist(File searchKey) {
        return searchKey.exists();
    }

    @Override
    protected File getSearchKey(String uuid) {
        return new File(directory, uuid);
    }

    @Override
    protected void insertElement(File index, Resume resume) {
        doWrite(index, resume);
    }

    @Override
    protected void deleteElement(File index) {
        if (!index.delete()) {
            throw new StorageException("File is not delete");
        }
    }

    @Override
    protected Resume getElement(File searchKey) {
        return doRead(searchKey);
    }


    @Override
    protected void updateElement(File searchKey, Resume resume) {
        doWrite(searchKey, resume);
    }

    @Override
    protected List<Resume> getList() {
        List<Resume> list = new ArrayList<>();
        Arrays.stream(Objects.requireNonNull(directory.listFiles())).map(this::doRead).forEach((list::add));
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

    protected abstract void doWrite(File index, Resume resume);

    protected abstract Resume doRead(File searchKey);

}
