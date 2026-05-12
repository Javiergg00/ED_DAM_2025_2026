package com.controller;

import com.clinicaVeterinaria.AppNavigator;
import com.model.Cliente;
import com.model.Mascota;
import com.model.SessionUser;

import javafx.collections.FXCollections;
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
        root.setStyle("-fx-background-color: #f0f4f8;");
        construirVista();
    }

    public BorderPane getView() {
        return root;
    }

    private void construirVista() {
        Label titulo = new Label("🐾 Gestión de Clínica Veterinaria");
        titulo.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        titulo.setStyle("-fx-text-fill: #2c3e50;");

        Label sessionLabel = new Label(sessionUser.getUsername() + " · " + sessionUser.getRole().getDisplayName());
        sessionLabel.setStyle("-fx-text-fill: #5b6773; -fx-font-weight: bold;");

        Button logoutButton = new Button("Cerrar sesión");
        logoutButton.setStyle("-fx-background-color: #d35454; -fx-text-fill: white; -fx-font-weight: bold;");
        logoutButton.setOnAction(event -> navigator.showLogin());

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox header = new HBox(14, titulo, spacer, sessionLabel, logoutButton);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(20));
        header.setStyle("-fx-background-color: #ffffff; -fx-border-color: #dce1e7; -fx-border-width: 0 0 1 0;");

        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        Tab tabClientes = new Tab("👤 Clientes", construirPanelClientes());
        Tab tabMascotas = new Tab("🐶 Mascotas", construirPanelMascotas());

        tabPane.getTabs().addAll(tabClientes, tabMascotas);

        root.setTop(header);
        root.setCenter(tabPane);
    }

    private VBox construirPanelClientes() {
        TableView<Cliente> tabla = new TableView<>(listaClientes);
        tabla.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Cliente, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colId.setMaxWidth(50);

        TableColumn<Cliente, String> colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));

        TableColumn<Cliente, String> colApellido = new TableColumn<>("Apellido");
        colApellido.setCellValueFactory(new PropertyValueFactory<>("apellido"));

        TableColumn<Cliente, String> colTelefono = new TableColumn<>("Teléfono");
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));

        TableColumn<Cliente, String> colEmail = new TableColumn<>("Email");
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

        TableColumn<Cliente, String> colDireccion = new TableColumn<>("Dirección");
        colDireccion.setCellValueFactory(new PropertyValueFactory<>("direccion"));

        tabla.getColumns().addAll(colId, colNombre, colApellido, colTelefono, colEmail, colDireccion);

        Button btnAlta      = boton("➕ Alta cliente",  "#27ae60");
        Button btnModificar = boton("✏️ Modificar",      "#2980b9");
        Button btnEliminar  = boton("🗑️ Eliminar",       "#e74c3c");
        Button btnVer       = boton("🔍 Ver datos",      "#8e44ad");

        btnAlta.setOnAction(e -> dialogoAltaCliente());
        btnModificar.setOnAction(e -> dialogoModificarCliente(tabla.getSelectionModel().getSelectedItem()));
        btnEliminar.setOnAction(e -> eliminarCliente(tabla.getSelectionModel().getSelectedItem()));
        btnVer.setOnAction(e -> verCliente(tabla.getSelectionModel().getSelectedItem()));

        HBox botones = new HBox(10, btnAlta, btnModificar, btnEliminar, btnVer);
        botones.setPadding(new Insets(10));
        botones.setAlignment(Pos.CENTER_LEFT);

        VBox panel = new VBox(10, botones, tabla);
        panel.setPadding(new Insets(15));
        VBox.setVgrow(tabla, Priority.ALWAYS);
        return panel;
    }

    private VBox construirPanelMascotas() {
        TableView<Mascota> tabla = new TableView<>(listaMascotas);
        tabla.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Mascota, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colId.setMaxWidth(50);

        TableColumn<Mascota, String> colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));

        TableColumn<Mascota, String> colEspecie = new TableColumn<>("Especie");
        colEspecie.setCellValueFactory(new PropertyValueFactory<>("especie"));

        TableColumn<Mascota, String> colRaza = new TableColumn<>("Raza");
        colRaza.setCellValueFactory(new PropertyValueFactory<>("raza"));

        TableColumn<Mascota, Integer> colEdad = new TableColumn<>("Edad");
        colEdad.setCellValueFactory(new PropertyValueFactory<>("edad"));

        TableColumn<Mascota, Integer> colCliente = new TableColumn<>("ID Cliente");
        colCliente.setCellValueFactory(new PropertyValueFactory<>("idCliente"));

        TableColumn<Mascota, String> colVet = new TableColumn<>("Veterinario");
        colVet.setCellValueFactory(new PropertyValueFactory<>("veterinario"));

        TableColumn<Mascota, Boolean> colREIAC = new TableColumn<>("REIAC");
        colREIAC.setCellValueFactory(new PropertyValueFactory<>("enREIAC"));

        tabla.getColumns().addAll(colId, colNombre, colEspecie, colRaza, colEdad, colCliente, colVet, colREIAC);

        Button btnRegistrar   = boton("➕ Registrar mascota",    "#27ae60");
        Button btnModificar   = boton("✏️ Modificar",             "#2980b9");
        Button btnEliminar    = boton("🗑️ Eliminar",              "#e74c3c");
        Button btnConsultar   = boton("🔍 Consultar",             "#8e44ad");
        Button btnREIAC       = boton("📋 Verificar REIAC",       "#e67e22");
        Button btnVeterinario = boton("👨‍⚕️ Asignar veterinario", "#16a085");

        btnRegistrar.setOnAction(e -> dialogoRegistrarMascota());
        btnModificar.setOnAction(e -> dialogoModificarMascota(tabla.getSelectionModel().getSelectedItem()));
        btnEliminar.setOnAction(e -> eliminarMascota(tabla.getSelectionModel().getSelectedItem()));
        btnConsultar.setOnAction(e -> verMascota(tabla.getSelectionModel().getSelectedItem()));
        btnREIAC.setOnAction(e -> verificarREIAC(tabla.getSelectionModel().getSelectedItem()));
        btnVeterinario.setOnAction(e -> asignarVeterinario(tabla.getSelectionModel().getSelectedItem()));

        HBox botones = new HBox(8, btnRegistrar, btnModificar, btnEliminar, btnConsultar, btnREIAC, btnVeterinario);
        botones.setPadding(new Insets(10));
        botones.setAlignment(Pos.CENTER_LEFT);

        VBox panel = new VBox(10, botones, tabla);
        panel.setPadding(new Insets(15));
        VBox.setVgrow(tabla, Priority.ALWAYS);
        return panel;
    }

    private void dialogoAltaCliente() {
        Dialog<Cliente> dialog = new Dialog<>();
        dialog.setTitle("Alta nuevo cliente");
        dialog.setHeaderText("Introduce los datos del cliente");

        ButtonType btnGuardar = new ButtonType("Guardar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(btnGuardar, ButtonType.CANCEL);

        GridPane grid = formGrid();
        TextField fNombre    = new TextField(); fNombre.setPromptText("Nombre");
        TextField fApellido  = new TextField(); fApellido.setPromptText("Apellido");
        TextField fTelefono  = new TextField(); fTelefono.setPromptText("Teléfono");
        TextField fEmail     = new TextField(); fEmail.setPromptText("Email");
        TextField fDireccion = new TextField(); fDireccion.setPromptText("Dirección");

        grid.addRow(0, new Label("Nombre:"),    fNombre);
        grid.addRow(1, new Label("Apellido:"),  fApellido);
        grid.addRow(2, new Label("Teléfono:"),  fTelefono);
        grid.addRow(3, new Label("Email:"),     fEmail);
        grid.addRow(4, new Label("Dirección:"), fDireccion);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(btn -> {
            if (btn == btnGuardar) {
                if (fNombre.getText().isEmpty() || fApellido.getText().isEmpty()) {
                    alerta("Nombre y apellido son obligatorios.");
                    return null;
                }
                return new Cliente(nextIdCliente++, fNombre.getText(), fApellido.getText(),
                        fTelefono.getText(), fEmail.getText(), fDireccion.getText());
            }
            return null;
        });

        Optional<Cliente> result = dialog.showAndWait();
        result.ifPresent(c -> {
            listaClientes.add(c);
            info("Cliente dado de alta con ID " + c.getId());
        });
    }

    private void dialogoModificarCliente(Cliente cliente) {
        if (cliente == null) { alerta("Selecciona un cliente."); return; }

        Dialog<Boolean> dialog = new Dialog<>();
        dialog.setTitle("Modificar cliente");
        dialog.setHeaderText("Edita los datos del cliente " + cliente.getId());

        ButtonType btnGuardar = new ButtonType("Guardar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(btnGuardar, ButtonType.CANCEL);

        GridPane grid = formGrid();
        TextField fNombre    = new TextField(cliente.getNombre());
        TextField fApellido  = new TextField(cliente.getApellido());
        TextField fTelefono  = new TextField(cliente.getTelefono());
        TextField fEmail     = new TextField(cliente.getEmail());
        TextField fDireccion = new TextField(cliente.getDireccion());

        grid.addRow(0, new Label("Nombre:"),    fNombre);
        grid.addRow(1, new Label("Apellido:"),  fApellido);
        grid.addRow(2, new Label("Teléfono:"),  fTelefono);
        grid.addRow(3, new Label("Email:"),     fEmail);
        grid.addRow(4, new Label("Dirección:"), fDireccion);

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
                info("Cliente eliminado.");
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

    private void dialogoRegistrarMascota() {
        if (listaClientes.isEmpty()) { alerta("Primero da de alta un cliente."); return; }

        Dialog<Mascota> dialog = new Dialog<>();
        dialog.setTitle("Registrar mascota");
        dialog.setHeaderText("Introduce los datos de la mascota");

        ButtonType btnGuardar = new ButtonType("Guardar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(btnGuardar, ButtonType.CANCEL);

        GridPane grid = formGrid();
        TextField fNombre  = new TextField(); fNombre.setPromptText("Nombre");
        TextField fEspecie = new TextField(); fEspecie.setPromptText("Perro, Gato...");
        TextField fRaza    = new TextField(); fRaza.setPromptText("Raza");
        TextField fEdad    = new TextField(); fEdad.setPromptText("Edad");
        ComboBox<Cliente> cbCliente = new ComboBox<>(listaClientes);
        cbCliente.setPromptText("Selecciona cliente");

        grid.addRow(0, new Label("Nombre:"),  fNombre);
        grid.addRow(1, new Label("Especie:"), fEspecie);
        grid.addRow(2, new Label("Raza:"),    fRaza);
        grid.addRow(3, new Label("Edad:"),    fEdad);
        grid.addRow(4, new Label("Cliente:"), cbCliente);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(btn -> {
            if (btn == btnGuardar) {
                if (fNombre.getText().isEmpty() || cbCliente.getValue() == null) {
                    alerta("Nombre y cliente son obligatorios.");
                    return null;
                }
                int edad = 0;
                try { edad = Integer.parseInt(fEdad.getText()); } catch (Exception ignored) {}
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

        ButtonType btnGuardar = new ButtonType("Guardar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(btnGuardar, ButtonType.CANCEL);

        GridPane grid = formGrid();
        TextField fNombre  = new TextField(mascota.getNombre());
        TextField fEspecie = new TextField(mascota.getEspecie());
        TextField fRaza    = new TextField(mascota.getRaza());
        TextField fEdad    = new TextField(String.valueOf(mascota.getEdad()));

        grid.addRow(0, new Label("Nombre:"),  fNombre);
        grid.addRow(1, new Label("Especie:"), fEspecie);
        grid.addRow(2, new Label("Raza:"),    fRaza);
        grid.addRow(3, new Label("Edad:"),    fEdad);

        dialog.getDialogPane().setContent(grid);
        dialog.setResultConverter(btn -> btn == btnGuardar);

        dialog.showAndWait().ifPresent(ok -> {
            if (ok) {
                mascota.setNombre(fNombre.getText());
                mascota.setEspecie(fEspecie.getText());
                mascota.setRaza(fRaza.getText());
                try { mascota.setEdad(Integer.parseInt(fEdad.getText())); } catch (Exception ignored) {}
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
                info("Mascota eliminada.");
            }
        });
    }


    private void verMascota(Mascota mascota){
        if (mascota == null) {
            alerta("Selecciona una mascota de la tabla primero.");
            return;
        }

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            java.sql.Connection con = java.sql.DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/clinica_entornos", "root", ""
            );
            java.sql.PreparedStatement ps = con.prepareStatement(
                    "SELECT * FROM animales WHERE id_animales = ?"
            );
            ps.setInt(1, mascota.getId());
            java.sql.ResultSet rs = ps.executeQuery();
            rs.close();
            ps.close();
            con.close();
        } catch (Exception e) {
            System.out.println("Error BD: " + e.getMessage());
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Datos de la mascota");
        alert.setHeaderText(mascota.getNombre() + " (ID: " + mascota.getId() + ")");
        alert.setContentText(
                "Especie:     " + mascota.getEspecie() + "\n" +
                        "Raza:        " + mascota.getRaza() + "\n" +
                        "Edad:        " + mascota.getEdad() + " años\n" +
                        "ID Cliente:  " + mascota.getIdCliente() + "\n" +
                        "Veterinario: " + mascota.getVeterinario() + "\n" +
                        "En REIAC:    " + (mascota.isEnREIAC() ? "Sí" : "No")
        );
        alert.showAndWait();
    }

    private void verificarREIAC(Mascota mascota) {
        if (mascota == null) { alerta("Selecciona una mascota."); return; }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                mascota.getNombre() + " está actualmente " +
                (mascota.isEnREIAC() ? "registrada" : "NO registrada") + " en REIAC.\n¿Cambiar estado?",
                ButtonType.YES, ButtonType.NO);
        confirm.setTitle("Verificar REIAC");
        confirm.showAndWait().ifPresent(btn -> {
            if (btn == ButtonType.YES) {
                mascota.setEnREIAC(!mascota.isEnREIAC());
                listaMascotas.set(listaMascotas.indexOf(mascota), mascota);
                info("Estado REIAC: " + (mascota.isEnREIAC() ? "Registrada" : "No registrada"));
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
            if (!vet.isEmpty()) {
                mascota.setVeterinario(vet);
                listaMascotas.set(listaMascotas.indexOf(mascota), mascota);
                info("Veterinario asignado: " + vet);
            }
        });
    }

    private GridPane formGrid() {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));
        return grid;
    }

    private Button boton(String texto, String color) {
        Button btn = new Button(texto);
        btn.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; " +
                     "-fx-font-weight: bold; -fx-background-radius: 5;");
        return btn;
    }

    private void alerta(String mensaje) {
        new Alert(Alert.AlertType.WARNING, mensaje, ButtonType.OK).showAndWait();
    }

    private void info(String mensaje) {
        new Alert(Alert.AlertType.INFORMATION, mensaje, ButtonType.OK).showAndWait();
    }
}
