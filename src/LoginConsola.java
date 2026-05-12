import java.sql.Connection;
import java.util.Scanner;

public class LoginConsola {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        UsuarioDAO dao = new UsuarioDAO();

        System.out.print("Usuario: ");
        String usuario = sc.nextLine();

        System.out.print("Contraseña: ");
        String clave = sc.nextLine();

        if (dao.validarLogin(usuario, clave)) {
            System.out.println("Login correcto. Bienvenido " + usuario);
        } else {
            System.out.println("Usuario o contraseña incorrectos.");
        }


    }


}

