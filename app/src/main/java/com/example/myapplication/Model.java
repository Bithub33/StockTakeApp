package com.example.myapplication;

public class Model {

    String name, barcode, item_code,qty;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getItem_code() {
        return item_code;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public void setItem_code(String item_code) {
        this.item_code = item_code;
    }
}
