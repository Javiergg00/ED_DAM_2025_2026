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

    public static List<Mascota> getMascotasByCliente(String idCliente) {
        List<Mascota> lista = new ArrayList<>();
        String sql = "SELECT * FROM mascotas WHERE id_cliente = ?";
        try (Connection con = ConexionDB.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, idCliente);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(new Mascota(
                        rs.getString("id_mascota"),
                        rs.getString("id_cliente"),
                        rs.getString("nombre"),
                        rs.getString("especie"),
                        rs.getString("raza"),
                        rs.getInt("edad"),
                        rs.getDouble("peso"),
                        rs.getString("estado"),
                        rs.getString("fecha_registro")
                ));
            }
        } catch (SQLException e) {
            System.out.println("[DB] Error getMascotas: " + e.getMessage());
        }
        return lista;
    }

    public static List<Consulta> getConsultas(String idCliente,
                                              String filtroEstado,
                                              String filtroEstadoPago,
                                              String idMascota,
                                              String busqueda) {
        List<Consulta> lista = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
                "SELECT * FROM consultas WHERE id_cliente = ?"
        );
        List<Object> params = new ArrayList<>();
        params.add(idCliente);

        if (filtroEstado != null && !filtroEstado.equals("todos")) {
            sql.append(" AND estado = ?");
            params.add(filtroEstado);
        }
        if (filtroEstadoPago != null && !filtroEstadoPago.equals("todos")) {
            sql.append(" AND estado_pago = ?");
            params.add(filtroEstadoPago);
        }
        if (idMascota != null && !idMascota.isEmpty()) {
            sql.append(" AND id_mascota = ?");
            params.add(idMascota);
        }
        if (busqueda != null && !busqueda.isEmpty()) {
            sql.append(" AND (titulo LIKE ? OR id_consulta LIKE ? OR descripcion LIKE ?)");
            String like = "%" + busqueda + "%";
            params.add(like); params.add(like); params.add(like);
        }
        sql.append(" ORDER BY fecha DESC");

        try (Connection con = ConexionDB.conectar();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) ps.setObject(i + 1, params.get(i));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(mapConsulta(rs));
            }
        } catch (SQLException e) {
            System.out.println("[DB] Error getConsultas: " + e.getMessage());
        }
        return lista;
    }
    public static List<Consulta> getConsultasPendientesPago(String idCliente) {
        return getConsultas(idCliente, null, "pendiente", null, null);
    }

}