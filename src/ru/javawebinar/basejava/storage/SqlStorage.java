package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.ContactType;
import ru.javawebinar.basejava.model.ListSection;
import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.model.Section;
import ru.javawebinar.basejava.model.SectionType;
import ru.javawebinar.basejava.model.TextSection;
import ru.javawebinar.basejava.sql.SqlHelper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

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
            insertContact(r, connection);
            insertSection(r, connection);
            return null;
        });
    }

    private void insertSection(Resume r, Connection connection) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO section(resume_uuid, typesection, content) VALUES (?, ?, ?)")) {
            for (Map.Entry<SectionType, Section> entry : r.getSections().entrySet()) {
                SectionType type = entry.getKey();
                String stringSection = null;
                switch (type) {
                    case PERSONAL, OBJECTIVE -> stringSection = ((TextSection) entry.getValue()).getContent();
                    case ACHIEVEMENT, QUALIFICATIONS -> stringSection = ((ListSection) entry.getValue()).getItems().stream()
                            .map(s -> s + System.lineSeparator())
                            .collect(Collectors.joining());
                }
                if (stringSection != null) {
                    preparedStatement.setString(1, r.getUuid());
                    preparedStatement.setString(2, entry.getKey().name());
                    preparedStatement.setString(3, stringSection);
                    preparedStatement.addBatch();
                }
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
            deleteContact(r, connection);
            insertContact(r, connection);
            deleteSection(r, connection);
            insertSection(r, connection);
            return null;
        });
    }

    private void deleteSection(Resume r, Connection connection) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM section WHERE resume_uuid =?")) {
            preparedStatement.setString(1, r.getUuid());
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }

    private void insertContact(Resume r, Connection connection) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO contact(resume_uuid, type, value) VALUES (?, ?, ?)")) {
            for (Map.Entry<ContactType, String> entry : r.getContacts().entrySet()) {
                preparedStatement.setString(1, r.getUuid());
                preparedStatement.setString(2, entry.getKey().name());
                preparedStatement.setString(3, entry.getValue());
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }

    private void deleteContact(Resume r, Connection connection) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM contact WHERE resume_uuid =?")) {
            preparedStatement.setString(1, r.getUuid());
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new StorageException(e);
        }
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

    private void addSection(ResultSet rs, Resume r) throws SQLException {
        String type = rs.getString("typeSection");
        if (type == null) {
            return;
        }
        SectionType sectionType = SectionType.valueOf(type);
        String sectionString = rs.getString("content");
        switch (sectionType) {
            case PERSONAL, OBJECTIVE -> r.addSection(sectionType, new TextSection(sectionString));
            case ACHIEVEMENT, QUALIFICATIONS -> r.addSection(sectionType, new ListSection(sectionString.split(System.lineSeparator())));
        }
    }

    private void addContact(ResultSet rs, Resume r) throws SQLException {
        String value = rs.getString("value");
        if (value != null) {
            r.addContact(ContactType.valueOf(rs.getString("type")), value);
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
