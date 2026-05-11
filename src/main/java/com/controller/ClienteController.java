package com.controller;

import com.clinicaVeterinaria.AppNavigator;
import com.model.Cliente;
import com.model.Mascota;
import com.model.SessionUser;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ClienteController {

    private final AppNavigator navigator;
    private final SessionUser sessionUser;
    private final BorderPane root;

    // ── Datos de ejemplo — sustituir por consulta a BD cuando esté lista ──
    // SELECT * FROM mascotas WHERE idCliente = ID_CLIENTE_SESION
    private final List<Mascota> todasLasMascotas = new ArrayList<>(List.of(
            new Mascota(1, "Max", "Perro", "Golden Retriever", 5, 1),
            new Mascota(2, "Luna", "Gato", "Siamés", 3, 1),
            new Mascota(3, "Rocky", "Perro", "Bulldog", 7, 1)
    ));

    // ID del cliente logueado — simulado a 1 hasta conectar con BD
    private static final int ID_CLIENTE_SESION = 1;

    // Datos del cliente logueado — sustituir por SELECT WHERE id = ID_CLIENTE_SESION
    private final Cliente clienteActual = new Cliente(
            ID_CLIENTE_SESION,
            "Juan", "Pérez",
            "612345678",
            "juan.perez@email.com",
            "Calle Mayor 12, Madrid"
    );

    // Paneles de cada sección
    private VBox panelMascotas;
    private VBox panelCitas;
    private VBox panelDatos;

    // Botones laterales
    private Button btnMascotas;
    private Button btnCitas;
    private Button btnDatos;
    
    public ClienteController(AppNavigator navigator, SessionUser sessionUser) {
        this.navigator   = navigator;
        this.sessionUser = sessionUser;
        this.root        = new BorderPane();
        buildView();
        mostrarSeccion("mascotas");
    }

    public Parent getView() {
        return root;
    }

    private void buildView() {
        root.setTop(buildHeader());
        root.setLeft(buildSidebar());
        root.setCenter(buildContent());
        root.setStyle("-fx-background-color: #fbfaf5;");
    }

    private HBox buildHeader() {
        Label title = new Label("Portal del Cliente");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        title.setStyle("-fx-text-fill: #1a3a5c;");

        Label user = new Label(sessionUser.getUsername() + "  ·  " + sessionUser.getRole().getDisplayName());
        user.setStyle("-fx-text-fill: #555; -fx-font-size: 13;");

        Button logout = new Button("Cerrar sesión");
        logout.setStyle(
                "-fx-background-color: #e8f0fe; -fx-text-fill: #1a3a5c;" +
                        "-fx-background-radius: 6; -fx-border-radius: 6; -fx-cursor: hand;"
        );
        logout.setOnAction(e -> navigator.showLogin());

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox header = new HBox(16, title, spacer, user, logout);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(16, 24, 16, 24));
        header.setStyle(
                "-fx-background-color: white;" +
                        "-fx-border-color: #e7dfcb; -fx-border-width: 0 0 1 0;"
        );
        return header;
    }
    private VBox buildPanelCitas() {
        Label titulo = sectionTitle("📅 Mis Citas");

        VBox lista = new VBox(12);
        lista.getChildren().addAll(
                buildCardCita("Consulta General",       "2026-04-18", "completada", "Dr. García",    "50,00 €", "pagado"),
                buildCardCita("Análisis Laboratorio",   "2026-04-19", "en_proceso", "Dr. Rodríguez", "85,00 €", "pendiente"),
                buildCardCita("Consulta Especializada", "2026-04-20", "pendiente",  "Dr. Martínez",  "120,00 €","pendiente")
        );

        Button btnNueva = new Button("+ Pedir nueva cita");
        btnNueva.setStyle(
                "-fx-background-color: #1a3a5c; -fx-text-fill: white;" +
                        "-fx-background-radius: 8; -fx-padding: 10 20; -fx-cursor: hand;"
        );
        btnNueva.setOnAction(e ->
                // TODO: abrir formulario de solicitud de cita (INSERT en BD)
                new Alert(Alert.AlertType.INFORMATION,
                        "Solicitud de cita próximamente disponible.",
                        ButtonType.OK).showAndWait()
        );

        ScrollPane scroll = scrollTransparente(lista);
        VBox.setVgrow(scroll, Priority.ALWAYS);

        VBox panel = new VBox(16, titulo, btnNueva, scroll);
        panel.setVisible(false);
        panel.setManaged(false);
        return panel;
    }

    private VBox buildCardCita(String titulo, String fecha, String estado,
                               String profesional, String monto, String pago) {
        Label tituloLbl = bold(titulo, 14);
        Label fechaLbl  = small("Fecha: " + fecha + "  ·  " + profesional);
        Label montoLbl  = small("Importe: " + monto);

        Label badgeEstado = badge(
                capitalize(estado.replace("_", " ")),
                switch (estado) {
                    case "completada" -> "#d4edda;#155724";
                    case "en_proceso" -> "#fff3cd;#856404";
                    case "pendiente"  -> "#cce5ff;#004085";
                    case "cancelada"  -> "#e2e3e5;#383d41";
                    default           -> "#e2e3e5;#383d41";
                }
        );
        Label badgePago = badge(
                pago.equals("pagado") ? "Pagado" : "Pago pendiente",
                pago.equals("pagado") ? "#d4edda;#155724" : "#fff3cd;#856404"
        );

        HBox cabecera = new HBox(10, tituloLbl, badgeEstado, badgePago);
        cabecera.setAlignment(Pos.CENTER_LEFT);

        VBox card = new VBox(5, cabecera, fechaLbl, montoLbl);
        card.setPadding(new Insets(14));
        card.setStyle(cardStyle());
        return card;
    }

    private VBox buildPanelDatos() {
        Label titulo = sectionTitle("👤 Mis Datos Personales");

        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(12);
        grid.setPadding(new Insets(20));
        grid.setStyle(cardStyle());

        addFilaDatos(grid, "Nombre:",    clienteActual.getNombre() + " " + clienteActual.getApellido(), 0);
        addFilaDatos(grid, "Email:",     clienteActual.getEmail(),     1);
        addFilaDatos(grid, "Teléfono:",  clienteActual.getTelefono(),  2);
        addFilaDatos(grid, "Dirección:", clienteActual.getDireccion(), 3);

        Button btnEditar = new Button("✏️  Editar datos");
        btnEditar.setStyle(
                "-fx-background-color: #1a3a5c; -fx-text-fill: white;" +
                        "-fx-background-radius: 8; -fx-padding: 10 20; -fx-cursor: hand;"
        );
        btnEditar.setOnAction(e -> abrirDialogoEdicion());

        VBox panel = new VBox(20, titulo, grid, btnEditar);
        panel.setVisible(false);
        panel.setManaged(false);
        return panel;
    }

    private void addFilaDatos(GridPane grid, String etiqueta, String valor, int fila) {
        Label lbl = new Label(etiqueta);
        lbl.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        lbl.setStyle("-fx-text-fill: #555;");

        Label val = new Label(valor != null ? valor : "—");
        val.setStyle("-fx-text-fill: #1a3a5c; -fx-font-size: 13;");

        grid.add(lbl, 0, fila);
        grid.add(val, 1, fila);
    }

    private void abrirDialogoEdicion() {
        // TODO: conectar con UPDATE usuarios SET ... WHERE id = ID_CLIENTE_SESION
        TextField fNombre    = new TextField(clienteActual.getNombre());
        TextField fApellido  = new TextField(clienteActual.getApellido());
        TextField fTelefono  = new TextField(clienteActual.getTelefono());
        TextField fEmail     = new TextField(clienteActual.getEmail());
        TextField fDireccion = new TextField(clienteActual.getDireccion());

        GridPane form = new GridPane();
        form.setHgap(12); form.setVgap(10); form.setPadding(new Insets(16));
        addFilaForm(form, "Nombre:",    fNombre,    0);
        addFilaForm(form, "Apellido:",  fApellido,  1);
        addFilaForm(form, "Teléfono:",  fTelefono,  2);
        addFilaForm(form, "Email:",     fEmail,     3);
        addFilaForm(form, "Dirección:", fDireccion, 4);

        Dialog<ButtonType> dlg = new Dialog<>();
        dlg.setTitle("Editar datos personales");
        dlg.setHeaderText("Modifica tus datos");
        dlg.getDialogPane().setContent(form);
        dlg.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dlg.showAndWait().ifPresent(bt -> {
            if (bt == ButtonType.OK) {
                // Actualizar objeto en memoria (y BD cuando esté conectada)
                clienteActual.setNombre(fNombre.getText().trim());
                clienteActual.setApellido(fApellido.getText().trim());
                clienteActual.setTelefono(fTelefono.getText().trim());
                clienteActual.setEmail(fEmail.getText().trim());
                clienteActual.setDireccion(fDireccion.getText().trim());

                // Refrescar vista de datos
                VBox nuevoPanelDatos = buildPanelDatos();
                nuevoPanelDatos.setVisible(true);
                nuevoPanelDatos.setManaged(true);
                panelDatos.getChildren().setAll(nuevoPanelDatos.getChildren());
            }
        });
    }

    private void addFilaForm(GridPane grid, String label, TextField field, int fila) {
        grid.add(new Label(label), 0, fila);
        grid.add(field, 1, fila);
        GridPane.setHgrow(field, Priority.ALWAYS);
    }

    private Label sectionTitle(String texto) {
        Label l = new Label(texto);
        l.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        l.setStyle("-fx-text-fill: #1a3a5c;");
        return l;
    }

    private Label bold(String texto, int size) {
        Label l = new Label(texto);
        l.setFont(Font.font("Arial", FontWeight.BOLD, size));
        l.setStyle("-fx-text-fill: #1a3a5c;");
        return l;
    }

    private Label small(String texto) {
        Label l = new Label(texto);
        l.setFont(Font.font("Arial", 12));
        l.setStyle("-fx-text-fill: #666;");
        l.setWrapText(true);
        return l;
    }

    private Label infoLabel(String texto) {
        Label l = new Label(texto);
        l.setStyle("-fx-text-fill: #999; -fx-font-style: italic;");
        return l;
    }

    /** colors = "bgColor;textColor" */
    private Label badge(String texto, String colors) {
        String[] c = colors.split(";");
        Label l = new Label(texto);
        l.setStyle(
                "-fx-background-color: " + c[0] + "; -fx-text-fill: " + c[1] + ";" +
                        "-fx-background-radius: 12; -fx-padding: 2 10; -fx-font-size: 11;"
        );
        return l;
    }

    private ScrollPane scrollTransparente(javafx.scene.Node contenido) {
        ScrollPane sp = new ScrollPane(contenido);
        sp.setFitToWidth(true);
        sp.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        return sp;
    }

    private String cardStyle() {
        return "-fx-background-color: white;" +
                "-fx-background-radius: 10;" +
                "-fx-border-color: #e7dfcb;" +
                "-fx-border-radius: 10;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.06), 6, 0, 0, 2);";
    }

    private String capitalize(String s) {
        if (s == null || s.isEmpty()) return s;
        return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }
    // ─────────────────────────────────────────────
    // BARRA LATERAL
    // ─────────────────────────────────────────────

    private VBox buildSidebar() {
        btnMascotas = sidebarButton("🐾  Mis Mascotas", "mascotas");
        btnCitas    = sidebarButton("📅  Mis Citas",    "citas");
        btnDatos    = sidebarButton("👤  Mis Datos",    "datos");

        VBox sidebar = new VBox(8, btnMascotas, btnCitas, btnDatos);
        sidebar.setPadding(new Insets(24, 12, 24, 12));
        sidebar.setPrefWidth(190);
        sidebar.setStyle(
                "-fx-background-color: #1a3a5c;" +
                        "-fx-border-color: #e7dfcb; -fx-border-width: 0 1 0 0;"
        );
        return sidebar;
    }

    private Button sidebarButton(String texto, String seccion) {
        Button btn = new Button(texto);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setAlignment(Pos.CENTER_LEFT);
        btn.setFont(Font.font("Arial", FontWeight.NORMAL, 13));
        styleInactivo(btn);
        btn.setOnAction(e -> mostrarSeccion(seccion));
        return btn;
    }

    private void styleActivo(Button btn) {
        btn.setStyle(
                "-fx-background-color: #e8f0fe; -fx-text-fill: #1a3a5c;" +
                        "-fx-background-radius: 8; -fx-border-radius: 8;" +
                        "-fx-padding: 10 14; -fx-cursor: hand; -fx-font-weight: bold;"
        );
    }

    private void styleInactivo(Button btn) {
        btn.setStyle(
                "-fx-background-color: transparent; -fx-text-fill: #c8d8ec;" +
                        "-fx-background-radius: 8; -fx-border-radius: 8;" +
                        "-fx-padding: 10 14; -fx-cursor: hand;"
        );
    }

    // ─────────────────────────────────────────────
    // CONTENIDO CENTRAL
    // ─────────────────────────────────────────────

    private StackPane buildContent() {
        panelMascotas = buildPanelMascotas();
        panelCitas    = buildPanelCitas();
        panelDatos    = buildPanelDatos();

        StackPane stack = new StackPane(panelMascotas, panelCitas, panelDatos);
        stack.setPadding(new Insets(24));
        StackPane.setAlignment(panelMascotas, Pos.TOP_LEFT);
        StackPane.setAlignment(panelCitas,    Pos.TOP_LEFT);
        StackPane.setAlignment(panelDatos,    Pos.TOP_LEFT);
        return stack;
    }

    private void mostrarSeccion(String seccion) {
        panelMascotas.setVisible(false); panelMascotas.setManaged(false);
        panelCitas.setVisible(false);    panelCitas.setManaged(false);
        panelDatos.setVisible(false);    panelDatos.setManaged(false);

        styleInactivo(btnMascotas);
        styleInactivo(btnCitas);
        styleInactivo(btnDatos);

        switch (seccion) {
            case "mascotas" -> { panelMascotas.setVisible(true); panelMascotas.setManaged(true); styleActivo(btnMascotas); }
            case "citas"    -> { panelCitas.setVisible(true);    panelCitas.setManaged(true);    styleActivo(btnCitas); }
            case "datos"    -> { panelDatos.setVisible(true);    panelDatos.setManaged(true);    styleActivo(btnDatos); }
        }
    }

    // ─────────────────────────────────────────────
    // PANEL: MIS MASCOTAS
    // Filtra por idCliente == ID_CLIENTE_SESION
    // TODO: reemplazar lista estática por consulta JDBC a tabla mascotas
    // ─────────────────────────────────────────────

    private VBox buildPanelMascotas() {
        Label titulo = sectionTitle("🐾 Mis Mascotas");

        List<Mascota> misMascotas = todasLasMascotas.stream()
                .filter(m -> m.getIdCliente() == ID_CLIENTE_SESION)
                .collect(Collectors.toList());

        VBox lista = new VBox(12);
        if (misMascotas.isEmpty()) {
            lista.getChildren().add(infoLabel("No tienes mascotas registradas."));
        } else {
            for (Mascota m : misMascotas) {
                lista.getChildren().add(buildCardMascota(m));
            }
        }

        ScrollPane scroll = scrollTransparente(lista);
        VBox.setVgrow(scroll, Priority.ALWAYS);

        VBox panel = new VBox(16, titulo, scroll);
        panel.setVisible(false);
        panel.setManaged(false);
        return panel;
    }

    private VBox buildCardMascota(Mascota m) {
        Label nombre  = bold(m.getNombre(), 15);
        Label detalle = small(m.getEspecie() + " · " + m.getRaza() + " · " + m.getEdad() + " años");
        Label vet     = small("Veterinario: " + m.getVeterinario());
        Label reiac   = small("En REIAC: " + (m.isEnREIAC() ? "✅ Sí" : "❌ No"));

        VBox card = new VBox(5, nombre, detalle, vet, reiac);
        card.setPadding(new Insets(16));
        card.setStyle(cardStyle());
        return card;
    }
}
