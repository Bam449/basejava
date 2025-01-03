package ru.javawebinar.basejava.model;

import java.util.UUID;

/**
 * Initial resume class
 */
public class Resume implements Comparable<Resume>{

    // Unique identifier
    private String uuid;

    public Resume(String uuid) {
        this.uuid = uuid;
    }

    public Resume() {
        this(UUID.randomUUID().toString());
    }

    @Override
    public String toString() {
        return uuid;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Resume resume = (Resume) o;
        return uuid.equals(resume.uuid);
    }

    @Override
    public int hashCode() {
        return uuid.hashCode();
    }

    @Override
    public int compareTo(Resume o) {
        return uuid.compareTo(o.uuid);
    }
}
