package com.example.vtys_java23;

public class Bagis {
    private int bagisID;
    private int aliciID;
    private int adresID;
    private int kargoFirmaID;
    private String alimTarihi;
    private String islemDurumu;
    private String aliciIsim;
    private String adresIcerigi; // Yeni eklenen adres içeriği
    private String kargoFirmasiAdi; // Yeni eklenen kargo firması adı

    public Bagis(int bagisID, int aliciID, int adresID, int kargoFirmaID, String alimTarihi, String islemDurumu,
                 String aliciIsim, String adresIcerigi, String kargoFirmasiAdi) {
        this.bagisID = bagisID;  // Yeni eklenen BagisID
        this.aliciID = aliciID;
        this.adresID = adresID;
        this.kargoFirmaID = kargoFirmaID;
        this.alimTarihi = alimTarihi;
        this.islemDurumu = islemDurumu;
        this.aliciIsim = aliciIsim;
        this.adresIcerigi = adresIcerigi;
        this.kargoFirmasiAdi = kargoFirmasiAdi;
    }


    public int getAliciID() {
        return aliciID;
    }
    public void setBagisID(int bagisID) {
        this.bagisID = bagisID;
    }
    public int getBagisID() {
        return bagisID;
    }

    public int getAdresID() {
        return adresID;
    }

    public int getKargoFirmaID() {
        return kargoFirmaID;
    }

    public String getAlimTarihi() {
        return alimTarihi;
    }

    public String getIslemDurumu() {
        return islemDurumu;
    }

    public void setIslemDurumu(String islemDurumu) {
        this.islemDurumu = islemDurumu;
    }

    public String getAliciIsim() {
        return aliciIsim;
    }

    public String getAdresIcerigi() {
        return adresIcerigi; // Yeni metod
    }

    public String getKargoFirmasiAdi() {
        return kargoFirmasiAdi; // Yeni metod
    }
}

