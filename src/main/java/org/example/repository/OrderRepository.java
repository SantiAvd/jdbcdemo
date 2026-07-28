package org.example.repository;

import org.example.model.Order;
import org.example.model.UserOrderInfo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderRepository {

    private final String connectionUrl;

    private static final String CREATE_TABLE_ORDERS = """
            CREATE TABLE IF NOT EXISTS orders (
            id INT AUTO_INCREMENT PRIMARY KEY,
            product VARCHAR(155),
            price DOUBLE,
            user_id INT,
                 FOREIGN KEY (user_id)
                REFERENCES users(id))""";

    private static final String SAVE_ORDER_SQL = """
            INSERT INTO orders(product, price, user_id)
            VALUES(?,?,?)
            """;

    private static final String FIND_ORDER_BY_ID_SQL = """
            SELECT * FROM orders
            WHERE id = ?
            """;

    private static final String FIND_ORDERS_BY_USER_ID_SQL = """
            SELECT * FROM orders
            WHERE user_id = ?
            """;
    private static final String FIND_ALL_ORDER_SQL = """
            SELECT * 
            FROM orders
            """;

    private static final String UPDATE_ORDER_SQL = """
            UPDATE orders
            SET product = ?, price = ?
            WHERE id = ?
            """;
    private static final String DELETE_ORDER_BY_ID_SQL = """
            DELETE FROM orders
            WHERE id = ?
            """;

    private static final String SELECT_JOIN_USERS_ORDER = """
            SELECT u.name, o.product, o.price
            FROM users u
            JOIN orders o
            ON u.id = o.user_id
            """;

    public OrderRepository(String connectionUrl) {
        this.connectionUrl = connectionUrl;
    }


    public void createOrdersTable(Connection connection) throws SQLException {
        try(
                PreparedStatement psCreate = connection.prepareStatement(CREATE_TABLE_ORDERS);
        ) {
            psCreate.execute();
        }
    }


    public void save(Order order) throws SQLException {
        try (
                Connection connection = DriverManager.getConnection(connectionUrl);
                PreparedStatement psSave = connection.prepareStatement(SAVE_ORDER_SQL, Statement.RETURN_GENERATED_KEYS);
                ){
            saveHelper(order, psSave);
        }
    }

    public void saveBatch(Connection connection, List<Order> orders) throws SQLException {
        try (PreparedStatement psBatch = connection.prepareStatement(SAVE_ORDER_SQL)){
            for (Order order: orders) {
                saveBatchHelper(order, psBatch);
            }
            psBatch.executeBatch();
        }
    }

    private void saveBatchHelper(Order order, PreparedStatement psBatch) throws SQLException {
        setOrderParams(order, psBatch);
        psBatch.addBatch();
    }

    private void setOrderParams(Order order, PreparedStatement ps) throws SQLException {
        ps.setString(1, order.getProduct());
        ps.setDouble(2, order.getPrice());
        ps.setInt(3, order.getUserId());
    }

    private void saveHelper(Order order, PreparedStatement psSave) throws SQLException {
        setOrderParams(order, psSave);
        psSave.executeUpdate();
        try (ResultSet rsSave = psSave.getGeneratedKeys()) {
            if (rsSave.next()) {
                int newId = rsSave.getInt(1);
                order.setId(newId);
            }
        }
    }

    public void save(Connection connection, Order order) throws SQLException {
        try (
                PreparedStatement psSave = connection.prepareStatement(SAVE_ORDER_SQL, Statement.RETURN_GENERATED_KEYS);
        ){
            saveHelper(order, psSave);
        }
    }

    public Order findById(int id) throws SQLException {
        try(
                Connection connection = DriverManager.getConnection(connectionUrl);
                PreparedStatement psFindById = connection.prepareStatement(FIND_ORDER_BY_ID_SQL);
                ) {
            psFindById.setInt(1, id);
            try (ResultSet resultSet = psFindById.executeQuery()){
                if (!resultSet.next())
                    return null;
                return mapRow(resultSet);
            }
        }
    }

    public List<Order> findOrdersByUserId(int id) throws SQLException {
        List<Order> orders = new ArrayList<>();

        try (
                Connection connection = DriverManager.getConnection(connectionUrl);
                PreparedStatement psFindUserById = connection.prepareStatement(FIND_ORDERS_BY_USER_ID_SQL);
                ){
            psFindUserById.setInt(1, id);
            try(ResultSet resultSet = psFindUserById.executeQuery()) {
                while (resultSet.next())
                    orders.add(mapRow(resultSet));
            }
        }
        return orders;
    }

    public void update(Order order) throws SQLException {
        try(
                Connection connection = DriverManager.getConnection(connectionUrl);
                PreparedStatement psUpdate = connection.prepareStatement(UPDATE_ORDER_SQL);
        ) {
            psUpdate.setString(1, order.getProduct());
            psUpdate.setDouble(2, order.getPrice());
            psUpdate.setInt(3, order.getId());

            psUpdate.executeUpdate();
        }
    }

    public List<Order> findAll() throws SQLException {
        List<Order> orders = new ArrayList<>();
        try(
                Connection connection = DriverManager.getConnection(connectionUrl);
                PreparedStatement psShowAll = connection.prepareStatement(FIND_ALL_ORDER_SQL);
                ) {
            try (ResultSet resultSet = psShowAll.executeQuery()) {
                while (resultSet.next())
                    orders.add(mapRow(resultSet));
            }
        }
        return orders;
    }
    public List<UserOrderInfo> showUsersOrder() throws SQLException {
        List<UserOrderInfo> userOrderList = new ArrayList<>();

        try(
                Connection connection = DriverManager.getConnection(connectionUrl);
                PreparedStatement psUsersOrder = connection.prepareStatement(SELECT_JOIN_USERS_ORDER)
        ){
            try (ResultSet resultSet = psUsersOrder.executeQuery()){
                while (resultSet.next()) {
                    userOrderList.add(mapRowUserOrders(resultSet));
                }
            }
        }
        return userOrderList;
    }

    public boolean deleteOrderBy(int id) throws SQLException {
        try(
                Connection connection = DriverManager.getConnection(connectionUrl);
                PreparedStatement psDelete = connection.prepareStatement(DELETE_ORDER_BY_ID_SQL);
                ) {
            psDelete.setInt(1, id);

            int rows = psDelete.executeUpdate();
            return  rows == 1;
        }
    }

    private Order mapRow(ResultSet rs) throws SQLException {
        String product = rs.getString("product");
        double price = rs.getDouble("price");
        int id = rs.getInt("id");
        int userId = rs.getInt("user_Id");
        return new Order(id, product, price, userId);
    }

    private UserOrderInfo mapRowUserOrders(ResultSet rs) throws SQLException {
        String product = rs.getString("product");
        double price = rs.getDouble("price");
        String name = rs.getString("name");
        return new UserOrderInfo(name, product, price);
    }
}
