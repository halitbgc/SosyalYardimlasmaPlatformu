package com.example.vtys_java23;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class BagisYapController {

    private boolean bagisYapildi = false;

    @FXML
    private Label lblIlanDetay;
    @FXML
    private ComboBox<String> comboKullanicilar;
    @FXML
    private ComboBox<String> comboKargoFirmalari; // Yeni ComboBox tanımı
    @FXML
    private Button btnBagisYap;
    @FXML
    private Button btnIptal;

    private Ilan selectedIlan;

    @FXML
    public void initialize() {
        kullanicilariYukle();
        kargoFirmalariniYukle(); // Kargo firmalarını yükle
    }

    public void setSelectedIlan(Ilan ilan) {
        this.selectedIlan = ilan;
        lblIlanDetay.setText(
                "Başlık: " + ilan.getBaslik() + "\n" +
                        "Açıklama: " + ilan.getAciklama() + "\n" +
                        "Ürün Durumu: " + ilan.getUrunDurumu() + "\n" +
                        "Kategori: " + ilan.getKategoriAdi()
        );
    }

    // Kullanıcıları yüklerken, giriş yapan kullanıcıyı dışarıda tutar
    // Kullanıcıları yüklerken, giriş yapan kullanıcıyı dışarıda tutar
    private void kullanicilariYukle() {
        int girisYapanKullaniciID = getGirisYapanKullaniciID();  // Giriş yapan kullanıcının ID'sini alıyoruz

        String query = "SELECT KullaniciID, CONCAT(Ad, ' ', Soyad) AS KullaniciAdi FROM Kullanicilar WHERE KullaniciID != ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // Giriş yapan kullanıcıyı dışarıda bırakmak için WHERE koşulunu kullanıyoruz
            stmt.setInt(1, girisYapanKullaniciID);
            ResultSet rs = stmt.executeQuery();

            // Kullanıcılar comboBox'a ekleniyor
            ObservableList<String> kullaniciListesi = FXCollections.observableArrayList();
            while (rs.next()) {
                String kullaniciAdi = rs.getString("KullaniciAdi");
                int kullaniciID = rs.getInt("KullaniciID");
                kullaniciListesi.add(kullaniciID + "-" + kullaniciAdi);
            }

            comboKullanicilar.setItems(kullaniciListesi);  // Listeyi ComboBox'a atıyoruz
        } catch (Exception e) {
            showAlert("Hata", "Kullanıcılar yüklenirken bir hata oluştu: " + e.getMessage());
        }
    }


    // Kargo firmalarını ComboBox'a yükler
    private void kargoFirmalariniYukle() {
        String query = "SELECT KargoFirmaID, KargoFirmasi FROM KargoFirmasi";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                comboKargoFirmalari.getItems().add(rs.getInt("KargoFirmaID") + "-" + rs.getString("KargoFirmasi"));
            }
        } catch (Exception e) {
            showAlert("Hata", "Kargo firmaları yüklenirken bir hata oluştu: " + e.getMessage());
        }
    }

    @FXML
    private void handleBagisYap() {
        String secilenKullanici = comboKullanicilar.getValue();
        String secilenKargoFirma = comboKargoFirmalari.getValue();

        if (secilenKullanici == null || secilenKargoFirma == null) {
            showAlert("Hata", "Kullanıcı ve kargo firması seçimleri zorunludur.");
            return;
        }
        System.out.println(secilenKullanici);
        int aliciID = Integer.parseInt(secilenKullanici.split("-")[0]);
        int kargoFirmaID = Integer.parseInt(secilenKargoFirma.split("-")[0]);
        int ilanID = getIlanID(selectedIlan.getBaslik());
        int adresID = getAdresID(aliciID);

        if (adresID == -1) {
            showAlert("Hata", "Seçilen kullanıcının adresi bulunamadı.");
            return;
        }

        kaydetBagis(ilanID, aliciID, adresID, kargoFirmaID);

        System.out.println("sssssss");
        // Bağış işlemi başarılıysa
        bagisYapildi = true;
    }

    // Bağış yapıldı mı, kontrol etmek için getter
    public boolean isBagisYapildi() {
        return bagisYapildi;
    }

    private int getIlanID(String baslik) {
        String query = "SELECT IlanID FROM Ilanlar WHERE Baslik = ?";
        int ilanID = -1;

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, baslik);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                ilanID = rs.getInt("IlanID");
            }
        } catch (Exception e) {
            showAlert("Hata", "İlan ID getirilirken hata oluştu: " + e.getMessage());
        }
        return ilanID;
    }

    private int getAdresID(int kullaniciID) {
        String query = "SELECT AdresID FROM Adresler WHERE KullaniciID = ?";
        int adresID = -1;

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, kullaniciID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                adresID = rs.getInt("AdresID");
            }
        } catch (Exception e) {
            showAlert("Hata", "Adres ID getirilirken hata oluştu: " + e.getMessage());
        }
        return adresID;
    }

    private void kaydetBagis(int ilanID, int aliciID, int adresID, int kargoFirmaID) {
        String query = """
                INSERT INTO Bagislar (IlanID, AliciID, AlimTarihi, AdresID, KargoFirmaID, IslemDurumu)
                VALUES (?, ?, GETDATE(), ?, ?, 'Beklemede')
                """;

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, ilanID);
            stmt.setInt(2, aliciID);
            stmt.setInt(3, adresID);
            stmt.setInt(4, kargoFirmaID);

            stmt.executeUpdate();
            showAlert("Başarılı", "Bağış başarıyla kaydedildi!");

            Stage stage = (Stage) btnBagisYap.getScene().getWindow();
            stage.close();

        } catch (Exception e) {
            showAlert("Hata", "Bağış yapılırken bir hata oluştu: " + e.getMessage());
            System.out.println(e.getMessage());
        }
    }

    private Connection getConnection() throws Exception {
        String url = "jdbc:sqlserver://localhost:1433;databaseName=SosyalYardimlasmaPlatformu;encrypt=false;";
        String user = "safa";
        String pass = "123456";
        return DriverManager.getConnection(url, user, pass);
    }

    private int getGirisYapanKullaniciID() {
        return LoginController.currentUserID; // Giriş yapan kullanıcının ID'sini alıyoruz
    }


    @FXML
    private void handleIptal() {
        bagisYapildi = false;  // Bağış yapılmadı olarak işaretle
        Stage stage = (Stage) btnIptal.getScene().getWindow();
        stage.close();
    }


    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }


}
