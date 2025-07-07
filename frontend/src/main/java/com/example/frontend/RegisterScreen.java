package com.example.frontend;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class RegisterScreen {

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public void start(Stage stage) {
        stage.setTitle("User Registration");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setVgap(10);
        grid.setHgap(10);

        TextField nameField = new TextField();
        TextField ageField = new TextField();
        TextField dobField = new TextField();
        TextField contactField = new TextField();

        ComboBox<String> countryBox = new ComboBox<>();
        countryBox.setPromptText("Select Country");

        ComboBox<String> stateBox = new ComboBox<>();
        stateBox.setPromptText("Select State");
        stateBox.setDisable(true);

        ComboBox<String> cityBox = new ComboBox<>();
        cityBox.setPromptText("Select City");
        cityBox.setDisable(true);

        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);

        grid.add(new Label("Age:"), 0, 1);
        grid.add(ageField, 1, 1);

        grid.add(new Label("Date of Birth (YYYY-MM-DD):"), 0, 2);
        grid.add(dobField, 1, 2);

        grid.add(new Label("Contact Number:"), 0, 3);
        grid.add(contactField, 1, 3);

        grid.add(new Label("Country:"), 0, 4);
        grid.add(countryBox, 1, 4);

        grid.add(new Label("State:"), 0, 5);
        grid.add(stateBox, 1, 5);

        grid.add(new Label("City:"), 0, 6);
        grid.add(cityBox, 1, 6);

        Button registerBtn = new Button("Register");
        grid.add(registerBtn, 1, 7);

        registerBtn.setOnAction(e -> {
            String name = nameField.getText();
            String ageStr = ageField.getText();
            String dob = dobField.getText();
            String contact = contactField.getText();
            String country = countryBox.getValue();
            String state = stateBox.getValue();
            String city = cityBox.getValue();

            if (name.isEmpty() || ageStr.isEmpty() || dob.isEmpty() || contact.isEmpty()
                    || country == null || state == null || city == null) {
                showAlert(Alert.AlertType.ERROR, "⚠️ Please fill all fields!");
                return;
            }

            if (!name.matches("^[A-Za-z ]+$")) {
                showAlert(Alert.AlertType.ERROR, "⚠️ Name must contain only letters and spaces.");
                return;
            }

            int age;
            try {
                age = Integer.parseInt(ageStr);
                if (age <= 0 || age > 80) {
                    showAlert(Alert.AlertType.ERROR, "⚠️ Age must be between 1 and 80.");
                    return;
                }
            } catch (NumberFormatException ex) {
                showAlert(Alert.AlertType.ERROR, "⚠️ Invalid age. Please enter a number.");
                return;
            }

            if (!dob.matches("^\\d{4}-\\d{2}-\\d{2}$")) {
                showAlert(Alert.AlertType.ERROR, "⚠️ Date of Birth must be in YYYY-MM-DD format.");
                return;
            }

            if (!contact.matches("^[0-9+\\-]+$")) {
                showAlert(Alert.AlertType.ERROR, "⚠️ Contact can only contain digits, + and -.");
                return;
            }

            // password is NOT collected here
            User user = new User(name, age, dob, contact, country, state, city, null);
            registerUser(user, stage);
        });

        stage.setScene(new Scene(grid, 450, 450));
        stage.show();

        loadCountries(countryBox);

        countryBox.setOnAction(e -> {
            String selectedCountry = countryBox.getValue();
            if (selectedCountry != null) {
                stateBox.getItems().clear();
                cityBox.getItems().clear();
                stateBox.setDisable(true);
                cityBox.setDisable(true);
                loadStates(selectedCountry, stateBox);
            }
        });

        stateBox.setOnAction(e -> {
            String selectedState = stateBox.getValue();
            if (selectedState != null) {
                cityBox.getItems().clear();
                cityBox.setDisable(true);
                loadCities(selectedState, cityBox);
            }
        });
    }

    private void loadCountries(ComboBox<String> countryBox) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8081/api/auth/countries"))
                .GET()
                .build();

        httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(response -> {
                    String[] countries = response
                            .replace("[", "")
                            .replace("]", "")
                            .replace("\"", "")
                            .split(",");
                    Platform.runLater(() -> countryBox.setItems(FXCollections.observableArrayList(countries)));
                })
                .exceptionally(ex -> {
                    ex.printStackTrace();
                    return null;
                });
    }

    private void loadStates(String country, ComboBox<String> stateBox) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8081/api/auth/states?country=" + country.replace(" ", "%20")))
                .GET()
                .build();

        httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(response -> {
                    String[] states = response
                            .replace("[", "")
                            .replace("]", "")
                            .replace("\"", "")
                            .split(",");
                    Platform.runLater(() -> {
                        stateBox.setItems(FXCollections.observableArrayList(states));
                        stateBox.setDisable(false);
                    });
                })
                .exceptionally(ex -> {
                    ex.printStackTrace();
                    return null;
                });
    }

    private void loadCities(String state, ComboBox<String> cityBox) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8081/api/auth/cities?state=" + state.replace(" ", "%20")))
                .GET()
                .build();

        httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(response -> {
                    String[] cities = response
                            .replace("[", "")
                            .replace("]", "")
                            .replace("\"", "")
                            .split(",");
                    Platform.runLater(() -> {
                        cityBox.setItems(FXCollections.observableArrayList(cities));
                        cityBox.setDisable(false);
                    });
                })
                .exceptionally(ex -> {
                    ex.printStackTrace();
                    return null;
                });
    }

    private void registerUser(User user, Stage stage) {
        try {
            String json = objectMapper.writeValueAsString(user);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8081/api/users/register"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenAccept(response -> {
                        Platform.runLater(() -> {
                            int statusCode = response.statusCode();
                            String body = response.body().trim();

                            if (statusCode >= 200 && statusCode < 300) {

                                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                alert.setTitle("Registration Successful");
                                alert.setHeaderText("Your Dummy Password");
                                alert.setContentText(body);

                                alert.showAndWait();


                                javafx.scene.input.Clipboard clipboard = javafx.scene.input.Clipboard.getSystemClipboard();
                                javafx.scene.input.ClipboardContent content = new javafx.scene.input.ClipboardContent();
                                content.putString(body);
                                clipboard.setContent(content);

                                LoginScreen.show(stage);
                            } else {
                                showAlert(Alert.AlertType.ERROR, "❌ Failed to register: " + body);
                            }
                        });
                    })
                    .exceptionally(ex -> {
                        ex.printStackTrace();
                        Platform.runLater(() -> showAlert(Alert.AlertType.ERROR, "🚨 Failed to register user: " + ex.getMessage()));
                        return null;
                    });
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "🚨 Failed to prepare registration request!");
        }
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type, message, ButtonType.OK);
        alert.showAndWait();
    }
}
