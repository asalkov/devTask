package com.ansa.testtask;

/**
 * Created by asalkov on 23.09.2015.
 */
public class Trade {
    private String productName;
    private Long price;
    private Long quantity;
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

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }
}
