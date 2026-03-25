package ru.javawebinar.basejava.storage.serializer;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import ru.javawebinar.basejava.model.Resume;

import java.io.InputStream;
import java.io.OutputStream;

public class XmlStream implements StreamSerializer {

    private final Marshaller marshaller;
    private final Unmarshaller unmarshaller;

    public XmlStream() {
        try {
            JAXBContext context = JAXBContext.newInstance(Resume.class);
            marshaller = context.createMarshaller();
            unmarshaller = context.createUnmarshaller();
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Resume doRead(InputStream inputStream) {
        try {
            return (Resume) unmarshaller.unmarshal(inputStream);
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void doWrite(Resume resume, OutputStream outputStream) {
        try {
            marshaller.marshal(resume, outputStream);
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }
}
