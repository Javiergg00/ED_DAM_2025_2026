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
 