package com.vetcare;

import com.vetcare.view.LoginView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        new LoginView().show(stage);
    }

    public static void main(String[] args) {
        launch();
    }
}
