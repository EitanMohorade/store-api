# FOLDER_STRUCTURE

Estructura completa del proyecto Store API (raíz del repositorio):

```
.
├── API_INTEGRACION.md
├── LICENSE
├── mvnw
├── mvnw.cmd
├── pom.xml
├── README.md
├── run
├── docs/
│   ├── README.md
│   ├── API_INTEGRACION.md
│   └── FOLDER_STRUCTURE.md
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── store/
│   │   │           └── api/
│   │   │               ├── StoreApiApplication.java
│   │   │               ├── config/
│   │   │               │   ├── AdminSeedConfig.java
│   │   │               │   └── SecurityConfig.java
│   │   │               ├── controller/
│   │   │               │   ├── AdminController.java
│   │   │               │   ├── CategoriaController.java
│   │   │               │   ├── CompaniaController.java
│   │   │               │   ├── ConfiguracionTiendaController.java
│   │   │               │   ├── ProductoController.java
│   │   │               │   └── VentaController.java
│   │   │               ├── dto/
│   │   │               │   ├── admin/
│   │   │               │   ├── categoria/
│   │   │               │   ├── compania/
│   │   │               │   ├── configuracionTienda/
│   │   │               │   ├── producto/
│   │   │               │   └── venta/
│   │   │               ├── entity/
│   │   │               │   ├── Admin.java
│   │   │               │   ├── Categoria.java
│   │   │               │   ├── Compania.java
│   │   │               │   ├── ConfiguracionTienda.java
│   │   │               │   ├── Producto.java
│   │   │               │   └── Venta.java
│   │   │               ├── exception/
│   │   │               ├── repository/
│   │   │               └── service/
│   │   └── resources/
│   │       ├── application-dev.properties
│   │       ├── application-dev.properties.example
│   │       └── application.properties
│   └── test/
│       ├── java/
│       │   └── com/
│       │       └── store/
│       │           └── api/
│       │               ├── StoreApiApplicationTests.java
│       │               └── controller/
│       └── resources/
│           └── application-dev.properties.example
├── target/
│   ├── classes/
│   │   ├── application-dev.properties
│   │   ├── application-dev.properties.example
│   │   ├── application.properties
│   │   └── com/
│   │       └── store/
│   │           └── api/
│   │               ├── config/
│   │               ├── controller/
│   │               ├── dto/
│   │               ├── entity/
│   │               └── exception/
│   ├── generated-sources/
│   ├── generated-test-sources/
│   └── test-classes/

```

