package org.example.model;

public class UserOrderInfo {
    private String userName;
    private String product;
    private double price;

    public UserOrderInfo(String name, String product, double price) {
        this.price = price;
        this.userName = name;
        this.product = product;
    }

    public String getUserName() { return userName;}
    public void setUserName(String userName) { this.userName = userName; }

    public String getProduct() { return product; }
    public void setProduct(String product) { this.product = product; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String toString() {
        return userName + " -> " + product + " (" + price + ")";
    }
}
