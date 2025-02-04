package ru.javawebinar.basejava.exeption;

public class NotExistStorageException extends StorageException{

    public NotExistStorageException(String uuid) {
        super("Resume " + uuid + " not exist", uuid);
    }
}
