package com.clinicaVeterinaria;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        AppNavigator navigator = new AppNavigator(primaryStage);
        navigator.showLogin();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
