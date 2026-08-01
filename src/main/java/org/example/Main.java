package org.example;
import org.example.repository.OrderRepository;
import org.example.repository.UserRepository;
import org.example.service.ShopService;
import org.example.view.ConsoleMenu;
import org.example.view.ConsolePrinter;
import java.sql.*;

public class Main {

    private static final String CONNECTION_URL = "jdbc:h2:./database";

    public static void main(String[] args) throws SQLException {
        UserRepository userRepository = new UserRepository();
        OrderRepository orderRepository = new OrderRepository();
        ShopService service = new ShopService(CONNECTION_URL, userRepository, orderRepository);
        ConsolePrinter printer = new ConsolePrinter();
        ConsoleMenu menu = new ConsoleMenu(service, printer);

        service.initSchema();
        menu.start();
    }
}
