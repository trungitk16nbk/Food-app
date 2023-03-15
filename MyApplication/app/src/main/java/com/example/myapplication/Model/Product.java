package com.example.myapplication.Model;

import java.io.Serializable;

public class Product implements Serializable {
    private String id, name, description, category;

    private int price_normal, price_sale, amount;

    public Product() {

    }

    public Product(String id, String name, String description, String category, int price_normal, int price_sale, int amount) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.category = category;
        this.price_normal = price_normal;
        this.price_sale = price_sale;
        this.amount = amount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getPrice_normal() {
        return price_normal;
    }

    public void setPrice_normal(int price_normal) {
        this.price_normal = price_normal;
    }

    public int getPrice_sale() {
        return price_sale;
    }

    public void setPrice_sale(int price_sale) {
        this.price_sale = price_sale;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", category='" + category + '\'' +
                ", price_normal=" + price_normal +
                ", price_sale=" + price_sale +
                ", amount=" + amount +
                '}';
    }
}
