package org.example.Mapping;

import org.example.model.Order;
import org.example.model.User;
import org.example.model.UserOrderInfo;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MappingSQL {

    public Order mapRowOrders(ResultSet rs) throws SQLException {
        String product = rs.getString("product");
        double price = rs.getDouble("price");
        int id = rs.getInt("id");
        int userId = rs.getInt("user_Id");
        return new Order(id, product, price, userId);
    }

    public UserOrderInfo mapRowUserOrders(ResultSet rs) throws SQLException {
        String product = rs.getString("product");
        double price = rs.getDouble("price");
        String name = rs.getString("name");
        return new UserOrderInfo(name, product, price);
    }

    public User mapRowUsers(ResultSet rs) throws SQLException {
        String name = rs.getString("name");
        String email = rs.getString("email");
        int age = rs.getInt("age");
        int idn = rs.getInt("id");
        return new User(idn,name, email, age);
    }
}
