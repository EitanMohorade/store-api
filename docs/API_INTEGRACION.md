# Guía de Integración API - Store API

Esta guía resume cómo consumir la API de forma práctica: seguridad, endpoints, payloads, respuestas y errores.

---

## 1) Información base

- **Tecnología:** Spring Boot + Spring Security (HTTP Basic) + JPA
- **Base URL local (por defecto):** `http://localhost:8080`
- **Formato general:** JSON (`Content-Type: application/json`), excepto los endpoints de creación/actualización de productos que usan `multipart/form-data` (ver sección 4.4)
- **Documentación OpenAPI/Swagger:**
  - UI: `http://localhost:8080/swagger-ui/index.html`
  - JSON: `http://localhost:8080/v3/api-docs`

> `application.properties` no define `server.port`, por lo que se usa `8080` por defecto.

---

## 2) Seguridad y acceso

### 2.1 Autenticación

La API usa HTTP Basic Auth.

```
Authorization: Basic base64(usuario:password)
```

Ejemplo con curl:
```bash
curl -u admin:tu_password http://localhost:8080/api/ventas
```

### 2.2 Roles y permisos

| Ruta | Roles permitidos |
|---|---|
| `GET /api/categorias/**` | ADMIN, USER |
| `GET /api/productos/**` | ADMIN, USER |
| `GET /api/companias/**` | ADMIN, USER |
| `GET /api/ventas/**` | solo ADMIN |
| `POST\|PUT\|DELETE /api/**` | solo ADMIN |
| `Swagger (/swagger-ui/**, /v3/api-docs/**)` | ADMIN, USER |

### 2.3 Nota importante sobre acceso público

La configuración actual asigna `ROLE_USER` a usuarios anónimos. En la práctica:

- Los `GET` de categorías, productos, compañías y Swagger quedan accesibles sin credenciales.
- Todo lo demás sigue requiriendo usuario admin válido.

### 2.4 Origen del usuario admin

Los usuarios se cargan desde la tabla `Admin` (campo `nombre` + `password` encriptado con BCrypt). Cada admin autenticado recibe rol `ADMIN`.

---

## 3) Convenciones de respuestas

| Código | Descripción |
|---|---|
| `200 OK` | Consulta o actualización exitosa |
| `201 Created` | Creación exitosa |
| `204 No Content` | Eliminación sin cuerpo (excepto ventas) |

> **Ventas DELETE** (`DELETE /api/ventas/{id}`): actualmente devuelve `200 OK` con cuerpo vacío.

---

## 4) Endpoints

### 4.1 Administradores (`/api/admins`)

Requiere ADMIN para todos.

**POST /api/admins** — Crea un administrador. `201 Created`
```json
{
  "nombre": "admin_principal",
  "password": "secreto123"
}
```
Respuesta:
```json
{
  "id": 1,
  "nombre": "admin_principal"
}
```

**PUT /api/admins/{id}** — Actualiza nombre y/o password. `200 OK`
```json
{
  "nombre": "admin_actualizado",
  "password": "nuevoPassword"
}
```

**DELETE /api/admins/{id}** — Elimina admin. `204 No Content`

---

### 4.2 Categorías (`/api/categorias`)

GET públicos por configuración actual. POST/PUT/DELETE requieren ADMIN.

**GET /api/categorias** — Lista categorías. `200 OK`

**GET /api/categorias/{id}** — Obtiene categoría por ID. `200 OK`

**POST /api/categorias** — Crea categoría. `201 Created`
```json
{
  "nombre": "bolsos",
  "descripcion": "Productos bolsos"
}
```

**PUT /api/categorias/{id}** — Actualiza categoría. `200 OK`
```json
{
  "nombre": "bolsos Premium",
  "descripcion": "Línea premium"
}
```

**DELETE /api/categorias/{id}** — Elimina categoría. `204 No Content`

Respuesta tipo categoría:
```json
{
  "id": 10,
  "nombre": "bolsos",
  "descripcion": "Productos bolsos"
}
```

---

### 4.3 Compañías (`/api/companias`)

GET públicos por configuración actual. POST/PUT/DELETE requieren ADMIN.

**GET /api/companias** — Lista compañías. `200 OK`

**GET /api/companias/{id}** — Obtiene compañía por ID. `200 OK`

**POST /api/companias** — Crea compañía. `201 Created`
```json
{ "nombre": "GC" }
```

**PUT /api/companias/{id}** — Actualiza compañía. `200 OK`
```json
{ "nombre": "GC SA" }
```

**DELETE /api/companias/{id}** — Elimina compañía. `204 No Content`

Respuesta tipo compañía:
```json
{
  "id": 5,
  "nombre": "GC"
}
```

---

### 4.4 Productos (`/api/productos`)

GET públicos por configuración actual. POST/PUT/DELETE requieren ADMIN.

**GET /api/productos** — Lista productos. `200 OK`

**GET /api/productos/{id}** — Obtiene producto por ID. `200 OK`

---

#### ⚠️ POST y PUT usan `multipart/form-data`

A diferencia del resto de endpoints, la creación y actualización de productos usan `Content-Type: multipart/form-data` porque permiten adjuntar una imagen.

La request se compone de **dos partes**:

| Parte | Nombre | Tipo | Requerido | Descripción |
|---|---|---|---|---|
| Datos del producto | `datos` | JSON (`application/json`) | ✅ Sí | Campos del producto |
| Imagen | `imagen` | Archivo (.jpg / .png / .webp, máx. 5MB) | ❌ No | Si se omite, el producto queda sin imagen |

---

**POST /api/productos** — Crea producto. `201 Created`

Parte `datos` (Content-Type: `application/json`):
```json
{
  "articulo": "AR-1001",
  "descripcion": "bolso de mano",
  "stock": 100,
  "precio": 1200,
  "precioUnitario": 1500,
  "categoria": {
    "id": 10
  },
  "compania": {
    "id": 5
  }
}
```

Parte `imagen`: archivo de imagen (opcional).

> **Nota:** `imagenUrl` ya no se envía en el JSON. La URL pública de la imagen es generada automáticamente por Cloudinary y devuelta en la respuesta.

---

**PUT /api/productos/{id}** — Actualiza producto. `200 OK`

Mismo formato que POST. Si no se adjunta imagen, **la imagen existente se conserva**.

---

**DELETE /api/productos/{id}** — Elimina producto y su imagen en Cloudinary. `204 No Content`

---

#### Ejemplos de consumo para productos

**Con curl (crear producto sin imagen):**
```bash
curl -u admin:tu_password -X POST http://localhost:8080/api/productos \
  -F 'datos={"articulo":"AR-1001","descripcion":"bolso de mano","stock":100,"precio":1200,"precioUnitario":1500,"categoria":{"id":10},"compania":{"id":5}};type=application/json'
```

**Con curl (crear producto con imagen):**
```bash
curl -u admin:tu_password -X POST http://localhost:8080/api/productos \
  -F 'datos={"articulo":"AR-1001","descripcion":"bolso de mano","stock":100,"precio":1200,"precioUnitario":1500,"categoria":{"id":10},"compania":{"id":5}};type=application/json' \
  -F 'imagen=@/ruta/local/imagen.jpg'
```

**Con fetch (JavaScript/Frontend):**
```javascript
const formData = new FormData();

const datos = {
  articulo: "AR-1001",
  descripcion: "bolso de mano",
  stock: 100,
  precio: 1200,
  precioUnitario: 1500,
  categoria: { id: 10 },
  compania: { id: 5 }
};

formData.append("datos", new Blob([JSON.stringify(datos)], { type: "application/json" }));

// Solo si hay imagen seleccionada
if (imagenFile) {
  formData.append("imagen", imagenFile);
}

const response = await fetch("http://localhost:8080/api/productos", {
  method: "POST",
  headers: {
    "Authorization": "Basic " + btoa("admin:tu_password")
  },
  body: formData
  // ⚠️ No establecer Content-Type manualmente, el browser lo hace automático con el boundary
});
```

**Con axios (JavaScript/Frontend):**
```javascript
const formData = new FormData();

const datos = {
  articulo: "AR-1001",
  descripcion: "BOLSO DE MANO",
  stock: 100,
  precio: 1200,
  precioUnitario: 1500,
  categoria: { id: 10 },
  compania: { id: 5 }
};

formData.append("datos", new Blob([JSON.stringify(datos)], { type: "application/json" }));

if (imagenFile) {
  formData.append("imagen", imagenFile);
}

await axios.post("http://localhost:8080/api/productos", formData, {
  auth: { username: "admin", password: "tu_password" }
  // ⚠️ No establecer Content-Type, axios lo hace automático
});
```

**Con Postman:**

1. Seleccionar método `POST` y URL `http://localhost:8080/api/productos`
2. En **Authorization**: tipo `Basic Auth`, ingresar usuario y password
3. En **Body**: seleccionar `form-data`
4. Agregar key `datos`, cambiar tipo a `Text`, y en el campo de Content-Type escribir `application/json`. Pegar el JSON en el value.
5. Agregar key `imagen`, cambiar tipo a `File`, seleccionar el archivo (opcional)

---

#### Respuesta tipo producto

```json
{
  "id": 20,
  "articulo": "AR-1001",
  "descripcion": "bolso re lindo",
  "stock": 100,
  "precio": 1200,
  "categoria": {
    "id": 10,
    "nombre": "CArteras",
    "descripcion": "un bolso pequeño"
  },
  "imagenUrl": "https://res.cloudinary.com/tu_cloud/image/upload/productos/uuid_imagen.jpg",
  "compania": {
    "id": 5,
    "nombre": "GC"
  }
}
```

> `imagenUrl` en la respuesta es la URL pública de Cloudinary, lista para usar directamente en `<img src="...">`. Si el producto no tiene imagen, este campo es `null`.

---

### 4.5 Ventas (`/api/ventas`)

Todos los endpoints de ventas requieren ADMIN.

**GET /api/ventas** — Lista todas las ventas. `200 OK`

**GET /api/ventas/{id}** — Obtiene venta por ID. `200 OK`

**GET /api/ventas/hoy** — Ventas del día. `200 OK`

**GET /api/ventas/semana** — Ventas de la semana actual (lunes a domingo). `200 OK`

**GET /api/ventas/mes** — Ventas del mes actual. `200 OK`

**POST /api/ventas** — Crea venta y descuenta stock del producto. `200 OK`
```json
{
  "producto": { "id": 20 },
  "cantidad": 2,
  "fecha": "2026-03-18T10:30:00"
}
```

**PUT /api/ventas/{id}** — Actualiza venta. `200 OK` (mismo formato que create)

**DELETE /api/ventas/{id}** — Elimina venta. `200 OK` con cuerpo vacío.

Respuesta tipo venta:
```json
{
  "id": 30,
  "producto": {
    "id": 20,
    "articulo": "AR-1001",
    "descripcion": "bolso re lindo",
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

### 4.6 Configuración de la tienda (`/api/configuracion`)

GET público. PUT requiere ADMIN.

**GET /api/configuracion** — Obtiene la configuración actual de la tienda. `200 OK`

No requiere autenticación ni body. Respuesta:
```json
{
  "nombre": "Mi Tienda",
  "direccion": "Av. Corrientes 1234, CABA",
  "tagline": "Productos frescos, precios justos",
  "descripcion": "Describí tu tienda en pocas palabras..."
}
```

Si nunca fue configurada, devuelve los valores por defecto con nombre: `"Mi Tienda"` y el resto en `null`.

**PUT /api/configuracion** — Actualiza la configuración. `200 OK`

```json
{
  "nombre": "Mi Tienda",
  "direccion": "Av. Corrientes 1234, CABA",
  "tagline": "Productos frescos, precios justos",
  "descripcion": "Describí tu tienda en pocas palabras..."
}
```

Devuelve el mismo objeto con los valores actualizados. Solo `nombre` es obligatorio; el resto es opcional.

---

## 5) Formato estándar de errores

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

| Campo | Descripción |
|---|---|
| `timestamp` | Fecha/hora del error |
| `status` | HTTP status numérico |
| `error` | Texto estándar HTTP |
| `code` | Código de dominio de la API |
| `message` | Detalle legible |
| `path` | Ruta solicitada |

### 5.1 Códigos de error de negocio

| Code | HTTP |
|---|---|
| `RESOURCE_NOT_FOUND` | 404 |
| `BAD_REQUEST` | 400 |
| `VALIDATION_ERROR` | 400 / 422 |
| `INVALID_STATE` | 422 |
| `DUPLICATE_RESOURCE` | 409 |
| `STOCK_INSUFFICIENT` | 400 |
| `OPERATION_NOT_ALLOWED` | 403 |
| `UNAUTHORIZED_OPERATION` | 403 |
| `DATA_INTEGRITY` | 409 |

### 5.2 Errores de seguridad Spring Security

- `401 Unauthorized`: credenciales ausentes o incorrectas en rutas protegidas.
- `403 Forbidden`: autenticado sin permisos de rol suficientes.

> Estos errores pueden no usar el formato `ErrorResponse` anterior porque los genera la capa de seguridad.

---

## 6) Arranque y solución rápida

Por qué puede fallar `mvn spring-boot:run`:

- No se cargaron las propiedades de conexión (no se activó el perfil que define `spring.datasource.url`).
- Falta el driver JDBC en el classpath.
- Problemas de permisos/DDL en la base de datos.

**Forma recomendada (con perfil dev configurado en `application-dev.properties`):**
```bash
mvn -DskipTests spring-boot:run
```

**Activar perfil explícitamente:**
```bash
mvn -DskipTests -Dspring-boot.run.profiles=dev spring-boot:run
```

**Pasar propiedades en línea de comandos:**
```bash
mvn -DskipTests -Dspring-boot.run.arguments="--spring.profiles.active=dev \
  --spring.datasource.url=jdbc:postgresql://localhost:5432/store_db \
  --spring.datasource.username=username \
  --spring.datasource.password=password" spring-boot:run
```

**Empaquetar y ejecutar el JAR:**
```bash
mvn -DskipTests package
java -jar target/store-api-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev
```

**¿Otro proceso ocupa el puerto 8080?**
```bash
lsof -iTCP:8080 -sTCP:LISTEN -P -n
# o
ss -ltnp | grep 8080
kill <PID>
```

---

## 7) Notas de integración importantes

- En DTOs de producto y venta se reciben objetos anidados (`categoria`, `compania`, `producto`). Basta enviar solo el `id`.
- Crear venta descuenta stock del producto asociado. Si `cantidad > stock`, se rechaza con error.
- Los endpoints `POST` y `PUT` de productos usan `multipart/form-data`. **No enviar `Content-Type: application/json` manualmente** en estos endpoints o fallará con `415 Unsupported Media Type`.
- La imagen es siempre opcional. Un producto puede crearse o actualizarse sin imagen.
- Al eliminar un producto, su imagen en Cloudinary también se elimina automáticamente.
- `imagenUrl` en la respuesta es una URL pública de Cloudinary, usable directamente en el frontend (`<img src="...">` o similar).
- El endpoint de creación de admin requiere rol ADMIN; en el primer despliegue se usa el admin semilla configurado en `AdminSeedConfig`.
-La configuración de la tienda (nombre, direccion, tagline, descripcion) ya no se guarda en localStorage. Usar GET /api/configuracion para leer los valores al montar el componente y PUT /api/configuracion para guardar cambios.