package com.example.vtys_java23;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AddIlanController {

    @FXML
    private TextField txtBaslik;
    @FXML
    private TextField txtAciklama;
    @FXML
    private TextField txtUrunDurumu;
    @FXML
    private ComboBox<String> comboKategori;
    @FXML
    private Button btnIlanEkle;
    @FXML
    private Button btnGeri;

    @FXML
    public void initialize() {
        // Kategori bilgilerini ComboBox'a yükle
        loadKategoriler();
    }

    private void loadKategoriler() {
        String url = "jdbc:sqlserver://localhost:1433;databaseName=SosyalYardimlasmaPlatformu;encrypt=false;";
        String user = "safa";
        String pass = "123456";

        String query = "SELECT KategoriAdi FROM Kategoriler";

        try (Connection conn = DriverManager.getConnection(url, user, pass);
             PreparedStatement stmt = conn.prepareStatement(query);
             var rs = stmt.executeQuery()) {

            while (rs.next()) {
                comboKategori.getItems().add(rs.getString("KategoriAdi"));
            }
        } catch (Exception e) {
            showAlert("Hata", "Kategoriler yüklenirken bir hata oluştu: " + e.getMessage());
        }
    }

    @FXML
    private void handleIlanEkle() {
        String baslik = txtBaslik.getText();
        String aciklama = txtAciklama.getText();
        String urunDurumu = txtUrunDurumu.getText();
        String kategoriAdi = comboKategori.getValue();

        if (baslik.isEmpty() || aciklama.isEmpty() || urunDurumu.isEmpty() || kategoriAdi == null) {
            showAlert("Hata", "Tüm alanlar doldurulmalıdır.");
            return;
        }

        int kullaniciID = LoginController.currentUserID; // Mevcut kullanıcı ID'si
        String url = "jdbc:sqlserver://localhost:1433;databaseName=SosyalYardimlasmaPlatformu;encrypt=false;";
        String user = "safa";
        String pass = "123456";

        String query = """
                INSERT INTO Ilanlar (Baslik, Aciklama, UrunDurumu, IlanTarihi, IlanDurumu, KategoriID, KullaniciID, isDelete)
                VALUES (?, ?, ?, ?, ?, (SELECT KategoriID FROM Kategoriler WHERE KategoriAdi = ?), ?, ?)
                """;

        String ilanTarihi = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        try (Connection conn = DriverManager.getConnection(url, user, pass);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, baslik);
            stmt.setString(2, aciklama);
            stmt.setString(3, urunDurumu);
            stmt.setString(4, ilanTarihi);
            stmt.setInt(5, 1); // ilanDurumu başlangıç değeri 0
            stmt.setString(6, kategoriAdi);
            stmt.setInt(7, kullaniciID); // Giriş yapan kullanıcı ID'si
            stmt.setInt(8, 0); // isDelete başlangıç değeri 0

            stmt.executeUpdate();
            showAlert("Başarılı", "İlan başarıyla eklendi.");

            // Pop-up'ı kapat
            Stage stage = (Stage) btnIlanEkle.getScene().getWindow();
            stage.close();

        } catch (Exception e) {
            showAlert("Hata", "İlan eklenirken bir hata oluştu: " + e.getMessage());
        }
    }

    @FXML
    private void handleGeri() {
        // Pop-up ekranı kapat
        Stage stage = (Stage) btnGeri.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
