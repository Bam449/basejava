package ru.javawebinar.basejava.exception;

public class StorageException extends RuntimeException{

    public StorageException(String message, String uuid) {
        super(message);
    }
}