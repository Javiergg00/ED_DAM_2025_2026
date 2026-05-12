package com.dao;

import com.db.DatabaseConnection;
import com.model.Mascota;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MascotaDAO {

    public boolean insertarMascota(Mascota mascota) {

        String sql = "INSERT INTO mascotas " +
                "(nombre, especie, raza, edad, id_cliente, veterinario, en_reiac) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, mascota.getNombre());
            ps.setString(2, mascota.getEspecie());
            ps.setString(3, mascota.getRaza());
            ps.setInt(4, mascota.getEdad());
            ps.setInt(5, mascota.getIdCliente());
            ps.setString(6, mascota.getVeterinario());
            ps.setBoolean(7, mascota.isEnREIAC());

            ps.executeUpdate();

            return true;

        } catch (SQLException e) {

            System.out.println("Error al insertar mascota: " + e.getMessage());

            return false;
        }
    }
}