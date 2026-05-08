package com.clinicaVeterinaria;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        RecepcionistaController controller = new RecepcionistaController(primaryStage);
        Scene scene = new Scene(controller.getView(), 900, 650);
        primaryStage.setTitle("Clínica Veterinaria - Recepcionista");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
