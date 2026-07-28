package org.example.model;

public class Order  {
    private Integer id;
    private String product;
    private double price;
    private Integer userId;

    public Order(String product, double price, Integer userId) {
        this.product = product;
        this.price = price;
        this.userId = userId;
    }

    public Order(Integer id, String product, double price, Integer userId) {
        this.id = id;
        this.product = product;
        this.price = price;
        this.userId = userId;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getProduct() { return product; }
    public void setProduct(String product) { this.product = product; }

    public Integer getUserId() { return userId;}
    public void setUserId(Integer userId) { this.userId = userId; }

    @Override
    public String toString() {
        return "Id: " + id + " Product: " + product + " Price: " + price + " User_Id: " + userId;
    }
}
