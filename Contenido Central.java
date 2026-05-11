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