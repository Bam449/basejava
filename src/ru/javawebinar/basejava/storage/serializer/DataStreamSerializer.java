package ru.javawebinar.basejava.storage.serializer;

import ru.javawebinar.basejava.exeption.StorageException;
import ru.javawebinar.basejava.model.*;
import ru.javawebinar.basejava.util.DateUtil;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DataStreamSerializer implements StreamSerializer {

    @Override
    public void doWrite(Resume r, OutputStream outputStream) throws IOException {
        try (DataOutputStream dos = new DataOutputStream(outputStream)) {
            dos.writeUTF(r.getUuid());
            dos.writeUTF(r.getFullName());
            writeCollection(r.getContacts().entrySet(), dos, (entry) -> {
                dos.writeUTF(entry.getKey().name());
                dos.writeUTF(entry.getValue());
            });
            writeCollection(r.getSections().entrySet(), dos, (entry) -> {
                SectionType type = entry.getKey();
                Section section = entry.getValue();
                dos.writeUTF(type.name());
                switch (type) {
                    case OBJECTIVE, PERSONAL -> dos.writeUTF(((TextSection) section).getContent());
                    case ACHIEVEMENT, QUALIFICATIONS ->
                            writeCollection(((ListSection) section).getItems(), dos, dos::writeUTF);
                    case EXPERIENCE, EDUCATION ->
                            writeCollection(((OrganizationSection) section).getOrganizations(), dos, (organization -> {
                                dos.writeUTF(organization.getHomePage().getName());
                                dos.writeUTF(organization.getHomePage().getUrl());
                                writeCollection(organization.getPositions(), dos, position -> {
                                    writeLocalDate(dos, position.getStartDate());
                                    writeLocalDate(dos, position.getEndDate());
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
            readItems(dis, () -> resume.setContact(ContactType.valueOf(dis.readUTF()), dis.readUTF()));
            readItems(dis, () -> {
                SectionType type = SectionType.valueOf(dis.readUTF());
                Section section = null;
                switch (type) {
                    case OBJECTIVE, PERSONAL -> section = new TextSection(dis.readUTF());
                    case ACHIEVEMENT, QUALIFICATIONS -> section = new ListSection(readList(dis, dis::readUTF));
                    case EXPERIENCE, EDUCATION -> section = new OrganizationSection(readList(dis, () -> {
                        Link link = new Link(dis.readUTF(), dis.readUTF());
                        return new Organization(link, readList(dis, () -> new Organization.Position(readLocalDate(dis), readLocalDate(dis), dis.readUTF(), dis.readUTF())));
                    }));
                }
                resume.setSection(type, section);
            });
            return resume;
        } catch (IOException e) {
            throw new StorageException("Error read Resume", e.toString());
        }
    }

    private void readItems(DataInputStream dis, MyAccepter accepter) throws IOException {
        int size = dis.readInt();
        for (int i = 0; i < size; i++) {
            accepter.accept();
        }
    }

    private void writeLocalDate(DataOutputStream dos, LocalDate date) throws IOException {
        dos.writeInt(date.getYear());
        dos.writeInt(date.getMonthValue());
    }

    private LocalDate readLocalDate(DataInputStream dis) throws IOException {
        return DateUtil.of(dis.readInt(), Month.of(dis.readInt()));
    }

    private <T> List<T> readList(DataInputStream dis, MySupplier<T> supplier) throws IOException {
        int size = dis.readInt();
        List<T> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            list.add(supplier.get());
        }
        return list;
    }

    private <T> void writeCollection(Collection<T> collection, DataOutputStream dos, MyConsumer<T> consumer) throws IOException {
        dos.writeInt(collection.size());
        for (T t : collection) {
            consumer.accept(t);
        }
    }

    private interface MySupplier<T> {
        T get() throws IOException;
    }

    private interface MyConsumer<T> {
        void accept(T t) throws IOException;
    }

    private interface MyAccepter {
        void accept() throws IOException;
    }
}
