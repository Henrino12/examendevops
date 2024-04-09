package com.example.examen_sem1javafx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;

public class MenueController implements Initializable {

    @FXML
    private Button btn_updatecat;
    @FXML
    private Button btn_clear;

    @FXML
    private Button btn_deletecat;
    @FXML
    private Button btn_cat;

    @FXML
    private Button btn_doc;

    @FXML
    private Button btn_home;

    @FXML
    private Button btn_logout;

    @FXML
    private Button btn_prod;

    @FXML
    private Button btn_stat;

    @FXML
    private Button btn_add;
    @FXML
    private TextField ch_libelle;
    @FXML
    private TableView<Category> tab_categery;
    @FXML
    private TableColumn<Category, Integer> colid;

    @FXML
    private TableColumn<Category, String> collibelle;
    @FXML
    private TableColumn<Produit, Integer> colpro_id;

    @FXML
    private TableColumn<Produit, String> colpro_libele;

    @FXML
    private TableColumn<Produit, Integer> colprod_prix;

    @FXML
    private TableColumn<Produit, Integer> colprod_quant;

    @FXML
    private TableColumn<Produit, Integer> colcat_id;
    @FXML
    private ChoiceBox<Category> prod_cat;

    @FXML
    private TextField prod_libele;

    @FXML
    private TextField prod_prix;

    @FXML
    private TextField prod_quantite;
    @FXML
    private TableView<Produit> tab_produit;

    @FXML
    private Button btn_addprod;
    @FXML
    private Button btn_updprod;
    @FXML
    private Button btn_prodclear;
    @FXML
    private Button btn_delprod;

    @FXML
    private PieChart pieChart;
    @FXML
    private StackedBarChart<String, Number> barchart;
    @FXML
    private Label lblMessage;

    @FXML
    private AnchorPane doc;
    @FXML
    private AnchorPane pcategory;


    @FXML
    private AnchorPane home;

    @FXML
    private AnchorPane product;

    @FXML
    private AnchorPane statistique;
    @FXML
    private TextField txtSearch;
    @FXML
    private Button btn_search;
    @FXML
    private Button exportExcel;

    @FXML
    private Button exportPdf;

    @FXML
    private Label label_welcome;
    private int id;
    private ObservableList<Produit> produits = FXCollections.observableArrayList();
    private ObservableList<Produit> filteredList = FXCollections.observableArrayList();



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        home.setVisible(true);
        pcategory.setVisible(false);
        product.setVisible(false);
        statistique.setVisible(false);
        doc.setVisible(false);

        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            ObservableList<Produit> filteredProducts = ProductController.getProduitsFromDB(newValue);
            tab_produit.setItems(filteredProducts);
        });

        generatePieChart();
        generateBarChart();

        btn_logout.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                connexionDB.changeScene(event, "/Fxml/login.fxml", "connexion", null);
            }
        });

        tab_categery.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {

                Category selectedCategory = tab_categery.getSelectionModel().getSelectedItem();
                if (selectedCategory != null) {
                    ch_libelle.setText(selectedCategory.getLibelle());
                }
            }
        });
        tab_produit.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                Produit selectedProduit = tab_produit.getSelectionModel().getSelectedItem();
                if (selectedProduit != null) {
                    prod_libele.setText(selectedProduit.getLibelle());
                    prod_prix.setText(String.valueOf(selectedProduit.getPrix()));
                    prod_quantite.setText(String.valueOf(selectedProduit.getQuantite()));
                    int categorieId = selectedProduit.getCategorieId();
                    ObservableList<Category> categories = CategoryController.getCategoriesFromDB();
                    for (Category category : categories) {
                        if (category.getId() == categorieId) {
                            prod_cat.setValue(category);
                            break;
                        }
                    }
                }
            }
        });

        btn_updatecat.setOnAction(this::handleUpdateButton);
        btn_deletecat.setOnAction(this::handleDeleteButton);
        btn_clear.setOnAction(this::handleClearButton);
        btn_addprod.setOnAction(this::handleAjouterButton);
        btn_prodclear.setOnAction(this::handleClearButton);


        btn_add.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (!ch_libelle.getText().trim().isEmpty()){
                    CategoryController.addcategorie(event,ch_libelle.getText(), ch_libelle);
                    updateTableView(tab_categery);
                }else {
                    System.out.println("Attention Libellé");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Veuillez saisir le nom de la catégorie !");
                    alert.show();
                }

            }
        });
        btn_updprod.setOnAction(event -> {
            Produit selectedProduit = tab_produit.getSelectionModel().getSelectedItem();
            if (selectedProduit != null) {
                selectedProduit.setLibelle(prod_libele.getText());
                selectedProduit.setPrix(Double.parseDouble(prod_prix.getText()));
                selectedProduit.setQuantite(Integer.parseInt(prod_quantite.getText()));
                selectedProduit.setCategorieId(prod_cat.getValue().getId());
                ProductController.updateProduit(selectedProduit);
                updateProductTableView(tab_produit);
                clearFields();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Veuillez sélectionner un produit à mettre à jour !");
                alert.show();
            }
        });

        btn_delprod.setOnAction(event -> {
            Produit selectedProduit = tab_produit.getSelectionModel().getSelectedItem();
            if (selectedProduit != null) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation de suppression");
                alert.setHeaderText("Suppression de produit");
                alert.setContentText("Êtes-vous sûr de vouloir supprimer le produit sélectionné ?");
                alert.getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);
                ButtonType result = alert.showAndWait().orElse(ButtonType.CANCEL);

                if (result == ButtonType.OK) {
                    ProductController.deleteProduit(selectedProduit);
                    updateProductTableView(tab_produit);
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Veuillez sélectionner un produit à supprimer !");
                alert.show();
            }
        });
        exportPdf.setOnAction(event -> {
                    ProductController.exportToPDF();


                });
        exportExcel.setOnAction(event -> {
            ProductController.exportToExcel();


        });

        btn_prodclear.setOnAction(event -> {
            clearFields();
        });


        ObservableList<Category> categories = CategoryController.getCategoriesFromDB();
        prod_cat.setItems(categories);
        prod_cat.setConverter(new StringConverter<Category>() {
            @Override
            public String toString(Category category) {
                return category.getLibelle();
            }

            @Override
            public Category fromString(String string) {
                return null;
            }
        });

        colid.setCellValueFactory(new PropertyValueFactory<>("id"));
        collibelle.setCellValueFactory(new PropertyValueFactory<>("libelle"));
        updateTableView(tab_categery);

        colpro_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        colpro_libele.setCellValueFactory(new PropertyValueFactory<>("libelle"));
        colprod_quant.setCellValueFactory(new PropertyValueFactory<>("quantite"));
        colprod_prix.setCellValueFactory(new PropertyValueFactory<>("prix"));
        colcat_id.setCellValueFactory(new PropertyValueFactory<>("categorieId"));
        updateProductTableView(tab_produit);
    }


    private void filterProducts(String txsearch) {
        filteredList.clear();
        if (txsearch.isEmpty()) {
            filteredList.addAll(produits);
        } else {
            for (Produit produit : produits) {
                if (produit.getLibelle().toLowerCase().startsWith(txsearch.toLowerCase())) {
                    filteredList.add(produit);
                }
            }
        }


    }



    private void updateTableView(TableView<com.example.examen_sem1javafx.Category> tab_categery) {
        CategoryController categoryController = new CategoryController();
        categoryController.updateTableView(this.tab_categery);
    }
    private void updateProductTableView(TableView<com.example.examen_sem1javafx.Produit> tab_produit) {
        ProductController productController=new ProductController();
        productController.updateProductTableView(this.tab_produit);

    }

    public void SwitchForm(ActionEvent event) {
        if (event.getSource() == btn_home) {
            home.setVisible(true);
            pcategory.setVisible(false);
            product.setVisible(false);
            statistique.setVisible(false);
            doc.setVisible(false);
        } else if (event.getSource() == btn_cat) {
            home.setVisible(false);
            pcategory.setVisible(true);
            product.setVisible(false);
            statistique.setVisible(false);
            doc.setVisible(false);
        } else if (event.getSource() == btn_prod) {
            home.setVisible(false);
            pcategory.setVisible(false);
            product.setVisible(true);
            statistique.setVisible(false);
            doc.setVisible(false);
        } else if (event.getSource() == btn_stat) {
            home.setVisible(false);
            pcategory.setVisible(false);
            product.setVisible(false);
            statistique.setVisible(true);
            doc.setVisible(false);
        } else if (event.getSource() == btn_doc) {
            home.setVisible(false);
            pcategory.setVisible(false);
            product.setVisible(false);
            statistique.setVisible(false);
            doc.setVisible(true);
        }
    }

    public void setInformation(String nom) {
        label_welcome.setText("Bienvenue " + nom + "!");
    }
    @FXML
    private void handleUpdateButton(ActionEvent event) {
        Category selectedCategory = tab_categery.getSelectionModel().getSelectedItem();
        if (selectedCategory != null) {
            selectedCategory.setLibelle(ch_libelle.getText());
            CategoryController.updateCategory(selectedCategory);
            updateTableView(tab_categery);
            ch_libelle.clear();

        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Veuillez sélectionner une catégorie à mettre à jour !");
            alert.show();
        }
    }
    @FXML
    private void handleDeleteButton(ActionEvent event) {
        Category selectedCategory = tab_categery.getSelectionModel().getSelectedItem();
        if (selectedCategory != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation de suppression");
            alert.setHeaderText("Suppression de catégorie");
            alert.setContentText("Êtes-vous sûr de vouloir supprimer la catégorie sélectionnée ?");
            alert.getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);
            ButtonType result = alert.showAndWait().orElse(ButtonType.CANCEL);
            if (result == ButtonType.OK) {
                selectedCategory.setLibelle(ch_libelle.getText());
                CategoryController.deleteCategory(selectedCategory);
                updateTableView(tab_categery);
            }

        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Veuillez sélectionner une catégorie à mettre à supprimer !");
            alert.show();
        }
    }
    private void handleClearButton(ActionEvent event) {
        ch_libelle.clear();
    }

    @FXML
    private void handleAjouterButton(ActionEvent event) {
        String libelle = prod_libele.getText();
        int quantite = Integer.parseInt(prod_quantite.getText());
        double prix = Double.parseDouble(prod_prix.getText());
        int categorieId = prod_cat.getSelectionModel().getSelectedItem().getId();
        Produit produit = new Produit(id, libelle, quantite, prix, categorieId);
        ProductController.addProduit(produit);
        updateProductTableView(tab_produit);

        clearFields();
    }


    private void clearFields() {
        prod_libele.clear();
        prod_prix.clear();
        prod_quantite.clear();
        prod_cat.getSelectionModel().clearSelection();
    }



    private void generatePieChart() {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/examenjavafx", "root", "");
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT c.libelle AS categorie, COUNT(p.idcategory) AS nombre_produits FROM produit p INNER JOIN category c ON p.idcategory = c.id GROUP BY p.idcategory");

            ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
            while (resultSet.next()) {
                String nomCategorie = resultSet.getString("categorie");
                int nombreProduits = resultSet.getInt("nombre_produits");
                pieChartData.add(new PieChart.Data(nomCategorie, nombreProduits));
            }

            pieChart.setData(pieChartData);

            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void generateBarChart() {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/examenjavafx", "root", "");
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT MONTH(date_ajout) AS mois, COUNT(*) AS nombre_produits FROM produit WHERE YEAR(date_ajout) = 2024 GROUP BY MONTH(date_ajout)");

            XYChart.Series<String, Number> series = new XYChart.Series<>();
            while (resultSet.next()) {
                int mois = resultSet.getInt("mois");
                int nombreProduits = resultSet.getInt("nombre_produits");
                series.getData().add(new XYChart.Data<>(getMonthName(mois), nombreProduits));
            }

            barchart.getData().add(series);

            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getMonthName(int month) {
        String[] months = {"", "Janvier", "Février", "Mars", "Avril", "Mai", "Juin", "Juillet", "Août", "Septembre", "Octobre", "Novembre", "Décembre"};
        if (month >= 1 && month <= 12) {
            return months[month];
        } else {
            throw new IllegalArgumentException("Mois invalide : " + month);
        }
    }


}
