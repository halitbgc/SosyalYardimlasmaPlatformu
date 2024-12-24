package com.example.vtys_java23;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class YorumGoruntuleController {

    @FXML
    private Label lblYazanAdi;

    @FXML
    private Label lblAlanAdi;

    @FXML
    private TextArea lblYorumMetni;

    @FXML
    private Label lblPuan;

    @FXML
    private Label lblTarih;

    // Yorum bilgilerini pop-up ekrana aktaran metod
    public void initialize(Yorum selectedYorum) {
        lblYazanAdi.setText(selectedYorum.getYazanAdi());
        lblAlanAdi.setText(selectedYorum.getAlanAdi());
        lblYorumMetni.setText(selectedYorum.getYorumMetni());
        lblPuan.setText(String.valueOf(selectedYorum.getPuan()));
        lblTarih.setText(selectedYorum.getYorumTarihi());
    }
}
