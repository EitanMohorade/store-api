# backend-dmtoolkit-api

Proyecto minimalista de una API REST con Spring Boot — punto de partida para un
controlador de stock (productos, categorías y stock).

**Estado:** scaffold inicial (controladores, servicios, entidades de ejemplo).

**Requisitos**
- Java 17+
- Maven (recomendado) o Docker (alternativa sin instalar Maven)

**Inicio rápido**

- Compilar y empaquetar:

	`mvn -q package`

- Ejecutar en desarrollo:

	`mvn -q spring-boot:run`

- Ejecutar JAR producido:

	`java -jar target/store-api-0.0.1-SNAPSHOT.jar`

**Estructura principal**

```
src/main/java/com/store_api
├── StoreApiApplication.java
├── controller/    # endpoints REST (Producto, Categoria, Stock)
├── service/       # lógica de negocio
├── repository/    # acceso a datos (placeholders)
└── entity/        # entidades (Producto, Categoria, Stock)
```

**Endpoints de ejemplo**
- `GET /api/productos`  → lista de productos (ejemplo)
- `GET /api/categorias` → lista de categorías (ejemplo)
- `GET /api/stock`      → estado de stock (ejemplo)

Puerto por defecto: `8080` (configurable en `src/main/resources/application.properties`).

**Licencia**
- Ver `LICENSE` en la raíz del repositorio.
