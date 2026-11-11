# 🏍️ Guía de Exposición y Sustentación Técnica: Sistema Health & Byke

**Proyecto:** Health & Byke (MVP 1.0)  
**Tecnologías:** Java 17, Spring Boot 3, Spring Data JPA / Hibernate, MySQL / MariaDB (XAMPP), Maven.  
**Propósito del documento:** Servir como guía estructurada, conceptual y técnica para exponer con claridad qué se hizo en el proyecto, cómo se logró conectar y hacer funcional la base de datos, y cómo se aplican rigurosamente los principios de **Programación Orientada a Objetos (POO)** y arquitectura de software.

---

## 📌 Tabla de Contenidos
1. [Resumen Ejecutivo del Proyecto](#1-resumen-ejecutivo-del-proyecto)
2. [Paso a Paso: ¿Qué se hizo para conectar y hacer funcional el sistema?](#2-paso-a-paso-qué-se-hizo-para-conectar-y-hacer-funcional-el-sistema)
3. [Arquitectura del Sistema y Diagrama Relacional](#3-arquitectura-del-sistema-y-diagrama-relacional)
4. [Fundamentos de Programación Orientada a Objetos (POO) en el Código](#4-fundamentos-de-programación-orientada-a-objetos-poo-en-el-código)
   - [4.1 Abstracción](#41-abstracción)
   - [4.2 Encapsulamiento](#42-encapsulamiento)
   - [4.3 Herencia](#43-herencia)
   - [4.4 Polimorfismo](#44-polimorfismo)
5. [Conceptos Avanzados de Ingeniería de Software Aplicados](#5-conceptos-avanzados-de-ingeniería-de-software-aplicados)
   - [5.1 Inyección de Dependencias (DI) e Inversión de Control (IoC)](#51-inyección-de-dependencias-di-e-inversión-de-control-ioc)
   - [5.2 Arquitectura en Capas (Layered Architecture)](#52-arquitectura-en-capas-layered-architecture)
   - [5.3 Mapeo Objeto-Relacional (ORM / JPA)](#53-mapeo-objeto-relacional-orm--jpa)
6. [Doble Canal de Exposición: Menú Interactivo en Consola y API REST](#6-doble-canal-de-exposición-menú-interactivo-en-consola-y-api-rest)
7. [Guion Sugerido para la Exposición Oral](#7-guion-sugerido-para-la-exposición-oral)

---

## 1. Resumen Ejecutivo del Proyecto

**Health & Byke** es una solución de software orientada a la gestión integral del mantenimiento, desgaste de componentes, control de costos e historial técnico de motocicletas. Resuelve el problema común de los motociclistas que carecen de registro preventivo sobre cuándo realizaron su último servicio, aumentando el riesgo de fallas mecánicas y accidentes viales.

El sistema se compone de **8 entidades nucleares**:
- `User`: Propietario del vehículo.
- `TipoMoto`: Motocicleta registrada con especificaciones técnicas y kilometraje.
- `EstadoMoto`: Monitoreo del desgaste de componentes (frenos, kit de arrastre, etc.).
- `Recordatorio`: Notificaciones preventivas basadas en el desgaste.
- `UbicacionTaller`: Directorio de talleres mecánicos y especialistas.
- `Historial`: Trazabilidad cronológica de revisiones realizadas.
- `Arreglos`: Orden de trabajo / reparación realizada en la moto.
- `CostoArreglo`: Desglose financiero (mano de obra, repuestos y gastos adicionales).

---

## 2. Paso a Paso: ¿Qué se hizo para conectar y hacer funcional el sistema?

Para llevar el proyecto desde una estructura inicial con base de datos en memoria (H2) hacia un sistema conectado a base de datos persistente y ejecutable en IntelliJ con interacción directa, se ejecutaron los siguientes pasos técnicos:

### Paso 1: Diagnóstico y Configuración de XAMPP (MySQL)
- **Problema detectado:** El equipo tenía ocupado el puerto por defecto `3306` por un servicio previo (`MySQL80` de Windows). Al intentar iniciar MySQL desde XAMPP se generaba el error `10048 (Address already in use)`.
- **Solución técnica:** Se reconfiguró el servicio MySQL de XAMPP para operar en el puerto **`3307`** modificando `C:\xampp\mysql\bin\my.ini` y sincronizando `phpMyAdmin` (`config.inc.php`). Se creó la base de datos `healthandbykedb` con cotejamiento UTF-8.

### Paso 2: Conector Oficial MySQL y Estandarización de Maven
- Se agregó al archivo `pom.xml` la dependencia oficial:
  ```xml
  <dependency>
      <groupId>com.mysql</groupId>
      <artifactId>mysql-connector-j</artifactId>
      <scope>runtime</scope>
  </dependency>
  ```
- Se configuró `<pluginManagement>` para definir versiones explícitas y estables de los plugins de Maven (`maven-compiler-plugin` en Java 17, `clean`, `resources`, `surefire`), resolviendo el problema donde los plugins aparecían subrayados en rojo en IntelliJ IDEA.
- Se fijó el SDK en IntelliJ IDEA a **Java 17 (LTS)**, resolviendo el error `Process finished with exit code 1` que ocurría al intentar compilar bajo Java 25.

### Paso 3: Configuración de Persistencia en `application.properties`
Se configuró Spring Data JPA e Hibernate en `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3307/healthandbykedb?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
spring.datasource.driverClassName=com.mysql.cj.jdbc.Driver
spring.datasource.username=root
spring.datasource.password=
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```
> `ddl-auto=update` permite que Hibernate lea las clases anotadas con `@Entity` y genere automáticamente en MySQL las tablas, columnas, tipos de datos, llaves primarias y restricciones de llave foránea (`FOREIGN KEY`).

### Paso 4: Conexión y Mapeo Relacional de Clases Faltantes
Se conectaron las entidades según el diseño del dominio:
- **`TipoMoto` ↔ `User`:** Se agregó la relación `@ManyToOne` para asociar la moto con su usuario propietario.
- **`EstadoMoto` ↔ `TipoMoto`:** Se vinculó el desgaste de componentes directamente con el vehículo evaluado.
- **`Historial` ↔ `UbicacionTaller`:** Se vinculó el taller físico donde se efectuó el mantenimiento.
- **`CostoArreglo` ↔ `Arreglos`:** Se mapeó una relación `@OneToOne` con `@MapsId`, garantizando que el costo comparta la misma llave primaria del arreglo asociado.
- Se implementó `@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})` en los modelos para evitar excepciones de serialización causadas por los proxies de carga perezosa de Hibernate.

### Paso 5: Corrección de Métodos `update` y Manejo Transaccional en Servicios
- Los métodos `update(int id, Entity e)` carecían de asignación del identificador antes de invocar `save()`, lo que provocaba que un `PUT` creara filas duplicadas en lugar de actualizar. Se corrigieron los 8 servicios.
- Se implementó la resolución automática de entidades foráneas: antes de guardar un objeto con relaciones (ej. una moto con un ID de usuario), el servicio consulta el repositorio padre (`userRepository.findById(...)`) para adjuntar la entidad gestionada y evitar violaciones de integridad referencial.
- En `CostoArregloServiceImpl`, se incorporó `@Transactional` y soporte de `EntityManager` (`persist` y `merge`) para gestionar limpiamente entidades con llave compartida (`@MapsId`).

### Paso 6: Implementación del Menú Interactivo en Consola (`ConsoleMenuRunner`)
Para permitir la interacción directa al ejecutar el proyecto sin depender exclusivamente de clientes HTTP como Postman o cURL, se diseñó un componente interactivo `ConsoleMenuRunner.java` que ejecuta un CLI navegable en un hilo independiente, permitiendo crear, listar, consultar, editar y eliminar datos de todas las entidades desde la consola de IntelliJ IDEA.

---

## 3. Arquitectura del Sistema y Diagrama Relacional

### Relaciones entre Entidades:
1. `User` (1) ── (N) `TipoMoto`: Un usuario puede tener registradas varias motocicletas.
2. `TipoMoto` (1) ── (N) `Arreglos`: Una motocicleta acumula múltiples órdenes de servicio o reparaciones.
3. `TipoMoto` (1) ── (N) `EstadoMoto`: Una moto tiene registros del estado y desgaste de sus piezas.
4. `Arreglos` (1) ── (1) `CostoArreglo`: Cada reparación tiene un desglose financiero exclusivo con la misma llave primaria.
5. `EstadoMoto` (1) ── (N) `Recordatorio`: El estado de las piezas genera recordatorios y alertas automáticas.
6. `Recordatorio` (1) ── (N) `Historial`: Las alertas concluyen registrándose en el historial de mantenimiento.
7. `UbicacionTaller` (1) ── (N) `Historial`: Un taller atiende múltiples mantenimientos del historial.

---

## 4. Fundamentos de Programación Orientada a Objetos (POO) en el Código

### 4.1 Abstracción

> **Definición:** La abstracción consiste en aislar las características esenciales de un objeto del mundo real para representarlo en el sistema, ignorando los detalles accesorios o irrelevantes. También se manifiesta al definir **contratos** (interfaces) que determinan *qué hace* un componente sin obligar a conocer *cómo lo hace internamente*.

#### ¿Dónde se aplica en el proyecto?

1. **Abstracción del Dominio (Modelos):**
   - En la clase `TipoMoto.java`: Una motocicleta real tiene millones de variables físicas (presión de neumáticos, temperatura del escape, color de tornillos, etc.). El sistema abstrae únicamente los atributos requeridos por la lógica de negocio: `marcaMoto`, `placa`, `odometroActual`, `cilindrajeMoto` y `user`.

2. **Abstracción de Servicios (Interfaces):**
   - En `UserService.java`:
     ```java
     public interface UserService {
         void create(User entity);
         List<User> selectAll();
         User selectById(int id);
         void update(int id, User entity);
         void delete(int id);
     }
     ```
     La interfaz expone el contrato de operaciones de usuario. Los controladores y consumidores no necesitan saber si los datos se guardan en MySQL, en archivos o en memoria.

3. **Abstracción de la Capa de Datos (Spring Data JPA):**
   - En `UserRepository.java`:
     ```java
     @Repository
     public interface UserRepository extends JpaRepository<User, Integer> {
     }
     ```
     Abstrae por completo el lenguaje SQL (`SELECT`, `INSERT`, `UPDATE`, `DELETE`, transacciones JDBC, conexiones). El desarrollador interactúa con métodos de alto nivel sin escribir consultas nativas a bajo nivel.

---

### 4.2 Encapsulamiento

> **Definición:** El encapsulamiento es el principio de agrupar datos (atributos) y comportamientos (métodos) dentro de una misma unidad lógica, protegiendo el estado interno del objeto mediante modificadores de acceso (`private`) y permitiendo la lectura o modificación exclusivamente a través de canales controlados (Getters, Setters y métodos de negocio).

#### ¿Dónde se aplica en el proyecto?

1. **Ocultamiento de Información (Information Hiding):**
   - En todas las clases del paquete `models` (por ejemplo `User.java`):
     ```java
     public class User {
         @Id
         @GeneratedValue(strategy = GenerationType.IDENTITY)
         private Integer userId;
         
         @Column(nullable = false, length = 100)
         private String lastName;

         @Column(nullable = false, length = 100)
         private String name;

         // Métodos accesores controlados
         public Integer getUserId() { return userId; }
         public void setUserId(Integer userId) { this.userId = userId; }
         public String getName() { return name; }
         public void setName(String name) { this.name = name; }
     }
     ```
     Ningún componente externo puede alterar directamente `userId` o `name` mediante asignación directa (`user.name = "Pedro"` no es permitido; se exige `user.setName("Pedro")`).

2. **Encapsulamiento de Reglas de Negocio en la Entidad:**
   - En `CostoArreglo.java`:
     ```java
     public double getTotalCosto() {
         return this.manoDeObra + this.costoRepuestos + this.otrosGastos;
     }
     ```
     El cálculo del costo total está encapsulado en la propia entidad. Si la fórmula cambia (por ejemplo, para incluir impuestos o descuentos), solo se modifica este método y todo el sistema mantendrá la coherencia.

3. **Encapsulamiento de Validaciones Auxiliares:**
   - En `DateValidator.java`:
     ```java
     public class DateValidator {
         public static boolean isValidDate(LocalDate date) {
             return date != null && !date.isAfter(LocalDate.now());
         }
     }
     ```
     Encapsula la regla que valida que una fecha de mantenimiento no sea nula ni futura.

---

### 4.3 Herencia

> **Definición:** Mecanismo mediante el cual una clase o interfaz adquiere o extiende los atributos, métodos y comportamientos de otra clase o interfaz base, fomentando la reutilización de código y la jerarquía de tipos.

#### ¿Dónde se aplica en el proyecto?

1. **Herencia de Interfaces en Repositorios (Framework JPA):**
   - En `TipoMotoRepository.java`:
     ```java
     public interface TipoMotoRepository extends JpaRepository<TipoMoto, Integer> {
     }
     ```
     `TipoMotoRepository` **hereda** toda la jerarquía de Spring Data:
     ```
     Repository (Marcador raíz)
         └── CrudRepository (save, findById, deleteById, count, ...)
                 └── ListPagingAndSortingRepository (paginación y ordenamiento)
                         └── JpaRepository (flush, saveAndFlush, findAll con listas tipadas)
                                 └── TipoMotoRepository
     ```
     Gracias a esta herencia, no fue necesario escribir ni una sola línea de código para tener disponibles métodos como `findAll()`, `findById()`, `save()`, `deleteById()`.

2. **Implementación de Contratos Estándar de Spring:**
   - En `ConsoleMenuRunner.java`:
     ```java
     public class ConsoleMenuRunner implements CommandLineRunner
     ```
     Hereda/implementa el contrato de `CommandLineRunner`, lo que le permite acoplarse al ciclo de vida del contenedor Spring e invocar su método `run()` al iniciar la aplicación.
   - En `WebMvcConfig.java`:
     ```java
     public class WebMvcConfig implements WebMvcConfigurer
     ```
     Extiende el comportamiento estándar de Spring MVC para configurar CORS.

---

### 4.4 Polimorfismo

> **Definición:** Propiedad que permite que una misma operación, mensaje o referencia adopte múltiples formas y comportamientos en tiempo de ejecución, según el tipo específico del objeto que la invoque.

#### ¿Dónde se aplica en el proyecto?

1. **Polimorfismo por Subtipado (Interfaces y Clases Concretas):**
   - En `UserController.java`:
     ```java
     @RestController
     public class UserController {
         @Autowired
         private UserService service; // Referencia a la interfaz abstracta
     ```
     El controlador interactúa con la variable polimórfica `UserService`. En tiempo de ejecución, Spring inyecta la instancia concreta `UserServiceImpl`. Si en el futuro se implementa una clase alternativa como `UserMockServiceImpl` o `UserCloudServiceImpl`, el controlador funcionará sin requerir ninguna modificación.

2. **Sobreescritura de Métodos (Method Overriding / `@Override`):**
   - En `TipoMotoServiceImpl.java`:
     ```java
     @Override
     public void create(TipoMoto entity) {
         resolveRelations(entity);
         repository.save(entity);
     }
     ```
     La clase concreta sobreescribe y provee la implementación específica del método `create` definido originalmente en la interfaz `TipoMotoService`.

3. **Polimorfismo Paramétrico (Genéricos de Java - Generics):**
   - En la interfaz `JpaRepository<T, ID>`:
     - `UserRepository extends JpaRepository<User, Integer>`
     - `ArreglosRepository extends JpaRepository<Arreglos, Integer>`
     - `HistorialRepository extends JpaRepository<Historial, Integer>`
     El mismo código genérico de persistencia se comporta de manera polimórfica con respecto al tipo de entidad (`T`) y al tipo de su identificador (`ID`).

4. **Sobrecarga de Métodos y Constructores (Overloading):**
   - En `User.java`:
     ```java
     // Constructor por defecto (requerido por JPA / reflexión)
     public User() {}

     // Constructor sobrecargado con parámetros de inicialización
     public User(String lastName, String name, String email, String phoneNumber, LocalDate date, String typeBike) {
         this.lastName = lastName;
         this.name = name;
         this.email = email;
         this.phoneNumber = phoneNumber;
         this.date = date;
         this.typeBike = typeBike;
     }
     ```
     Mismo nombre de constructor con diferente firma y cantidad de argumentos.

---

## 5. Conceptos Avanzados de Ingeniería de Software Aplicados

### 5.1 Inyección de Dependencias (DI) e Inversión de Control (IoC)

- **Inversión de Control (IoC):** En lugar de que las clases creen manualmente sus propias dependencias usando `new` (por ejemplo, `new UserRepository()`), el control de instanciación se transfiere al **Contenedor IoC de Spring** (`ApplicationContext`).
- **Inyección de Dependencias (DI):** Spring inyecta las instancias requeridas mediante la anotación `@Autowired`:
  ```java
  @Service
  public class TipoMotoServiceImpl implements TipoMotoService {
      @Autowired
      private TipoMotoRepository repository;

      @Autowired
      private UserRepository userRepository;
  ```
  **Beneficio:** Desacopla componentes, simplifica pruebas unitarias y cumple con el principio de Inversión de Dependencias (DIP) de SOLID.

---

### 5.2 Arquitectura en Capas (Layered Architecture)

El proyecto está estructurado estrictamente en 4 capas desacopladas:

1. **Capa de Controladores / Consola (`controllers`, `console`):** Recibe solicitudes del usuario (HTTP JSON o teclado en consola), valida formato básico y delega la ejecución a los servicios.
2. **Capa de Negocio / Servicios (`services`, `services.impl`):** Contiene las reglas del negocio, validación de integridad referencial, cálculo de costos y transaccionalidad.
3. **Capa de Persistencia (`repositories`):** Gestiona la comunicación directa con la base de datos a través de Spring Data JPA.
4. **Capa de Entidades / Dominio (`models`):** Define el modelo relacional mapeado a objetos Java (`@Entity`, `@Table`, `@Column`, `@ManyToOne`, `@OneToOne`).

---

### 5.3 Mapeo Objeto-Relacional (ORM / JPA)

El **Mapeo Objeto-Relacional (ORM)** elimina la discrepancia de impedancia entre el paradigma orientado a objetos (clases, referencias, tipos) y el modelo relacional SQL (tablas, filas, llaves foráneas).

| Anotación JPA | Propósito en el Código | Ejemplo en el Proyecto |
|---|---|---|
| `@Entity` | Declara que la clase Java representa una tabla en MySQL. | `@Entity public class User` |
| `@Table(name = "...")` | Asigna el nombre físico de la tabla en MySQL. | `@Table(name = "tipo_moto")` |
| `@Id` | Marca el campo como Clave Primaria (Primary Key). | `@Id private Integer tipoMotoId;` |
| `@GeneratedValue` | Configura el autoincremento delegado al motor de BD (`AUTO_INCREMENT`). | `strategy = GenerationType.IDENTITY` |
| `@ManyToOne` | Modela relaciones Muchos a Uno (Llave foránea en la tabla hija). | `TipoMoto` contiene `@ManyToOne private User user;` |
| `@OneToOne` y `@MapsId` | Modela relaciones Uno a Uno donde la clave primaria es compartida. | `CostoArreglo` hereda la misma PK de `Arreglos`. |

---

## 6. Doble Canal de Exposición: Menú Interactivo en Consola y API REST

El proyecto cuenta con **dos mecanismos simultáneos de interacción**:

### Canal 1: Menú Interactivo por Consola (CLI)
- Al pulsar **Run (▶️)** en IntelliJ IDEA, el componente `ConsoleMenuRunner` lanza un submenú que permite realizar el ciclo completo CRUD (Crear, Listar, Consultar por ID, Editar y Eliminar) para cada una de las 8 entidades.
- **Ventaja pedagógica:** Permite demostrar al docente o evaluador la funcionalidad inmediata del sistema directamente desde la ventana de ejecución de IntelliJ sin requerir herramientas externas.

### Canal 2: API REST (Endpoints Web)
- El servidor embebido Apache Tomcat permanece activo en el puerto `8080`.
- Soporta peticiones HTTP estándar (`GET`, `POST`, `PUT`, `DELETE`) en formato JSON.
- Cuenta con CORS habilitado (`@CrossOrigin(origins = "*")`), lo que permite conectar de inmediato un frontend en React, Angular, Vue o una aplicación móvil.

---

## 7. Guion Sugerido para la Exposición Oral

Puedes estructurar tu sustentación en 4 momentos clave:

1. **Introducción y Problema (1 minuto):**
   > *"Buenos días/tardes. El proyecto Health & Byke es una solución diseñada para gestionar el ciclo de vida y mantenimiento preventivo de motocicletas, evitando accidentes y costos imprevistos por falta de seguimiento a repuestos y revisiones."*

2. **Arquitectura y Conexión con XAMPP (2 minutos):**
   > *"El sistema está construido en Java 17 con Spring Boot y Spring Data JPA. Solucionamos el conflicto de puertos locales configurando MySQL en el puerto 3307 de XAMPP. La base de datos 'healthandbykedb' sincroniza sus 8 tablas automáticamente gracias al motor ORM Hibernate."*

3. **Principios de Programación Orientada a Objetos (3 minutos):**
   > *"La aplicación aplica con rigor los 4 pilares de la POO:*
   > - ***Abstracción:*** *A través de interfaces de servicio y repositorios JPA que aíslan los contratos de la implementación.*
   > - ***Encapsulamiento:*** *Todos los atributos son privados con getters y setters, y cálculos como el costo total están encapsulados en la entidad.*
   > - ***Herencia:*** *Nuestros repositorios heredan toda la jerarquía de JpaRepository, y los runners extienden componentes del framework.*
   > - ***Polimorfismo:*** *Los controladores inyectan interfaces abstractas y no clases concretas, utilizando sobreescritura (@Override) y genéricos de Java."*

4. **Demostración en Vivo (2 minutos):**
   > *"Al ejecutar el código en IntelliJ, se levanta el servidor web en el puerto 8080 y al mismo tiempo se activa un Menú Interactivo por consola. Vamos a registrar un usuario, una motocicleta asociada y un arreglo mecánico, comprobando inmediatamente su persistencia en la base de datos MySQL de XAMPP y en phpMyAdmin."*
