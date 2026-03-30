# Sistema de Gestión de Solicitudes de Medicamentos

Sistema full stack para la gestión de solicitudes de medicamentos con autenticación de usuarios y formularios condicionales para medicamentos NO POS.

## 📋 Tabla de Contenidos

- [Descripción](#descripción)
- [Tecnologías Utilizadas](#tecnologías-utilizadas)
- [Arquitectura del Sistema](#arquitectura-del-sistema)
- [Requisitos Previos](#requisitos-previos)
- [Instalación](#instalación)
- [Configuración](#configuración)
- [Ejecución](#ejecución)
- [Endpoints API](#endpoints-api)
- [Estructura del Proyecto](#estructura-del-proyecto)
- [Base de Datos](#base-de-datos)
- [Funcionalidades](#funcionalidades)
- [Capturas de Pantalla](#capturas-de-pantalla)

## 📖 Descripción

Aplicación web completa que permite a los usuarios registrarse, autenticarse y gestionar solicitudes de medicamentos. El sistema diferencia entre medicamentos POS y NO POS, requiriendo información adicional para estos últimos.

### Características Principales

- ✅ Sistema de autenticación con sesiones
- ✅ Registro y login de usuarios
- ✅ Gestión de solicitudes de medicamentos
- ✅ Formulario condicional para medicamentos NO POS
- ✅ Listado paginado de solicitudes
- ✅ Validaciones en frontend y backend
- ✅ Arquitectura modular y escalable
- ✅ Interfaz moderna y responsiva

## 🛠 Tecnologías Utilizadas

### Backend
- **Java 21**
- **Spring Boot 3.3.5**
  - Spring Web
  - Spring Data JPA
  - Spring Security
  - Spring Validation (Jakarta)
- **MySQL 8.0+**
- **Gradle 8.10.2**
- **Lombok**

### Frontend
- **Angular 20**
- **TypeScript 5.7**
- **Reactive Forms**
- **TailwindCSS**
- **Standalone Components**

### Base de Datos
- **MySQL 8.0** (via Docker Compose)
- **Docker Compose V2**

## 🏗 Arquitectura del Sistema

El sistema sigue una arquitectura en capas con separación de responsabilidades:

```
┌─────────────────────────────────────────────────┐
│              Frontend (Angular 20)               │
│  - Standalone Components                        │
│  - Features (Auth, Solicitudes)                 │
│  - Services (AuthService, SolicitudService)     │
│  - Guards (AuthGuard)                           │
│  - Reactive Forms                               │
└─────────────────────────────────────────────────┘
                      ↓ HTTP/REST
┌─────────────────────────────────────────────────┐
│           Backend (Spring Boot 3.3.5)            │
│  ┌───────────────────────────────────────────┐  │
│  │  Módulo de Autenticación                  │  │
│  │  - AuthController                         │  │
│  │  - AuthService                            │  │
│  │  - UsuarioRepository                      │  │
│  └───────────────────────────────────────────┘  │
│  ┌───────────────────────────────────────────┐  │
│  │  Módulo de Solicitudes                    │  │
│  │  - SolicitudController                    │  │
│  │  - SolicitudService                       │  │
│  │  - SolicitudRepository                    │  │
│  │  - MedicamentoRepository                  │  │
│  └───────────────────────────────────────────┘  │
│  ┌───────────────────────────────────────────┐  │
│  │  Spring Security Configuration            │  │
│  │  - Session-based Authentication           │  │
│  │  - Password Encryption (BCrypt)           │  │
│  │  - CORS Configuration                     │  │
│  └───────────────────────────────────────────┘  │
└─────────────────────────────────────────────────┘
                      ↓ JDBC
┌─────────────────────────────────────────────────┐
│          MySQL Database (Docker)                 │
│  - usuarios                                     │
│  - medicamentos                                 │
│  - solicitudes                                  │
└─────────────────────────────────────────────────┘
```

## 📦 Requisitos Previos

Antes de comenzar, asegúrate de tener instalado:

- **Java JDK 21** o superior
- **Gradle 8.10+** (incluido con wrapper)
- **Node.js 20+** y **npm 10+**
- **Docker** y **Docker Compose V2**
- **Git**

### Verificar Instalaciones

```bash
java -version        # Debe mostrar Java 21
node -version        # Debe mostrar v20.x o superior
npm -version         # Debe mostrar 10.x o superior
docker --version     # Debe mostrar Docker version 20.x o superior
docker compose version  # Debe mostrar Docker Compose version v2.x
```

## 🚀 Instalación y Configuración

### 1. Clonar el Repositorio

```bash
git clone <repository-url>
cd pruebastecnicas
```

### 2. Configurar y Levantar la Base de Datos con Docker

La forma más sencilla de configurar la base de datos es usando Docker Compose:

```bash
cd database
docker compose up -d
```

Esto automáticamente:
- ✅ Descarga la imagen de MySQL 8.0
- ✅ Crea el contenedor `medicamentos-mysql`
- ✅ Crea la base de datos `medicamentos_db`
- ✅ Ejecuta `schema.sql` para crear las tablas
- ✅ Ejecuta `seed.sql` para poblar datos iniciales
- ✅ Configura usuarios y permisos

#### Verificar que la base de datos esté corriendo

```bash
docker compose ps
```

Deberías ver el contenedor `medicamentos-mysql` en estado `running`.

#### Ver logs de la base de datos

```bash
docker compose logs -f mysql
```

#### Credenciales de la Base de Datos

- **Host**: `localhost`
- **Puerto**: `3306`
- **Base de datos**: `medicamentos_db`
- **Usuario root**: `root` / `root`
- **Usuario aplicación**: `medicamentos_user` / `medicamentos_pass`

#### Detener la base de datos

```bash
docker compose down
```

#### Reiniciar la base de datos (eliminar datos)

```bash
docker compose down -v  # Elimina el volumen con los datos
docker compose up -d    # Vuelve a crear todo desde cero
```

### 3. Configurar el Backend (Java/Spring Boot)

El backend ya está configurado en `backend_java/src/main/resources/application.yml`.

#### Instalar dependencias y compilar

```bash
cd backend_java
./gradlew build
```

En Windows:
```bash
gradlew.bat build
```

### 4. Configurar el Frontend (Angular)

```bash
cd frontend-angular
npm install
```

## ⚙️ Configuración

### Backend (`application.yml`)

Ubicación: `backend_java/src/main/resources/application.yml`

```yaml
server:
  port: 8181
  servlet:
    session:
      timeout: 30m

spring:
  application:
    name: medicamentos-api
  
  datasource:
    url: jdbc:mysql://localhost:3306/medicamentos_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
    username: medicamentos_user
    password: medicamentos_pass
    driver-class-name: com.mysql.cj.jdbc.Driver
  
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    properties:
      hibernate:
        format_sql: true
        dialect: org.hibernate.dialect.MySQLDialect

cors:
  allowed-origins: http://localhost:4200
  allowed-methods: GET,POST,PUT,DELETE,OPTIONS
  allowed-headers: "*"
  allow-credentials: true
  max-age: 3600
```

### Frontend (Proxy Configuration)

Ubicación: `frontend-angular/proxy.conf.json`

```json
{
  "/auth": {
    "target": "http://localhost:8181",
    "secure": false,
    "changeOrigin": true
  },
  "/api": {
    "target": "http://localhost:8181",
    "secure": false,
    "changeOrigin": true
  }
}
```

### Base de Datos (Docker Compose)

Ubicación: `database/docker-compose.yml`

```yaml
version: '3.8'

services:
  mysql:
    image: mysql:8.0
    container_name: medicamentos-mysql
    restart: always
    environment:
      MYSQL_ROOT_PASSWORD: root
      MYSQL_DATABASE: medicamentos_db
      MYSQL_USER: medicamentos_user
      MYSQL_PASSWORD: medicamentos_pass
    ports:
      - "3306:3306"
    volumes:
      - ./schema.sql:/docker-entrypoint-initdb.d/01-schema.sql
      - ./seed.sql:/docker-entrypoint-initdb.d/02-seed.sql
      - mysql_data:/var/lib/mysql

volumes:
  mysql_data:
```

## ▶️ Ejecución

### Paso 1: Iniciar la Base de Datos

```bash
cd database
docker compose up -d
```

Espera unos segundos para que MySQL termine de inicializarse.

### Paso 2: Iniciar el Backend

En una nueva terminal:

```bash
cd backend_java
./gradlew bootRun
```

En Windows:
```bash
gradlew.bat bootRun
```

El backend estará disponible en: **`http://localhost:8181`**

**Nota**: El puerto del backend es `8181`, no `8080`.

### Paso 3: Iniciar el Frontend

En otra terminal:

```bash
cd frontend-angular
npm start
```

El frontend estará disponible en: **`http://localhost:4200`**

### Acceso a la Aplicación

1. Abrir el navegador en **`http://localhost:4200`**
2. **Opción 1 - Registrar un nuevo usuario**:
   - Ir a http://localhost:4200/register
   - Completar el formulario de registro
   - Iniciar sesión con las credenciales creadas

3. **Opción 2 - Usar credenciales de prueba**:
   - **Usuario**: `admin` / **Contraseña**: `password123`
   - **Usuario**: `usuario1` / **Contraseña**: `password123`

**Nota**: El sistema permite el registro de nuevos usuarios. Cualquier usuario registrado puede iniciar sesión y gestionar sus propias solicitudes de medicamentos.

### Resumen de Puertos

| Servicio | Puerto | URL |
|----------|--------|-----|
| Frontend (Angular) | 4200 | http://localhost:4200 |
| Backend (Spring Boot) | 8181 | http://localhost:8181 |
| MySQL | 3306 | localhost:3306 |

### Orden de Inicio Recomendado

1. **Base de datos** (Docker) → `docker compose up -d`
2. **Backend** (Spring Boot) → `./gradlew bootRun`
3. **Frontend** (Angular) → `npm start`

## 📡 Endpoints API

### Módulo de Autenticación

#### POST `/auth/register`
Registrar un nuevo usuario.

**Request Body:**
```json
{
  "username": "usuario1",
  "email": "usuario1@example.com",
  "password": "password123"
}
```

**Response:** `201 Created`
```json
{
  "message": "Usuario registrado exitosamente",
  "user": {
    "id": 1,
    "username": "usuario1",
    "email": "usuario1@example.com",
    "createdAt": "2024-03-27T10:30:00"
  }
}
```

#### POST `/auth/login`
Iniciar sesión.

**Request Body:**
```json
{
  "username": "usuario1",
  "password": "password123"
}
```

**Response:** `200 OK`
```json
{
  "message": "Inicio de sesión exitoso",
  "user": {
    "id": 1,
    "username": "usuario1",
    "email": "usuario1@example.com",
    "createdAt": "2024-03-27T10:30:00"
  }
}
```

#### POST `/auth/logout`
Cerrar sesión.

**Response:** `200 OK`
```json
{
  "message": "Sesión cerrada exitosamente"
}
```

#### GET `/auth/me`
Obtener usuario actual autenticado.

**Response:** `200 OK`
```json
{
  "id": 1,
  "username": "usuario1",
  "email": "usuario1@example.com",
  "createdAt": "2024-03-27T10:30:00"
}
```

### Módulo de Solicitudes

#### GET `/api/medicamentos`
Listar todos los medicamentos disponibles.

**Response:** `200 OK`
```json
[
  {
    "id": 1,
    "nombre": "Acetaminofén 500mg",
    "descripcion": "Analgésico y antipirético",
    "esNoPos": false
  },
  {
    "id": 11,
    "nombre": "Adalimumab 40mg",
    "descripcion": "Medicamento biológico",
    "esNoPos": true
  }
]
```

#### POST `/api/solicitudes`
Crear una nueva solicitud de medicamento.

**Request Body (Medicamento POS):**
```json
{
  "medicamentoId": 1
}
```

**Request Body (Medicamento NO POS):**
```json
{
  "medicamentoId": 11,
  "numeroOrden": "ORD-2024-001",
  "direccion": "Calle 123 #45-67, Bogotá",
  "telefono": "3001234567",
  "correo": "usuario@example.com"
}
```

**Response:** `201 Created`
```json
{
  "message": "Solicitud creada exitosamente",
  "solicitud": {
    "id": 1,
    "usuarioId": 1,
    "usuarioUsername": "usuario1",
    "medicamento": {
      "id": 11,
      "nombre": "Adalimumab 40mg",
      "descripcion": "Medicamento biológico",
      "esNoPos": true
    },
    "numeroOrden": "ORD-2024-001",
    "direccion": "Calle 123 #45-67, Bogotá",
    "telefono": "3001234567",
    "correo": "usuario@example.com",
    "estado": "PENDIENTE",
    "createdAt": "2024-03-27T10:35:00"
  }
}
```

#### GET `/api/solicitudes?page=0&size=10`
Listar solicitudes del usuario autenticado (paginado).

**Query Parameters:**
- `page`: Número de página (default: 0)
- `size`: Tamaño de página (default: 10)

**Response:** `200 OK`
```json
{
  "solicitudes": [
    {
      "id": 1,
      "usuarioId": 1,
      "usuarioUsername": "usuario1",
      "medicamento": {
        "id": 11,
        "nombre": "Adalimumab 40mg",
        "descripcion": "Medicamento biológico",
        "esNoPos": true
      },
      "numeroOrden": "ORD-2024-001",
      "direccion": "Calle 123 #45-67, Bogotá",
      "telefono": "3001234567",
      "correo": "usuario@example.com",
      "estado": "PENDIENTE",
      "createdAt": "2024-03-27T10:35:00"
    }
  ],
  "currentPage": 0,
  "totalItems": 1,
  "totalPages": 1
}
```

## 📁 Estructura del Proyecto

```
pruebastecnicas/
├── database/                         # Scripts SQL y Docker
│   ├── docker-compose.yml           # Configuración Docker MySQL
│   ├── schema.sql                   # Estructura de tablas
│   ├── seed.sql                     # Datos iniciales
│   └── README.md                    # Documentación de BD
│
├── backend_java/                     # Backend Spring Boot
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/medicamentos/
│   │   │   │   ├── MedicamentosApplication.java
│   │   │   │   ├── auth/            # Módulo de Autenticación
│   │   │   │   │   ├── controller/
│   │   │   │   │   │   └── AuthController.java
│   │   │   │   │   ├── service/
│   │   │   │   │   │   └── AuthService.java
│   │   │   │   │   ├── repository/
│   │   │   │   │   │   └── UsuarioRepository.java
│   │   │   │   │   └── dto/
│   │   │   │   │       ├── LoginRequest.java
│   │   │   │   │       ├── RegisterRequest.java
│   │   │   │   │       └── UserResponse.java
│   │   │   │   ├── solicitudes/     # Módulo de Solicitudes
│   │   │   │   │   ├── controller/
│   │   │   │   │   │   └── SolicitudController.java
│   │   │   │   │   ├── service/
│   │   │   │   │   │   └── SolicitudService.java
│   │   │   │   │   ├── repository/
│   │   │   │   │   │   ├── SolicitudRepository.java
│   │   │   │   │   │   └── MedicamentoRepository.java
│   │   │   │   │   ├── validator/   # Validadores personalizados
│   │   │   │   │   │   ├── ValidSolicitudNoPOS.java
│   │   │   │   │   │   └── SolicitudNoPOSValidator.java
│   │   │   │   │   └── dto/
│   │   │   │   │       ├── SolicitudRequest.java
│   │   │   │   │       ├── SolicitudResponse.java
│   │   │   │   │       └── MedicamentoResponse.java
│   │   │   │   ├── model/           # Entidades JPA
│   │   │   │   │   ├── Usuario.java
│   │   │   │   │   ├── Medicamento.java
│   │   │   │   │   └── Solicitud.java
│   │   │   │   ├── config/          # Configuración
│   │   │   │   │   └── SecurityConfig.java
│   │   │   │   └── exception/       # Manejo de Errores
│   │   │   │       ├── GlobalExceptionHandler.java
│   │   │   │       ├── BadRequestException.java
│   │   │   │       └── NotFoundException.java
│   │   │   └── resources/
│   │   │       └── application.yml
│   │   └── test/
│   ├── gradle/
│   ├── gradlew
│   ├── gradlew.bat
│   └── build.gradle
│
├── frontend-angular/                 # Frontend Angular 20
│   ├── src/
│   │   ├── app/
│   │   │   ├── core/                # Servicios y Guards
│   │   │   │   ├── guards/
│   │   │   │   │   └── auth.guard.ts
│   │   │   │   └── services/
│   │   │   │       ├── auth.service.ts
│   │   │   │       └── solicitud.service.ts
│   │   │   ├── features/            # Módulos de funcionalidad
│   │   │   │   ├── auth/
│   │   │   │   │   ├── login/
│   │   │   │   │   │   └── login.component.ts
│   │   │   │   │   └── register/
│   │   │   │   │       └── register.component.ts
│   │   │   │   └── solicitudes/
│   │   │   │       ├── nueva-solicitud/
│   │   │   │       │   └── nueva-solicitud.component.ts
│   │   │   │       └── lista-solicitudes/
│   │   │   │           └── lista-solicitudes.component.ts
│   │   │   ├── shared/              # Componentes compartidos
│   │   │   │   ├── components/
│   │   │   │   │   └── navbar/
│   │   │   │   │       └── navbar.component.ts
│   │   │   │   └── validators/
│   │   │   │       └── password-match.validator.ts
│   │   │   ├── app.component.ts
│   │   │   ├── app.config.ts
│   │   │   └── app.routes.ts
│   │   ├── styles.css
│   │   └── main.ts
│   ├── proxy.conf.json
│   ├── package.json
│   ├── tailwind.config.js
│   ├── tsconfig.json
│   └── angular.json
│
├── .gitignore
└── README.md
```

## 🗄 Base de Datos

### Diagrama Entidad-Relación

```
┌─────────────────────┐
│      usuarios       │
├─────────────────────┤
│ id (PK)            │
│ username (UNIQUE)  │
│ email (UNIQUE)     │
│ password           │
│ created_at         │
└─────────────────────┘
          │
          │ 1
          │
          │ N
          ▼
┌─────────────────────┐         ┌─────────────────────┐
│    solicitudes      │    N    │   medicamentos      │
├─────────────────────┤ ◄────── ├─────────────────────┤
│ id (PK)            │    1    │ id (PK)            │
│ usuario_id (FK)    │         │ nombre             │
│ medicamento_id (FK)│         │ descripcion        │
│ numero_orden       │         │ es_no_pos          │
│ direccion          │         │ created_at         │
│ telefono           │         └─────────────────────┘
│ correo             │
│ estado             │
│ created_at         │
└─────────────────────┘
```

### Tablas

#### `usuarios`
Almacena información de usuarios registrados.

| Campo      | Tipo         | Descripción                    |
|------------|--------------|--------------------------------|
| id         | BIGINT (PK)  | Identificador único            |
| username   | VARCHAR(50)  | Nombre de usuario (único)      |
| email      | VARCHAR(100) | Correo electrónico (único)     |
| password   | VARCHAR(255) | Contraseña encriptada (BCrypt) |
| created_at | TIMESTAMP    | Fecha de creación              |

#### `medicamentos`
Catálogo de medicamentos disponibles.

| Campo       | Tipo         | Descripción                      |
|-------------|--------------|----------------------------------|
| id          | BIGINT (PK)  | Identificador único              |
| nombre      | VARCHAR(200) | Nombre del medicamento           |
| descripcion | TEXT         | Descripción del medicamento      |
| es_no_pos   | BOOLEAN      | Indica si es medicamento NO POS  |
| created_at  | TIMESTAMP    | Fecha de creación                |

#### `solicitudes`
Registro de solicitudes de medicamentos.

| Campo          | Tipo         | Descripción                          |
|----------------|--------------|--------------------------------------|
| id             | BIGINT (PK)  | Identificador único                  |
| usuario_id     | BIGINT (FK)  | Referencia al usuario                |
| medicamento_id | BIGINT (FK)  | Referencia al medicamento            |
| numero_orden   | VARCHAR(100) | Número de orden (NO POS)             |
| direccion      | VARCHAR(255) | Dirección de entrega (NO POS)        |
| telefono       | VARCHAR(20)  | Teléfono de contacto (NO POS)        |
| correo         | VARCHAR(100) | Correo electrónico (NO POS)          |
| estado         | ENUM         | PENDIENTE, APROBADA, RECHAZADA       |
| created_at     | TIMESTAMP    | Fecha de creación                    |

## ✨ Funcionalidades

### 1. Autenticación y Autorización

- **Registro de usuarios**: Sistema de registro abierto que permite a cualquier usuario crear una cuenta. Incluye validación de datos y encriptación de contraseñas con BCrypt
- **Inicio de sesión**: Autenticación basada en sesiones HTTP-only cookies
- **Protección de rutas**: Solo usuarios autenticados pueden acceder a las funcionalidades del sistema
- **Cierre de sesión**: Invalidación de sesión del lado del servidor
- **Gestión de usuarios**: Cada usuario puede gestionar únicamente sus propias solicitudes

### 2. Gestión de Solicitudes

- **Listado de medicamentos**: Catálogo completo con indicador POS/NO POS
- **Creación de solicitudes**: Formulario con validaciones
- **Formulario condicional**: Campos adicionales obligatorios para NO POS
- **Listado paginado**: Visualización de solicitudes con paginación
- **Estados de solicitud**: PENDIENTE, APROBADA, RECHAZADA

### 3. Validaciones

#### Backend
- Validación de campos obligatorios
- Validación de formato de email
- Validación de longitud de contraseña
- Validación condicional para medicamentos NO POS
- Manejo de errores con mensajes descriptivos

#### Frontend
- Validación en tiempo real
- Mensajes de error claros
- Prevención de envíos duplicados
- Validación de formato de email

## 🎨 Capturas de Pantalla

### Página de Login
Formulario de inicio de sesión con validaciones y manejo de errores.

### Página de Registro
Formulario de registro con validación de contraseñas y campos obligatorios.

### Lista de Solicitudes
Tabla paginada con todas las solicitudes del usuario, mostrando estado y detalles.

### Nueva Solicitud
Formulario con selector de medicamento y campos condicionales para NO POS.

## 🔒 Seguridad

- **Encriptación de contraseñas**: BCrypt con salt
- **Sesiones HTTP**: Configuración segura con cookies
- **CORS**: Configurado para orígenes permitidos
- **Validaciones**: En ambos lados (frontend y backend)
- **SQL Injection**: Protección mediante JPA/Hibernate
- **XSS**: Sanitización de inputs en Angular

## 🧪 Pruebas

### Usuarios de Prueba

El script `seed.sql` incluye usuarios de prueba (contraseñas encriptadas con BCrypt):

| Usuario   | Contraseña   | Email                    |
|-----------|--------------|--------------------------|
| admin     | admin123     | admin@medicamentos.com   |
| usuario1  | password123  | usuario1@example.com     |

### Datos de Prueba Incluidos

- **77 medicamentos** en total:
  - 43 medicamentos POS (analgésicos, antibióticos, antiinflamatorios, etc.)
  - 34 medicamentos NO POS (biológicos, antivirales de alto costo, enfermedades raras, etc.)
- **2 usuarios** de prueba
- **Solicitudes de ejemplo** con diferentes estados

### Categorías de Medicamentos NO POS

- Medicamentos biológicos (Adalimumab, Infliximab, etc.)
- Oncológicos de alto costo (Rituximab, Trastuzumab, etc.)
- Antivirales de alto costo (Sofosbuvir, Ledipasvir, etc.)
- Enfermedades raras (Eculizumab, Nusinersen, etc.)
- Esclerosis múltiple (Ocrelizumab, Natalizumab, etc.)
- Hemofilia (Factor VIII, Factor IX, etc.)

## 🐛 Solución de Problemas

### Error de conexión a MySQL

**Problema**: El backend no puede conectarse a MySQL.

**Solución**:
```bash
# Verificar que el contenedor de Docker esté corriendo
cd database
docker compose ps

# Si no está corriendo, iniciarlo
docker compose up -d

# Ver logs para identificar errores
docker compose logs -f mysql
```

### Puerto 3306 ya en uso

**Problema**: Ya tienes MySQL corriendo localmente.

**Solución**:
```bash
# Opción 1: Detener MySQL local
sudo systemctl stop mysql

# Opción 2: Cambiar el puerto en docker-compose.yml
# Modificar "3306:3306" a "3307:3306" y actualizar application.yml
```

### Error de CORS

**Problema**: El frontend no puede comunicarse con el backend.

**Solución**: Verificar que:
1. El backend esté corriendo en el puerto `8181`
2. El frontend esté corriendo en el puerto `4200`
3. El archivo `application.yml` incluya `http://localhost:4200` en `cors.allowed-origins`

### Error 401 Unauthorized

**Problema**: No puedes acceder a rutas protegidas.

**Solución**:
- Asegúrate de estar autenticado (login exitoso)
- Las cookies de sesión deben estar habilitadas en el navegador
- Verifica que `withCredentials: true` esté configurado en las peticiones HTTP

### El backend no inicia

**Problema**: Error al ejecutar `./gradlew bootRun`

**Solución**:
```bash
# Verificar versión de Java
java -version  # Debe ser Java 21

# Limpiar y reconstruir
./gradlew clean build

# Verificar que MySQL esté corriendo
docker compose ps
```

### El frontend no compila

**Problema**: Error al ejecutar `npm start`

**Solución**:
```bash
# Limpiar node_modules y reinstalar
rm -rf node_modules package-lock.json
npm install

# Verificar versión de Node
node -version  # Debe ser v20.x o superior
```

### La base de datos no tiene datos

**Problema**: Las tablas están vacías después de iniciar Docker.

**Solución**:
```bash
# Eliminar el volumen y recrear
cd database
docker compose down -v
docker compose up -d

# Esperar unos segundos y verificar logs
docker compose logs -f mysql
```

## 📝 Notas Adicionales

### Mejoras Futuras

- Implementar roles de usuario (ADMIN, USER)
- Agregar filtros y búsqueda en solicitudes
- Implementar notificaciones por email
- Agregar dashboard con estadísticas
- Implementar tests unitarios y de integración
- Agregar documentación Swagger/OpenAPI
- Implementar caché con Redis
- Agregar logs estructurados

### Buenas Prácticas Implementadas

- ✅ Arquitectura en capas
- ✅ Separación de responsabilidades
- ✅ DTOs para transferencia de datos
- ✅ Manejo centralizado de excepciones
- ✅ Validaciones en ambos lados
- ✅ Código limpio y legible
- ✅ Nombres descriptivos
- ✅ Paginación para listas grandes
- ✅ Configuración externa

## 👥 Autor

Desarrollado como prueba técnica para demostrar habilidades en desarrollo full stack con Java Spring Boot y React.

## 📄 Licencia

Este proyecto es de código abierto y está disponible bajo la licencia MIT.
