# Health & Byke - Backend MVP

Sistema de Gestión, Mantenimiento e Historial de Motocicletas desarrollado con **Spring Boot 3 (Java 17)** y conectado a **MySQL / MariaDB en XAMPP**.

---

## 🎮 Menú Interactivo en Consola (Al ejecutar el proyecto)

Al ejecutar el proyecto (desde IntelliJ IDEA o terminal), el sistema despliega automáticamente un **Menú Interactivo de Consola (CLI)** en la pestaña **Run** de IntelliJ. 

Desde este menú puedes **Agregar**, **Consultar**, **Editar** y **Eliminar** datos en tiempo real de todas las entidades:

```text
====================================================================
           🏍️  SISTEMA HEALTH & BYKE - MENÚ PRINCIPAL 🏍️           
====================================================================
 1. 👤 Gestión de Usuarios (Propietarios)
 2. 🏍️  Gestión de Motocicletas (TipoMoto)
 3. ⚙️  Gestión de Estado de Motos / Piezas (EstadoMoto)
 4. ⏰  Gestión de Recordatorios (Alertas de Repuestos)
 5. 🏬  Gestión de Ubicación de Talleres y Mecánicos
 6. 📋  Gestión de Historial de Mantenimientos
 7. 🔧  Gestión de Arreglos y Reparaciones
 8. 💰  Gestión de Costos de Arreglo (Mano de Obra / Repuestos)
 9. 🌐  Pausar menú de consola (Dejar solo API Web activa)
 0. ❌  Salir de la Aplicación
====================================================================
```

Cada módulo cuenta con su propio submenú:
- **1. Listar todos:** Consulta y lista los registros existentes en MySQL.
- **2. Consultar por ID:** Muestra la información completa y sus relaciones.
- **3. Agregar nuevo:** Te pide los datos campo por campo con validaciones (y te lista las opciones disponibles para vincular, por ejemplo para asignar un usuario a una moto).
- **4. Editar existente:** Muestra el valor actual y te permite escribir uno nuevo o presionar `[ENTER]` para conservarlo.
- **5. Eliminar:** Pide confirmación y lo borra de la base de datos.
- **0. Volver:** Regresa al menú principal.

*(Al mismo tiempo, el servidor web REST sigue activo en `http://localhost:8080`).*

---

## 🚀 Conexión con XAMPP

### 1. Estado de la Base de Datos
- **Base de datos:** `healthandbykedb`
- **Host:** `localhost`
- **Puerto:** `3307` *(configurado en `C:\xampp\mysql\bin\my.ini` y phpMyAdmin para evitar colisión con el servicio nativo de Windows `MySQL80` que ocupaba el puerto 3306)*.
- **Usuario:** `root`
- **Contraseña:** *(vacía por defecto en XAMPP)*
- **Panel phpMyAdmin:** [http://localhost/phpmyadmin/](http://localhost/phpmyadmin/)

> **Nota:** Si en algún momento decides detener el servicio `MySQL80` de Windows y usar el puerto estándar `3306` en XAMPP, simplemente edita `src/main/resources/application.properties` y cambia `3307` por `3306`.

---

## 🛠️ Cómo Ejecutar el Proyecto

### Opción A: Desde Terminal (PowerShell / CMD)
Utilizando el Maven Wrapper incluido en el proyecto:
```powershell
.\mvnw.cmd spring-boot:run
```
*(O en sistemas Unix / Git Bash:* `./mvnw spring-boot:run`*)*

### Opción B: Desde IntelliJ IDEA
1. Abre la carpeta `HealthAndByke_Full_App` en IntelliJ IDEA.
2. Espera a que Maven sincronice las dependencias del `pom.xml`.
3. Ejecuta la clase principal [`HealthAndBykeApplication.java`](file:///c:/Users/yojan/OneDrive/Escritorio/HealthAndByke_Full_App/src/main/java/com/healthandbyke/HealthAndBykeApplication.java).

El servidor iniciará en: **`http://localhost:8080`**.

---

## 🔗 Conexión de Clases y Relaciones del Dominio

Todas las entidades están interconectadas de manera coherente según el diagrama y los requerimientos del MVP:

```
[User] (Propietario)
   │ 1
   ▼ N
[TipoMoto] (Vehículo / Motocicleta)
   │ 1                                   │ 1
   ▼ N                                   ▼ N
[Arreglos] (Mantenimientos/Reparaciones)  [EstadoMoto] (Estado de Piezas/Desgaste)
   │ 1                                   │ 1
   ▼ 1                                   ▼ N
[CostoArreglo]                           [Recordatorio] (Alertas de Repuestos)
                                         │ 1
                                         ▼ N
                                  [Historial] ◀─── [UbicacionTaller] (Taller y Mecánico)
```

1. **`User`** (`/api/users`): Representa al usuario / propietario de la motocicleta.
2. **`TipoMoto`** (`/api/tipomotos`): Motocicleta registrada con placa, modelo, odómetro y asociada a su propietario (`User`).
3. **`EstadoMoto`** (`/api/estadomotos`): Evaluación del estado de piezas y desgaste, asociada a la motocicleta (`TipoMoto`).
4. **`Recordatorio`** (`/api/recordatorios`): Notificaciones y alertas generadas a partir del estado de las piezas (`EstadoMoto`).
5. **`UbicacionTaller`** (`/api/ubicaciontallers` o `/api/talleres`): Información del taller, mecánico y especialidad.
6. **`Historial`** (`/api/historial`): Registro histórico del servicio, asociando el `Recordatorio` y el `UbicacionTaller`.
7. **`Arreglos`** (`/api/arreglos`): Reparaciones y servicios aplicados sobre la motocicleta (`TipoMoto`).
8. **`CostoArreglo`** (`/api/costoarreglos`): Detalle financiero de mano de obra, repuestos y otros gastos, vinculado uno a uno (`@MapsId`) con `Arreglos`.

---

## 📡 Endpoints de la API REST

Todos los endpoints soportan operaciones CRUD (`GET`, `POST`, `PUT`, `DELETE`) y tienen habilitado CORS (`@CrossOrigin`) para cualquier frontend.

| Módulo | Métodos Soportados | Rutas Principales |
|---|---|---|
| **Usuarios** | `GET`, `POST`, `PUT`, `DELETE` | `/api/users`, `/api/users/{id}` |
| **Motos** | `GET`, `POST`, `PUT`, `DELETE` | `/api/tipomotos`, `/api/tipomotos/{id}` |
| **Estado Piezas** | `GET`, `POST`, `PUT`, `DELETE` | `/api/estadomotos`, `/api/estadomotos/{id}` |
| **Recordatorios** | `GET`, `POST`, `PUT`, `DELETE` | `/api/recordatorios`, `/api/recordatorios/{id}` |
| **Talleres** | `GET`, `POST`, `PUT`, `DELETE` | `/api/talleres`, `/api/ubicaciontallers` |
| **Historial** | `GET`, `POST`, `PUT`, `DELETE` | `/api/historial`, `/api/historial/{id}` |
| **Arreglos** | `GET`, `POST`, `PUT`, `DELETE` | `/api/arreglos`, `/api/arreglos/{id}` |
| **Costo Arreglo** | `GET`, `POST`, `PUT`, `DELETE` | `/api/costoarreglos`, `/api/costoarreglos/{id}` |

---

## 🧪 Ejemplos de Peticiones JSON (POST)

### 1. Registrar Usuario (`POST /api/users`)
```json
{
  "name": "Carlos",
  "lastName": "Gomez",
  "email": "carlos.gomez@gmail.com",
  "phoneNumber": "3112233445",
  "date": "2026-03-15",
  "typeBike": "Yamaha MT-09"
}
```

### 2. Registrar Motocicleta vinculada a Usuario (`POST /api/tipomotos`)
```json
{
  "marcaMoto": "Yamaha",
  "tipoUso": "Deportivo",
  "categoriaMoto": "Naked",
  "cilindrajeMoto": "890cc",
  "modeloMoto": "2024",
  "placa": "MT999X",
  "odometroActual": 5000,
  "user": { "userId": 1 }
}
```

### 3. Registrar Estado de Piezas (`POST /api/estadomotos`)
```json
{
  "piezasMoto": "Pastillas de Freno",
  "estadoPiezas": "Desgaste medio",
  "reparacionesMoto": "Revision general",
  "fechaReparacion": "2026-09-01",
  "kilometrosEstimadosUso": 4000,
  "moto": { "tipoMotoId": 1 }
}
```

### 4. Crear Recordatorio (`POST /api/recordatorios`)
```json
{
  "notificacionR": "Cambio preventivo de pastillas",
  "estadoRepuesto": "Pendiente compra",
  "descripcionRecordatorio": "Pastillas al 30%",
  "estadoMoto": { "estadoId": 1 }
}
```

### 5. Registrar Taller (`POST /api/talleres`)
```json
{
  "municipioTaller": "Medellin",
  "barrioTaller": "Poblado",
  "direccionTaller": "Cra 43A # 10-20",
  "nombreMecanico": "Mario Hernandez",
  "especialidadTaller": "Frenos y suspension",
  "telefonoTaller": "3009988776"
}
```

### 6. Registrar en Historial (`POST /api/historial`)
```json
{
  "fechaHistorial": "2026-10-15",
  "nomTaller": "Taller Central",
  "tipoMantenimiento": "Preventivo",
  "descripcionMantenimiento": "Revision de frenos completada",
  "recordatorio": { "recordatorioId": 1 },
  "taller": { "tallerId": 1 }
}
```

### 7. Registrar Arreglo (`POST /api/arreglos`)
```json
{
  "fechaArreglo": "2026-10-20",
  "descripcionArreglos": "Sincronizacion de motor",
  "estadoArreglo": "COMPLETADO",
  "kilometrajeMomento": 5500,
  "moto": { "tipoMotoId": 1 }
}
```

### 8. Registrar Costo del Arreglo (`POST /api/costoarreglos`)
```json
{
  "arreglosId": 1,
  "manoDeObra": 85000,
  "costoRepuestos": 140000,
  "otrosGastos": 15000,
  "arreglos": { "arreglosId": 1 }
}
```
*(Calcula `totalCosto` automáticamente en las respuestas).*
# health-and-byke-full-app
