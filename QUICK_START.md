# Guía de Inicio Rápido

Esta guía te ayudará a poner en marcha el sistema en menos de 10 minutos.

## ⚡ Inicio Rápido (3 Pasos)

### 1️⃣ Configurar Base de Datos (2 minutos)

```bash
# Crear base de datos
mysql -u root -p -e "CREATE DATABASE medicamentos_db;"

# Ejecutar scripts
mysql -u root -p medicamentos_db < database/schema.sql
mysql -u root -p medicamentos_db < database/seed.sql
```

### 2️⃣ Iniciar Backend (1 minuto)

```bash
cd backend
mvn spring-boot:run
```

Espera a ver: `Started MedicamentosApplication in X seconds`

### 3️⃣ Iniciar Frontend (1 minuto)

```bash
cd frontend
npm install  # Solo la primera vez
npm run dev
```

Abre tu navegador en: **http://localhost:5173**

## 🎯 Probar la Aplicación

### Opción 1: Usar Usuario de Prueba

1. Ir a http://localhost:5173/login
2. Usar credenciales:
   - **Usuario**: `admin`
   - **Contraseña**: `password123`
3. ¡Listo! Ya puedes crear solicitudes

### Opción 2: Registrar Nuevo Usuario

1. Ir a http://localhost:5173/register
2. Completar el formulario
3. Iniciar sesión con tus credenciales

## 📝 Crear una Solicitud

### Solicitud de Medicamento POS (Simple)

1. Click en "Nueva Solicitud"
2. Seleccionar un medicamento POS (ej: "Acetaminofén 500mg")
3. Click en "Crear Solicitud"
4. ¡Listo!

### Solicitud de Medicamento NO POS (Con Datos Adicionales)

1. Click en "Nueva Solicitud"
2. Seleccionar un medicamento NO POS (ej: "Adalimumab 40mg")
3. Completar campos adicionales:
   - Número de orden: `ORD-2024-001`
   - Dirección: `Calle 123 #45-67, Bogotá`
   - Teléfono: `3001234567`
   - Correo: `tu@email.com`
4. Click en "Crear Solicitud"
5. ¡Listo!

## 🔧 Configuración Personalizada

### Cambiar Puerto del Backend

Editar `backend/src/main/resources/application.properties`:
```properties
server.port=8080  # Cambiar a tu puerto preferido
```

### Cambiar Credenciales de Base de Datos

Editar `backend/src/main/resources/application.properties`:
```properties
spring.datasource.username=tu_usuario
spring.datasource.password=tu_contraseña
```

### Cambiar URL del Backend en Frontend

Editar `frontend/src/services/api.js`:
```javascript
const API_BASE_URL = 'http://localhost:8080';  // Tu URL
```

## ❓ Problemas Comunes

### Backend no inicia

**Error**: `Cannot connect to database`

**Solución**:
```bash
# Verificar que MySQL esté corriendo
sudo systemctl status mysql
sudo systemctl start mysql

# Verificar credenciales en application.properties
```

### Frontend no carga

**Error**: `CORS error`

**Solución**: Verificar que el backend esté corriendo en el puerto correcto y que `cors.allowed-origins` en `application.properties` incluya la URL del frontend.

### Error 401 al hacer solicitudes

**Solución**: Cerrar sesión y volver a iniciar sesión. Las cookies de sesión deben estar habilitadas.

## 📊 Datos de Prueba Incluidos

El sistema viene con datos de prueba:

- **2 usuarios** (admin, usuario1)
- **18 medicamentos** (10 POS, 8 NO POS)
- **5 solicitudes de ejemplo**

## 🚀 Siguientes Pasos

1. Explorar el listado de solicitudes
2. Crear solicitudes de diferentes tipos
3. Revisar el código fuente
4. Leer el README.md completo para más detalles

## 📞 Soporte

Para más información, consulta el archivo `README.md` principal.
