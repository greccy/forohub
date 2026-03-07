# 🗣️ ForoHub API

API REST para la gestión de tópicos de un foro, desarrollada como parte del **Challenge Back End – Alura/Oracle ONE**.

---

## 📋 Descripción

ForoHub permite a usuarios autenticados crear, consultar, actualizar y eliminar tópicos de discusión, aplicando validaciones de negocio como la prevención de duplicados y la verificación de existencia de registros.

---

## 🛠️ Tecnologías

| Tecnología | Versión |
|---|---|
| Java | 17 |
| Spring Boot | 3.4.3 |
| Spring Security | (incluido en Boot) |
| Spring Data JPA | (incluido en Boot) |
| Flyway | (incluido en Boot) |
| MySQL | 8+ |
| Lombok | (incluido en Boot) |
| Auth0 Java JWT | 4.5.0 |
| SpringDoc OpenAPI (Swagger) | 2.8.3 |
| Maven | 3.x |

---

## ⚙️ Configuración

### Variables de entorno requeridas

| Variable | Descripción |
|---|---|
| `DB_USERNAME` | Usuario de la base de datos MySQL |
| `DB_PASSWORD` | Contraseña de la base de datos MySQL |
| `JWT_SECRET` | Clave secreta para firmar los tokens JWT (valor por defecto: `123456`) |

### Base de datos

Crea una base de datos MySQL con el nombre `forohub_app`:

```sql
CREATE DATABASE forohub_app;
```

Las tablas se crean automáticamente con **Flyway** al iniciar la aplicación:

- `V1` → tabla `usuarios`
- `V2` → tabla `topicos`

---

## ▶️ Ejecución

```bash
# Clona el repositorio
git clone <url-del-repositorio>
cd forohub

# Configura las variables de entorno (ejemplo en Linux/Mac)
export DB_USERNAME=root
export DB_PASSWORD=tu_contraseña
export JWT_SECRET=una_clave_secreta_segura

# Ejecuta la aplicación
./mvnw spring-boot:run
```

La API quedará disponible en: `http://localhost:8080`

---

## 🔐 Autenticación

Todos los endpoints de tópicos requieren autenticación con **JWT Bearer Token**.

### 1. Obtener token

```http
POST /login
Content-Type: application/json

{
  "login": "usuario@email.com",
  "contrasena": "123456"
}
```

**Respuesta:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

### 2. Usar el token

Incluye el token en el encabezado de cada solicitud:

```
Authorization: Bearer <token>
```

---

## 📡 Endpoints

### Tópicos

| Método | URL | Descripción |
|---|---|---|
| `POST` | `/topicos` | Registrar un nuevo tópico |
| `GET` | `/topicos` | Listar tópicos (paginado, 10 por página, orden ASC por fecha) |
| `GET` | `/topicos/{id}` | Obtener el detalle de un tópico |
| `PUT` | `/topicos/{id}` | Actualizar un tópico |
| `DELETE` | `/topicos/{id}` | Eliminar un tópico |

---

### `POST /topicos` — Registrar tópico

**Body:**
```json
{
  "titulo": "¿Cómo usar Spring Security?",
  "mensaje": "Necesito ayuda para configurar la autenticación JWT.",
  "autor": "Juan Pérez",
  "curso": "Spring Boot"
}
```

**Validaciones:**
- Todos los campos son **obligatorios**
- No se permite registrar un tópico con un **título o mensaje ya existente**

**Respuestas:**
- `201 Created` → tópico creado exitosamente
- `400 Bad Request` → campo faltante o tópico duplicado

---

### `GET /topicos` — Listar tópicos

Retorna los tópicos paginados, ordenados por fecha de creación de forma ascendente.

**Parámetros opcionales (query params):**

| Parámetro | Descripción | Valor por defecto |
|---|---|---|
| `page` | Número de página (base 0) | `0` |
| `size` | Resultados por página | `10` |
| `sort` | Campo de ordenación | `fechaCreacion,asc` |

**Ejemplo:** `GET /topicos?page=1&size=5`

---

### `GET /topicos/{id}` — Detalle de tópico

**Validaciones:**
- El `id` debe corresponder a un tópico existente

**Respuestas:**
- `200 OK` → detalle del tópico
- `400 Bad Request` → `"No existe un tópico con el ID informado"`

---

### `PUT /topicos/{id}` — Actualizar tópico

**Body (todos los campos son opcionales):**
```json
{
  "titulo": "Nuevo título",
  "mensaje": "Nuevo mensaje actualizado",
  "status": "CERRADO",
  "curso": "Java Avanzado"
}
```

**Status disponibles:** `ABIERTO`, `CERRADO`, `NO_RESPONDIDO`

**Validaciones:**
- El `id` debe corresponder a un tópico existente
- Si se modifica el `titulo` o `mensaje`, no puede coincidir con el de otro tópico existente

**Respuestas:**
- `200 OK` → tópico actualizado
- `400 Bad Request` → ID inválido o duplicado detectado

---

### `DELETE /topicos/{id}` — Eliminar tópico

**Respuestas:**
- `204 No Content` → tópico eliminado

---

## 🏗️ Estructura del proyecto

```
src/main/java/com/alura/forohub/
├── controller/
│   ├── AutenticacionController.java   # POST /login
│   └── TopicoController.java          # CRUD de tópicos
├── domain/
│   ├── ValidacionException.java       # Excepción de negocio reutilizable
│   ├── topico/
│   │   ├── Topico.java
│   │   ├── TopicoRepository.java
│   │   ├── TopicoStatus.java
│   │   ├── RegistroDeTopicos.java     # Servicio: orquesta validadores
│   │   ├── DatosRegistroTopico.java
│   │   ├── DatosActualizacionTopico.java
│   │   ├── DatosDetalleTopico.java
│   │   ├── DatosListaTopico.java
│   │   └── validaciones/
│   │       ├── ValidadorDeTopicos.java          # Interfaz
│   │       └── ValidadorTopicosDuplicados.java  # Valida duplicados
│   └── usuario/
│       └── ...
└── infra/
    ├── exceptions/
    │   └── GestorDeErrores.java       # Manejador global de errores
    ├── security/
    │   ├── SecurityConfigurations.java
    │   ├── SecurityFilter.java
    │   ├── TokenService.java
    │   └── DatosTokenJWT.java
    └── springdoc/
        └── SpringDocConfigurations.java
```

---

## 📖 Documentación (Swagger)

Con la aplicación en ejecución, accede a la documentación interactiva:

```
http://localhost:8080/swagger-ui.html
```

---

## 🧩 Reglas de negocio

1. **Campos obligatorios en registro:** `titulo`, `mensaje`, `autor` y `curso` son requeridos. Un campo vacío o ausente devuelve `400` con detalle del campo inválido.
2. **Sin duplicados:** No se puede registrar ni actualizar un tópico con un `titulo` o `mensaje` que ya exista en la base de datos.
3. **Verificación de ID:** Los endpoints de detalle y actualización validan explícitamente que el `id` exista antes de operar.
4. **Arquitectura extensible:** Los validadores implementan la interfaz `ValidadorDeTopicos` y son inyectados automáticamente por Spring, permitiendo añadir nuevas reglas sin modificar el servicio.
