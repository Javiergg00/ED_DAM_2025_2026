package com.clinicaVeterinaria;

import com.controller.ClienteController;
import com.controller.LoginController;
import com.controller.RecepcionistaController;
import com.controller.VeterinarioController;
import com.model.SessionUser;
import com.model.UserRole;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AppNavigator {
    private static final double WINDOW_WIDTH = 960;
    private static final double WINDOW_HEIGHT = 680;

    private final Stage stage;

    public AppNavigator(Stage stage) {
        this.stage = stage;
    }

    public void showLogin() {
        LoginController controller = new LoginController(this);
        showScene(controller.getView(), "Clínica Veterinaria - Acceso");
    }

    public void openRoleHome(SessionUser sessionUser) {
        switch (sessionUser.getRole()) {
            case AUXILIAR -> {
                RecepcionistaController controller = new RecepcionistaController(stage, this, sessionUser);
                showScene(controller.getView(), "Clínica Veterinaria - Auxiliar");
            }
            case VETERINARIO -> {
                VeterinarioController controller = new VeterinarioController(this, sessionUser);
                showScene(controller.getView(), "Clínica Veterinaria - Veterinario");
            }
            case CLIENTE -> {
                ClienteController controller = new ClienteController(this, sessionUser);
                showScene(controller.getView(), "Clínica Veterinaria - Cliente");
            }
            default -> throw new IllegalArgumentException("Rol no soportado: " + sessionUser.getRole());
        }
    }

    public UserRole[] availableRoles() {
        return UserRole.values();
    }

    private void showScene(javafx.scene.Parent root, String title) {
        stage.setTitle(title);
        stage.setScene(new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT));
        stage.show();
    }
}
