# ED_DAM_2025_2026
Repositorio Proyecto Final de Curso Clínica Veterinaria DAM

 Diseño UI (Figma)

Añadido enlaces al Prototipo de la pantalla de login de la clínica veterinaria con selección de roles (Cliente, Auxiliar, Veterinario).

##🔗 Ver diseño en Figma:
Visualización pantalla para selección de tipo de acceso:
https://www.figma.com/proto/KoAfPy5sPq2tAE7sLztIp3/Login-Clinica-veterinaria?t=q7tKJTuUtue1Fsto-1
Visualización pantalla para selección de tipo de acceso con los tres roles desplegados:
https://www.figma.com/proto/KoAfPy5sPq2tAE7sLztIp3/Login-Clinica-veterinaria?node-id=22-95&t=q7tKJTuUtue1Fsto-1
Visualización pantalla para selección de tipo de acceso con el rol de cliente seleccionado:
https://www.figma.com/proto/KoAfPy5sPq2tAE7sLztIp3/Login-Clinica-veterinaria?node-id=22-204&t=q7tKJTuUtue1Fsto-1

##🏛️Estructura general del proyecto
ClinicaVeterinaria/
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/clinicaveterinaria/
│   │   │       ├── Main.java
│   │   │       ├── MainApp.java              # Extiende Application (JavaFX)
│   │   │       │
│   │   │       ├── modelo/
│   │   │       │   ├── Cliente.java
│   │   │       │   ├── Mascota.java
│   │   │       │   ├── Veterinario.java
│   │   │       │   ├── Auxiliar.java
│   │   │       │   ├── Consulta.java
│   │   │       │   └── Pago.java
│   │   │       │
│   │   │       ├── controlador/
│   │   │       │   ├── LoginControlador.java
│   │   │       │   ├── ClienteControlador.java
│   │   │       │   ├── MascotaControlador.java
│   │   │       │   ├── VeterinarioControlador.java
│   │   │       │   └── AuxiliarControlador.java
│   │   │       │
│   │   │       ├── db/
│   │   │       │   ├── Conexion.java
│   │   │       │   ├── ClienteDAO.java
│   │   │       │   ├── MascotaDAO.java
│   │   │       │   ├── VeterinarioDAO.java
│   │   │       │   ├── ConsultaDAO.java
│   │   │       │   └── PagoDAO.java
│   │   │       │
│   │   │       ├── externos/
│   │   │       │   ├── REIACSimulado.java
│   │   │       │   └── BancoSimulado.java
│   │   │       │
│   │   │       └── utils/
│   │   │           ├── Encriptador.java
│   │   │           ├── ValidadorEmail.java
│   │   │           └── Sesion.java
│   │   │
│   │   └── resources/
│   │       └── com/clinicaveterinaria/
│   │           ├── vistas/
│   │           │   ├── login.fxml
│   │           │   ├── auxiliar/
│   │           │   │   ├── panel-auxiliar.fxml
│   │           │   │   ├── lista-clientes.fxml
│   │           │   │   ├── form-cliente.fxml
│   │           │   │   ├── lista-mascotas.fxml
│   │           │   │   └── form-mascota.fxml
│   │           │   ├── veterinario/
│   │           │   │   ├── panel-veterinario.fxml
│   │           │   │   ├── lista-espera.fxml
│   │           │   │   ├── registrar-consulta.fxml
│   │           │   │   └── historial-consultas.fxml
│   │           │   └── cliente/
│   │           │       ├── panel-cliente.fxml
│   │           │       ├── mis-mascotas.fxml
│   │           │       └── pago.fxml
│   │           │
│   │           ├── estilos/
│   │           │   └── estilos.css
│   │           └── imagenes/
│   │
│   └── test/
│       └── java/
│           └── com/clinicaveterinaria/
│               ├── db/
│               │   └── ConexionTest.java
│               └── modelo/
│                   └── ClienteTest.java
│
├── pom.xml
└── README.md

