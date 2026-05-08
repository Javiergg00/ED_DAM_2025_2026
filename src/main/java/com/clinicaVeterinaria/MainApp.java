package com.clinicaVeterinaria;

import javafx.application.Application;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) {
        stage.setTitle("Clinica Veterinaria");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}