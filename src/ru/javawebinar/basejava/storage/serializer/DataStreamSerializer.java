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

public class DataStreamSerializer implements StreamSerializer {

    @Override
    public void doWrite(Resume r, OutputStream outputStream) throws IOException {
        try (DataOutputStream dos = new DataOutputStream(outputStream)) {
            dos.writeUTF(r.getUuid());
            dos.writeUTF(r.getFullName());
            writeItems(dos, r.getContacts().entrySet(), entry -> {
                dos.writeUTF(entry.getKey().name());
                dos.writeUTF(entry.getValue());
            });
            writeItems(dos, r.getSections().entrySet(), entry -> {
                SectionType sectionType = entry.getKey();
                Section section = entry.getValue();
                dos.writeUTF(sectionType.name());
                switch (sectionType) {
                    case PERSONAL, OBJECTIVE -> dos.writeUTF(((TextSection) section).getContent());
                    case ACHIEVEMENT, QUALIFICATIONS -> writeItems(dos, ((ListSection) section).getItems(), dos::writeUTF);
                    case EXPERIENCE, EDUCATION -> writeItems(dos, ((OrganizationSection) section).getOrganizations(), organization -> {
                        Link link = organization.getHomePage();
                        dos.writeUTF(link.getName());
                        dos.writeUTF(link.getUrl());
                        writeItems(dos, organization.getPositions(), position -> {
                            writeLocalDate(dos, position.getStartDate());
                            writeLocalDate(dos, position.getEndDate());
                            dos.writeUTF(position.getTitle());
                            dos.writeUTF(position.getDescription());
                        });
                    });
                }
            });
        }
    }

    @Override
    public Resume doRead(InputStream inputStream) throws IOException {
        try (DataInputStream dis = new DataInputStream(inputStream)) {
            String uuid = dis.readUTF();
            String fullName = dis.readUTF();
            Resume resume = new Resume(uuid, fullName);
            readItems(dis, () -> resume.addContact(ContactType.valueOf(dis.readUTF()), dis.readUTF()));
            readItems(dis, () -> {
                SectionType sectionType = SectionType.valueOf(dis.readUTF());
                switch (sectionType) {
                    case PERSONAL, OBJECTIVE -> resume.addSection(sectionType, new TextSection(dis.readUTF()));
                    case ACHIEVEMENT, QUALIFICATIONS -> resume.addSection(sectionType, new ListSection(readList(dis, dis::readUTF)));
                    case EXPERIENCE, EDUCATION -> resume.addSection(sectionType, new OrganizationSection(readList(dis, () ->
                            new Organization(new Link(dis.readUTF(), dis.readUTF()), readList(dis, () ->
                                    new Organization.Position(readLocalDate(dis), readLocalDate(dis), dis.readUTF(), dis.readUTF()))))));
                }
            });
            return resume;
        }
    }

    private <T> void writeItems(DataOutputStream dos, Collection<T> collection, DataStreamConsumer<T> dsc) throws IOException {
        dos.writeInt(collection.size());
        for (T t : collection) {
            dsc.accept(t);
        }
    }

    @FunctionalInterface
    interface DataStreamConsumer<T> {
        void accept(T t) throws IOException;
    }

    @FunctionalInterface
    interface DataStreamSupplier<T> {
        T get() throws IOException;
    }

    @FunctionalInterface
    interface ElementProcess {
        void process() throws IOException;
    }

    private <T> List<T> readList(DataInputStream dis, DataStreamSupplier<T> dss) throws IOException {
        int size = dis.readInt();
        List<T> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            list.add(dss.get());
        }
        return list;
    }

    private void readItems(DataInputStream dis, ElementProcess elementProcess) throws IOException {
        int size = dis.readInt();
        for (int i = 0; i < size; i++) {
            elementProcess.process();
        }
    }

    private void writeLocalDate(DataOutputStream dos, LocalDate ldt) throws IOException {
        dos.writeInt(ldt.getYear());
        dos.writeInt(ldt.getMonthValue());
        dos.writeInt(ldt.getDayOfMonth());
    }

    private LocalDate readLocalDate(DataInputStream dis) throws IOException {
        return LocalDate.of(dis.readInt(), dis.readInt(), dis.readInt());
    }
}
