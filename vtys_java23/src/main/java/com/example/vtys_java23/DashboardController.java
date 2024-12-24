package com.example.vtys_java23;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DashboardController {
    @FXML
    private Button exit;
    @FXML
    private Button logout;
    @FXML
    private Button btnIlanEkle;
    @FXML
    private Button btnBagisYap;
    @FXML
    private Button btnIlanSil;
    @FXML
    private Button btnYorumSil;

    //İlanlar
    @FXML
    private TableView<Ilan> ilanTable;
    @FXML
    private TableColumn<Ilan, String> colBaslik;
    @FXML
    private TableColumn<Ilan, String> colAciklama;
    @FXML
    private TableColumn<Ilan, String> colUrunDurumu;
    @FXML
    private TableColumn<Ilan, String> colIlanTarihi;
    @FXML
    private TableColumn<Ilan, Integer> colIlanDurumu; // int tipi kullanılıyor
    @FXML
    private TableColumn<Ilan, String> colKategori;

    private final ObservableList<Ilan> ilanListesi = FXCollections.observableArrayList();

    //Bagislar
    @FXML
    private TableColumn<Bagis, String> colAliciIsim;
    @FXML
    private TableColumn<Bagis, String> colAdresIcerigi;
    @FXML
    private TableColumn<Bagis, String> colKargoFirmasiAdi;

    @FXML
    private TableView<Bagis> bagisTable;

    @FXML
    private TableColumn<Bagis, Integer> colAliciID;

    @FXML
    private TableColumn<Bagis, Integer> colAdresID;

    @FXML
    private TableColumn<Bagis, Integer> colKargoFirmaID;

    @FXML
    private TableColumn<Bagis, String> colAlimTarihi;

    @FXML
    private TableColumn<Bagis, String> colIslemDurumu;

    private final ObservableList<Bagis> bagisListesi = FXCollections.observableArrayList();

    //Yorumlar
    @FXML
    private TableView<Yorum> yorumTable;

    @FXML
    private TableColumn<Yorum, Integer> colYorumID;
    @FXML
    private TableColumn<Yorum, Integer> colYazanID;
    @FXML
    private TableColumn<Yorum, Integer> colAlanID;
    @FXML
    private TableColumn<Yorum, String> colYorumMetni;
    @FXML
    private TableColumn<Yorum, Integer> colPuan;
    @FXML
    private TableColumn<Yorum, String> colTarih;
    @FXML
    private TableColumn<Yorum, String> colYazanAdi;
    @FXML
    private TableColumn<Yorum, String> colAlanAdi;

    private final ObservableList<Yorum> yorumListesi = FXCollections.observableArrayList();


    @FXML
    public void initialize() {
        // İlan tablosunun kolonlarını ayarla
        colBaslik.setCellValueFactory(new PropertyValueFactory<>("baslik"));
        colAciklama.setCellValueFactory(new PropertyValueFactory<>("aciklama"));
        colUrunDurumu.setCellValueFactory(new PropertyValueFactory<>("urunDurumu"));
        colIlanTarihi.setCellValueFactory(new PropertyValueFactory<>("ilanTarihi"));
        colKategori.setCellValueFactory(new PropertyValueFactory<>("kategoriAdi"));

        colIlanDurumu.setCellValueFactory(new PropertyValueFactory<>("ilanDurumu"));
        colIlanDurumu.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Integer ilanDurumu, boolean empty) {
                super.updateItem(ilanDurumu, empty);
                if (empty || ilanDurumu == null) {
                    setText(null);
                } else {
                    setText(ilanDurumu == 1 ? "Aktif" : "Bağışlanmış");
                }
            }
        });

        ilanTable.setItems(ilanListesi);


        colAliciIsim.setCellValueFactory(new PropertyValueFactory<>("aliciIsim"));
        colAlimTarihi.setCellValueFactory(new PropertyValueFactory<>("alimTarihi"));
        colIslemDurumu.setCellValueFactory(new PropertyValueFactory<>("islemDurumu"));
        colAdresIcerigi.setCellValueFactory(new PropertyValueFactory<>("adresIcerigi")); // Yeni adres içeriği kolonu
        colKargoFirmasiAdi.setCellValueFactory(new PropertyValueFactory<>("kargoFirmasiAdi")); // Yeni kargo firması adı kolonu

        bagisTable.setItems(bagisListesi);

        colYorumMetni.setCellValueFactory(new PropertyValueFactory<>("yorumMetni"));
        colPuan.setCellValueFactory(new PropertyValueFactory<>("puan"));
        colTarih.setCellValueFactory(new PropertyValueFactory<>("yorumTarihi"));

        colYazanAdi.setCellValueFactory(new PropertyValueFactory<>("yazanAdi"));
        colAlanAdi.setCellValueFactory(new PropertyValueFactory<>("alanAdi"));

        // Verileri yükle
        yorumlariYukle();
        ilanlariYukle();
        bagislariYukle();
    }


    @FXML
    private void handleYorumGoruntule() {
        Yorum selectedYorum = yorumTable.getSelectionModel().getSelectedItem();

        if (selectedYorum != null) {
            try {
                // Yeni pop-up penceresini yükle
                FXMLLoader loader = new FXMLLoader(getClass().getResource("YorumGoruntuleView.fxml"));
                Parent root = loader.load();

                // Controller'a seçilen yorum bilgilerini ilet
                YorumGoruntuleController controller = loader.getController();
                controller.initialize(selectedYorum);

                // Pop-up penceresini göster
                Stage stage = new Stage();
                stage.setTitle("Yorum Detayı");
                stage.setScene(new Scene(root, 300, 500));
                stage.show();

            } catch (Exception e) {
                showAlert("Hata", "Yorum görüntülenirken bir hata oluştu: " + e.getMessage());
                System.out.println(e.getMessage());
            }
        } else {
            showAlert("Seçim Hatası", "Lütfen bir yorum seçin.");
        }
    }


    @FXML
    private void handleYorumEkle() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("YorumEkleView.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Yorum Ekle");
            stage.setScene(new Scene(loader.load()));

            YorumEkleController controller = loader.getController();
            controller.setKullaniciYazanID(LoginController.currentUserID); // Giriş yapan kullanıcı ID'si

            stage.showAndWait();
            yorumListesi.clear(); // Yorum tablosunu güncelle
            yorumlariYukle();
        } catch (IOException e) {
            showAlert("Hata", "Yorum ekleme ekranı yüklenemedi: " + e.getMessage());
            System.out.println(e.getMessage());
        }
    }


    private void yorumlariYukle() {
        String url = "jdbc:sqlserver://localhost:1433;databaseName=SosyalYardimlasmaPlatformu;encrypt=false;";
        String user = "safa";
        String pass = "123456";

        // SQL sorgusunu doğru şekilde yazalım
        String query = """
                SELECT y.yorumID, y.kullaniciID_Yazan, y.kullaniciID_Alan,
                y.yorumMetni, y.puan, y.yorumTarihi, k1.ad + ' ' + k1.soyad AS yazanAdi,
                k2.ad + ' ' + k2.soyad AS alanAdi
                FROM yorumlar y
                JOIN kullanicilar k1 ON y.kullaniciID_Yazan = k1.kullaniciID
                JOIN kullanicilar k2 ON y.kullaniciID_Alan = k2.kullaniciID
                WHERE y.isDelete = 0 
                """;



        try (Connection conn = DriverManager.getConnection(url, user, pass);
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            // Yorumları almak için döngü
            while (rs.next()) {
                yorumListesi.add(new Yorum(
                        rs.getInt("yorumID"),
                        rs.getInt("kullaniciID_Yazan"),
                        rs.getInt("kullaniciID_Alan"),
                        rs.getString("yorumMetni"),
                        rs.getInt("puan"),
                        rs.getString("yorumTarihi"),
                        rs.getString("yazanAdi"),
                        rs.getString("alanAdi")
                ));
            }

            // TableView'e yorumları yükle
            yorumTable.setItems(yorumListesi);
        } catch (Exception e) {
            showAlert("Hata", "Yorumlar yüklenirken bir hata oluştu: " + e.getMessage());
            System.out.println(e.getMessage());
        }
    }


    @FXML
    private void handleYorumSil() {
        Yorum selectedYorum = yorumTable.getSelectionModel().getSelectedItem();

        if (selectedYorum == null) {
            showAlert("Hata", "Silmek için bir yorum seçmelisiniz.");
            return;
        }

        // Onay penceresi oluştur
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Silme Onayı");
        confirmationAlert.setHeaderText("Seçili yorumu silmek istediğinizden emin misiniz?");
        confirmationAlert.setContentText("Yorum: " + selectedYorum.getYorumMetni());

        // Onay penceresindeki yanıtı al
        ButtonType result = confirmationAlert.showAndWait().orElse(ButtonType.CANCEL);

        if (result == ButtonType.OK) {
            try {
                String url = "jdbc:sqlserver://localhost:1433;databaseName=SosyalYardimlasmaPlatformu;encrypt=false;";
                String user = "safa";
                String pass = "123456";

                // SQL sorgusu: Yorumları silmek için isDelete = 1 olarak güncelle
                String query = "UPDATE Yorumlar SET isDelete = 1 WHERE YorumID = ?";

                try (Connection conn = DriverManager.getConnection(url, user, pass);
                     PreparedStatement stmt = conn.prepareStatement(query)) {

                    stmt.setInt(1, selectedYorum.getYorumID());
                    int affectedRows = stmt.executeUpdate();


                    if (affectedRows > 0) {
                        showAlert("Bilgi", "Yorum başarıyla silindi.");
                        yorumListesi.clear(); // Listeyi temizle
                        yorumlariYukle(); // Yorumları yeniden yükle
                    } else {
                        showAlert("Hata", "Yorum silinirken bir sorun oluştu.");
                    }
                }
            } catch (Exception e) {
                showAlert("Hata", "Yorum silinirken bir hata oluştu: " + e.getMessage());
            }
        } else {
            showAlert("Bilgi", "Yorum silme işlemi iptal edildi.");
        }
    }



    private void ilanlariYukle() {
        String url = "jdbc:sqlserver://localhost:1433;databaseName=SosyalYardimlasmaPlatformu;encrypt=false;";
        String user = "safa";
        String pass = "123456";

        String query = """
                SELECT i.Baslik, i.Aciklama, i.UrunDurumu, i.IlanTarihi, i.IlanDurumu, k.KategoriAdi
                FROM Ilanlar i
                JOIN Kategoriler k ON i.KategoriID = k.KategoriID
                WHERE i.isDelete = 0
                """;

        try (Connection conn = DriverManager.getConnection(url, user, pass);
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                ilanListesi.add(new Ilan(
                        rs.getString("Baslik"),
                        rs.getString("Aciklama"),
                        rs.getString("UrunDurumu"),
                        rs.getString("IlanTarihi"),
                        rs.getInt("IlanDurumu"), // İlan durumu int olarak alınıyor
                        rs.getString("KategoriAdi")
                ));
            }
        } catch (Exception e) {
            showAlert("Hata", "İlanlar yüklenirken bir hata oluştu: " + e.getMessage());
        }
    }

    private void bagislariYukle() {
        String url = "jdbc:sqlserver://localhost:1433;databaseName=SosyalYardimlasmaPlatformu;encrypt=false;";
        String user = "safa";
        String pass = "123456";

        String query = """
    SELECT b.BagisID, b.AliciID, b.AdresID, b.KargoFirmaID, b.AlimTarihi, b.IslemDurumu, 
    k.Ad + ' ' + k.Soyad AS AliciIsim, a.AdresIcerigi, kf.KargoFirmasi
    FROM Bagislar b
    JOIN Kullanicilar k ON b.AliciID = k.KullaniciID
    JOIN Adresler a ON b.AdresID = a.AdresID
    JOIN KargoFirmasi kf ON b.KargoFirmaID = kf.KargoFirmaID
    """;

        try (Connection conn = DriverManager.getConnection(url, user, pass);
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                bagisListesi.add(new Bagis(
                        rs.getInt("BagisID"),
                        rs.getInt("AliciID"),
                        rs.getInt("AdresID"),
                        rs.getInt("KargoFirmaID"),
                        rs.getString("AlimTarihi"),
                        rs.getString("IslemDurumu"),
                        rs.getString("AliciIsim"),
                        rs.getString("AdresIcerigi"),
                        rs.getString("KargoFirmasi")
                ));
            }
        } catch (Exception e) {
            showAlert("Hata", "Bağışlar yüklenirken bir hata oluştu: " + e.getMessage());
        }
    }



    @FXML
    private void handleExit() {
        System.exit(0);
    }

    @FXML
    private void handleLogout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MainView.fxml"));
            Stage stage = (Stage) exit.getScene().getWindow();
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.setTitle("Sosyal Yardımlaşma Platformu - Giriş");
        } catch (IOException e) {
            showAlert("Hata", "Ana ekrana yönlendirme başarısız: " + e.getMessage());
        }
    }

    @FXML
    private void handleIlanEkle() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AddIlanView.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Yeni İlan Ekle");
            stage.setScene(new Scene(loader.load()));
            stage.showAndWait();

            // Yeni ilanları tabloya yükle
            ilanListesi.clear();
            ilanlariYukle();

        } catch (IOException e) {
            showAlert("Hata", "İlan ekleme ekranı yüklenemedi: " + e.getMessage());
        }
    }

    @FXML
    private void handleIlanSil() {
        Ilan selectedIlan = ilanTable.getSelectionModel().getSelectedItem();
        if (selectedIlan == null) {
            showAlert("Hata", "Silmek için bir ilan seçmelisiniz.");
            return;
        }

        // Onay penceresi oluştur
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Silme Onayı");
        confirmationAlert.setHeaderText("Seçili ilanı silmek istediğinizden emin misiniz?");
        confirmationAlert.setContentText("İlan Başlığı: " + selectedIlan.getBaslik());

        // Onay penceresindeki yanıtı al
        ButtonType result = confirmationAlert.showAndWait().orElse(ButtonType.CANCEL);

        if (result == ButtonType.OK) {
            try {
                // isDelete = 1 olarak güncelleme
                String query = "UPDATE Ilanlar SET isDelete = 1 WHERE Baslik = ?";

                try (Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=SosyalYardimlasmaPlatformu;encrypt=false;", "safa", "123456");
                     PreparedStatement stmt = conn.prepareStatement(query)) {

                    stmt.setString(1, selectedIlan.getBaslik());
                    stmt.executeUpdate();
                }

                // Tabloyu yeniden yükle
                ilanListesi.clear();
                ilanlariYukle();
                showAlert("Bilgi", "İlan başarıyla silindi.");
            } catch (Exception e) {
                showAlert("Hata", "İlan silinirken bir hata oluştu: " + e.getMessage());
            }
        } else {
            // Kullanıcı silme işlemini iptal etti
            showAlert("Bilgi", "İlan silme işlemi iptal edildi.");
        }
    }


    @FXML
    private void handleBagisYap() {
        Ilan selectedIlan = ilanTable.getSelectionModel().getSelectedItem();
        if (selectedIlan == null) {
            showAlert("Hata", "Bağış yapmak için bir ilan seçmelisiniz.");
            return;
        }

        // İlan Durumunu Kontrol Et
        if (selectedIlan.getIlanDurumu() == 0) {
            showAlert("Hata", "Bu ilan daha önce bağışlanmış. Tekrar bağış yapılamaz.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("BagisYapView.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Bağış Yap");
            stage.setScene(new Scene(loader.load()));

            // Bağış yap ekranına seçilen ilan bilgisini aktar
            BagisYapController controller = loader.getController();
            controller.setSelectedIlan(selectedIlan);

            stage.showAndWait(); // showAndWait, kullanıcı işlemi bitene kadar bekler

            // Bağış yapıldıktan sonra, ilanın durumunu güncelle
            if (controller.isBagisYapildi()) {
                ilanDurumunuGuncelle(selectedIlan.getBaslik());
            }

            // İlanları tekrar yükle
            ilanListesi.clear();  // Önceki ilanları temizle
            ilanlariYukle();  // Yeni ilanlarla tabloyu güncelle

            bagisListesi.clear();
            bagislariYukle();

        } catch (IOException e) {
            showAlert("Hata", "Bağış yap ekranı yüklenemedi: " + e.getMessage());
        }
    }


    @FXML
    private void handleBagisTamamla() {
        Bagis selectedBagis = bagisTable.getSelectionModel().getSelectedItem();
        if (selectedBagis == null) {
            showAlert("Hata", "Tamamlamak için bir bağış seçmelisiniz.");
            return;
        }

        // Update the 'IslemDurumu' in the database to "Completed" (1)
        String updateQuery = "UPDATE Bagislar SET IslemDurumu = 'Tamamlandı' WHERE BagisID = ?";

        try (Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=SosyalYardimlasmaPlatformu;encrypt=false;", "safa", "123456");
             PreparedStatement stmt = conn.prepareStatement(updateQuery)) {

            // Ensure you get the correct BagisID
            stmt.setInt(1, selectedBagis.getBagisID()); // Verify BagisID is correctly set
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                // Update the 'IslemDurumu' in the table view
                selectedBagis.setIslemDurumu("Tamamlandı");
                bagisTable.refresh();  // Refresh the table view to reflect the update

                showAlert("Bilgi", "Bağış başarıyla tamamlandı.");
            } else {
                showAlert("Hata", "Veritabanında güncelleme yapılırken bir sorun oluştu.");
            }
        } catch (Exception e) {
            showAlert("Hata", "Bağış tamamlanırken bir hata oluştu: " + e.getMessage());
        }
    }





    private void ilanDurumunuGuncelle(String baslik) {
        String query = "UPDATE Ilanlar SET IlanDurumu = 0 WHERE Baslik = ?";

        try (Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=SosyalYardimlasmaPlatformu;encrypt=false;", "safa", "123456");
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, baslik);
            stmt.executeUpdate();

        } catch (Exception e) {
            showAlert("Hata", "İlan durumu güncellenirken bir hata oluştu: " + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
