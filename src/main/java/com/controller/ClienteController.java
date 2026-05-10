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
}