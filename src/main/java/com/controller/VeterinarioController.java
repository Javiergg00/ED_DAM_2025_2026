package com.controller;

import com.clinicaVeterinaria.AppNavigator;
import com.model.SessionUser;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class VeterinarioController {
    private final AppNavigator navigator;
    private final SessionUser sessionUser;
    private final BorderPane root;

    private VBox mainBody;
    private VBox formRegistrarConsulta;

    // Estado del paciente seleccionado
    private String selectedPaciente = "Luna";
    private String selectedEspecie = "Gato - Persa";
    private String selectedDueno = "María González";
    private Label selectedPacienteLabel;
    private Label selectedEspecieLabel;
    private Label selectedDuenoLabel;

    // Botones del sidebar para resaltar activo
    private Button btnListaEspera;
    private Button btnRegistrarConsulta;
    private Button btnHistorial;

    public VeterinarioController(AppNavigator navigator, SessionUser sessionUser) {
        this.navigator = navigator;
        this.sessionUser = sessionUser;
        this.root = new BorderPane();
        buildView();
    }

    public Parent getView() {
        return root;
    }
    //Ensambla las tres piezas en el borderpane en el que cada set coloca la seccion en su zona correspondiente
    private void buildView() {
        root.setTop(buildHeader());
        root.setLeft(buildSidebar());
        root.setCenter(buildMainBody());
        root.setStyle("-fx-background-color: #f0f2f5;");
    }

    // ─── HEADER ────────────────────────────────────────────────────────────────
    //En el va tdo el header metido con sus label correspondientes
    private HBox buildHeader() {
        // Icono corazón
        StackPane iconBox = new StackPane();
        iconBox.setMinSize(44, 44);
        iconBox.setMaxSize(44, 44);
        iconBox.setStyle("-fx-background-color: #e8f0fe; -fx-background-radius: 10;");
        Label heartIcon = new Label("♥");
        heartIcon.setStyle("-fx-text-fill: #2563eb; -fx-font-size: 20;");
        iconBox.getChildren().add(heartIcon);
        //Nombre de la app
        Label title = new Label("VerCare");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        title.setStyle("-fx-text-fill: #111827;");
        Label subtitle = new Label("Dr. jht");
        subtitle.setStyle("-fx-text-fill: #6b7280; -fx-font-size: 13;");
        VBox titleBox = new VBox(2, title, subtitle);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button logout = new Button("⎋  Cerrar Sesión");
        logout.setStyle(
                "-fx-background-color: #ef4444;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 13;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 8;" +
                        "-fx-padding: 9 18 9 18;" +
                        "-fx-cursor: hand;"
        );
        logout.setOnAction(event -> navigator.showLogin());

        HBox header = new HBox(14, iconBox, titleBox, spacer, logout);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(12, 24, 12, 24));
        header.setStyle("-fx-background-color: white; -fx-border-color: #e5e7eb; -fx-border-width: 0 0 1 0;");
        return header;
    }

    // ─── SIDEBAR ───────────────────────────────────────────────────────────────
    //Barra lateral
    private VBox buildSidebar() {
        VBox sidebar = new VBox(4);
        sidebar.setPadding(new Insets(20, 12, 20, 12));
        sidebar.setMinWidth(220);
        sidebar.setMaxWidth(220);
        sidebar.setStyle("-fx-background-color: white; -fx-border-color: #e5e7eb; -fx-border-width: 0 1 0 0;");

        btnListaEspera = createNavButton("☰  Lista de Espera", true);
        btnListaEspera.setOnAction(e -> {
            setActiveNav(btnListaEspera);
            showListaEsperaView();
        });

        btnRegistrarConsulta = createNavButton("☑  Registrar Consulta", false);
        btnRegistrarConsulta.setOnAction(e -> {
            setActiveNav(btnRegistrarConsulta);
            showRegistrarConsultaView();
        });

        btnHistorial = createNavButton("🕐  Historial de Consultas", false);
        btnHistorial.setOnAction(e -> {
            setActiveNav(btnHistorial);
            showHistorialView();
        });

        // Separador
        Separator sep = new Separator();
        sep.setPadding(new Insets(8, 0, 8, 0));

        // Card paciente seleccionado
        VBox pacienteCard = new VBox(4);
        pacienteCard.setPadding(new Insets(12));
        pacienteCard.setStyle(
                "-fx-background-color: #f8faff;" +
                        "-fx-border-color: #dbeafe;" +
                        "-fx-border-radius: 8;" +
                        "-fx-background-radius: 8;"
        );

        Label pLabel = new Label("Paciente Seleccionado:");
        pLabel.setStyle("-fx-text-fill: #6b7280; -fx-font-size: 11;");

        selectedPacienteLabel = new Label(selectedPaciente);
        selectedPacienteLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        selectedPacienteLabel.setStyle("-fx-text-fill: #1d4ed8;");

        selectedEspecieLabel = new Label(selectedEspecie);
        selectedEspecieLabel.setStyle("-fx-text-fill: #3b82f6; -fx-font-size: 12;");

        selectedDuenoLabel = new Label("Dueño: " + selectedDueno);
        selectedDuenoLabel.setStyle("-fx-text-fill: #6b7280; -fx-font-size: 11;");

        pacienteCard.getChildren().addAll(pLabel, selectedPacienteLabel, selectedEspecieLabel, selectedDuenoLabel);

        Region grow = new Region();
        VBox.setVgrow(grow, Priority.ALWAYS);

        sidebar.getChildren().addAll(btnListaEspera, btnRegistrarConsulta, btnHistorial, sep, pacienteCard, grow);
        return sidebar;
    }

    private Button createNavButton(String text, boolean active) {
        Button btn = new Button(text);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setAlignment(Pos.CENTER_LEFT);
        btn.setPadding(new Insets(10, 14, 10, 14));
        btn.setFont(Font.font("Segoe UI", 13));
        if (active) {
            btn.setStyle(
                    "-fx-background-color: #2563eb;" +
                            "-fx-text-fill: white;" +
                            "-fx-background-radius: 8;" +
                            "-fx-cursor: hand;" +
                            "-fx-font-weight: bold;"
            );
        } else {
            btn.setStyle(
                    "-fx-background-color: transparent;" +
                            "-fx-text-fill: #374151;" +
                            "-fx-background-radius: 8;" +
                            "-fx-cursor: hand;"
            );
        }
        return btn;
    }

    private void setActiveNav(Button active) {
        for (Button b : new Button[]{btnListaEspera, btnRegistrarConsulta, btnHistorial}) {
            if (b == active) {
                b.setStyle(
                        "-fx-background-color: #2563eb;" +
                                "-fx-text-fill: white;" +
                                "-fx-background-radius: 8;" +
                                "-fx-cursor: hand;" +
                                "-fx-font-weight: bold;"
                );
            } else {
                b.setStyle(
                        "-fx-background-color: transparent;" +
                                "-fx-text-fill: #374151;" +
                                "-fx-background-radius: 8;" +
                                "-fx-cursor: hand;"
                );
            }
        }
    }

    // ─── MAIN BODY ─────────────────────────────────────────────────────────────
    private ScrollPane buildMainBody() {
        mainBody = new VBox(20);
        mainBody.setPadding(new Insets(24));

        showListaEsperaView();

        ScrollPane scroll = new ScrollPane(mainBody);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: transparent; -fx-background: #f0f2f5;");
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        return scroll;
    }

    // ─── LISTA DE ESPERA VIEW ──────────────────────────────────────────────────
    private void showListaEsperaView() {
        mainBody.getChildren().clear();

        // Título
        Label title = new Label("Lista de Espera");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 22));
        title.setStyle("-fx-text-fill: #111827;");

        // Subtítulo con urgentes en rojo
        HBox subtitle = new HBox(6);
        subtitle.setAlignment(Pos.CENTER_LEFT);
        Label sub1 = new Label("5 mascotas en espera");
        sub1.setStyle("-fx-text-fill: #6b7280; -fx-font-size: 13;");
        Label sub2 = new Label("(2 urgentes)");
        sub2.setStyle("-fx-text-fill: #ef4444; -fx-font-size: 13; -fx-font-weight: bold;");
        subtitle.getChildren().addAll(sub1, sub2);

        // Sección Urgentes
        HBox urgentesHeader = new HBox(8);
        urgentesHeader.setAlignment(Pos.CENTER_LEFT);
        Label urgIcon = new Label("⚠");
        urgIcon.setStyle("-fx-text-fill: #ef4444; -fx-font-size: 16;");
        Label urgTitle = new Label("Casos Urgentes");
        urgTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        urgTitle.setStyle("-fx-text-fill: #ef4444;");
        urgentesHeader.getChildren().addAll(urgIcon, urgTitle);

        HBox urgentesRow = new HBox(16);
        urgentesRow.getChildren().addAll(
                createPacienteCard("Luna", "Persa - 3 años", "María González", "09:45", true, "L"),
                createPacienteCard("Toby", "Beagle - 4 años", "Luis Fernández", "10:30", true, "T")
        );
        urgentesRow.setFillHeight(false);

        // Sección Regulares
        HBox regularesHeader = new HBox(8);
        regularesHeader.setAlignment(Pos.CENTER_LEFT);
        Label regIcon = new Label("☐");
        regIcon.setStyle("-fx-text-fill: #374151; -fx-font-size: 16;");
        Label regTitle = new Label("Casos Regulares");
        regTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        regTitle.setStyle("-fx-text-fill: #374151;");
        regularesHeader.getChildren().addAll(regIcon, regTitle);

        HBox regularesRow = new HBox(16);
        regularesRow.getChildren().addAll(
                createPacienteCard("Max", "Labrador - 5 años", "Juan Pérez", "09:30", false, "M"),
                createPacienteCard("Rocky", "Pastor Alemán - 7 años", "Carlos Rodríguez", "10:00", false, "R"),
                createPacienteCard("Mimi", "Siamés - 2 años", "Ana Martínez", "10:15", false, "Mi")
        );
        regularesRow.setFillHeight(false);

        mainBody.getChildren().addAll(title, subtitle, urgentesHeader, urgentesRow, regularesHeader, regularesRow);
    }

    private VBox createPacienteCard(String nombre, String razaEdad, String dueno, String llegada, boolean urgente, String initials) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(16));
        card.setPrefWidth(280);

        if (urgente) {
            card.setStyle(
                    "-fx-background-color: white;" +
                            "-fx-border-color: #fca5a5;" +
                            "-fx-border-radius: 12;" +
                            "-fx-background-radius: 12;" +
                            "-fx-border-width: 1.5;"
            );
        } else {
            card.setStyle(
                    "-fx-background-color: white;" +
                            "-fx-border-color: #e5e7eb;" +
                            "-fx-border-radius: 12;" +
                            "-fx-background-radius: 12;" +
                            "-fx-border-width: 1.5;"
            );
        }

        // Avatar + nombre + badge urgente
        HBox topRow = new HBox(12);
        topRow.setAlignment(Pos.CENTER_LEFT);

        StackPane avatar = buildAvatar(initials, urgente);

        VBox nameBox = new VBox(2);
        Label nameLabel = new Label(nombre);
        nameLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 15));
        nameLabel.setStyle("-fx-text-fill: #111827;");
        Label razaLabel = new Label(razaEdad);
        razaLabel.setStyle("-fx-text-fill: #6b7280; -fx-font-size: 12;");
        nameBox.getChildren().addAll(nameLabel, razaLabel);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        topRow.getChildren().addAll(avatar, nameBox, spacer);

        if (urgente) {
            Label badge = new Label("⚠ URGENTE");
            badge.setStyle(
                    "-fx-background-color: #fee2e2;" +
                            "-fx-text-fill: #ef4444;" +
                            "-fx-font-size: 10;" +
                            "-fx-font-weight: bold;" +
                            "-fx-background-radius: 6;" +
                            "-fx-padding: 3 7 3 7;"
            );
            topRow.getChildren().add(badge);
        }

        // Dueño
        HBox duenoRow = new HBox(8);
        duenoRow.setAlignment(Pos.CENTER_LEFT);
        Label duenoIcon = new Label("👤");
        duenoIcon.setStyle("-fx-font-size: 12;");
        Label duenoLabel = new Label(dueno);
        duenoLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 13));
        duenoLabel.setStyle("-fx-text-fill: #374151;");
        duenoRow.getChildren().addAll(duenoIcon, duenoLabel);

        // Llegada
        HBox llegadaRow = new HBox(8);
        llegadaRow.setAlignment(Pos.CENTER_LEFT);
        Label clockIcon = new Label("🕐");
        clockIcon.setStyle("-fx-font-size: 12;");
        Label llegadaLabel = new Label("Llegada: " + llegada);
        llegadaLabel.setStyle("-fx-text-fill: #6b7280; -fx-font-size: 12;");
        llegadaRow.getChildren().addAll(clockIcon, llegadaLabel);

        // Botón seleccionar
        Button selectBtn = new Button("Seleccionar Paciente");
        selectBtn.setMaxWidth(Double.MAX_VALUE);
        selectBtn.setStyle(
                "-fx-background-color: #2563eb;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 13;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 8;" +
                        "-fx-padding: 9 0 9 0;" +
                        "-fx-cursor: hand;"
        );
        selectBtn.setOnAction(e -> {
            selectedPaciente = nombre;
            selectedEspecie = razaEdad;
            selectedDueno = dueno;
            selectedPacienteLabel.setText(nombre);
            selectedEspecieLabel.setText(razaEdad);
            selectedDuenoLabel.setText("Dueño: " + dueno);
            setActiveNav(btnRegistrarConsulta);
            showRegistrarConsultaWithData(nombre, dueno, razaEdad.split(" - ")[0]);
        });

        card.getChildren().addAll(topRow, duenoRow, llegadaRow, selectBtn);
        return card;
    }

    private StackPane buildAvatar(String initials, boolean urgente) {
        StackPane avatar = new StackPane();
        avatar.setMinSize(48, 48);
        avatar.setMaxSize(48, 48);

        // Color según urgente o no
        String bg = urgente ? "#c084fc" : "#3b82f6"; // púrpura para urgentes, azul para regulares
        avatar.setStyle("-fx-background-color: " + bg + "; -fx-background-radius: 24;");

        // Icono simple (ojo o estrella) - simulado con label
        Label icon = new Label(urgente ? "✦" : "◉");
        icon.setStyle("-fx-text-fill: white; -fx-font-size: 18;");
        avatar.getChildren().add(icon);
        return avatar;
    }

    // ─── REGISTRAR CONSULTA ────────────────────────────────────────────────────
    private void showRegistrarConsultaView() {
        showRegistrarConsultaWithData(null, null, null);
    }

    private void showRegistrarConsultaWithData(String nombrePaciente, String dueno, String especie) {
        mainBody.getChildren().clear();

        VBox formContainer = new VBox(20);
        formContainer.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 12;" +
                        "-fx-border-color: #e5e7eb;" +
                        "-fx-border-radius: 12;"
        );
        formContainer.setPadding(new Insets(28));

        Label title = new Label("Registrar Consulta");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 22));
        title.setStyle("-fx-text-fill: #111827;");

        // Grid datos paciente
        GridPane patientData = new GridPane();
        patientData.setHgap(16);
        patientData.setVgap(12);
        patientData.setPadding(new Insets(8, 0, 8, 0));

        patientData.add(styledLabel("Paciente:"), 0, 0);
        TextField pacienteField = styledTextField(nombrePaciente != null ? nombrePaciente : "", "Nombre del paciente");
        patientData.add(pacienteField, 1, 0);

        patientData.add(styledLabel("Dueño:"), 0, 1);
        TextField duenoField = styledTextField(dueno != null ? dueno : "", "Nombre del dueño");
        patientData.add(duenoField, 1, 1);

        patientData.add(styledLabel("Especie:"), 0, 2);
        TextField especieField = styledTextField(especie != null ? especie : "", "Especie / Raza");
        patientData.add(especieField, 1, 2);

        // Diagnóstico
        Label diagnosticoLabel = styledSectionLabel("Diagnóstico *");
        TextArea diagnosticoArea = new TextArea();
        diagnosticoArea.setPromptText("Describa el diagnóstico de la consulta...");
        diagnosticoArea.setPrefRowCount(3);
        diagnosticoArea.setWrapText(true);
        styleTextArea(diagnosticoArea);

        // Síntomas
        Label sintomasLabel = styledSectionLabel("Registrar Síntomas (Opcional)");
        TextArea sintomasArea = new TextArea();
        sintomasArea.setPromptText("Ej: Tos persistente, fiebre, vómitos...");
        sintomasArea.setPrefRowCount(2);
        sintomasArea.setWrapText(true);
        styleTextArea(sintomasArea);
        Button agregarSintomaBtn = outlineButton("Agregar");
        VBox sintomasBox = new VBox(8, sintomasLabel, sintomasArea, agregarSintomaBtn);

        // Receta
        Label recetaLabel = styledSectionLabel("Registrar Receta (Opcional)");
        TextField medicamentoField = styledTextField("", "Medicamento");
        TextField dosisField = styledTextField("", "Dosis (ej: 250mg)");
        TextField duracionField = styledTextField("", "Duración (ej: 7 días)");
        HBox recetaFields = new HBox(12, medicamentoField, dosisField, duracionField);
        HBox.setHgrow(medicamentoField, Priority.ALWAYS);
        HBox.setHgrow(dosisField, Priority.ALWAYS);
        HBox.setHgrow(duracionField, Priority.ALWAYS);
        Button agregarMedBtn = outlineButton("Agregar medicamento");
        VBox recetaBox = new VBox(8, recetaLabel, recetaFields, agregarMedBtn);

        // Botones acción
        HBox actionButtons = new HBox(12);
        actionButtons.setAlignment(Pos.CENTER_RIGHT);
        Button cancelarBtn = new Button("Cancelar");
        cancelarBtn.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-text-fill: #374151;" +
                        "-fx-border-color: #d1d5db;" +
                        "-fx-border-radius: 8;" +
                        "-fx-background-radius: 8;" +
                        "-fx-padding: 9 18 9 18;" +
                        "-fx-cursor: hand;"
        );
        cancelarBtn.setOnAction(e -> {
            setActiveNav(btnListaEspera);
            showListaEsperaView();
        });

        Button guardarBtn = new Button("Guardar Consulta");
        guardarBtn.setStyle(
                "-fx-background-color: #2563eb;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 8;" +
                        "-fx-padding: 9 20 9 20;" +
                        "-fx-cursor: hand;"
        );

        actionButtons.getChildren().addAll(cancelarBtn, guardarBtn);

        formContainer.getChildren().addAll(
                title, patientData,
                new Separator(),
                diagnosticoLabel, diagnosticoArea,
                new Separator(),
                sintomasBox,
                new Separator(),
                recetaBox,
                actionButtons
        );

        mainBody.getChildren().add(formContainer);
    }

    // ─── HISTORIAL ─────────────────────────────────────────────────────────────
    private void showHistorialView() {
        mainBody.getChildren().clear();

        VBox container = new VBox(16);
        container.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 12;" +
                        "-fx-border-color: #e5e7eb;" +
                        "-fx-border-radius: 12;"
        );
        container.setPadding(new Insets(28));

        Label title = new Label("Historial de Consultas");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 22));
        title.setStyle("-fx-text-fill: #111827;");

        VBox card1 = createHistorialCard("Max", "Juan Pérez", "26/4/2026", "10:30",
                "Infección respiratoria leve", "3 síntomas, 1 medicamento", "Dr. García");
        VBox card2 = createHistorialCard("Lina", "María González", "25/4/2026", "15:45",
                "Gastritis", "2 síntomas, 1 medicamento", "Dr. Martínez");

        container.getChildren().addAll(title, card1, card2);
        mainBody.getChildren().add(container);
    }

    private VBox createHistorialCard(String nombre, String dueno, String fecha, String hora,
                                     String diagnostico, String detalles, String medico) {
        VBox card = new VBox(8);
        card.setPadding(new Insets(16));
        card.setStyle(
                "-fx-background-color: #f9fafb;" +
                        "-fx-border-color: #e5e7eb;" +
                        "-fx-border-radius: 10;" +
                        "-fx-background-radius: 10;"
        );

        Label nombreLabel = new Label(nombre);
        nombreLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        nombreLabel.setStyle("-fx-text-fill: #111827;");

        Label infoLabel = new Label(dueno + " · " + fecha + " " + hora);
        infoLabel.setStyle("-fx-text-fill: #6b7280; -fx-font-size: 12;");

        Label diagLabel = new Label("Diagnóstico: " + diagnostico);
        diagLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 13));
        diagLabel.setStyle("-fx-text-fill: #374151;");

        Label detallesLabel = new Label(detalles);
        detallesLabel.setStyle("-fx-text-fill: #2563eb; -fx-font-size: 12;");

        Label medicoLabel = new Label("Atendido por: " + medico);
        medicoLabel.setStyle("-fx-text-fill: #9ca3af; -fx-font-size: 11;");

        Button verDetalleBtn = new Button("Ver detalles");
        verDetalleBtn.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-text-fill: #2563eb;" +
                        "-fx-border-color: #2563eb;" +
                        "-fx-border-radius: 7;" +
                        "-fx-background-radius: 7;" +
                        "-fx-padding: 5 14 5 14;" +
                        "-fx-cursor: hand;"
        );

        card.getChildren().addAll(nombreLabel, infoLabel, diagLabel, detallesLabel, medicoLabel, verDetalleBtn);
        return card;
    }

    // ─── HELPERS DE ESTILO ─────────────────────────────────────────────────────
    private Label styledLabel(String text) {
        Label l = new Label(text);
        l.setFont(Font.font("Segoe UI", FontWeight.BOLD, 13));
        l.setStyle("-fx-text-fill: #374151;");
        return l;
    }

    private Label styledSectionLabel(String text) {
        Label l = new Label(text);
        l.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        l.setStyle("-fx-text-fill: #111827;");
        return l;
    }

    private TextField styledTextField(String value, String prompt) {
        TextField tf = new TextField(value);
        tf.setPromptText(prompt);
        tf.setStyle(
                "-fx-background-color: white;" +
                        "-fx-border-color: #d1d5db;" +
                        "-fx-border-radius: 7;" +
                        "-fx-background-radius: 7;" +
                        "-fx-padding: 8 12 8 12;" +
                        "-fx-font-size: 13;"
        );
        return tf;
    }

    private void styleTextArea(TextArea ta) {
        ta.setStyle(
                "-fx-background-color: white;" +
                        "-fx-border-color: #d1d5db;" +
                        "-fx-border-radius: 7;" +
                        "-fx-background-radius: 7;" +
                        "-fx-font-size: 13;"
        );
    }

    private Button outlineButton(String text) {
        Button btn = new Button(text);
        btn.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-text-fill: #2563eb;" +
                        "-fx-border-color: #2563eb;" +
                        "-fx-border-radius: 7;" +
                        "-fx-background-radius: 7;" +
                        "-fx-padding: 7 16 7 16;" +
                        "-fx-cursor: hand;"
        );
        return btn;
    }
}