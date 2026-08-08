package org.example.repository;

import org.example.Mapping.MappingSQL;
import org.example.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserRepository {

    private static final String CREATE_TABLE_USER = """
            CREATE TABLE IF NOT EXISTS users (
            id INT AUTO_INCREMENT PRIMARY KEY,
            name VARCHAR(155),
            age INT,   
            email VARCHAR(155))
            """;

    private static final String DELETE_USER_SQL = """
            DELETE FROM users
            WHERE id = ?
            """;

    private static final String UPDATE_USER_SQL = """
            UPDATE users
            SET name = ?, age = ?, email = ?
            WHERE id = ?
            """;

    private static final String GET_ALL_DATA_SQL = """
            SELECT name, email, age , id FROM users
            """;

    private static final String SAVE_USER_SQL = """
            INSERT INTO users (name, age, email)
            VALUES (?, ?, ?)
        """;

    private static final String FIND_BY_ID_SQL = """
            SELECT name, email, age , id
            FROM users
            WHERE id = ?
            """;

    private final MappingSQL mapRow = new MappingSQL();

    public UserRepository() {}

    public void createUserTable(Connection connection) throws SQLException {
        try(
                PreparedStatement psCreate = connection.prepareStatement(CREATE_TABLE_USER);
        ) {
            psCreate.execute();
        }
    }

    public void save(Connection connection, User user) throws SQLException {
        try (
                PreparedStatement psSaveUser = connection.prepareStatement(SAVE_USER_SQL, Statement.RETURN_GENERATED_KEYS);
        ) {
            saveHelper(user, psSaveUser);
        }
    }

    public void saveWithOrder(Connection connection, User user) throws SQLException {
        try (
                PreparedStatement psSaveUser = connection.prepareStatement(SAVE_USER_SQL, Statement.RETURN_GENERATED_KEYS);
        ) {
            saveHelper(user, psSaveUser);
        }
    }

    private void saveHelper(User user, PreparedStatement psSaveUser) throws SQLException {
        psSaveUser.setString(1, user.getName());
        psSaveUser.setInt(2, user.getAge());
        psSaveUser.setString(3, user.getEmail());
        psSaveUser.executeUpdate();
        try (ResultSet generatedKey = psSaveUser.getGeneratedKeys()) {
            if (generatedKey.next()) {
                int newId = generatedKey.getInt(1);
                user.setId(newId);
            }
        }
    }

    public User findById(Connection connection, int id) throws SQLException {
        try (
                PreparedStatement psSelectById = connection.prepareStatement(FIND_BY_ID_SQL);
        ) {
            psSelectById.setInt(1, id);
            try(ResultSet resultSet = psSelectById.executeQuery()) {
                if (!resultSet.next())
                    return  null;
                return mapRow.mapRowUsers(resultSet);
            }
        }
    }

    public boolean findId(Connection connection, int id) throws  SQLException {
        try (PreparedStatement psSelectById = connection.prepareStatement(FIND_BY_ID_SQL)) {
            psSelectById.setInt(1, id);
            try (ResultSet resultSet = psSelectById.executeQuery()) {
                if (!resultSet.next())
                    return false;
                return true;
            }
        }
    }

    public List<User> findAll(Connection connection) throws SQLException {
        List<User> users = new ArrayList<User>();
        try(
                PreparedStatement psGetAll = connection.prepareStatement(GET_ALL_DATA_SQL)
        ) {
            try(ResultSet rsAll = psGetAll.executeQuery()) {

                while (rsAll.next()) {
                    users.add(mapRow.mapRowUsers(rsAll));
                }
            }
        }
        return users;
    }


    public boolean update(Connection connection, User user) throws SQLException {
        try(
                PreparedStatement psUpdate = connection.prepareStatement(UPDATE_USER_SQL);
        ) {
            psUpdate.setString(1, user.getName());
            psUpdate.setInt(2, user.getAge());
            psUpdate.setString(3, user.getEmail());
            psUpdate.setInt(4, user.getId());
            int rows = psUpdate.executeUpdate();
            return  rows == 1;
        }

    }

    public boolean deleteById(Connection connection, int id) throws SQLException {
        try (
                PreparedStatement psDelete = connection.prepareStatement(DELETE_USER_SQL);
                )
        {
            psDelete.setInt(1, id);
            int rows = psDelete.executeUpdate();
            return  rows == 1;
        }
    }
}
