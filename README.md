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

## 🚀 Tecnologías utilizadas
- Java
- JavaFX
- MySQL (o el que uses)
- Maven

## 📌 Descripción
Aplicación de gestión para una clínica veterinaria con módulos de clientes, mascotas, veterinarios y pagos.

## 🏛️ Estructura general del proyecto

```text
ClinicaVeterinaria/
│
├── src/
│   └──  main/
│      ├── java/
│      │   └── com/clinicaveterinaria/
│      │       ├── Main.java
│      │       ├── MainApp.java  # Extiende Application (JavaFX)
│      │       │
│      │       ├── modelo/
│      │       │
│      │       ├── controlador/
│      │       ├── db/
│      │       │
│      │       ├── externos/
│      │       └── utils/
│      └── resources/
│          └── com/clinicaveterinaria/
│              ├── vistas/
│              │   ├── auxiliar/
│              │   ├── veterinario/
│              │   └── cliente/
│              │
│              ├── estilos/
│              │   └── estilos.css
│              └── imagenes/
│   
├── pom.xml
└── README.md


