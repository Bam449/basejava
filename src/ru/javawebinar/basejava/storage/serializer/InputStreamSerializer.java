package ru.javawebinar.basejava.storage.serializer;

import ru.javawebinar.basejava.model.ContactType;
import ru.javawebinar.basejava.model.Link;
import ru.javawebinar.basejava.model.ListSection;
import ru.javawebinar.basejava.model.Organization;
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class InputStreamSerializer implements Serializer {

    @Override
    public Resume doRead(InputStream is) throws IOException {
        Resume resume;
        try (DataInputStream dis = new DataInputStream(is)) {
            resume = new Resume(dis.readUTF(), dis.readUTF());
            int size = dis.readInt();
            for (int i = 0; i < size; i++) {
                ContactType type = ContactType.valueOf(dis.readUTF());
                String value = dis.readUTF();
                resume.addContact(type, value);
            }
            size = dis.readInt();
            for (int i = 0; i < size; i++) {
                String string = dis.readUTF();
                SectionType type = SectionType.valueOf(string);
                Section section = null;
                switch (type) {
                    case PERSONAL, OBJECTIVE -> section = new TextSection(dis.readUTF());
                    case ACHIEVEMENT, QUALIFICATIONS -> section = new ListSection(readList(dis, dis::readUTF));
                    case EXPERIENCE, EDUCATION ->
                            section = new OrganizationSection(readList(dis, () -> new Organization(new Link(dis.readUTF(), dis.readUTF()),
                                    readList(dis, () -> new Organization.Position(
                                            LocalDate.parse(dis.readUTF()),
                                            LocalDate.parse(dis.readUTF()),
                                            dis.readUTF(), dis.readUTF()
                                    ))
                            )));
                }
                resume.addSection(type, section);
            }
        }
        return resume;
    }

    @Override
    public void doWrite(Resume resume, OutputStream os) throws IOException {
        try (DataOutputStream dos = new DataOutputStream(os)) {
            dos.writeUTF(resume.getUuid());
            dos.writeUTF(resume.getFullName());
            writeItems(resume.getContacts().entrySet(), dos, entry -> {
                dos.writeUTF(entry.getKey().name());
                dos.writeUTF(entry.getValue());
            });
            writeItems(resume.getSections().entrySet(), dos, enty -> {
                        SectionType type = enty.getKey();
                        dos.writeUTF(type.name());
                        Section section = enty.getValue();
                        switch (type) {
                            case PERSONAL, OBJECTIVE -> dos.writeUTF(((TextSection) section).getContent());
                            case ACHIEVEMENT, QUALIFICATIONS ->
                                    writeItems(((ListSection) section).getItems(), dos, dos::writeUTF);

                            case EXPERIENCE, EDUCATION -> writeItems(((OrganizationSection) section).getOrganizations(),
                                    dos,
                                    organization -> {
                                        dos.writeUTF(organization.getHomePage().getName());
                                        dos.writeUTF(organization.getHomePage().getUrl());
                                        writeItems(organization.getPositions(),
                                                dos,
                                                position -> {
                                                    dos.writeUTF(position.getStartDate().toString());
                                                    dos.writeUTF(position.getEndDate().toString());
                                                    dos.writeUTF(position.getTitle());
                                                    dos.writeUTF(position.getDescription());
                                                });
                                    }
                            );
                        }
                    }
            );
        }
    }

    private <T> void writeItems(Collection<T> set, DataOutputStream dos, MyConsumer<T> consumer) throws IOException {
        dos.writeInt(set.size());
        for (T item : set) {
            consumer.accept(item);
        }
    }

    private <T> List<T> readList(DataInputStream dis, MySupplier<T> supplier) throws IOException {
        List<T> list = new ArrayList<>();
        int size = dis.readInt();
        for (int i = 0; i < size; i++) {
            list.add(supplier.accept());
        }
        return list;
    }

    private interface MyConsumer<T> {
        void accept(T t) throws IOException;
    }

    private interface MySupplier<T> {
        T accept() throws IOException;
    }
}
