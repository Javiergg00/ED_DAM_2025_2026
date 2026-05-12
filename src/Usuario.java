public class Usuario {

    private int id;
    private String nombre;
    private String apellidos;
    private String dni;
    private String usuario;
    private String password;
    private int perfilId;

    public Usuario() {}

    public Usuario(String usuario, String password) {
        this.usuario = usuario;
        this.password = password;
    }

    // Getters y setters
}
