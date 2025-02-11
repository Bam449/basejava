package ru.javawebinar.basejava.storage.serializer;

import ru.javawebinar.basejava.model.Link;
import ru.javawebinar.basejava.model.ListSection;
import ru.javawebinar.basejava.model.Organization;
import ru.javawebinar.basejava.model.OrganizationSection;
import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.model.TextSection;
import ru.javawebinar.basejava.util.XmlResume;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class XmlStreamSerializer implements StreamSerializer {

    private final XmlResume xmlAdapter;

    public XmlStreamSerializer() {
        xmlAdapter = new XmlResume(Resume.class, Organization.class, Link.class, OrganizationSection.class,
                TextSection.class, ListSection.class, Organization.Position.class);
    }

    @Override
    public void doWrite(Resume r, OutputStream outputStream) {
        xmlAdapter.marshall(r, new OutputStreamWriter(outputStream));
    }

    @Override
    public Resume doRead(InputStream inputStream) {
        return xmlAdapter.unmarshall(new InputStreamReader(inputStream));
    }
}
