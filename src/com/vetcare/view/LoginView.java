package com.vetcare.view;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LoginView {

    public void show(Stage stage) {

        Label titulo = new Label("Clínica Veterinaria VetCare");

        TextField usuario = new TextField(); //Crea la barra de texto para introducir el usuario
        usuario.setPromptText("Usuario"); //Muestra Usuario en la barra de texto usurio

        PasswordField contraseña = new PasswordField(); //Crea la barra de texto para introducir la contraseña
        contraseña.setPromptText("Contraseña"); //Muestra Contraseña en la barra de texto contraseña

        Button login = new Button("Iniciar sesión"); //Crea el botón con el nombre Iniciar sesión

        Label mensaje = new Label(); //Muestra mensaje si a accedido bien o si no

        login.setOnAction(e -> {
            String user = usuario.getText();
            String pass = contraseña.getText();

            if (user.equals("admin") && pass.equals("1234")) {
                mensaje.setText("✔ Acceso correcto");
            } else {
                mensaje.setText("Usuario o contraseña incorrectos");
            }
        });

        VBox layout = new VBox(10); //Separación entre la barra de usuario y la de contraseña
        layout.getChildren().addAll(titulo, usuario, contraseña, login, mensaje); //Añade Usuario, Contraseña y botón en la ventana para que se vean

        Scene scene = new Scene(layout, 650, 450); //Tamaño de la ventana

        stage.setTitle("Login VetCare"); //Muestra el titulo de la ventana
        stage.setScene(scene);
        stage.show(); //Abre la ventana

        //Para conectar con el archivo login.css
        scene.getStylesheets().add(
                LoginView.class.getResource("/login.css").toExternalForm()
        );
    }

}
