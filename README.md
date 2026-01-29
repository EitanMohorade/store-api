# Store API - Controlador de Stock

API REST con Spring Boot para gestión de inventario, productos, categorías y ventas.

## Stack
- Java 17+ | Spring Boot | PostgreSQL

## Inicio Rápido
```bash
mvn spring-boot:run
```

## Estructura y Lógica

```
src/main/java/com/store/api/
├── controller/   → Endpoints REST (productos, categorías, ventas, stock)
├── service/      → Lógica de negocio y validaciones
├── repository/   → Acceso a base de datos (JPA)
├── entity/       → Modelos (Producto, Categoria, Compania, Admin, Venta)
├── dto/          → Objetos de transferencia para requests/responses
├── exception/    → Manejo centralizado de errores
└── config/       → Configuración de seguridad y aplicación
```

**Flujo típico:** Controller → Service → Repository → Database

**Testing:** Toda la funcionalidad está cubierta con tests unitarios e integración en `src/test/`

## Endpoints Principales
- `GET/POST /api/productos` - Gestión de productos
- `GET/POST /api/categorias` - Gestión de categorías
- `POST /api/ventas` - Registro de ventas
- `GET /api/stock` - Consulta de inventario

Puerto: `8080` (configurable en `application.properties`)
