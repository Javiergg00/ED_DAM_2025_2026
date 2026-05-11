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
