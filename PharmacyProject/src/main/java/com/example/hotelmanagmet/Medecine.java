package com.example.hotelmanagmet;

public class Medecine {
    private String medecineCode;
    private String name;
    private Float price;
    private Integer quantity;

    public Medecine(String medecineCode, String name, Float price, Integer quantity) {
        this.medecineCode = medecineCode;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public String getMedecineCode() {
        return medecineCode;
    }

    public String getName() {
        return name;
    }

    public Float getPrice() {
        return price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setMedecineCode(String medecineCode) {
        this.medecineCode = medecineCode;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}