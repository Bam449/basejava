package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.io.File;
import java.util.List;

public class AbstractFileStorage extends AbstractStorage <File>{
    private final File directory;

    @Override
    protected Resume doGet(File key) {
        return null;
    }

    @Override
    protected boolean isExist(File key) {
        return false;
    }

    @Override
    protected void saveKey(Resume resume, File key) {

    }

    @Override
    protected File getSearchKey(String uuid) {
        return null;
    }

    @Override
    protected void updateKey(Resume resume, File key) {

    }

    @Override
    protected void deleteKey(File key) {

    }

    @Override
    protected List<Resume> getList() {
        return List.of();
    }

    @Override
    public void clear() {

    }

    @Override
    public int size() {
        return 0;
    }
}
