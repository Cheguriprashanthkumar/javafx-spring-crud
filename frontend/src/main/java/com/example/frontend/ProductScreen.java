package com.example.frontend;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.scene.layout.GridPane;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Optional;

public class ProductScreen {

    private final TableView<Product> table = new TableView<>();
    private final ObservableList<Product> data = FXCollections.observableArrayList();
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    private final String BASE_URL = "http://localhost:8081/api/products";

    public void start(Stage stage) {
        stage.setTitle("Product Management");

        TableColumn<Product, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(cellData -> cellData.getValue().nameProperty());

        TableColumn<Product, String> categoryCol = new TableColumn<>("Category");
        categoryCol.setCellValueFactory(cellData -> cellData.getValue().categoryProperty());

        TableColumn<Product, Number> priceCol = new TableColumn<>("Unit Price");
        priceCol.setCellValueFactory(cellData -> {
            BigDecimal price = cellData.getValue().getUnitPrice();
            double value = (price != null) ? price.doubleValue() : 0.0;
            return new SimpleDoubleProperty(value);
        });

        TableColumn<Product, Number> hsnCol = new TableColumn<>("HSN Code");
        hsnCol.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getHsnCode()));

        table.getColumns().addAll(nameCol, categoryCol, priceCol, hsnCol);
        table.setItems(data);

        Button addButton = new Button("Add Product");
        addButton.setOnAction(e -> showProductForm());

        HBox buttons = new HBox(10, addButton);
        buttons.setPadding(new Insets(10));

        BorderPane root = new BorderPane();
        root.setCenter(table);
        root.setBottom(buttons);

        stage.setScene(new Scene(root, 600, 400));
        stage.show();

        loadProducts();
    }

    private void loadProducts() {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .GET()
                .build();

        httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(response -> {
                    try {
                        List<Product> products = mapper.readValue(response, new TypeReference<List<Product>>() {});
                        data.setAll(products);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
    }

    private void showProductForm() {
        Dialog<Product> dialog = new Dialog<>();
        dialog.setTitle("Add Product");

        Label nameLabel = new Label("Name:");
        TextField nameField = new TextField();

        Label priceLabel = new Label("Unit Price:");
        TextField priceField = new TextField();

        Label categoryLabel = new Label("Category:");
        ComboBox<String> categoryBox = new ComboBox<>();
        categoryBox.getItems().addAll("book", "fruits", "vegtables");
        categoryBox.setPromptText("Select Category");

        Label hsnLabel = new Label("HSN Code:");
        TextField hsnField = new TextField();

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        grid.add(nameLabel, 0, 0);
        grid.add(nameField, 1, 0);

        grid.add(priceLabel, 0, 1);
        grid.add(priceField, 1, 1);

        grid.add(categoryLabel, 0, 2);
        grid.add(categoryBox, 1, 2);

        grid.add(hsnLabel, 0, 3);
        grid.add(hsnField, 1, 3);

        dialog.getDialogPane().setContent(grid);

        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                String name = nameField.getText();
                String priceStr = priceField.getText();
                String category = categoryBox.getValue();
                String hsnStr = hsnField.getText();

                if (!name.matches("[A-Za-z ]+")) {
                    showAlert(Alert.AlertType.ERROR, "Invalid name: only letters & spaces allowed.");
                    return null;
                }

                BigDecimal price;
                try {
                    price = new BigDecimal(priceStr);
                    if (price.compareTo(BigDecimal.ZERO) <= 0) {
                        showAlert(Alert.AlertType.ERROR, "Unit price must be > 0.");
                        return null;
                    }
                } catch (Exception e) {
                    showAlert(Alert.AlertType.ERROR, "Invalid unit price: must be a decimal.");
                    return null;
                }

                if (category == null || category.isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, "Category is required.");
                    return null;
                }

                int hsnCode;
                try {
                    hsnCode = Integer.parseInt(hsnStr);
                    if (hsnCode <= 0) {
                        showAlert(Alert.AlertType.ERROR, "HSN Code must be > 0.");
                        return null;
                    }
                } catch (Exception e) {
                    showAlert(Alert.AlertType.ERROR, "Invalid HSN Code: must be a number.");
                    return null;
                }

                Product product = new Product();
                product.setName(name);
                product.setUnitPrice(price);
                product.setCategory(category);
                product.setHsnCode(hsnCode);

                return product;
            }
            return null;
        });

        Optional<Product> result = dialog.showAndWait();
        result.ifPresent(this::saveProduct);
    }

    private void saveProduct(Product product) {
        try {
            String json = mapper.writeValueAsString(product);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenAccept(response -> {
                        System.out.println("Response: " + response.body());
                        loadProducts();
                    });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type, message, ButtonType.OK);
        alert.showAndWait();
    }
}
