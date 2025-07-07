package com.example.frontend;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class LoginScreen {

    private static final HttpClient httpClient = HttpClient.newHttpClient();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void show(Stage stage) {
        TextField usernameField = new TextField();
        usernameField.setPromptText("Name");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        Button loginBtn = new Button("Login");
        Button registerBtn = new Button("Register");

        loginBtn.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();

            if (username.isEmpty() || password.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "⚠️ Please enter both name and password.");
                return;
            }


            validateLogin(username, password, stage);
        });

        registerBtn.setOnAction(e -> {
            new RegisterScreen().start(stage);
        });

        VBox vbox = new VBox(10, usernameField, passwordField, loginBtn, registerBtn);
        vbox.setPadding(new Insets(20));
        stage.setScene(new Scene(vbox, 300, 250));
        stage.setTitle("Login / Register");
        stage.show();
    }


    private static void validateLogin(String username, String password, Stage stage) {
        try {
            LoginRequest loginRequest = new LoginRequest(username, password);
            String json = objectMapper.writeValueAsString(loginRequest);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8081/api/auth/login"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenAccept(response -> Platform.runLater(() -> {
                        int status = response.statusCode();

                        if (status >= 200 && status < 300) {
                            AuthContext.username = username;
                            showAlert(Alert.AlertType.INFORMATION, "✅ Login successful.");
                            new ProductScreen().start(stage);
                        } else {
                            showAlert(Alert.AlertType.ERROR, "❌ Invalid name or password.");
                        }
                    }))
                    .exceptionally(ex -> {
                        ex.printStackTrace();
                        Platform.runLater(() ->
                                showAlert(Alert.AlertType.ERROR, "🚨 Failed to login: " + ex.getMessage()));
                        return null;
                    });
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "🚨 Failed to prepare login request.");
        }
    }


    private static void showAlert(Alert.AlertType type, String msg) {
        Alert alert = new Alert(type, msg, ButtonType.OK);
        alert.showAndWait();
    }


    static class LoginRequest {
        public String username;
        public String password;

        public LoginRequest(String username, String password) {
            this.username = username;
            this.password = password;
        }
    }
}
