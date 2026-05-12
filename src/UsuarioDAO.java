import java.sql.*;

public class UsuarioDAO {

    public boolean validarLogin(String usuario, String clave) {

        String sql = "SELECT * FROM usuarios WHERE usuario = ? AND password = SHA2(?, 256)";

        try (Connection con = ConexionDB.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, usuario);
            ps.setString(2, clave);

            ResultSet rs = ps.executeQuery();

            return rs.next();

        } catch (SQLException e) {
            System.out.println("Error al validar login: " + e.getMessage());
            return false;
        }
    }
}
