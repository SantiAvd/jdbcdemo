package org.example.view;

import org.example.model.Order;
import org.example.model.User;
import org.example.model.UserOrderInfo;
import java.sql.SQLException;
import java.util.List;



public class ConsolePrinter {

    public void printUsers(List<User> users) {
        System.out.printf("%-5s %-15s %-5s %-25s%n",
                "ID", "NAME", "AGE", "EMAIL");

        System.out.println("-------------------------------------------------------");

        for (User user : users) {
            System.out.printf("%-5d %-15s %-5d %-25s%n",
                    user.getId(),
                    user.getName(),
                    user.getAge(),
                    user.getEmail());
        }
    }

    public void printOrders(List<Order> orders) {
        System.out.printf("%-5s %-20s %-10s %-10s%n",
                "ID", "PRODUCT", "PRICE", "USER_ID");

        System.out.println("------------------------------------------------");

        for (Order order : orders) {
            System.out.printf("%-5d %-20s %-10.2f %-10d%n",
                    order.getId(),
                    order.getProduct(),
                    order.getPrice(),
                    order.getUserId());
        }
    }

    public void printUsersOrder(List<UserOrderInfo> userOrderInfos) {
        for (UserOrderInfo userOrderInfo: userOrderInfos) {
            System.out.println(userOrderInfo);
        }
    }
}
