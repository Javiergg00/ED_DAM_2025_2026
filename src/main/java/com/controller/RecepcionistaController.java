package com.controller;

import com.clinicaVeterinaria.AppNavigator;
import com.model.Cliente;
import com.model.Mascota;
import com.model.SessionUser;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.Optional;

public class RecepcionistaController {

    // ─────────────────────────────────────────────────────────────
    //  PALETA DE COLORES  (cambia aquí para rediseñar toda la UI)
    // ─────────────────────────────────────────────────────────────
    private static final String COLOR_FONDO          = "#F4F7FB";   // fondo general
    private static final String COLOR_HEADER_BG      = "#1B2A4A";   // azul marino oscuro
    private static final String COLOR_HEADER_BORDE   = "#0F1C33";
    private static final String COLOR_TITULO         = "#FFFFFF";
    private static final String COLOR_SESION         = "#A8BFDA";   // gris-azulado suave
    private static final String COLOR_TAB_SELECTED   = "#1B2A4A";
    private static final String COLOR_TAB_TEXT       = "#FFFFFF";

    private static final String COLOR_BTN_ALTA       = "#1A7F5A";   // verde clínico
    private static final String COLOR_BTN_MODIFICAR  = "#1A5FA8";   // azul medio
    private static final String COLOR_BTN_ELIMINAR   = "#C0392B";   // rojo apagado
    private static final String COLOR_BTN_VER        = "#5B3FA6";   // violeta sobrio
    private static final String COLOR_BTN_REIAC      = "#C47A1E";   // naranja tostado
    private static final String COLOR_BTN_VET        = "#0D7377";   // teal profundo
    private static final String COLOR_BTN_LOGOUT     = "#C0392B";

    private static final String COLOR_TABLA_HEADER   = "#1B2A4A";   // cabecera tabla
    private static final String COLOR_TABLA_FILA_PAR = "#EBF0F8";   // filas alternas
    private static final String COLOR_TABLA_SELEC    = "#BDD5F0";   // fila seleccionada

    private static final String FONT_PRINCIPAL       = "Segoe UI";
    // ─────────────────────────────────────────────────────────────

    private final Stage stage;
    private final AppNavigator navigator;
    private final SessionUser sessionUser;
    private final BorderPane root;

    private ObservableList<Cliente> listaClientes = FXCollections.observableArrayList();
    private ObservableList<Mascota> listaMascotas = FXCollections.observableArrayList();
    private int nextIdCliente = 1;
    private int nextIdMascota = 1;

    public RecepcionistaController(Stage stage, AppNavigator navigator, SessionUser sessionUser) {
        this.stage = stage;
        this.navigator = navigator;
        this.sessionUser = sessionUser;
        root = new BorderPane();
        root.setStyle("-fx-background-color: " + COLOR_FONDO + ";");
        construirVista();
    }

    public BorderPane getView() {
        return root;
    }

    // ══════════════════════════════════════════════════════════════
    //  VISTA PRINCIPAL
    // ══════════════════════════════════════════════════════════════
    private void construirVista() {
        // ── Header ────────────────────────────────────────────────
        Label titulo = new Label("🐾  Clínica Veterinaria");
        titulo.setFont(Font.font(FONT_PRINCIPAL, FontWeight.BOLD, 22));
        titulo.setStyle("-fx-text-fill: " + COLOR_TITULO + ";");

        Label sessionLabel = new Label("👤  " + sessionUser.getUsername()
                + "  ·  " + sessionUser.getRole().getDisplayName());
        sessionLabel.setStyle(
                "-fx-text-fill: " + COLOR_SESION + ";" +
                        "-fx-font-size: 13px;" +
                        "-fx-font-family: '" + FONT_PRINCIPAL + "';"
        );

        Button logoutButton = boton("⏻  Cerrar sesión", COLOR_BTN_LOGOUT);
        logoutButton.setOnAction(e -> navigator.showLogin());

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox header = new HBox(16, titulo, spacer, sessionLabel, logoutButton);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(18, 24, 18, 24));
        header.setStyle(
                "-fx-background-color: " + COLOR_HEADER_BG + ";" +
                        "-fx-border-color: " + COLOR_HEADER_BORDE + ";" +
                        "-fx-border-width: 0 0 2 0;"
        );

        // ── TabPane ───────────────────────────────────────────────
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabPane.setStyle(
                "-fx-tab-min-width: 140px;" +
                        "-fx-font-family: '" + FONT_PRINCIPAL + "';" +
                        "-fx-font-size: 13px;"
        );

        Tab tabClientes = new Tab("👤   Clientes",  construirPanelClientes());
        Tab tabMascotas = new Tab("🐶   Mascotas",  construirPanelMascotas());

        tabPane.getTabs().addAll(tabClientes, tabMascotas);

        root.setTop(header);
        root.setCenter(tabPane);
    }

    // ══════════════════════════════════════════════════════════════
    //  PANEL CLIENTES
    // ══════════════════════════════════════════════════════════════
    private VBox construirPanelClientes() {
        TableView<Cliente> tabla = crearTablaBase();
        tabla.setItems(listaClientes);

        TableColumn<Cliente, Integer> colId       = columna("ID",        "id");
        TableColumn<Cliente, String>  colNombre   = columna("Nombre",    "nombre");
        TableColumn<Cliente, String>  colApellido = columna("Apellido",  "apellido");
        TableColumn<Cliente, String>  colTelefono = columna("Teléfono",  "telefono");
        TableColumn<Cliente, String>  colEmail    = columna("Email",     "email");
        TableColumn<Cliente, String>  colDir      = columna("Dirección", "direccion");

        colId.setMaxWidth(55);
        tabla.getColumns().addAll(colId, colNombre, colApellido, colTelefono, colEmail, colDir);
        aplicarEstiloTabla(tabla);

        Button btnAlta      = boton("➕  Alta cliente",  COLOR_BTN_ALTA);
        Button btnModificar = boton("✏️  Modificar",      COLOR_BTN_MODIFICAR);
        Button btnEliminar  = boton("🗑️  Eliminar",       COLOR_BTN_ELIMINAR);
        Button btnVer       = boton("🔍  Ver datos",      COLOR_BTN_VER);

        btnAlta.setOnAction(e -> dialogoAltaCliente());
        btnModificar.setOnAction(e -> dialogoModificarCliente(tabla.getSelectionModel().getSelectedItem()));
        btnEliminar.setOnAction(e -> eliminarCliente(tabla.getSelectionModel().getSelectedItem()));
        btnVer.setOnAction(e -> verCliente(tabla.getSelectionModel().getSelectedItem()));

        HBox botones = barraAcciones(btnAlta, btnModificar, btnEliminar, btnVer);

        VBox panel = new VBox(0, botones, tabla);
        panel.setPadding(new Insets(18));
        VBox.setVgrow(tabla, Priority.ALWAYS);
        return panel;
    }

    // ══════════════════════════════════════════════════════════════
    //  PANEL MASCOTAS
    // ══════════════════════════════════════════════════════════════
    private VBox construirPanelMascotas() {
        TableView<Mascota> tabla = crearTablaBase();
        tabla.setItems(listaMascotas);

        TableColumn<Mascota, Integer> colId       = columna("ID",          "id");
        TableColumn<Mascota, String>  colNombre   = columna("Nombre",      "nombre");
        TableColumn<Mascota, String>  colEspecie  = columna("Especie",     "especie");
        TableColumn<Mascota, String>  colRaza     = columna("Raza",        "raza");
        TableColumn<Mascota, Integer> colEdad     = columna("Edad",        "edad");
        TableColumn<Mascota, Integer> colCliente  = columna("ID Cliente",  "idCliente");
        TableColumn<Mascota, String>  colVet      = columna("Veterinario", "veterinario");
        TableColumn<Mascota, Boolean> colREIAC    = columna("REIAC",       "enREIAC");

        colId.setMaxWidth(55);
        tabla.getColumns().addAll(colId, colNombre, colEspecie, colRaza, colEdad, colCliente, colVet, colREIAC);
        aplicarEstiloTabla(tabla);

        Button btnRegistrar   = boton("➕  Registrar",       COLOR_BTN_ALTA);
        Button btnModificar   = boton("✏️  Modificar",        COLOR_BTN_MODIFICAR);
        Button btnEliminar    = boton("🗑️  Eliminar",         COLOR_BTN_ELIMINAR);
        Button btnConsultar   = boton("🔍  Consultar",        COLOR_BTN_VER);
        Button btnREIAC       = boton("📋  REIAC",            COLOR_BTN_REIAC);
        Button btnVeterinario = boton("👨‍⚕️  Veterinario",  COLOR_BTN_VET);

        btnRegistrar.setOnAction(e -> dialogoRegistrarMascota());
        btnModificar.setOnAction(e -> dialogoModificarMascota(tabla.getSelectionModel().getSelectedItem()));
        btnEliminar.setOnAction(e -> eliminarMascota(tabla.getSelectionModel().getSelectedItem()));
        btnConsultar.setOnAction(e -> verMascota(tabla.getSelectionModel().getSelectedItem()));
        btnREIAC.setOnAction(e -> verificarREIAC(tabla.getSelectionModel().getSelectedItem()));
        btnVeterinario.setOnAction(e -> asignarVeterinario(tabla.getSelectionModel().getSelectedItem()));

        HBox botones = barraAcciones(btnRegistrar, btnModificar, btnEliminar, btnConsultar, btnREIAC, btnVeterinario);

        VBox panel = new VBox(0, botones, tabla);
        panel.setPadding(new Insets(18));
        VBox.setVgrow(tabla, Priority.ALWAYS);
        return panel;
    }

    // ══════════════════════════════════════════════════════════════
    //  DIÁLOGOS — CLIENTES
    // ══════════════════════════════════════════════════════════════
    private void dialogoAltaCliente() {
        Dialog<Cliente> dialog = new Dialog<>();
        dialog.setTitle("Alta nuevo cliente");
        dialog.setHeaderText("Introduce los datos del cliente");
        estilizarDialogo(dialog.getDialogPane());

        ButtonType btnGuardar = new ButtonType("Guardar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(btnGuardar, ButtonType.CANCEL);

        GridPane grid = formGrid();
        TextField fNombre    = campo("Nombre");
        TextField fApellido  = campo("Apellido");
        TextField fTelefono  = campo("Teléfono");
        TextField fEmail     = campo("Email");
        TextField fDireccion = campo("Dirección");

        grid.addRow(0, etiqueta("Nombre:"),    fNombre);
        grid.addRow(1, etiqueta("Apellido:"),  fApellido);
        grid.addRow(2, etiqueta("Teléfono:"),  fTelefono);
        grid.addRow(3, etiqueta("Email:"),     fEmail);
        grid.addRow(4, etiqueta("Dirección:"), fDireccion);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(btn -> {
            if (btn == btnGuardar) {
                if (fNombre.getText().isBlank() || fApellido.getText().isBlank()) {
                    alerta("Nombre y apellido son obligatorios.");
                    return null;
                }
                return new Cliente(nextIdCliente++, fNombre.getText(), fApellido.getText(),
                        fTelefono.getText(), fEmail.getText(), fDireccion.getText());
            }
            return null;
        });

        dialog.showAndWait().ifPresent(c -> {
            listaClientes.add(c);
            info("Cliente dado de alta con ID " + c.getId());
        });
    }

    private void dialogoModificarCliente(Cliente cliente) {
        if (cliente == null) { alerta("Selecciona un cliente."); return; }

        Dialog<Boolean> dialog = new Dialog<>();
        dialog.setTitle("Modificar cliente");
        dialog.setHeaderText("Edita los datos del cliente ID " + cliente.getId());
        estilizarDialogo(dialog.getDialogPane());

        ButtonType btnGuardar = new ButtonType("Guardar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(btnGuardar, ButtonType.CANCEL);

        GridPane grid = formGrid();
        TextField fNombre    = new TextField(cliente.getNombre());
        TextField fApellido  = new TextField(cliente.getApellido());
        TextField fTelefono  = new TextField(cliente.getTelefono());
        TextField fEmail     = new TextField(cliente.getEmail());
        TextField fDireccion = new TextField(cliente.getDireccion());

        aplicarEstiloCampo(fNombre, fApellido, fTelefono, fEmail, fDireccion);

        grid.addRow(0, etiqueta("Nombre:"),    fNombre);
        grid.addRow(1, etiqueta("Apellido:"),  fApellido);
        grid.addRow(2, etiqueta("Teléfono:"),  fTelefono);
        grid.addRow(3, etiqueta("Email:"),     fEmail);
        grid.addRow(4, etiqueta("Dirección:"), fDireccion);

        dialog.getDialogPane().setContent(grid);
        dialog.setResultConverter(btn -> btn == btnGuardar);

        dialog.showAndWait().ifPresent(ok -> {
            if (ok) {
                cliente.setNombre(fNombre.getText());
                cliente.setApellido(fApellido.getText());
                cliente.setTelefono(fTelefono.getText());
                cliente.setEmail(fEmail.getText());
                cliente.setDireccion(fDireccion.getText());
                listaClientes.set(listaClientes.indexOf(cliente), cliente);
                info("Cliente modificado correctamente.");
            }
        });
    }

    private void eliminarCliente(Cliente cliente) {
        if (cliente == null) { alerta("Selecciona un cliente."); return; }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "¿Eliminar al cliente " + cliente.getNombre() + " " + cliente.getApellido() + "?",
                ButtonType.OK, ButtonType.CANCEL);
        confirm.setTitle("Confirmar eliminación");
        confirm.showAndWait().ifPresent(btn -> {
            if (btn == ButtonType.OK) {
                listaClientes.remove(cliente);
                info("Cliente eliminado correctamente.");
            }
        });
    }

    private void verCliente(Cliente cliente) {
        if (cliente == null) { alerta("Selecciona un cliente."); return; }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Datos del cliente");
        alert.setHeaderText("Cliente ID: " + cliente.getId());
        alert.setContentText(
                "Nombre:    " + cliente.getNombre() + " " + cliente.getApellido() + "\n" +
                        "Teléfono:  " + cliente.getTelefono() + "\n" +
                        "Email:     " + cliente.getEmail() + "\n" +
                        "Dirección: " + cliente.getDireccion()
        );
        alert.showAndWait();
    }

    // ══════════════════════════════════════════════════════════════
    //  DIÁLOGOS — MASCOTAS
    // ══════════════════════════════════════════════════════════════
    private void dialogoRegistrarMascota() {
        if (listaClientes.isEmpty()) { alerta("Primero da de alta un cliente."); return; }

        Dialog<Mascota> dialog = new Dialog<>();
        dialog.setTitle("Registrar mascota");
        dialog.setHeaderText("Introduce los datos de la mascota");
        estilizarDialogo(dialog.getDialogPane());

        ButtonType btnGuardar = new ButtonType("Guardar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(btnGuardar, ButtonType.CANCEL);

        GridPane grid = formGrid();
        TextField fNombre  = campo("Nombre");
        TextField fEspecie = campo("Perro, Gato…");
        TextField fRaza    = campo("Raza");
        TextField fEdad    = campo("Edad (años)");
        ComboBox<Cliente> cbCliente = new ComboBox<>(listaClientes);
        cbCliente.setPromptText("Selecciona cliente");
        cbCliente.setStyle(
                "-fx-background-color: white;" +
                        "-fx-border-color: #B0BEC5;" +
                        "-fx-border-radius: 4;" +
                        "-fx-font-family: '" + FONT_PRINCIPAL + "';"
        );

        grid.addRow(0, etiqueta("Nombre:"),   fNombre);
        grid.addRow(1, etiqueta("Especie:"),  fEspecie);
        grid.addRow(2, etiqueta("Raza:"),     fRaza);
        grid.addRow(3, etiqueta("Edad:"),     fEdad);
        grid.addRow(4, etiqueta("Cliente:"),  cbCliente);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(btn -> {
            if (btn == btnGuardar) {
                if (fNombre.getText().isBlank() || cbCliente.getValue() == null) {
                    alerta("Nombre y cliente son obligatorios.");
                    return null;
                }
                int edad = 0;
                try { edad = Integer.parseInt(fEdad.getText().trim()); } catch (Exception ignored) {}
                return new Mascota(nextIdMascota++, fNombre.getText(), fEspecie.getText(),
                        fRaza.getText(), edad, cbCliente.getValue().getId());
            }
            return null;
        });

        dialog.showAndWait().ifPresent(m -> {
            listaMascotas.add(m);
            info("Mascota registrada con ID " + m.getId());
        });
    }

    private void dialogoModificarMascota(Mascota mascota) {
        if (mascota == null) { alerta("Selecciona una mascota."); return; }

        Dialog<Boolean> dialog = new Dialog<>();
        dialog.setTitle("Modificar mascota");
        dialog.setHeaderText("Edita los datos de " + mascota.getNombre());
        estilizarDialogo(dialog.getDialogPane());

        ButtonType btnGuardar = new ButtonType("Guardar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(btnGuardar, ButtonType.CANCEL);

        GridPane grid = formGrid();
        TextField fNombre  = new TextField(mascota.getNombre());
        TextField fEspecie = new TextField(mascota.getEspecie());
        TextField fRaza    = new TextField(mascota.getRaza());
        TextField fEdad    = new TextField(String.valueOf(mascota.getEdad()));

        aplicarEstiloCampo(fNombre, fEspecie, fRaza, fEdad);

        grid.addRow(0, etiqueta("Nombre:"),  fNombre);
        grid.addRow(1, etiqueta("Especie:"), fEspecie);
        grid.addRow(2, etiqueta("Raza:"),    fRaza);
        grid.addRow(3, etiqueta("Edad:"),    fEdad);

        dialog.getDialogPane().setContent(grid);
        dialog.setResultConverter(btn -> btn == btnGuardar);

        dialog.showAndWait().ifPresent(ok -> {
            if (ok) {
                mascota.setNombre(fNombre.getText());
                mascota.setEspecie(fEspecie.getText());
                mascota.setRaza(fRaza.getText());
                try { mascota.setEdad(Integer.parseInt(fEdad.getText().trim())); } catch (Exception ignored) {}
                listaMascotas.set(listaMascotas.indexOf(mascota), mascota);
                info("Mascota modificada correctamente.");
            }
        });
    }

    private void eliminarMascota(Mascota mascota) {
        if (mascota == null) { alerta("Selecciona una mascota."); return; }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "¿Eliminar a " + mascota.getNombre() + "?",
                ButtonType.OK, ButtonType.CANCEL);
        confirm.setTitle("Confirmar eliminación");
        confirm.showAndWait().ifPresent(btn -> {
            if (btn == ButtonType.OK) {
                listaMascotas.remove(mascota);
                info("Mascota eliminada correctamente.");
            }
        });
    }

    private void verMascota(Mascota mascota) {
        if (mascota == null) { alerta("Selecciona una mascota."); return; }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Datos de la mascota");
        alert.setHeaderText(mascota.getNombre() + "  (ID: " + mascota.getId() + ")");
        alert.setContentText(
                "Especie:     " + mascota.getEspecie()    + "\n" +
                        "Raza:        " + mascota.getRaza()       + "\n" +
                        "Edad:        " + mascota.getEdad()       + " años\n" +
                        "ID Cliente:  " + mascota.getIdCliente()  + "\n" +
                        "Veterinario: " + mascota.getVeterinario()+ "\n" +
                        "En REIAC:    " + (mascota.isEnREIAC() ? "Sí ✔" : "No ✘")
        );
        alert.showAndWait();
    }

    private void verificarREIAC(Mascota mascota) {
        if (mascota == null) { alerta("Selecciona una mascota."); return; }

        String estado = mascota.isEnREIAC() ? "registrada ✔" : "NO registrada ✘";
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                mascota.getNombre() + " está actualmente " + estado + " en REIAC.\n¿Cambiar estado?",
                ButtonType.YES, ButtonType.NO);
        confirm.setTitle("Verificar REIAC");
        confirm.showAndWait().ifPresent(btn -> {
            if (btn == ButtonType.YES) {
                mascota.setEnREIAC(!mascota.isEnREIAC());
                listaMascotas.set(listaMascotas.indexOf(mascota), mascota);
                info("Estado REIAC actualizado: " + (mascota.isEnREIAC() ? "Registrada ✔" : "No registrada ✘"));
            }
        });
    }

    private void asignarVeterinario(Mascota mascota) {
        if (mascota == null) { alerta("Selecciona una mascota."); return; }

        TextInputDialog dialog = new TextInputDialog(mascota.getVeterinario());
        dialog.setTitle("Asignar veterinario");
        dialog.setHeaderText("Mascota: " + mascota.getNombre());
        dialog.setContentText("Nombre del veterinario:");

        dialog.showAndWait().ifPresent(vet -> {
            if (!vet.isBlank()) {
                mascota.setVeterinario(vet);
                listaMascotas.set(listaMascotas.indexOf(mascota), mascota);
                info("Veterinario asignado: " + vet);
            }
        });
    }

    // ══════════════════════════════════════════════════════════════
    //  HELPERS DE UI
    // ══════════════════════════════════════════════════════════════

    /** Crea un TableView con estilos base aplicados. */
    @SuppressWarnings("unchecked")
    private <T> TableView<T> crearTablaBase() {
        TableView<T> tabla = new TableView<>();
        tabla.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tabla.setStyle(
                "-fx-font-family: '" + FONT_PRINCIPAL + "';" +
                        "-fx-font-size: 13px;" +
                        "-fx-background-color: white;" +
                        "-fx-border-color: #CFD8DC;" +
                        "-fx-border-radius: 6;" +
                        "-fx-background-radius: 6;"
        );
        return tabla;
    }

    /** Aplica estilos de filas alternas y cabecera de tabla vía CSS en línea. */
    private <T> void aplicarEstiloTabla(TableView<T> tabla) {
        // Cabecera — requiere hoja de estilos CSS externa para colorear .column-header-background
        // Con JavaFX inline-style podemos mejorar el placeholder:
        tabla.setPlaceholder(new Label("  Sin registros  "));
        tabla.setStyle(tabla.getStyle() +
                "-fx-selection-bar: " + COLOR_TABLA_SELEC + ";" +
                "-fx-selection-bar-non-focused: " + COLOR_TABLA_SELEC + ";"
        );

        // Filas alternas con color
        tabla.setRowFactory(tv -> new TableRow<>() {
            @Override
            protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setStyle("");
                } else if (isSelected()) {
                    setStyle("-fx-background-color: " + COLOR_TABLA_SELEC + ";");
                } else {
                    setStyle(getIndex() % 2 == 0
                            ? "-fx-background-color: white;"
                            : "-fx-background-color: " + COLOR_TABLA_FILA_PAR + ";");
                }
            }
        });
    }

    /** Crea una columna tipada con PropertyValueFactory. */
    @SuppressWarnings("unchecked")
    private <S, T> TableColumn<S, T> columna(String titulo, String propiedad) {
        TableColumn<S, T> col = new TableColumn<>(titulo);
        col.setCellValueFactory(new PropertyValueFactory<>(propiedad));
        col.setStyle("-fx-alignment: CENTER-LEFT; -fx-font-family: '" + FONT_PRINCIPAL + "';");
        return col;
    }

    /** Barra de botones con separador visual. */
    private HBox barraAcciones(Button... botones) {
        HBox barra = new HBox(8, botones);
        barra.setPadding(new Insets(12, 0, 12, 0));
        barra.setAlignment(Pos.CENTER_LEFT);
        barra.setStyle(
                "-fx-background-color: " + COLOR_FONDO + ";" +
                        "-fx-border-color: transparent transparent #CFD8DC transparent;" +
                        "-fx-border-width: 0 0 1 0;"
        );
        return barra;
    }

    /** Botón estilizado con hover effect. */
    private Button boton(String texto, String color) {
        Button btn = new Button(texto);
        String base =
                "-fx-background-color: " + color + ";" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-font-family: '" + FONT_PRINCIPAL + "';" +
                        "-fx-font-size: 12px;" +
                        "-fx-background-radius: 5;" +
                        "-fx-padding: 7 14 7 14;" +
                        "-fx-cursor: hand;";
        btn.setStyle(base);

        // Hover: oscurecer ligeramente
        btn.setOnMouseEntered(e -> btn.setStyle(base + "-fx-opacity: 0.85;"));
        btn.setOnMouseExited(e  -> btn.setStyle(base));
        return btn;
    }

    /** TextField con estilo uniforme + prompt. */
    private TextField campo(String prompt) {
        TextField tf = new TextField();
        tf.setPromptText(prompt);
        aplicarEstiloCampo(tf);
        return tf;
    }

    /** Aplica estilo de campo a uno o varios TextFields. */
    private void aplicarEstiloCampo(TextField... campos) {
        String estilo =
                "-fx-background-color: white;" +
                        "-fx-border-color: #B0BEC5;" +
                        "-fx-border-radius: 4;" +
                        "-fx-background-radius: 4;" +
                        "-fx-padding: 5 8 5 8;" +
                        "-fx-font-family: '" + FONT_PRINCIPAL + "';" +
                        "-fx-font-size: 13px;";
        for (TextField tf : campos) tf.setStyle(estilo);
    }

    /** Label de etiqueta para formularios. */
    private Label etiqueta(String texto) {
        Label lbl = new Label(texto);
        lbl.setStyle(
                "-fx-font-family: '" + FONT_PRINCIPAL + "';" +
                        "-fx-font-size: 13px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #37474F;"
        );
        return lbl;
    }

    /** Estilo base de diálogos. */
    private void estilizarDialogo(DialogPane pane) {
        pane.setStyle(
                "-fx-background-color: " + COLOR_FONDO + ";" +
                        "-fx-font-family: '" + FONT_PRINCIPAL + "';"
        );
    }

    /** GridPane para formularios. */
    private GridPane formGrid() {
        GridPane grid = new GridPane();
        grid.setHgap(14);
        grid.setVgap(12);
        grid.setPadding(new Insets(20, 24, 20, 24));
        ColumnConstraints col1 = new ColumnConstraints(100);
        ColumnConstraints col2 = new ColumnConstraints(260);
        grid.getColumnConstraints().addAll(col1, col2);
        return grid;
    }

    /** Alerta de advertencia. */
    private void alerta(String mensaje) {
        new Alert(Alert.AlertType.WARNING, mensaje, ButtonType.OK).showAndWait();
    }

    /** Alerta informativa. */
    private void info(String mensaje) {
        new Alert(Alert.AlertType.INFORMATION, mensaje, ButtonType.OK).showAndWait();
    }
}