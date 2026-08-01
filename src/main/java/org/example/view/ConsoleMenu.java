package org.example.view;

import org.example.model.Order;
import org.example.model.User;
import org.example.service.ShopService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ConsoleMenu {
    private final ShopService service;
    private final Scanner scanner = new Scanner(System.in);
    private final ConsolePrinter printer;

    public ConsoleMenu(ShopService service, ConsolePrinter consolePrinter) {
        this.service = service;
        this.printer = consolePrinter;
    }

     public void start() throws SQLException {
         String input;
         boolean stop = false;
        do {
            printMenu();
            input = scanner.nextLine();
            try {
                int choice = Integer.parseInt(input);
                switch (choice) {
                    case 1: userMenu(); break;
                    case 2: orderMenu();break;
                    case 0: stop = true; break;
                    default:
                        break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Введите число");
            }
        } while (!stop);
     }

     private List<Order> inputManyOrders(int userId) {
         String product;
         String price;
         boolean stop = false;
         List<Order> orders = new ArrayList<>();
        do {
            product = askValidInput("Введите название продукта или Введите 0 для выхода\n", ValidationEnum.PRODUCT_NAME);
            if (product.equals("0")) {
                stop = true;
            } else {
                price = askValidInput("Введите цену продукта\n", ValidationEnum.DIGITWITHTOGLE);
                orders.add(new Order(product,Double.parseDouble(price), userId));
                System.out.println(" ");
            }
        } while (!stop);
        return orders;
     }

     private User createUser() {
        String name;
        String email;
        String age;

        name = askValidInput("Введите имя пользователя\n", ValidationEnum.LINE);
        age = askValidInput("Введите возраст пользователя\n", ValidationEnum.DIGIT);
        email = askValidInput("Введите почту пользователя\n", ValidationEnum.EMAIL);

        return new User(name,email,Integer.parseInt(age));
     }

    private User updateUserWithId() throws SQLException {
        String name;
        String email;
        String age;
        String id;

        id = askValidInput("Введите id пользователя которому хотите внести изменения\n", ValidationEnum.DIGIT);
         if (!service.chekUserId(Integer.parseInt(id))) {
             boolean isValidId = false;
             do {
                 id = askValidInput("Gользователя под таким ID не существует попробуйте снова", ValidationEnum.DIGIT);
                 isValidId = service.chekUserId(Integer.parseInt(id));
             } while (!isValidId);
         }
        name = askValidInput("Введите новое имя пользователя\n", ValidationEnum.LINE);
        age = askValidInput("Введите новый возраст пользователя\n", ValidationEnum.DIGIT);
        email = askValidInput("Введите новую почту пользователя\n", ValidationEnum.EMAIL);

        return new User(Integer.parseInt(id), name,email,Integer.parseInt(age));
    }

    private Order createOrder() throws SQLException {
        String product;
        String price;

        product = askValidInput("Введите название продукта\n", ValidationEnum.PRODUCT_NAME);
        price = askValidInput("Введите цену продукта\n", ValidationEnum.DIGITWITHTOGLE);

        return new Order(product,Double.parseDouble(price), null);
    }

    private Order createOrderWithId() throws SQLException {
        String product;
        String price;
        String id;

        id = askValidInput("Введите id заказа которому хотите внести изменения\n", ValidationEnum.DIGIT);
        if (!service.chekOrderId(Integer.parseInt(id))) {
            boolean isValidId = false;
            do {
                id = askValidInput("Заказа под таким ID не существует попробуйте снова", ValidationEnum.DIGIT);
                isValidId = service.chekOrderId(Integer.parseInt(id));
            } while (!isValidId);
        }
        product = askValidInput("Введите новое название продукта\n", ValidationEnum.PRODUCT_NAME);
        price = askValidInput("Введите новую цену продукта\n", ValidationEnum.DIGITWITHTOGLE);

        return new Order(Integer.parseInt(id), product,Double.parseDouble(price),null);
    }

    private String askValidInput(String prompt, ValidationEnum validationEnum) {
        System.out.println(prompt);
        boolean isValid = false;
        String result = "";
        do {
            String input = scanner.nextLine();
            if (check(input,validationEnum)) {
                result = input;
                isValid = true;
            } else {
                System.out.println("Ввод не совпадает с требованиями. Попробуйте снова");
            }
        } while (!isValid);

        return result;
    }

    private boolean check(String input, ValidationEnum validationEnum) throws NumberFormatException {
        switch (validationEnum) {
            case DIGIT -> {
                try {
                    Integer.parseInt(input);
                    return true;
                } catch (NumberFormatException e) {
                    return false;
                }
            }
            case DIGITWITHTOGLE -> {
                try {
                    Double.parseDouble(input);
                    return true;
                } catch (NumberFormatException e) {
                    return false;
                }
            }
            case LINE -> {
                return input.matches("^[a-zA-Zа-яА-Я]+$");
            }
            case EMAIL -> {
                return input.matches("^\\w+@\\w+\\.\\w+$");
            }
            case PRODUCT_NAME -> {
                return input.matches("^[a-zA-Zа-яА-Я 0-9]+$");
            }
        }
        return true;
    }

    private void orderMenu() throws SQLException {
        String input;
        boolean stop = false;
        do {
            printOrderMenu();
            input = scanner.nextLine();

            try {
                switch (Integer.parseInt(input)) {
                    case 1: printer.printOrders(service.showAllOrders()); break;
                    case 2:
                        Order order = createOrder();
                        printer.printUsers(service.showAllUsers());
                        String result = askValidInput("Введите id пользователя",ValidationEnum.DIGIT);
                        order.setUserId(Integer.parseInt(result));
                        service.createOrder(order);
                        break;
                    case 3:
                        System.out.println("Введите id пользователя который закупает товары оптом");
                        printer.printUsers(service.showAllUsers());
                        System.out.println("");
                        service.createOrdersBatch(inputManyOrders(Integer.parseInt(
                                askValidInput("Введите id пользователя",ValidationEnum.DIGIT)))); break;
                    case 4: printer.printOrders(service.showAllOrders());
                        service.deleteOrderById(Integer.parseInt(askValidInput("Введите id заказа на удаление", ValidationEnum.DIGIT)));break;
                    case 5:
                        printer.printOrders(service.showAllOrders());
                        service.updateOrder(createOrderWithId());
                    case 0: stop = true; break;
                    default: break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Введите число");
            }
        } while (!stop);
    }

    private void userMenu() throws SQLException {
        String input;
        boolean stop = false;
        do {
            printUserMenu();
            input = scanner.nextLine();

            try {
                switch (Integer.parseInt(input)) {
                    case 1: service.createUser(createUser());break;
                    case 2: service.createUserWithOrder(createUser(), createOrder()); break;
                    case 3:
                        printer.printUsers(service.showAllUsers());
                        service.deletUserById(Integer.parseInt(askValidInput("Введите id пользователя", ValidationEnum.DIGIT)));
                        break;
                    case 4: printer.printUsersOrder(service.showAllUsersOrder()); break;
                    case 5: service.showOrdersByUserId(Integer.parseInt(askValidInput("Введите id пользователя",ValidationEnum.DIGIT))); break;
                    case 6: printer.printUsers(service.showAllUsers()); break;
                    case 7:
                        printer.printUsers(service.showAllUsers());
                        service.updateUser(updateUserWithId());
                    case 0: stop = true; break;
                    default: break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Введите число");
            }
        } while (!stop);
    }

     private void printMenu() {
        String menu = """
        
    
        =========================
                SHOP MENU
        =========================
                
        1.  Меню пользователей
        2.  Меню заказов
        0.  Выход
                
                 Введите пункт:
        """;
        System.out.println(menu);
     }

    private void printUserMenu() {
        String menu = """
    
    
        =========================
                USER MENU
        =========================
               
        1. Создать пользователя
        2. Создать пользователя с заказом
        3. Удалить пользователя
        4. Показать покупки пользователей
        5. Показать покупки конкретного пользователя
        6. Показать пользователей
        7. Обновить пользователя
        0. Выход в главное меню
        
                 Введите пункт:
        """;
        System.out.println(menu);
    }

    private void printOrderMenu() {
        String menu = """
    
    
        =========================
                ORDER MENU
        =========================
                
        1. Показать заказы
        2. Создать заказ
        3. Сделать несколько заказов на одного пользователя
        4. Удалить заказ
        5. Обновить заказ
        0. Выход в главное меню
                
                 Введите пункт:
        """;
        System.out.println(menu);
    }
}
