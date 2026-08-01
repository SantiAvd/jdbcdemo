package org.example.service;

import org.example.model.UserOrderInfo;
import org.example.repository.OrderRepository;
import org.example.model.Order;
import org.example.model.User;
import org.example.repository.UserRepository;
import java.sql.*;
import java.util.List;

public class ShopService {
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final String connectionUrl;

    public ShopService(String connectionUrl, UserRepository userRepository, OrderRepository orderRepository) {
        this.connectionUrl = connectionUrl;
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
    }

    public void initSchema() throws SQLException {
        try(Connection connection = DriverManager.getConnection(connectionUrl)) {
            userRepository.createUserTable(connection);
            orderRepository.createOrdersTable(connection);
        }
    }

    public void deleteOrderById(int id) throws SQLException {
        try(Connection connection = DriverManager.getConnection(connectionUrl)) {
            orderRepository.deleteOrderBy(connection, id);
        }
    }

    public void deletUserById(int id) throws SQLException {
        try(Connection connection = DriverManager.getConnection(connectionUrl)) {
            userRepository.deleteById(connection, id);
        }
    }

    public void createOrdersBatch(List<Order> orders) throws SQLException {
        try (Connection connection = DriverManager.getConnection(connectionUrl)){
            connection.setAutoCommit(false);
            try {
                orderRepository.saveBatch(connection, orders);
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw e;
            }
        }
    }

    public void createUserWithOrder(User user, Order order) throws SQLException {
        try(Connection connection = DriverManager.getConnection(connectionUrl)) {
            connection.setAutoCommit(false);
            try {
                userRepository.saveWithOrder(connection, user);
                order.setUserId(user.getId());
                orderRepository.saveWithUser(connection, order);
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw e;
            }
        }
    }

    public boolean chekUserId(int id) throws SQLException {
        try (Connection connection = DriverManager.getConnection(connectionUrl)){
            return userRepository.findId(connection, id);
        }
    }

    public boolean chekOrderId(int id) throws SQLException {
        try (Connection connection = DriverManager.getConnection(connectionUrl)){
            return orderRepository.findById(connection, id);
        }
    }

    public boolean updateUser(User user) throws SQLException {
        try (Connection connection = DriverManager.getConnection(connectionUrl)) {
            return userRepository.update(connection, user);
        }
    }

    public void updateOrder(Order order) throws SQLException {
        try (Connection connection = DriverManager.getConnection(connectionUrl)) {
            orderRepository.update(connection, order);
        }
    }

    public List<User> showAllUsers() throws SQLException {
        try (Connection connection = DriverManager.getConnection(connectionUrl)) {
            return userRepository.findAll(connection);
        }
    }

    public List<Order> showAllOrders() throws SQLException {
        try(Connection connection = DriverManager.getConnection(connectionUrl)) {
            return orderRepository.findAll(connection);
        }
    }

    public List<UserOrderInfo> showAllUsersOrder() throws SQLException {
        try(Connection connection = DriverManager.getConnection(connectionUrl)) {
            return orderRepository.showUsersOrder(connection);
        }
    }

    public List<Order> showOrdersByUserId(int id) throws SQLException {
        try(Connection connection = DriverManager.getConnection(connectionUrl)) {
            return orderRepository.findOrdersByUserId(connection,id);
        }
    }

    public void createUser(User user) throws SQLException {
        try(Connection connection = DriverManager.getConnection(connectionUrl)) {
            userRepository.save(connection, user);
        }
    }

    public void createOrder(Order order) throws SQLException {
        try(Connection connection = DriverManager.getConnection(connectionUrl)) {
            orderRepository.save(connection, order);
        }

    }
}
