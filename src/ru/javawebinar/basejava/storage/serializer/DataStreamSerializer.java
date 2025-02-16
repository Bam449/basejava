package ru.javawebinar.basejava.storage.serializer;

import ru.javawebinar.basejava.exeption.StorageException;
import ru.javawebinar.basejava.model.ListSection;
import ru.javawebinar.basejava.model.OrganizationSection;
import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.model.Section;
import ru.javawebinar.basejava.model.SectionType;
import ru.javawebinar.basejava.model.TextSection;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalDate;
import java.util.Collection;

public class DataStreamSerializer implements StreamSerializer {

    @Override
    public void doWrite(Resume r, OutputStream outputStream) throws IOException {
        try (DataOutputStream dos = new DataOutputStream(outputStream)) {
            dos.writeUTF(r.getUuid());
            dos.writeUTF(r.getFullName());
            saveCollection(r.getContacts().entrySet(), dos, (entry) -> {
                dos.writeUTF(entry.getKey().name());
                dos.writeUTF(entry.getValue());
            });
            saveCollection(r.getSections().entrySet(), dos, (entry) -> {
                SectionType type = entry.getKey();
                Section section = entry.getValue();
                dos.writeUTF(type.name());
                switch (type) {
                    case OBJECTIVE, PERSONAL -> dos.writeUTF(((TextSection) section).getContent());
                    case ACHIEVEMENT, QUALIFICATIONS ->
                            saveCollection(((ListSection) section).getItems(), dos, dos::writeUTF);
                    case EXPERIENCE, EDUCATION ->
                            saveCollection(((OrganizationSection) section).getOrganizations(), dos, (organization -> {
                                dos.writeUTF(organization.getHomePage().getName());
                                dos.writeUTF(organization.getHomePage().getUrl());
                                saveCollection(organization.getPositions(), dos, position -> {
                                    saveLocalDate(dos, position.getStartDate());
                                    saveLocalDate(dos, position.getEndDate());
                                    dos.writeUTF(position.getTitle());
                                    dos.writeUTF(position.getDescription());
                                });
                            }));
                }
            });
        }
    }

    @Override
    public Resume doRead(InputStream inputStream) {
        try (DataInputStream dis = new DataInputStream(inputStream)) {
            Resume resume = new Resume(dis.readUTF(), dis.readUTF());

            return resume;
        } catch (IOException e) {
            throw new StorageException("Error read Resume", e.toString());
        }
    }

    private void saveLocalDate(DataOutputStream dos, LocalDate date) throws IOException {
        dos.writeInt(date.getYear());
        dos.writeInt(date.getMonthValue());
    }

    private <T> void saveCollection(Collection<T> collection, DataOutputStream dos, MyConsumer<T> consumer) throws IOException {
        dos.writeInt(collection.size());
        for (T t : collection) {
            consumer.accept(t);
        }
    }

    public interface MyConsumer<T> {
        void accept(T t) throws IOException;
    }
}
