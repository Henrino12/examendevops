package com.example.examen_sem1javafx;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class ProductController implements Initializable {

    @FXML
    private PieChart pieChart;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public static void addProduit(Produit produit) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            // Établir une connexion à la base de données
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/examenjavafx", "root", "");

            // Préparer la requête SQL pour ajouter le produit
            String query = "INSERT INTO produit (libelle, quantite, prix_unitaire, idcategory) VALUES (?, ?, ?, ?)";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, produit.getLibelle());
            preparedStatement.setInt(2, produit.getQuantite());
            preparedStatement.setDouble(3, produit.getPrix());
            preparedStatement.setInt(4, produit.getCategorieId());
            preparedStatement.executeUpdate();


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public static ObservableList<Produit> getProduitsFromDB() {
        ObservableList<Produit> produits = FXCollections.observableArrayList();

        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/examenjavafx", "root", "");
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM produit");
            while (resultSet.next()) {
                int id = resultSet.getInt("id_prod");
                String libelle = resultSet.getString("libelle");
                int quantite = resultSet.getInt("quantite");
                double prix = resultSet.getDouble("prix_unitaire");
                int categorieId = resultSet.getInt("idcategory");

                produits.add(new Produit(id, libelle, quantite, prix, categorieId));
            }
        } catch (SQLException e) {
            e.printStackTrace();

        } finally {

            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();

            }
        }

        return produits;
    }
    



    public static void updateProduit(Produit produit) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/examenjavafx", "root", "");
            String query = "UPDATE produit SET libelle = ?, quantite = ?, prix_unitaire = ?, idcategory = ? WHERE id_prod = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, produit.getLibelle());
            preparedStatement.setInt(2, produit.getQuantite());
            preparedStatement.setDouble(3, produit.getPrix());
            preparedStatement.setInt(4, produit.getCategorieId());
            preparedStatement.setInt(5, produit.getId());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    public static void deleteProduit(Produit produit) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/examenjavafx", "root", "");
            String query = "DELETE FROM produit  WHERE id_prod = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, produit.getId());
            preparedStatement.executeUpdate();


            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Produit supprimer avec succes !");
            alert.show();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
   public static void exportToPDF() {
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream("C:/Users/User/Documents/examenjava/liste_produits.pdf"));
            document.open();

            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/examenjavafx", "root", "");
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM produit");

            while (resultSet.next()) {
                String libelle = resultSet.getString("libelle");
                int quantite = resultSet.getInt("quantite");
                double prix = resultSet.getDouble("prix_unitaire");

                Paragraph p = new Paragraph("Libelle: " + libelle +", quantité: " + quantite + ", Prix: " + prix);
                document.add(p);

            }
            document.close();
            connection.close();
            System.out.println("Exportation réussie !");
        } catch (Exception e) {
            System.out.println("Erreur lors de l'exportation vers PDF !");
            e.printStackTrace();
        }
    }
    public static void exportToExcel() {
        try {
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Nombre de Produits par Catégorie");

            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/examenjavafx", "root", "");
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT c.libelle AS categorie, COUNT(p.idcategory) AS nombre_produits FROM produit p INNER JOIN category c ON p.idcategory = c.id GROUP BY p.idcategory");

            int rowNum = 0;
            while (resultSet.next()) {
                String nomCategorie = resultSet.getString("categorie");
                int nombreProduits = resultSet.getInt("nombre_produits");

                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(nomCategorie); // Ajoutez la colonne nomCategorie
                row.createCell(1).setCellValue(nombreProduits); // Ajoutez la colonne nombreProduits
            }

            String filePath = "C:/Users/User/Documents/examenjava/nombre_produits_par_categorie.xlsx";
            FileOutputStream outputStream = new FileOutputStream(filePath);
            workbook.write(outputStream);
            outputStream.close();

            connection.close();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Exportation réussie ! !");
            alert.show();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Erreur lors de l'exportation vers Excel ! !");
            alert.show();
            e.printStackTrace();
        }
    }

    public void updateProductTableView(TableView<Produit> tab_produit) {

        tab_produit.setItems(getProduitsFromDB());
    }

    public static ObservableList<Produit> getProduitsFromDB(String searchText) {
        ObservableList<Produit> produits = FXCollections.observableArrayList();

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/examenjavafx", "root", "");

            // Préparer la requête SQL avec une clause WHERE pour filtrer par libellé
            String query = "SELECT * FROM produit WHERE libelle LIKE ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, "%" + searchText + "%");

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id_prod");
                String libelle = resultSet.getString("libelle");
                int quantite = resultSet.getInt("quantite");
                double prix = resultSet.getDouble("prix_unitaire");
                int categorieId = resultSet.getInt("idcategory");

                produits.add(new Produit(id, libelle, quantite, prix, categorieId));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();

            }
        }

        return produits;
    }




}
