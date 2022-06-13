package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.ContactType;
import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.model.Section;
import ru.javawebinar.basejava.model.SectionType;
import ru.javawebinar.basejava.sql.SqlHelper;
import ru.javawebinar.basejava.util.JsonParser;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class SqlStorage implements Storage {
    private final SqlHelper sqlHelper;

    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new StorageException(e);
        }
        sqlHelper = new SqlHelper(() -> DriverManager.getConnection(dbUrl, dbUser, dbPassword));
    }

    public SqlStorage(Properties properties) {
        this(properties.getProperty("db.url"),
                properties.getProperty("db.user"),
                properties.getProperty("db.password"));
    }

    @Override
    public void clear() {
        sqlHelper.executeSqlRequest("DELETE FROM resume", PreparedStatement::execute);
    }

    @Override
    public void save(Resume r) {
        sqlHelper.transactionalExecute(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO resume(uuid, full_name) VALUES (?, ?)");
            preparedStatement.setString(1, r.getUuid());
            preparedStatement.setString(2, r.getFullName());
            preparedStatement.execute();
            insertType(r, connection, r.getContacts(),  ContactType.class, String.class);
            insertType(r, connection, r.getSections(), SectionType.class, Section.class);
            return null;
        });
    }

    private <K, V> void insertType(Resume r, Connection connection, Map<K, V> map,  Class <K> kClass, Class <V> vClass)  {
        String type = vClass.getName().equals("java.lang.String") ? "contact(resume_uuid, type, value)" : "section(resume_uuid, type_section, content)";
        try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO " + type + " VALUES (?, ?, ?)")) {
            for (Map.Entry<K, V> entry : map.entrySet()) {
                preparedStatement.setString(1, r.getUuid());
                preparedStatement.setString(2, JsonParser.write(entry.getKey(), kClass));
                preparedStatement.setString(3, JsonParser.write(entry.getValue(), vClass));
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }

    @Override
    public void update(Resume r) {
        sqlHelper.transactionalExecute(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE resume SET full_name =? WHERE uuid =?");
            preparedStatement.setString(1, r.getFullName());
            preparedStatement.setString(2, r.getUuid());
            if (preparedStatement.executeUpdate() == 0) {
                throw new NotExistStorageException(r.getUuid());
            }
            deleteType(r, connection, true);
            insertType(r, connection, r.getContacts(), ContactType.class, String.class);
            deleteType(r, connection, false);
            insertType(r, connection, r.getSections(), SectionType.class, Section.class);
            return null;
        });
    }

    private void deleteType(Resume r, Connection connection, boolean contactsOrSections) {
       String type = contactsOrSections ? "contact" : "section";
        try (PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM " + type + " WHERE resume_uuid =?")) {
            preparedStatement.setString(1, r.getUuid());
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }

    private void addSection(ResultSet rs, Resume r) throws SQLException {
        String content = rs.getString("content");
        if (content != null) {
            String typeSection = rs.getString("type_section");
            SectionType sectionType = JsonParser.read(typeSection, SectionType.class);
            Section section = JsonParser.read(content, Section.class);
            r.addSection(sectionType, section);
        }
    }

    private void addContact(ResultSet rs, Resume r) throws SQLException {
        String value = rs.getString("value");
        if (value != null) {
            ContactType contactType = JsonParser.read(rs.getString("type"), ContactType.class);
            String contact = JsonParser.read(value, String.class);
            r.addContact(contactType, contact);
        }
    }

    @Override
    public void delete(String uuid) {
        sqlHelper.executeSqlRequest("DELETE FROM resume WHERE uuid =?", (ps) -> {
            if (ps.executeUpdate() == 0) {
                throw new NotExistStorageException(uuid);
            }
            return null;
        }, uuid);
    }
    @Override
    public Resume get(String uuid) {
        return sqlHelper.executeSqlRequest("SELECT * FROM resume r " +
                        "LEFT JOIN contact c on r.uuid = c.resume_uuid " +
                        "LEFT JOIN section s on r.uuid = s.resume_uuid " +
                        "WHERE r.uuid =?",

                ps -> {
                    ResultSet rs = ps.executeQuery();
                    if (!rs.next()) {
                        throw new NotExistStorageException("Resume " + uuid + " is not exist");
                    }
                    Resume r = new Resume(uuid, rs.getString("full_name"));
                    do {
                        addContact(rs, r);
                        addSection(rs, r);
                    }
                    while (rs.next());
                    return r;
                }, uuid);
    }
    @Override
    public List<Resume> getAllSorted() {
        return sqlHelper.executeSqlRequest("SELECT * FROM resume r " +
                        "LEFT JOIN contact c on r.uuid = c.resume_uuid " +
                        "LEFT JOIN section s on r.uuid = s.resume_uuid " +
                        "ORDER BY full_name, uuid",

                ps -> {
                    List<Resume> list = new ArrayList<>();
                    ResultSet rs = ps.executeQuery();
                    if (!rs.next()) {
                        throw new NotExistStorageException("Resume  is not exist");
                    }
                    Resume r = new Resume();
                    do {
                        String uuid = rs.getString("uuid");
                        if (!r.getUuid().equals(uuid)) {
                            r = new Resume(uuid, rs.getString("full_name"));
                            list.add(r);
                        }
                        addContact(rs, r);
                        addSection(rs, r);
                    }
                    while (rs.next());
                    return list;
                });
    }

    @Override
    public int size() {
        return sqlHelper.executeSqlRequest("SELECT COUNT(*) FROM resume ", (ps) -> {
            ResultSet rs = ps.executeQuery();
            return !rs.next() ? 0 : rs.getInt(1);
        });
    }
}
