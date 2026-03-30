# Backend Java con Gradle

Este es el backend del Sistema de Gestión de Solicitudes de Medicamentos construido con **Gradle** en lugar de Maven.

## 🔧 Tecnologías

- **Java 21**
- **Spring Boot 3.3.5**
- **Gradle 8.10.2**
- **MySQL 8.0+** (via Docker Compose)
- **Configuración YAML** (application.yml)
- **Jakarta Validation** para validaciones
- **BCrypt** para encriptación de contraseñas

## 📁 Estructura del Proyecto

```
backend_java/
├── src/
│   ├── main/
│   │   ├── java/com/medicamentos/
│   │   │   ├── MedicamentosApplication.java
│   │   │   ├── auth/                    # Módulo de Autenticación
│   │   │   │   ├── controller/
│   │   │   │   ├── service/
│   │   │   │   ├── repository/
│   │   │   │   └── dto/
│   │   │   ├── solicitudes/             # Módulo de Solicitudes
│   │   │   │   ├── controller/
│   │   │   │   ├── service/
│   │   │   │   ├── repository/
│   │   │   │   ├── validator/           # Validadores personalizados
│   │   │   │   └── dto/
│   │   │   ├── model/                   # Entidades JPA
│   │   │   ├── config/                  # Configuración Spring Security
│   │   │   └── exception/               # Manejo de Errores
│   │   └── resources/
│   │       └── application.yml          # Configuración en YAML
│   └── test/
├── build.gradle                         # Configuración de Gradle
├── settings.gradle
├── gradlew                              # Gradle Wrapper (Linux/Mac)
├── gradlew.bat                          # Gradle Wrapper (Windows)
└── .gitignore
```

## ⚙️ Configuración

### application.yml

El archivo de configuración utiliza formato YAML en lugar de properties:

```yaml
spring:
  application:
    name: sistema-medicamentos
  
  datasource:
    url: jdbc:mysql://localhost:3306/medicamentos_db?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC
    username: root
    password: root
    driver-class-name: com.mysql.cj.jdbc.Driver
  
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    properties:
      hibernate:
        dialect: org.hibernate.dialect.MySQLDialect
        format_sql: true

server:
  port: 8181
  servlet:
    session:
      timeout: 30m
      cookie:
        http-only: true
        secure: false
        same-site: lax

cors:
  allowed-origins: http://localhost:4200
  allowed-methods: GET,POST,PUT,DELETE,OPTIONS
  allowed-headers: "*"
  allow-credentials: true
  max-age: 3600

logging:
  level:
    com.medicamentos: DEBUG
    org.springframework.security: DEBUG
```

## 🚀 Ejecución

### Requisitos Previos

1. **Base de datos MySQL** corriendo (via Docker Compose)
2. **Java 21** instalado
3. **Gradle Wrapper** incluido en el proyecto

### Paso 1: Iniciar la Base de Datos

```bash
cd database
docker compose up -d
```

Esto iniciará MySQL con la base de datos `medicamentos_db` y ejecutará automáticamente los scripts `schema.sql` y `seed.sql`.

### Paso 2: Ejecutar el Backend

### Usando Gradle Wrapper (Recomendado)

#### Linux/Mac:
```bash
./gradlew bootRun
```

#### Windows:
```bash
gradlew.bat bootRun
```

### Compilar el proyecto

```bash
./gradlew build
```

### Ejecutar tests

```bash
./gradlew test
```

### Limpiar el proyecto

```bash
./gradlew clean
```

### Generar JAR ejecutable

```bash
./gradlew bootJar
```

El JAR se generará en: `build/libs/sistema-medicamentos-1.0.0.jar`

Para ejecutarlo:
```bash
java -jar build/libs/sistema-medicamentos-1.0.0.jar
```

## 📦 Dependencias (build.gradle)

```gradle
dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-web'
    implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
    implementation 'org.springframework.boot:spring-boot-starter-security'
    implementation 'org.springframework.boot:spring-boot-starter-validation'
    
    runtimeOnly 'com.mysql:mysql-connector-j'
    
    compileOnly 'org.projectlombok:lombok'
    annotationProcessor 'org.projectlombok:lombok'
    
    developmentOnly 'org.springframework.boot:spring-boot-devtools'
    
    testImplementation 'org.springframework.boot:spring-boot-starter-test'
    testImplementation 'org.springframework.security:spring-security-test'
}
```

## 🔄 Diferencias con el Backend Maven

### 1. Sistema de Build
- **Maven**: Usa `pom.xml` y comando `mvn`
- **Gradle**: Usa `build.gradle` y comando `gradle` o `./gradlew`

### 2. Configuración
- **Maven**: `application.properties` (formato properties)
- **Gradle**: `application.yml` (formato YAML)

### 3. Comandos Comunes

| Acción | Maven | Gradle |
|--------|-------|--------|
| Ejecutar | `mvn spring-boot:run` | `./gradlew bootRun` |
| Compilar | `mvn compile` | `./gradlew build` |
| Tests | `mvn test` | `./gradlew test` |
| Limpiar | `mvn clean` | `./gradlew clean` |
| Empaquetar | `mvn package` | `./gradlew bootJar` |

### 4. Estructura de Directorios
- **Maven**: `target/` para archivos compilados
- **Gradle**: `build/` para archivos compilados

## 🎯 Funcionalidades

### Módulo de Autenticación
- `POST /auth/register` - Registro de usuarios con validaciones
- `POST /auth/login` - Inicio de sesión con cookies HTTP-only
- `POST /auth/logout` - Cerrar sesión
- `GET /auth/me` - Usuario actual autenticado

### Módulo de Solicitudes
- `GET /api/medicamentos` - Listar todos los medicamentos (77 medicamentos)
- `POST /api/solicitudes` - Crear solicitud con validaciones condicionales
- `GET /api/solicitudes?page=0&size=10` - Listar solicitudes paginadas

### Validaciones Implementadas

#### Validaciones Básicas (Jakarta Validation)
- **RegisterRequest**: username (3-50 chars), email válido, password (min 6 chars)
- **LoginRequest**: username y password obligatorios
- **SolicitudRequest**: medicamentoId obligatorio, email válido

#### Validaciones Personalizadas
- **@ValidSolicitudNoPOS**: Validador personalizado para medicamentos NO POS
  - Si el medicamento es NO POS, requiere:
    - `numeroOrden` (obligatorio)
    - `direccion` (obligatoria)
    - `telefono` (obligatorio)
    - `correo` (obligatorio)

### Manejo de Errores
- **GlobalExceptionHandler**: Manejo centralizado de excepciones
- **BadRequestException**: Para errores de validación
- **NotFoundException**: Para recursos no encontrados
- Respuestas JSON estructuradas con mensajes descriptivos

## 🔧 Configuración de Base de Datos

### Opción 1: Docker Compose (Recomendado)

La forma más sencilla es usar Docker Compose:

```bash
cd database
docker compose up -d
```

Credenciales por defecto:
- **Host**: localhost
- **Puerto**: 3306
- **Base de datos**: medicamentos_db
- **Usuario**: medicamentos_user
- **Contraseña**: medicamentos_pass

### Opción 2: MySQL Local

Si prefieres usar MySQL local, editar `src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/medicamentos_db
    username: tu_usuario
    password: tu_contraseña
```

Y ejecutar los scripts SQL:
```bash
mysql -u root -p medicamentos_db < database/schema.sql
mysql -u root -p medicamentos_db < database/seed.sql
```

## 🐛 Troubleshooting

### Error: Permission denied en gradlew

```bash
chmod +x gradlew
```

### Error: Gradle Wrapper no encontrado

Ejecutar:
```bash
gradle wrapper
```

### Error de conexión a MySQL

Verificar que MySQL esté corriendo y las credenciales sean correctas en `application.yml`.

## � Seguridad

### Autenticación
- **Sesiones HTTP-only**: Las cookies de sesión no son accesibles desde JavaScript
- **BCrypt**: Contraseñas encriptadas con salt automático
- **Spring Security**: Configuración personalizada para endpoints públicos y protegidos

### CORS
- Configuración externalizada en `application.yml`
- Permite credenciales (`allow-credentials: true`)
- Orígenes permitidos configurables

### Validaciones
- **Jakarta Validation**: Validaciones declarativas en DTOs
- **Validadores personalizados**: Lógica de validación compleja
- **Manejo de errores**: Respuestas estructuradas con mensajes claros

## 📝 Ventajas de Gradle

1. **Más rápido**: Builds incrementales y caché de dependencias
2. **Más flexible**: DSL basado en Groovy/Kotlin
3. **Menos verboso**: Configuración más concisa
4. **Mejor para proyectos grandes**: Builds multi-proyecto más eficientes
5. **Gradle Wrapper**: No requiere instalación de Gradle
6. **Configuración YAML**: Más legible que properties

## 🔗 Integración con Frontend Angular

El backend está configurado para trabajar con el frontend Angular 20:

```bash
# Terminal 1: Base de Datos (Docker)
cd database
docker compose up -d

# Terminal 2: Backend Gradle (Puerto 8181)
cd backend_java
./gradlew bootRun

# Terminal 3: Frontend Angular (Puerto 4200)
cd frontend-angular
npm start
```

**URLs:**
- Backend API: `http://localhost:8181`
- Frontend: `http://localhost:4200`
- MySQL: `localhost:3306`

### CORS Configuration

El backend está configurado para aceptar peticiones desde:
- `http://localhost:4200` (Angular)

Las cookies de sesión funcionan automáticamente con `withCredentials: true`.

## 📚 Documentación Adicional

- [Gradle User Guide](https://docs.gradle.org/current/userguide/userguide.html)
- [Spring Boot with Gradle](https://docs.spring.io/spring-boot/docs/current/gradle-plugin/reference/html/)
- [Gradle vs Maven](https://gradle.org/maven-vs-gradle/)

## ✅ Verificación

Para verificar que todo funciona correctamente:

```bash
# 1. Compilar el proyecto
./gradlew build

# 2. Ejecutar tests
./gradlew test

# 3. Ejecutar la aplicación
./gradlew bootRun
```

Si ves el mensaje: `Started MedicamentosApplication in X seconds`, ¡todo está funcionando correctamente!
