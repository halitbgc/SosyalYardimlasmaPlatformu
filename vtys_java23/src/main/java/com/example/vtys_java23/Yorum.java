package com.example.vtys_java23;

public class Yorum {
    private final Integer yorumID;
    private final Integer kullaniciYazanID;
    private final Integer kullaniciAlanID;
    private final String yorumMetni;
    private final Integer puan;
    private final String yorumTarihi;
    private final String yazanAdi; // Yeni alan
    private final String alanAdi;  // Yeni alan

    public Yorum(Integer yorumID, Integer kullaniciYazanID, Integer kullaniciAlanID,
                 String yorumMetni, Integer puan, String yorumTarihi,
                 String yazanAdi, String alanAdi) {
        this.yorumID = yorumID;
        this.kullaniciYazanID = kullaniciYazanID;
        this.kullaniciAlanID = kullaniciAlanID;
        this.yorumMetni = yorumMetni;
        this.puan = puan;
        this.yorumTarihi = yorumTarihi;
        this.yazanAdi = yazanAdi;
        this.alanAdi = alanAdi;
    }

    // Getters
    public String getYazanAdi() { return yazanAdi; }
    public String getAlanAdi() { return alanAdi; }
    public Integer getYorumID() { return yorumID; }
    public Integer getKullaniciYazanID() { return kullaniciYazanID; }
    public Integer getKullaniciAlanID() { return kullaniciAlanID; }
    public String getYorumMetni() { return yorumMetni; }
    public Integer getPuan() { return puan; }
    public String getYorumTarihi() { return yorumTarihi; }
}

