package ru.javawebinar.basejava.sql;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface SqlExecutor<T> {
    T get(PreparedStatement preparedStatement) throws SQLException;
}
