# Store API - Controlador de Stock

API REST en Spring Boot para gestión de inventario, productos, categorías, compañías, administradores y ventas.

## Stack
- Java 21
- Spring Boot 3.5.8
- Spring Security (HTTP Basic)
- Spring Data JPA
- PostgreSQL

## Inicio rápido
```bash
mvn spring-boot:run
```

Puerto por defecto: `8080` (configurable en `application.properties`).


### Flujo de solicitud
```
HTTP Request → Controller → Service → Repository → Database
                ↓              ↓
             Validación    Lógica de negocio
             (DTOs)        (Reglas)
                ↓
           HTTP Response (DTOs)
```

## Seguridad y roles

La seguridad se configura en `SecurityConfig` con HTTP Basic.

- Usuario anónimo tiene rol por defecto `USER`.
- `GET /api/productos/**`, `GET /api/categorias/**`, `GET /api/companias/**`:
	- permitido para `ADMIN` y `USER`.
- `GET /api/ventas/**`:
	- solo `ADMIN`.
- `POST`, `PUT`, `DELETE` sobre `/api/**`:
	- solo `ADMIN`.

## Endpoints

### Productos
- `GET /api/productos`
- `GET /api/productos/{id}`
- `POST /api/productos` (ADMIN)
- `PUT /api/productos/{id}` (ADMIN)
- `DELETE /api/productos/{id}` (ADMIN)

### Categorías
- `GET /api/categorias`
- `GET /api/categorias/{id}`
- `POST /api/categorias` (ADMIN)
- `PUT /api/categorias/{id}` (ADMIN)
- `DELETE /api/categorias/{id}` (ADMIN)

### Compañías
- `GET /api/companias`
- `GET /api/companias/{id}`
- `POST /api/companias` (ADMIN)
- `PUT /api/companias/{id}` (ADMIN)
- `DELETE /api/companias/{id}` (ADMIN)

### Ventas
- `GET /api/ventas` (ADMIN)
- `GET /api/ventas/{id}` (ADMIN)
- `GET /api/ventas/hoy` (ADMIN)
- `GET /api/ventas/semana` (ADMIN)
- `GET /api/ventas/mes` (ADMIN)
- `POST /api/ventas` (ADMIN)
- `PUT /api/ventas/{id}` (ADMIN)
- `DELETE /api/ventas/{id}` (ADMIN)

### Administradores
- `POST /api/admins` (ADMIN)
- `PUT /api/admins/{id}` (ADMIN)
- `DELETE /api/admins/{id}` (ADMIN)


## Estructura del proyecto

Estructura principal del código dentro de `src/main/java/com/store/api`:

```
src/main/java/com/store/api/
├── StoreApiApplication.java
├── config/                         → Configuraciones y semillas
├── controller/                     → Controladores REST por dominio
├── service/                        → Lógica de negocio y validaciones
├── repository/                     → Persistencia con JPA
├── entity/                         → Entidades JPA
├── dto/                            → DTOs de entrada/salida
└── exception/                      → Excepciones y manejo global
```

## Carpeta de documentación

Para documentación del proyecto: `docs/`.

