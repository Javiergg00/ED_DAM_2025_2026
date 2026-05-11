package com.controller;

import com.clinicaVeterinaria.AppNavigator;
import com.model.SessionUser;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class VeterinarioController {
    private final AppNavigator navigator;
    private final SessionUser sessionUser;
    private final BorderPane root;

    public VeterinarioController(AppNavigator navigator, SessionUser sessionUser) {
        this.navigator = navigator;
        this.sessionUser = sessionUser;
        this.root = new BorderPane();
        buildView();
    }

    public Parent getView() {
        return root;
    }

    private void buildView() {
        root.setTop(buildHeader());
        root.setCenter(buildBody());
        root.setStyle("-fx-background-color: #f4f7fb;");
    }

    private HBox buildHeader() {
        Label title = new Label("Panel de Veterinario");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));

        Label user = new Label(sessionUser.getUsername() + " · " + sessionUser.getRole().getDisplayName());

        Button logout = new Button("Cerrar sesión");
        logout.setOnAction(event -> navigator.showLogin());

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox header = new HBox(16, title, spacer, user, logout);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(20));
        header.setStyle("-fx-background-color: white; -fx-border-color: #d8e0ea; -fx-border-width: 0 0 1 0;");
        return header;
    }

    private VBox buildBody() {
        VBox body = new VBox(16,
                buildCard("Agenda del día", "Espacio para listar citas y revisiones."),
                buildCard("Historial clínico", "Plantilla para consultas, diagnósticos y tratamientos."),
                buildCard("Pruebas y recetas", "Zona base para acciones médicas del veterinario.")
        );
        body.setPadding(new Insets(24));
        return body;
    }

    private VBox buildCard(String title, String description) {
        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));

        Label textLabel = new Label(description);
        textLabel.setWrapText(true);

        VBox card = new VBox(8, titleLabel, textLabel);
        card.setPadding(new Insets(18));
        card.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-border-color: #d8e0ea; -fx-border-radius: 10;");
        return card;
    }
}
