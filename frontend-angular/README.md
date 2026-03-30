# Frontend Angular - Sistema de Gestión de Medicamentos

Frontend desarrollado en **Angular 20** para el Sistema de Gestión de Solicitudes de Medicamentos.

## 🚀 Características

- ✅ **Angular 20** con arquitectura standalone components
- ✅ **TailwindCSS** para estilos modernos
- ✅ **Autenticación con cookies HTTP-only** (sin localStorage/sessionStorage)
- ✅ **Reactive Forms** con validaciones
- ✅ **Lazy Loading** de componentes
- ✅ **Guards** para protección de rutas
- ✅ **Signals** para manejo de estado reactivo
- ✅ **HTTP Interceptors** para configuración global
- ✅ **Proxy configuration** para desarrollo

## 📁 Estructura del Proyecto

```
frontend-angular/
├── src/
│   ├── app/
│   │   ├── core/
│   │   │   ├── guards/
│   │   │   │   └── auth.guard.ts          # Guard de autenticación
│   │   │   ├── interceptors/
│   │   │   │   └── auth.interceptor.ts    # Interceptor HTTP
│   │   │   └── services/
│   │   │       ├── auth.service.ts        # Servicio de autenticación
│   │   │       └── solicitud.service.ts   # Servicio de solicitudes
│   │   ├── features/
│   │   │   ├── auth/
│   │   │   │   ├── login/
│   │   │   │   │   └── login.component.ts
│   │   │   │   └── register/
│   │   │   │       └── register.component.ts
│   │   │   └── solicitudes/
│   │   │       ├── lista-solicitudes/
│   │   │       │   └── lista-solicitudes.component.ts
│   │   │       └── nueva-solicitud/
│   │   │           └── nueva-solicitud.component.ts
│   │   ├── shared/
│   │   │   └── components/
│   │   │       └── navbar/
│   │   │           └── navbar.component.ts
│   │   ├── app.component.ts
│   │   └── app.routes.ts
│   ├── styles.css
│   ├── index.html
│   └── main.ts
├── angular.json
├── package.json
├── tailwind.config.js
├── proxy.conf.json
└── tsconfig.json
```

## 🔧 Requisitos Previos

- **Node.js** 20+ y **npm** 10+
- **Angular CLI** (se instalará con las dependencias)
- **Backend** corriendo en puerto 8181
- **Base de datos MySQL** (via Docker Compose)

## 📦 Instalación

```bash
cd frontend-angular

# Instalar dependencias
npm install
```

## ⚙️ Configuración

### Proxy para Desarrollo

El archivo `proxy.conf.json` está configurado para redirigir las peticiones al backend:

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

### Variables de Entorno

Los servicios están configurados para usar rutas relativas que el proxy redirige al backend:
- Auth API: `/auth/*`
- Solicitudes API: `/api/*`

## ▶️ Ejecución

### Modo Desarrollo

```bash
npm start
# o
ng serve
```

La aplicación estará disponible en: **http://localhost:4200**

### Build para Producción

```bash
npm run build
# o
ng build
```

Los archivos compilados estarán en `dist/frontend-angular/`

## 🔐 Autenticación sin localStorage/sessionStorage

Este frontend utiliza **cookies HTTP-only** para la autenticación, lo que proporciona mayor seguridad:

### Características de Seguridad

1. **Cookies HTTP-only**: Las cookies de sesión no son accesibles desde JavaScript
2. **withCredentials**: Todas las peticiones HTTP incluyen credenciales
3. **CORS configurado**: El backend permite peticiones desde el frontend
4. **Interceptor global**: Configura automáticamente `withCredentials: true`

### Flujo de Autenticación

```typescript
// 1. Login
authService.login({ username, password })
  → POST /auth/login
  → Backend crea sesión y envía cookie
  → Cookie se guarda automáticamente en el navegador

// 2. Peticiones autenticadas
solicitudService.getMySolicitudes()
  → GET /api/solicitudes
  → Cookie se envía automáticamente
  → Backend valida sesión

// 3. Logout
authService.logout()
  → POST /auth/logout
  → Backend invalida sesión
  → Cookie se elimina
```

## 🎨 Componentes Principales

### 1. Login Component
- Formulario de inicio de sesión
- Validación de campos
- Manejo de errores
- Redirección automática

### 2. Register Component
- Formulario de registro
- Validación de email y contraseña
- Confirmación de contraseña
- Manejo de errores del servidor

### 3. Nueva Solicitud Component
- Selector de medicamentos
- **Formulario condicional** para medicamentos NO POS
- Validaciones dinámicas
- Feedback visual de éxito

### 4. Lista Solicitudes Component
- Tabla paginada de solicitudes
- Badges de estado (PENDIENTE, APROBADA, RECHAZADA)
- Información adicional para NO POS
- Navegación entre páginas

### 5. Navbar Component
- Navegación principal
- Información del usuario autenticado
- Botón de logout
- Responsive design

## 🛡️ Guards y Protección de Rutas

```typescript
// auth.guard.ts
export const authGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  if (authService.isAuthenticated()) {
    return true;
  }

  router.navigate(['/login']);
  return false;
};
```

Rutas protegidas:
- `/solicitudes` - Lista de solicitudes
- `/solicitudes/nueva` - Nueva solicitud

## 📡 Servicios

### AuthService

```typescript
// Signals para estado reactivo
currentUser = signal<User | null>(null);
isAuthenticated = signal<boolean>(false);
loading = signal<boolean>(true);

// Métodos
register(data: RegisterRequest): Observable<AuthResponse>
login(data: LoginRequest): Observable<AuthResponse>
logout(): Observable<any>
checkAuth(): void
```

### SolicitudService

```typescript
getMedicamentos(): Observable<Medicamento[]>
createSolicitud(data: SolicitudRequest): Observable<SolicitudResponse>
getMySolicitudes(page: number, size: number): Observable<SolicitudesPageResponse>
```

## 🎯 Características Especiales

### 1. Formulario Condicional NO POS

El formulario de nueva solicitud muestra campos adicionales solo si el medicamento seleccionado es NO POS:

```typescript
@if (selectedMedicamento?.esNoPos) {
  <div class="border-t pt-6">
    <!-- Campos adicionales: numeroOrden, direccion, telefono, correo -->
  </div>
}
```

### 2. Signals para Estado Reactivo

```typescript
// En AuthService
currentUser = signal<User | null>(null);

// En componentes
{{ authService.currentUser()?.username }}
```

### 3. Lazy Loading

```typescript
{
  path: 'solicitudes',
  loadComponent: () => import('./features/solicitudes/lista-solicitudes/lista-solicitudes.component')
    .then(m => m.ListaSolicitudesComponent)
}
```

## 🔄 Integración con Backend

### Conexión con Backend Gradle

**Importante**: El backend corre en el puerto **8181**, no en el 8080.

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

### Endpoints Utilizados

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/auth/register` | Registro de usuario |
| POST | `/auth/login` | Inicio de sesión |
| POST | `/auth/logout` | Cerrar sesión |
| GET | `/auth/me` | Usuario actual |
| GET | `/api/medicamentos` | Listar medicamentos |
| POST | `/api/solicitudes` | Crear solicitud |
| GET | `/api/solicitudes?page=0&size=10` | Listar solicitudes |

## 🎨 Estilos con TailwindCSS

```css
/* styles.css */
@tailwind base;
@tailwind components;
@tailwind utilities;
```

Clases utilizadas:
- **Layout**: `flex`, `grid`, `container`
- **Spacing**: `p-4`, `m-2`, `space-x-4`
- **Colors**: `bg-emerald-600`, `text-gray-900` (tema verde esmeralda)
- **Typography**: `text-xl`, `font-bold`
- **Forms**: `rounded-md`, `shadow-sm`
- **States**: `hover:bg-emerald-700`, `disabled:opacity-50`

## 🧪 Testing

### Ejecutar Tests

```bash
# Ejecutar tests en modo watch
npm test
# o
ng test

# Ejecutar tests una sola vez
ng test --watch=false

# Ejecutar tests con cobertura
ng test --code-coverage
```

### Cobertura de Tests

El proyecto incluye pruebas unitarias para:

#### **Servicios**
- ✅ **AuthService** (auth.service.spec.ts)
  - Registro de usuarios
  - Inicio de sesión
  - Cierre de sesión
  - Verificación de autenticación
  - Manejo de signals
  - Manejo de errores

- ✅ **SolicitudService** (solicitud.service.spec.ts)
  - Obtener lista de medicamentos
  - Crear solicitud POS
  - Crear solicitud NO POS con validaciones
  - Obtener solicitudes paginadas
  - Manejo de errores

#### **Componentes**
- ✅ **LoginComponent** (login.component.spec.ts)
  - Inicialización del formulario
  - Validaciones de campos
  - Envío de formulario
  - Navegación exitosa
  - Manejo de errores de autenticación

- ✅ **RegisterComponent** (register.component.spec.ts)
  - Inicialización del formulario
  - Validaciones de campos
  - Validación de coincidencia de contraseñas
  - Envío de formulario
  - Navegación exitosa
  - Manejo de errores

### Estructura de Tests

```
src/app/
├── core/
│   └── services/
│       ├── auth.service.spec.ts
│       └── solicitud.service.spec.ts
└── features/
    └── auth/
        ├── login/
        │   └── login.component.spec.ts
        └── register/
            └── register.component.spec.ts
```

### Tecnologías de Testing

- **Jasmine**: Framework de testing
- **Karma**: Test runner
- **HttpClientTestingModule**: Para testing de peticiones HTTP
- **ReactiveFormsModule**: Para testing de formularios reactivos

### Comandos Útiles

```bash
# Tests en modo headless (CI/CD)
ng test --browsers=ChromeHeadless --watch=false

# Ver reporte de cobertura
ng test --code-coverage
# El reporte se genera en: coverage/frontend-angular/index.html
```

## 📝 Scripts Disponibles

```json
{
  "start": "ng serve",              // Desarrollo
  "build": "ng build",              // Build producción
  "watch": "ng build --watch",      // Build con watch
  "test": "ng test"                 // Tests unitarios
}
```

## 🌐 Navegación

```
/                       → Redirect a /solicitudes
/login                  → Página de login
/register               → Página de registro
/solicitudes            → Lista de solicitudes (protegida)
/solicitudes/nueva      → Nueva solicitud (protegida)
```

## 🔍 Diferencias con React Frontend

| Característica | React | Angular |
|----------------|-------|---------|
| Framework | React 18 + Vite | Angular 20 |
| Estado | Context API | Signals |
| Routing | React Router | Angular Router |
| Forms | Controlled Components | FormsModule |
| HTTP | Axios | HttpClient |
| Estilos | TailwindCSS | TailwindCSS |
| Autenticación | Cookies | Cookies |

## 🚀 Ventajas de Angular

1. **TypeScript nativo**: Mejor tipado y autocompletado
2. **Dependency Injection**: Gestión automática de dependencias
3. **RxJS integrado**: Manejo poderoso de streams asíncronos
4. **CLI robusto**: Generación de código y scaffolding
5. **Signals**: Estado reactivo moderno y eficiente
6. **Standalone Components**: Sin necesidad de NgModules

## 📚 Recursos

- [Angular Documentation](https://angular.io/docs)
- [Angular Signals](https://angular.io/guide/signals)
- [TailwindCSS](https://tailwindcss.com/docs)
- [RxJS](https://rxjs.dev/)

## ✅ Checklist de Funcionalidades

- ✅ Registro de usuarios
- ✅ Inicio de sesión
- ✅ Cierre de sesión
- ✅ Protección de rutas
- ✅ Listar medicamentos
- ✅ Crear solicitud POS
- ✅ Crear solicitud NO POS con campos adicionales
- ✅ Listar solicitudes con paginación
- ✅ Validaciones en formularios
- ✅ Manejo de errores
- ✅ Feedback visual
- ✅ Responsive design
- ✅ Autenticación con cookies HTTP-only

## 🎉 Resultado Final

Frontend Angular completamente funcional que:
- Se conecta al backend Gradle (`backend_java`) en puerto **8181**
- No utiliza localStorage ni sessionStorage
- Utiliza arquitectura moderna de Angular 20 con standalone components
- Implementa autenticación segura con cookies HTTP-only
- Tema de color **verde esmeralda** (emerald)
- Formularios reactivos con validaciones completas
- Validaciones condicionales para medicamentos NO POS
- Manejo de errores del backend con feedback visual
