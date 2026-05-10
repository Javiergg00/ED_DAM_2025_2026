package com.controller;

import com.clinicaVeterinaria.AppNavigator;
import com.model.SessionUser;
import com.model.UserRole;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class LoginController {
    private final AppNavigator navigator;
    private final BorderPane root;

    public LoginController(AppNavigator navigator) {
        this.navigator = navigator;
        this.root = new BorderPane();
        buildView();
    }

    public Parent getView() {
        return root;
    }

    private void buildView() {
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #f7fbf9, #e4f0ea);");
        root.setCenter(buildContent());
    }

    private VBox buildContent() {
        Label title = new Label("Clínica Veterinaria");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        title.setTextFill(Color.web("#214d3d"));

        Label subtitle = new Label("Plantilla base con acceso por perfil para el equipo.");
        subtitle.setTextFill(Color.web("#4f6f63"));

        TabPane tabs = new TabPane(buildLoginTab(), buildRegisterTab());
        tabs.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabs.setMaxWidth(520);
        tabs.setStyle("-fx-background-color: white;");

        VBox box = new VBox(18, title, subtitle, tabs);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(36));
        box.setMaxWidth(620);
        return box;
    }

    private Tab buildLoginTab() {
        TextField usernameField = new TextField();
        usernameField.setPromptText("Usuario o email");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Contraseña");

        ComboBox<UserRole> roleBox = new ComboBox<>();
        roleBox.getItems().addAll(navigator.availableRoles());
        roleBox.setPromptText("Selecciona tu perfil");

        Label feedback = createFeedbackLabel();

        Button loginButton = new Button("Iniciar sesión");
        loginButton.setDefaultButton(true);
        loginButton.setStyle(buttonStyle("#2f7d61"));
        loginButton.setOnAction(event -> {
            if (usernameField.getText().isBlank() || passwordField.getText().isBlank() || roleBox.getValue() == null) {
                feedback.setText("Completa usuario, contraseña y perfil.");
                feedback.setTextFill(Color.FIREBRICK);
                return;
            }

            navigator.openRoleHome(new SessionUser(usernameField.getText().trim(), roleBox.getValue()));
        });

        VBox box = new VBox(14,
                sectionTitle("Acceso"),
                buildFormRow("Usuario", usernameField),
                buildFormRow("Contraseña", passwordField),
                buildFormRow("Perfil", roleBox),
                loginButton,
                feedback
        );
        box.setPadding(new Insets(24));

        Tab tab = new Tab("Iniciar sesión");
        tab.setContent(box);
        return tab;
    }

    private Tab buildRegisterTab() {
        TextField fullNameField = new TextField();
        fullNameField.setPromptText("Nombre y apellidos");

        TextField emailField = new TextField();
        emailField.setPromptText("Correo");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Contraseña");

        ComboBox<UserRole> roleBox = new ComboBox<>();
        roleBox.getItems().addAll(navigator.availableRoles());
        roleBox.setPromptText("Perfil para pruebas");

        Label feedback = createFeedbackLabel();

        Button registerButton = new Button("Registrarse");
        registerButton.setStyle(buttonStyle("#4477aa"));
        registerButton.setOnAction(event -> {
            if (fullNameField.getText().isBlank() || emailField.getText().isBlank()
                    || passwordField.getText().isBlank() || roleBox.getValue() == null) {
                feedback.setText("Completa todos los campos del registro.");
                feedback.setTextFill(Color.FIREBRICK);
                return;
            }

            feedback.setText("Registro simulado. La integración con BD se añadirá después.");
            feedback.setTextFill(Color.web("#2f7d61"));
        });

        VBox box = new VBox(14,
                sectionTitle("Registro"),
                buildFormRow("Nombre", fullNameField),
                buildFormRow("Correo", emailField),
                buildFormRow("Contraseña", passwordField),
                buildFormRow("Perfil", roleBox),
                registerButton,
                feedback
        );
        box.setPadding(new Insets(24));

        Tab tab = new Tab("Registrarse");
        tab.setContent(box);
        return tab;
    }

    private Label sectionTitle(String text) {
        Label label = new Label(text);
        label.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        label.setTextFill(Color.web("#1f2f29"));
        return label;
    }

    private GridPane buildFormRow(String labelText, javafx.scene.Node field) {
        Label label = new Label(labelText);
        label.setMinWidth(110);

        GridPane row = new GridPane();
        row.setHgap(12);
        row.setVgap(6);
        row.add(label, 0, 0);
        row.add(field, 1, 0);
        GridPane.setHgrow(field, Priority.ALWAYS);
        return row;
    }

    private Label createFeedbackLabel() {
        Label feedback = new Label(" ");
        feedback.setWrapText(true);
        return feedback;
    }

    private String buttonStyle(String color) {
        return "-fx-background-color: " + color + ";"
                + "-fx-text-fill: white;"
                + "-fx-font-weight: bold;"
                + "-fx-background-radius: 6;"
                + "-fx-padding: 10 18;";
    }
}
