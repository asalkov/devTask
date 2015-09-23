package com.ansa.testtask;

public class Price {
    private String productName;
    private Long price;

    public Price(String productName, Long price) {
        this.productName = productName;
        this.price = price;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
}
