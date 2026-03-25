package ru.javawebinar.basejava.storage.serializer;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import ru.javawebinar.basejava.model.Resume;

import java.io.InputStream;
import java.io.OutputStream;

public class XmlStream implements StreamSerializer {

    private final JAXBContext context;

    public XmlStream() {
        try {
            context = JAXBContext.newInstance(Resume.class);
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Resume doRead(InputStream inputStream) {
        try {
            Unmarshaller unmarshaller = context.createUnmarshaller();
            return (Resume) unmarshaller.unmarshal(inputStream);
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void doWrite(Resume resume, OutputStream outputStream) {
        try {
            Marshaller marshaller = context.createMarshaller();
            marshaller.marshal(resume, outputStream);
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }
}
