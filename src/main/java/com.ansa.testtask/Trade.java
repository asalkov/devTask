package com.ansa.testtask;

public class Trade {
    private String productName;
    private long price;
    private long quantity;
    private Direction direction;


    public Trade(String productName, Long price, Direction direction, Long quantity) {
        this.productName = productName;
        this.price = price;
        this.direction = direction;
        this.quantity = quantity;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }
}
