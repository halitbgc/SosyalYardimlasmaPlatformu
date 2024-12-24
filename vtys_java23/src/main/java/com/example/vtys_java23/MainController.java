package com.example.vtys_java23;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class MainController {
    @FXML
    private Button btnLogin;

    @FXML
    private Button btnRegister;

    @FXML
    private void handleLogin() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("LoginView.fxml"));
        Stage stage = (Stage) btnLogin.getScene().getWindow();
        stage.setScene(new Scene(loader.load(), 400, 300));
    }

    @FXML
    private void handleRegister() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("RegisterView.fxml"));
        Stage stage = (Stage) btnRegister.getScene().getWindow();
        stage.setScene(new Scene(loader.load(), 255, 600));
    }
}
