package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

public interface Storage {

    void save(Resume r);
    Resume get(String uuid);
    void update (Resume r);
    void delete(String uuid);
    void clear();
    int size();
    Resume[] getAll();


}
