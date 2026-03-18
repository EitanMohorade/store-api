# Guía de Integración API - Store API

Esta guía resume cómo consumir la API de forma práctica: seguridad, endpoints, payloads, respuestas y errores.

## 1) Información base

- **Tecnología**: Spring Boot + Spring Security (HTTP Basic) + JPA
- **Base URL local (por defecto)**: `http://localhost:8080`
- **Formato**: JSON (`Content-Type: application/json`)
- **Documentación OpenAPI/Swagger**:
  - UI: `http://localhost:8080/swagger-ui/index.html`
  - JSON: `http://localhost:8080/v3/api-docs`

> `application.properties` no define `server.port`, por lo que se usa 8080 por defecto.

---

## 2) Seguridad y acceso

## 2.1 Autenticación

La API usa **HTTP Basic Auth**.

Ejemplo de header:

```http
Authorization: Basic base64(usuario:password)
```

Ejemplo con curl:

```bash
curl -u admin:tu_password http://localhost:8080/api/ventas
```

## 2.2 Roles y permisos

Reglas efectivas de seguridad:

- `GET /api/categorias/**` -> `ROLE_ADMIN` o `ROLE_USER`
- `GET /api/productos/**` -> `ROLE_ADMIN` o `ROLE_USER`
- `GET /api/companias/**` -> `ROLE_ADMIN` o `ROLE_USER`
- `GET /api/ventas/**` -> **solo** `ROLE_ADMIN`
- `POST|PUT|DELETE /api/**` -> **solo** `ROLE_ADMIN`
- Swagger (`/swagger-ui/**`, `/v3/api-docs/**`) -> `ROLE_ADMIN` o `ROLE_USER`

## 2.3 Nota importante sobre acceso público

La configuración actual asigna `ROLE_USER` a usuarios anónimos. En la práctica:

- Los GET de categorías, productos, compañías y Swagger quedan accesibles sin credenciales.
- Todo lo demás sigue requiriendo usuario admin válido.

## 2.4 Origen del usuario admin

- Los usuarios se cargan desde la tabla `Admin` (campo `nombre` + `password` encriptado con BCrypt).
- Cada admin autenticado recibe rol `ADMIN`.

---

## 3) Convenciones de respuestas

- **200 OK**: consulta o actualización exitosa.
- **201 Created**: creación exitosa (en la mayoría de recursos).
- **204 No Content**: eliminación sin cuerpo (excepto ventas, ver abajo).
- **Ventas DELETE** (`DELETE /api/ventas/{id}`): actualmente devuelve `200 OK` con cuerpo vacío.

---

## 4) Endpoints

## 4.1 Administradores (`/api/admins`)

> Requiere `ADMIN` para todos.

### POST `/api/admins`
Crea un administrador.

- **Status**: `201 Created`
- **Body request**:

```json
{
  "nombre": "admin_principal",
  "password": "secreto123"
}
```

- **Body response**:

```json
{
  "id": 1,
  "nombre": "admin_principal"
}
```

### PUT `/api/admins/{id}`
Actualiza nombre y/o password.

- **Status**: `200 OK`
- **Body request**:

```json
{
  "nombre": "admin_actualizado",
  "password": "nuevoPassword"
}
```

### DELETE `/api/admins/{id}`
Elimina admin.

- **Status**: `204 No Content`

---

## 4.2 Categorías (`/api/categorias`)

- `GET` públicos por configuración actual.
- `POST/PUT/DELETE` requieren `ADMIN`.

### GET `/api/categorias`
Lista categorías.

- **Status**: `200 OK`

### GET `/api/categorias/{id}`
Obtiene categoría por ID.

- **Status**: `200 OK`

### POST `/api/categorias`
Crea categoría.

- **Status**: `201 Created`
- **Body request**:

```json
{
  "nombre": "Lácteos",
  "descripcion": "Productos lácteos"
}
```

### PUT `/api/categorias/{id}`
Actualiza categoría.

- **Status**: `200 OK`
- **Body request**:

```json
{
  "nombre": "Lácteos Premium",
  "descripcion": "Línea premium"
}
```

### DELETE `/api/categorias/{id}`
Elimina categoría.

- **Status**: `204 No Content`

### Respuesta tipo categoría

```json
{
  "id": 10,
  "nombre": "Lácteos",
  "descripcion": "Productos lácteos"
}
```

---

## 4.3 Compañías (`/api/companias`)

- `GET` públicos por configuración actual.
- `POST/PUT/DELETE` requieren `ADMIN`.

### GET `/api/companias`
Lista compañías.

- **Status**: `200 OK`

### GET `/api/companias/{id}`
Obtiene compañía por ID.

- **Status**: `200 OK`

### POST `/api/companias`
Crea compañía.

- **Status**: `201 Created`
- **Body request**:

```json
{
  "nombre": "Acme Foods"
}
```

### PUT `/api/companias/{id}`
Actualiza compañía.

- **Status**: `200 OK`
- **Body request**:

```json
{
  "nombre": "Acme Foods SA"
}
```

### DELETE `/api/companias/{id}`
Elimina compañía.

- **Status**: `204 No Content`

### Respuesta tipo compañía

```json
{
  "id": 5,
  "nombre": "Acme Foods"
}
```

---

## 4.4 Productos (`/api/productos`)

- `GET` públicos por configuración actual.
- `POST/PUT/DELETE` requieren `ADMIN`.

### GET `/api/productos`
Lista productos.

- **Status**: `200 OK`

### GET `/api/productos/{id}`
Obtiene producto por ID.

- **Status**: `200 OK`

### POST `/api/productos`
Crea producto.

- **Status**: `201 Created`
- **Body request** (estructura esperada por DTO actual):

```json
{
  "articulo": "AR-1001",
  "descripcion": "Leche entera 1L",
  "stock": 100,
  "precio": 1200,
  "precioUnitario": 1500,
  "imagenUrl": "https://cdn.ejemplo.com/leche.jpg",
  "categoria": {
    "id": 10
  },
  "compania": {
    "id": 5
  }
}
```

### PUT `/api/productos/{id}`
Actualiza producto.

- **Status**: `200 OK`
- **Body request**: mismo formato que create.

### DELETE `/api/productos/{id}`
Elimina producto.

- **Status**: `204 No Content`

### Respuesta tipo producto

```json
{
  "id": 20,
  "articulo": "AR-1001",
  "descripcion": "Leche entera 1L",
  "stock": 100,
  "precio": 1200,
  "categoria": {
    "id": 10,
    "nombre": "Lácteos",
    "descripcion": "Productos lácteos"
  },
  "imagenUrl": "https://cdn.ejemplo.com/leche.jpg",
  "compania": {
    "id": 5,
    "nombre": "Acme Foods"
  }
}
```

---

## 4.5 Ventas (`/api/ventas`)

> Todos los endpoints de ventas requieren `ADMIN`.

### GET `/api/ventas`
Lista todas las ventas.

- **Status**: `200 OK`

### GET `/api/ventas/{id}`
Obtiene venta por ID.

- **Status**: `200 OK`

### GET `/api/ventas/hoy`
Ventas del día.

- **Status**: `200 OK`

### GET `/api/ventas/semana`
Ventas de la semana actual (lunes a domingo).

- **Status**: `200 OK`

### GET `/api/ventas/mes`
Ventas del mes actual.

- **Status**: `200 OK`

### POST `/api/ventas`
Crea venta y descuenta stock del producto.

- **Status actual**: `200 OK` (no `201` en implementación actual)
- **Body request**:

```json
{
  "producto": {
    "id": 20
  },
  "cantidad": 2,
  "fecha": "2026-03-18T10:30:00"
}
```

### PUT `/api/ventas/{id}`
Actualiza venta.

- **Status**: `200 OK`
- **Body request**: mismo formato que create.

### DELETE `/api/ventas/{id}`
Elimina venta.

- **Status actual**: `200 OK` con cuerpo vacío.

### Respuesta tipo venta

```json
{
  "id": 30,
  "producto": {
    "id": 20,
    "articulo": "AR-1001",
    "descripcion": "Leche entera 1L",
    "stock": 98,
    "precio": 1200,
    "precioUnitario": 1500
  },
  "cantidad": 2,
  "fecha": "2026-03-18T10:30:00",
  "precioUnitario": 1500,
  "precio": 1200,
  "totalPrecioUnitario": 3000,
  "totalPrecio": 2400
}
```

---

## 5) Formato estándar de errores

Para excepciones de negocio y validación manejadas por `GlobalExceptionHandler`:

```json
{
  "timestamp": "2026-03-18T12:00:00Z",
  "status": 400,
  "error": "Bad Request",
  "code": "VALIDATION_ERROR",
  "message": "articulo: El artículo no puede estar vacío",
  "path": "/api/productos"
}
```

Campos:

- `timestamp`: fecha/hora del error.
- `status`: HTTP status numérico.
- `error`: texto estándar HTTP.
- `code`: código de dominio de la API.
- `message`: detalle legible.
- `path`: ruta solicitada.

## 5.1 Códigos de error de negocio

- `RESOURCE_NOT_FOUND` -> 404
- `BAD_REQUEST` -> 400
- `VALIDATION_ERROR` -> 400 o 422 (según origen)
- `INVALID_STATE` -> 422
- `DUPLICATE_RESOURCE` -> 409
- `STOCK_INSUFFICIENT` -> 400
- `OPERATION_NOT_ALLOWED` -> 403
- `UNAUTHORIZED_OPERATION` -> 403
- `DATA_INTEGRITY` -> 409

## 5.2 Errores de seguridad Spring Security

- `401 Unauthorized`: credenciales ausentes/incorrectas en rutas protegidas.
- `403 Forbidden`: autenticado sin permisos de rol suficientes.

> Estos errores pueden no usar el formato `ErrorResponse` anterior, porque los genera la capa de seguridad.

---

## 6) Ejemplos rápidos de consumo

## 6.1 Lectura pública (sin credenciales, según configuración actual)

```bash
curl http://localhost:8080/api/productos
```

## 6.2 Crear categoría (ADMIN)

```bash
curl -u admin:tu_password -X POST http://localhost:8080/api/categorias \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Lácteos","descripcion":"Productos lácteos"}'
```

## 6.3 Registrar venta (ADMIN)

```bash
curl -u admin:tu_password -X POST http://localhost:8080/api/ventas \
  -H "Content-Type: application/json" \
  -d '{"producto":{"id":20},"cantidad":2,"fecha":"2026-03-18T10:30:00"}'
```

---

## 7) Notas de integración importantes

- En DTOs de producto y venta se reciben objetos anidados (`categoria`, `compania`, `producto`). En cliente normalmente basta enviar el `id`.
- Crear venta descuenta stock del producto asociado.
- Si `cantidad > stock`, se rechaza con error de validación.
- El endpoint de creación de admin requiere rol `ADMIN`; en despliegue real suele necesitarse un admin semilla o script de inicialización para bootstrap inicial.
- Si deseas una API estrictamente privada, conviene retirar el rol anónimo o no asignar `ROLE_USER` a anónimos.

---
