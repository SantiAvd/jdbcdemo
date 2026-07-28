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
        orderRepository.deleteOrderBy(id);
    }

    public void deletUserById(int id) throws SQLException {
        userRepository.deleteById(id);
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
                userRepository.save(connection, user);
                order.setUserId(user.getId());
                orderRepository.save(connection, order);
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw e;
            }
        }
    }

    public void updateUser(User user) throws SQLException {
        userRepository.update(user);
    }

    public void updateOrder(Order order) throws SQLException {
        orderRepository.update(order);
    }

    public List<User> showAllUsers() throws SQLException {
        return userRepository.findAll();
    }

    public List<Order> showAllOrders() throws SQLException {
        return orderRepository.findAll();
    }

    public List<UserOrderInfo> showAllUsersOrder() throws SQLException {
        return orderRepository.showUsersOrder();
    }

    public List<Order> showOrdersByUserId(int id) throws SQLException {
        return orderRepository.findOrdersByUserId(id);
    }

    public void createUser(User user) throws SQLException {
        userRepository.save(user);
    }

    public void createOrder(Order order) throws SQLException {
        orderRepository.save(order);
    }



}
