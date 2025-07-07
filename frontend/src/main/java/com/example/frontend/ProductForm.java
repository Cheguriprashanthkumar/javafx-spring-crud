package com.example.frontend;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.math.BigDecimal;

public class ProductForm extends Dialog<Product> {

    private TextField nameField = new TextField();
    private TextField unitPriceField = new TextField();
    private TextField categoryField = new TextField();
    private TextField hsnCodeField = new TextField();

    public ProductForm(Product product) {
        setTitle(product == null ? "Add Product" : "Edit Product");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        // Labels + fields
        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);

        grid.add(new Label("Unit Price:"), 0, 1);
        grid.add(unitPriceField, 1, 1);

        grid.add(new Label("Category:"), 0, 2);
        grid.add(categoryField, 1, 2);

        grid.add(new Label("HSN Code:"), 0, 3);
        grid.add(hsnCodeField, 1, 3);

        getDialogPane().setContent(grid);

        ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);


        if (product != null) {
            nameField.setText(product.getName());
            unitPriceField.setText(String.valueOf(product.getUnitPrice()));
            categoryField.setText(product.getCategory());
            hsnCodeField.setText(String.valueOf(product.getHsnCode()));
        }

        setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                Product p = new Product();
                p.setName(nameField.getText().trim());
                try {
                    p.setUnitPrice(new BigDecimal(unitPriceField.getText().trim()));
                } catch (NumberFormatException e) {
                    showError("Invalid unit price.");
                    return null;
                }

                p.setCategory(categoryField.getText().trim());
                try {
                    p.setHsnCode(Integer.parseInt(hsnCodeField.getText().trim()));
                } catch (NumberFormatException e) {
                    showError("Invalid HSN code.");
                    return null;
                }
                return p;
            }
            return null;
        });
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Validation Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
