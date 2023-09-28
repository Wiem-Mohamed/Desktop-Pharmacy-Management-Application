package com.example.hotelmanagmet;

public class Sale {


    private Integer saleCode;

    private String medecineCode;
    private String name;
    private Integer quantity;
    private Float amount;


    public String getMedecineCode() {
        return medecineCode;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Float getAmount() {
        return amount;
    }

    public String getName() {
        return name;
    }

    public Integer getSaleCode() {
        return saleCode;
    }

    public void setMedecineCode(String medecineCode) {
        this.medecineCode = medecineCode;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSaleCode(Integer saleCode) {
        this.saleCode = saleCode;
    }

    public Sale(Integer saleCode, String medecineCode, String name, Integer quantity, Float amount) {
        this.saleCode=saleCode;
        this.medecineCode = medecineCode;
        this.name=name;
        this.quantity =Integer.valueOf (quantity);
        this.amount = amount;
    }
}
