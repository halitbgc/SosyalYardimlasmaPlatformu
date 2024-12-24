package com.example.vtys_java23;

public class Kategori {
    private int id;
    private String adi;

    public Kategori(int id, String adi) {
        this.id = id;
        this.adi = adi;
    }

    public int getId() {
        return id;
    }

    public String getAdi() {
        return adi;
    }

    @Override
    public String toString() {
        return adi; // ComboBox'ta kategori adını göstermek için
    }
}
