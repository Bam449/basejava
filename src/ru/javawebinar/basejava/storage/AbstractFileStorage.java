package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;

public abstract class AbstractFileStorage extends AbstractStorage<File> {

    private final File directory = new File("/filestorage");

    protected abstract void doSave(Resume resume, OutputStream outputStream);
    protected abstract Resume doRead(FileInputStream fileInputStream);

    @Override
    protected File getIndex(String uuid) {
        return new File (directory, uuid);
    }

    @Override
    protected boolean isExist(File file) {
        return file.exists();
    }

    @Override
    protected void insertElement(Resume resume, File index) {
        doSave(resume, new FileOutputStream(index));
    }

    @Override
    protected Resume getElement(File index) {
        return doRead(new FileInputStream(index));
    }

    @Override
    protected void updateElement(Resume resume, File index) {

    }

    @Override
    protected void deleteElement(File index) {
        index.delete();
    }

    @Override
    protected List<Resume> getList() {
        return List.of();
    }

    @Override
    public void clear() {
        directory.delete();
    }

    @Override
    public int size() {
        return directory.listFiles().length;
    }
}
