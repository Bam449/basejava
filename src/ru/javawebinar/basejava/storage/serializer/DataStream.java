package ru.javawebinar.basejava.storage.serializer;

import ru.javawebinar.basejava.model.Link;
import ru.javawebinar.basejava.model.ListSection;
import ru.javawebinar.basejava.model.OrganizationSection;
import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.model.SectionType;
import ru.javawebinar.basejava.model.TextSection;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;

public class DataStream implements StreamSerializer {

    @Override
    public void doWrite(Resume resume, OutputStream outputStream) throws IOException {
        try (DataOutputStream dos = new DataOutputStream(outputStream)) {
            dos.writeUTF(resume.getUuid());
            dos.writeUTF(resume.getFullName());
            writeCollection(resume.getContacts().entrySet(), dos, (entry) -> {
                        try {
                            dos.writeUTF(entry.getKey().name());
                            dos.writeUTF(entry.getValue());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
            );
            writeCollection(resume.getSections().entrySet(), dos, (entry) -> {
                        try {
                            SectionType type = entry.getKey();
                            dos.writeUTF(type.name());
                            switch (type) {
                                case PERSONAL, OBJECTIVE -> {
                                    dos.writeUTF(((TextSection) entry.getValue()).getContent());
                                }
                                case ACHIEVEMENT, QUALIFICATIONS -> {
                                    writeCollection(((ListSection) entry.getValue()).getItems(), dos, dos::writeUTF);
                                }
                                case EXPERIENCE, EDUCATION ->
                                    writeCollection(((OrganizationSection) entry.getValue()).getOrganizations(), dos, (organization) -> {
                                                Link link = organization.getHomePage();
                                                dos.writeUTF(link.getName());
                                                dos.writeUTF(link.getUrl());
                                                writeCollection(organization.getPositions(), dos, (position) -> {
                                                    dos.writeUTF(position.getStartDate().toString());
                                                    dos.writeUTF(position.getEndDate().toString());
                                                    dos.writeUTF(position.getTitle());
                                                    dos.writeUTF(position.getDescription());
                                                });
                                            }
                                    );
                            }

                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Resume doRead(InputStream inputStream) throws IOException {
        try (DataInputStream dis = new DataInputStream(inputStream)) {
            Resume resume = new Resume(dis.readUTF(), dis.readUTF());
            resume.setContact();
            return resume;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private <T> void writeCollection(Collection<T> collection, DataOutputStream dos, MyConsumer<T> consumer) throws IOException {
        dos.writeInt(collection.size());
        for (T item : collection) {
            consumer.accept(item);
        }
    }

    private <T> Collection <T> readCollection(DataInputStream dis, MySupplier<T> supplier) throws IOException {
        int size = dis.readInt();
        for (int i = 0; i < size; i++) {
            supplier.accept();
        }
    }

    interface MyConsumer<T> {
        void accept(T consumer) throws IOException;
    }

    interface MySupplier<T> {
        void accept () throws IOException;
    }
}
