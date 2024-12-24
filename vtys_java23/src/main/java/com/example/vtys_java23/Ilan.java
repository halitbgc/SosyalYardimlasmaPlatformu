package com.example.vtys_java23;

public class Ilan {
    private int ilanID;  // Bu ID'nin veritabanındaki karşılığı olmalı
    private String baslik;
    private String aciklama;
    private String urunDurumu;
    private String ilanTarihi;
    private int ilanDurumu; // int tipi
    private String kategoriAdi;

    public Ilan( String baslik, String aciklama, String urunDurumu, String ilanTarihi, int ilanDurumu, String kategoriAdi) {

        this.baslik = baslik;
        this.aciklama = aciklama;
        this.urunDurumu = urunDurumu;
        this.ilanTarihi = ilanTarihi;
        this.ilanDurumu = ilanDurumu;
        this.kategoriAdi = kategoriAdi;
    }

    public void setIlanDurumu(int ilanDurumu) {
        this.ilanDurumu = ilanDurumu;
    }

    public String getBaslik() {
        return baslik;
    }

    public String getAciklama() {
        return aciklama;
    }

    public String getUrunDurumu() {
        return urunDurumu;
    }

    public String getIlanTarihi() {
        return ilanTarihi;
    }

    public int getIlanDurumu() {
        return ilanDurumu;
    }

    public String getKategoriAdi() {
        return kategoriAdi;
    }
    public int getIlanID() {
        return ilanID;
    }

}

