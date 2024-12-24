package com.example.vtys_java23;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class RegisterController {
    @FXML
    private Button exit;
    @FXML
    private TextField txtName, txtSurname, txtEmail, txtPhone, txtAddressTitle, txtAddressContent;
    @FXML
    private PasswordField txtPassword;

    @FXML
    private void handleExit() {
        System.exit(0);
    }

    @FXML
    private void handleRegister() {
        String name = txtName.getText();
        String surname = txtSurname.getText();
        String email = txtEmail.getText();
        String phone = txtPhone.getText();
        String password = txtPassword.getText();
        String addressTitle = txtAddressTitle.getText();
        String addressContent = txtAddressContent.getText();

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        // MSSQL bağlantı bilgileri
        String url = "jdbc:sqlserver://localhost:1433;databaseName=SosyalYardimlasmaPlatformu;encrypt=false;";
        String user = "safa";
        String pass = "123456";

        try {
            conn = DriverManager.getConnection(url, user, pass);

            // Kullanıcıyı Kullanicilar tablosuna ekleme
            String query = "INSERT INTO Kullanicilar (Ad, Soyad, Email, Telefon, Sifre, KayitTarihi) VALUES (?, ?, ?, ?, ?, GETDATE())";
            stmt = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            stmt.setString(1, name);
            stmt.setString(2, surname);
            stmt.setString(3, email);
            stmt.setString(4, phone);
            stmt.setString(5, password);

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    int userId = rs.getInt(1);  // Kullanıcı ID'sini alıyoruz

                    // Kullanıcıyı Adresler tablosuna ekleme (AdresID otomatik artacak)
                    String addressQuery = "INSERT INTO Adresler (AdresBasligi, AdresIcerigi, KullaniciID, isDelete) VALUES (?, ?, ?, ?)";
                    PreparedStatement addressStmt = conn.prepareStatement(addressQuery);
                    addressStmt.setString(1, addressTitle);
                    addressStmt.setString(2, addressContent);
                    addressStmt.setInt(3, userId);  // Kullanıcı ID'si
                    addressStmt.setBoolean(4, false);  // isDelete başlangıçta False

                    addressStmt.executeUpdate();
                    showAlert("Başarılı", "Kayıt başarılı.");
                    redirectToMainScreen(); // Kullanıcıyı ana ekrana yönlendir
                }
            } else {
                showAlert("Hata", "Kayıt işlemi başarısız.");
            }
        } catch (Exception e) {
            showAlert("Hata", "Bağlantı hatası: " + e.getMessage());
            System.out.println(e.getMessage());
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void redirectToMainScreen() {
        try {
            // Ana ekranı yükleyin
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MainView.fxml"));
            Stage stage = (Stage) txtName.getScene().getWindow(); // Mevcut pencereyi alın
            Scene scene = new Scene(loader.load());
            stage.setScene(scene); // Yeni sahneyi pencereye ayarlayın
            stage.setTitle("Sosyal Yardımlaşma Platformu - Ana Ekran");
        } catch (IOException e) {
            showAlert("Hata", "Ana ekrana yönlendirme başarısız: " + e.getMessage());
        }
    }
}
