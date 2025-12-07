# backend-dmtoolkit-api

Proyecto minimalista de una API REST con Spring Boot — punto de partida para un
controlador de stock (productos, categorías y stock).

**Estado:** scaffold inicial (controladores, servicios, entidades de ejemplo).

**Stack técnico**
- Java 17+
- Spring Boot
- PostgreSQL (base de datos)

**Requisitos**
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
src/main/java/com/store/api
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


**Configuración de base de datos con .env**

Podes usar el archivo `.env` para definir el usuario, contraseña, URL de la base de datos, etc.

Ejemplo de `.env`:

```
DB_HOST=localhost
DB_PORT=5432
DB_NAME=milocal
DB_USER=postgres
DB_PASSWORD=miClaveSegura

SPRING_JPA_DDL=update
SPRING_SHOW_SQL=true
SPRING_FORMAT_SQL=true
```

**Licencia**
- Ver `LICENSE` en la raíz del repositorio.
