package com.example.frontend;

import javafx.application.Application;
import javafx.stage.Stage;

public class JavaFXApp extends Application {
    @Override
    public void start(Stage stage) {
        LoginScreen.show(stage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
