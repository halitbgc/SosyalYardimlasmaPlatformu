package com.example.vtys_java23;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;

public class YorumEkleController {
    @FXML
    private ComboBox<String> cmbAliciKullanicilar;

    @FXML
    private ComboBox<Integer> cmbPuan;

    @FXML
    private TextArea txtYorumMetni;

    @FXML
    private Button btnYorumKaydet;

    private int kullaniciYazanID; // Giriş yapan kullanıcının ID'si
    private final ObservableList<String> aliciKullanicilar = FXCollections.observableArrayList();

    public void setKullaniciYazanID(int kullaniciID) {
        this.kullaniciYazanID = kullaniciID;
    }

    @FXML
    public void initialize() {
        // Puan Dropdown
        cmbPuan.getItems().addAll(1, 2, 3, 4, 5);

        // Kullanıcıları yükle
        aliciKullanicilariYukle();
        cmbAliciKullanicilar.setItems(aliciKullanicilar);
    }

    private void aliciKullanicilariYukle() {
        String query = "SELECT KullaniciID, Ad + ' ' + Soyad AS AdSoyad FROM Kullanicilar";

        try (Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=SosyalYardimlasmaPlatformu;encrypt=false;", "safa", "123456");
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String kullanici = rs.getInt("KullaniciID") + " - " + rs.getString("AdSoyad");
                aliciKullanicilar.add(kullanici);
            }
        } catch (Exception e) {
            showAlert("Hata", "Kullanıcılar yüklenirken bir hata oluştu: " + e.getMessage());
        }
    }

    @FXML
    private void handleYorumKaydet() {
        String selectedAlici = cmbAliciKullanicilar.getValue();
        int selectedPuan = cmbPuan.getValue();
        String yorumMetni = txtYorumMetni.getText();

        if (selectedAlici == null || yorumMetni.isEmpty() || selectedPuan == 0) {
            showAlert("Hata", "Lütfen tüm alanları doldurun!");
            return;
        }

        int aliciID = Integer.parseInt(selectedAlici.split(" - ")[0]);
        LocalDate tarih = LocalDate.now();

        String query = """
                INSERT INTO Yorumlar (KullaniciID_Yazan, KullaniciID_Alan, YorumMetni, Puan, YorumTarihi, isDelete)
                VALUES (?, ?, ?, ?, ?, 0)
                """;

        try (Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=SosyalYardimlasmaPlatformu;encrypt=false;", "safa", "123456");
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, kullaniciYazanID);
            stmt.setInt(2, aliciID);
            stmt.setString(3, yorumMetni);
            stmt.setInt(4, selectedPuan);
            stmt.setString(5, tarih.toString());

            stmt.executeUpdate();
            showAlert("Bilgi", "Yorum başarıyla eklendi.");
            btnYorumKaydet.getScene().getWindow().hide();
        } catch (Exception e) {
            showAlert("Hata", "Yorum eklenirken hata oluştu: " + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
