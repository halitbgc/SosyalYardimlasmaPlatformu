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

public class LoginController {
    @FXML
    private Button exit;
    @FXML
    private TextField txtEmail;
    @FXML
    private PasswordField txtPassword;

    // Giriş yapan kullanıcının ID'sini tutacak statik değişken
    public static int currentUserID;

    @FXML
    private void handleExit() {
        System.exit(0);
    }

    @FXML
    private void handleLogin() {
        String email = txtEmail.getText().trim();
        String password = txtPassword.getText().trim();

        if (email.isEmpty() || password.isEmpty()) {
            showAlert("Hata", "E-posta ve şifre boş bırakılamaz.");
            return;
        }

        String url = "jdbc:sqlserver://localhost:1433;databaseName=SosyalYardimlasmaPlatformu;encrypt=false;";
        String user = "safa";
        String pass = "123456";

        String query = "SELECT KullaniciID FROM Kullanicilar WHERE Email = ? AND Sifre = ?";

        try (Connection conn = DriverManager.getConnection(url, user, pass);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, email);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                currentUserID = rs.getInt("KullaniciID"); // Kullanıcı ID'sini kaydet
                redirectToDashboard(); // Başarılı giriş sonrası yönlendirme
            } else {
                showAlert("Hata", "E-posta veya şifre hatalı.");
            }
        } catch (Exception e) {
            showAlert("Hata", "Bağlantı hatası: " + e.getMessage());
        }
    }

    private void redirectToDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Dashboard.fxml"));
            Stage stage = (Stage) txtEmail.getScene().getWindow(); // Mevcut pencereyi alın
            Scene scene = new Scene(loader.load());
            stage.setScene(scene); // Yeni sahneyi pencereye ayarlayın
            stage.setTitle("Sosyal Yardımlaşma Platformu - Ana Ekran");
        } catch (IOException e) {
            showAlert("Hata", "Ana ekrana yönlendirme başarısız: " + e.getMessage());
            System.out.println(e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
