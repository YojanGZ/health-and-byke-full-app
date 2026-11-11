package com.healthandbyke.console;

import com.healthandbyke.enums.EstadoArregloEnum;
import com.healthandbyke.models.*;
import com.healthandbyke.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

@Component
public class ConsoleMenuRunner implements CommandLineRunner {

    @Autowired
    private UserService userService;

    @Autowired
    private TipoMotoService tipoMotoService;

    @Autowired
    private EstadoMotoService estadoMotoService;

    @Autowired
    private RecordatorioService recordatorioService;

    @Autowired
    private UbicacionTallerService tallerService;

    @Autowired
    private HistorialService historialService;

    @Autowired
    private ArreglosService arreglosService;

    @Autowired
    private CostoArregloService costoArregloService;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public void run(String... args) {
        // Ejecutar el menú en un hilo separado para no bloquear el inicio de Spring Boot
        Thread consoleThread = new Thread(this::startInteractiveMenu, "HealthAndByke-ConsoleMenu");
        consoleThread.setDaemon(false);
        consoleThread.start();
    }

    private void startInteractiveMenu() {
        try {
            // Esperar brevemente a que terminen los logs de inicio de Spring Boot
            Thread.sleep(700);
        } catch (InterruptedException ignored) {}

        try (Scanner sc = new Scanner(System.in)) {
            boolean running = true;
            while (running) {
                printMainMenu();
                int option = readInt(sc, "Seleccione una opción: ", -1);

                switch (option) {
                    case 1 -> menuUsuarios(sc);
                    case 2 -> menuMotos(sc);
                    case 3 -> menuEstadoMoto(sc);
                    case 4 -> menuRecordatorios(sc);
                    case 5 -> menuTalleres(sc);
                    case 6 -> menuHistorial(sc);
                    case 7 -> menuArreglos(sc);
                    case 8 -> menuCostoArreglo(sc);
                    case 9 -> {
                        System.out.println("\n🌐 Modo solo Servidor Web activado.");
                        System.out.println("El servidor REST sigue escuchando en http://localhost:8080");
                        System.out.println("Presione [ENTER] en cualquier momento para volver al menú de consola...");
                        sc.nextLine();
                    }
                    case 0 -> {
                        System.out.println("\n👋 Cerrando Health & Byke. ¡Hasta pronto!");
                        running = false;
                        System.exit(0);
                    }
                    default -> System.out.println("❌ Opción inválida. Intente de nuevo.\n");
                }
            }
        } catch (NoSuchElementException e) {
            System.out.println("\n[Consola] Entrada estándar finalizada o no interactiva. El servidor web continúa activo.");
        } catch (Exception e) {
            System.out.println("\n⚠️ Error en la consola: " + e.getMessage());
        }
    }

    private void printMainMenu() {
        System.out.println("\n====================================================================");
        System.out.println("           🏍️  SISTEMA HEALTH & BYKE - MENÚ PRINCIPAL 🏍️           ");
        System.out.println("====================================================================");
        System.out.println(" 1. 👤 Gestión de Usuarios (Propietarios)");
        System.out.println(" 2. 🏍️  Gestión de Motocicletas (TipoMoto)");
        System.out.println(" 3. ⚙️  Gestión de Estado de Motos / Piezas (EstadoMoto)");
        System.out.println(" 4. ⏰  Gestión de Recordatorios (Alertas de Repuestos)");
        System.out.println(" 5. 🏬  Gestión de Ubicación de Talleres y Mecánicos");
        System.out.println(" 6. 📋  Gestión de Historial de Mantenimientos");
        System.out.println(" 7. 🔧  Gestión de Arreglos y Reparaciones");
        System.out.println(" 8. 💰  Gestión de Costos de Arreglo (Mano de Obra / Repuestos)");
        System.out.println(" 9. 🌐  Pausar menú de consola (Dejar solo API Web activa)");
        System.out.println(" 0. ❌  Salir de la Aplicación");
        System.out.println("====================================================================");
    }

    // =========================================================================
    // 1. GESTIÓN DE USUARIOS
    // =========================================================================
    private void menuUsuarios(Scanner sc) {
        boolean back = false;
        while (!back) {
            System.out.println("\n------------------------------------------------");
            System.out.println("             👤 GESTIÓN DE USUARIOS             ");
            System.out.println("------------------------------------------------");
            System.out.println("1. Listar todos los usuarios");
            System.out.println("2. Consultar usuario por ID");
            System.out.println("3. Agregar nuevo usuario");
            System.out.println("4. Editar usuario existente");
            System.out.println("5. Eliminar usuario");
            System.out.println("0. Volver al menú principal");
            int opt = readInt(sc, "Seleccione una opción: ", -1);

            switch (opt) {
                case 1 -> listarUsuarios();
                case 2 -> {
                    int id = readInt(sc, "Ingrese el ID del usuario: ", -1);
                    User u = userService.selectById(id);
                    if (u != null) {
                        mostrarUsuario(u);
                    } else {
                        System.out.println("⚠️ Usuario no encontrado con ID: " + id);
                    }
                }
                case 3 -> agregarUsuario(sc);
                case 4 -> editarUsuario(sc);
                case 5 -> eliminarUsuario(sc);
                case 0 -> back = true;
                default -> System.out.println("❌ Opción inválida.");
            }
        }
    }

    private void listarUsuarios() {
        List<User> list = userService.selectAll();
        System.out.println("\n📋 Lista de Usuarios (" + list.size() + " registrados):");
        if (list.isEmpty()) {
            System.out.println("  (No hay usuarios registrados)");
            return;
        }
        for (User u : list) {
            System.out.printf("  • [ID: %d] %s %s | Email: %s | Tel: %s | Moto: %s | Fecha: %s\n",
                    u.getUserId(), u.getName(), u.getLastName(), u.getEmail(),
                    u.getPhoneNumber() != null ? u.getPhoneNumber() : "N/A",
                    u.getTypeBike() != null ? u.getTypeBike() : "N/A",
                    u.getDate());
        }
    }

    private void mostrarUsuario(User u) {
        System.out.println("\n--- Detalle del Usuario ---");
        System.out.println("  ID:           " + u.getUserId());
        System.out.println("  Nombre:       " + u.getName());
        System.out.println("  Apellido:     " + u.getLastName());
        System.out.println("  Email:        " + u.getEmail());
        System.out.println("  Teléfono:     " + u.getPhoneNumber());
        System.out.println("  Tipo de Moto: " + u.getTypeBike());
        System.out.println("  Fecha Reg.:   " + u.getDate());
    }

    private void agregarUsuario(Scanner sc) {
        System.out.println("\n➕ Agregar Nuevo Usuario:");
        String name = readString(sc, "Nombre: ", false, null);
        String lastName = readString(sc, "Apellido: ", false, null);
        String email = readString(sc, "Email: ", false, null);
        String phone = readString(sc, "Teléfono: ", true, "");
        LocalDate date = readDate(sc, "Fecha (AAAA-MM-DD) [Enter para hoy]: ", LocalDate.now());
        String typeBike = readString(sc, "Tipo o Modelo de moto de preferencia: ", true, "");

        User user = new User(lastName, name, email, phone, date, typeBike);
        try {
            userService.create(user);
            System.out.println("✅ Usuario registrado exitosamente con ID: " + user.getUserId());
        } catch (Exception e) {
            System.out.println("❌ Error al guardar usuario: " + e.getMessage());
        }
    }

    private void editarUsuario(Scanner sc) {
        int id = readInt(sc, "Ingrese el ID del usuario a editar: ", -1);
        User u = userService.selectById(id);
        if (u == null) {
            System.out.println("⚠️ Usuario no encontrado con ID: " + id);
            return;
        }
        System.out.println("\n✏️ Editando Usuario #" + id + " (Presione [ENTER] para mantener el valor actual):");
        u.setName(readString(sc, "Nombre [" + u.getName() + "]: ", true, u.getName()));
        u.setLastName(readString(sc, "Apellido [" + u.getLastName() + "]: ", true, u.getLastName()));
        u.setEmail(readString(sc, "Email [" + u.getEmail() + "]: ", true, u.getEmail()));
        u.setPhoneNumber(readString(sc, "Teléfono [" + u.getPhoneNumber() + "]: ", true, u.getPhoneNumber()));
        u.setDate(readDate(sc, "Fecha [" + u.getDate() + "]: ", u.getDate()));
        u.setTypeBike(readString(sc, "Tipo Moto [" + u.getTypeBike() + "]: ", true, u.getTypeBike()));

        try {
            userService.update(id, u);
            System.out.println("✅ Usuario #" + id + " actualizado correctamente.");
        } catch (Exception e) {
            System.out.println("❌ Error al actualizar usuario: " + e.getMessage());
        }
    }

    private void eliminarUsuario(Scanner sc) {
        int id = readInt(sc, "Ingrese el ID del usuario a eliminar: ", -1);
        User u = userService.selectById(id);
        if (u == null) {
            System.out.println("⚠️ Usuario no encontrado con ID: " + id);
            return;
        }
        String confirm = readString(sc, "¿Está seguro de eliminar al usuario '" + u.getName() + " " + u.getLastName() + "'? (s/n): ", false, "n");
        if (confirm.equalsIgnoreCase("s") || confirm.equalsIgnoreCase("si")) {
            try {
                userService.delete(id);
                System.out.println("✅ Usuario #" + id + " eliminado con éxito.");
            } catch (Exception e) {
                System.out.println("❌ Error al eliminar usuario (puede tener motos asociadas): " + e.getMessage());
            }
        } else {
            System.out.println("Operación cancelada.");
        }
    }

    // =========================================================================
    // 2. GESTIÓN DE MOTOCICLETAS (TipoMoto)
    // =========================================================================
    private void menuMotos(Scanner sc) {
        boolean back = false;
        while (!back) {
            System.out.println("\n------------------------------------------------");
            System.out.println("           🏍️ GESTIÓN DE MOTOCICLETAS           ");
            System.out.println("------------------------------------------------");
            System.out.println("1. Listar todas las motocicletas");
            System.out.println("2. Consultar motocicleta por ID");
            System.out.println("3. Agregar nueva motocicleta");
            System.out.println("4. Editar motocicleta existente");
            System.out.println("5. Eliminar motocicleta");
            System.out.println("0. Volver al menú principal");
            int opt = readInt(sc, "Seleccione una opción: ", -1);

            switch (opt) {
                case 1 -> listarMotos();
                case 2 -> {
                    int id = readInt(sc, "Ingrese el ID de la moto: ", -1);
                    TipoMoto m = tipoMotoService.selectById(id);
                    if (m != null) {
                        mostrarMoto(m);
                    } else {
                        System.out.println("⚠️ Motocicleta no encontrada con ID: " + id);
                    }
                }
                case 3 -> agregarMoto(sc);
                case 4 -> editarMoto(sc);
                case 5 -> eliminarMoto(sc);
                case 0 -> back = true;
                default -> System.out.println("❌ Opción inválida.");
            }
        }
    }

    private void listarMotos() {
        List<TipoMoto> list = tipoMotoService.selectAll();
        System.out.println("\n📋 Lista de Motocicletas (" + list.size() + " registradas):");
        if (list.isEmpty()) {
            System.out.println("  (No hay motocicletas registradas)");
            return;
        }
        for (TipoMoto m : list) {
            String owner = (m.getUser() != null) ? m.getUser().getName() + " " + m.getUser().getLastName() : "Sin asignar";
            System.out.printf("  • [ID: %d] Placa: %s | %s %s (Modelo: %s, Cilindraje: %s) | Odómetro: %d km | Propietario: %s\n",
                    m.getTipoMotoId(), m.getPlaca(), m.getMarcaMoto(), m.getCategoriaMoto(),
                    m.getModeloMoto(), m.getCilindrajeMoto(), m.getOdometroActual(), owner);
        }
    }

    private void mostrarMoto(TipoMoto m) {
        System.out.println("\n--- Detalle de la Motocicleta ---");
        System.out.println("  ID:              " + m.getTipoMotoId());
        System.out.println("  Placa:           " + m.getPlaca());
        System.out.println("  Marca:           " + m.getMarcaMoto());
        System.out.println("  Modelo (Año):    " + m.getModeloMoto());
        System.out.println("  Cilindraje:      " + m.getCilindrajeMoto());
        System.out.println("  Categoría:       " + m.getCategoriaMoto());
        System.out.println("  Tipo de Uso:     " + m.getTipoUso());
        System.out.println("  Odómetro Actual: " + m.getOdometroActual() + " km");
        if (m.getUser() != null) {
            System.out.println("  Propietario:     [" + m.getUser().getUserId() + "] " + m.getUser().getName() + " " + m.getUser().getLastName() + " (" + m.getUser().getEmail() + ")");
        } else {
            System.out.println("  Propietario:     Sin asignar");
        }
    }

    private void agregarMoto(Scanner sc) {
        System.out.println("\n➕ Agregar Nueva Motocicleta:");
        String marca = readString(sc, "Marca (ej. Yamaha, Honda, Suzuki): ", false, null);
        String modelo = readString(sc, "Modelo/Año (ej. 2024, MT-03): ", false, null);
        String cilindraje = readString(sc, "Cilindraje (ej. 250cc, 689cc): ", false, null);
        String categoria = readString(sc, "Categoría (ej. Naked, Sport, Scooter, Enduro): ", true, "Naked");
        String tipoUso = readString(sc, "Tipo de Uso (ej. Urbano, Viajes, Trabajo): ", true, "Urbano");
        String placa = readString(sc, "Placa única (ej. ABC12D): ", false, null);
        int odometro = readInt(sc, "Odómetro actual en km (ej. 15000): ", 0);

        User owner = seleccionarUsuario(sc);

        TipoMoto moto = new TipoMoto();
        moto.setMarcaMoto(marca);
        moto.setModeloMoto(modelo);
        moto.setCilindrajeMoto(cilindraje);
        moto.setCategoriaMoto(categoria);
        moto.setTipoUso(tipoUso);
        moto.setPlaca(placa);
        moto.setOdometroActual(odometro);
        moto.setUser(owner);

        try {
            tipoMotoService.create(moto);
            System.out.println("✅ Motocicleta registrada exitosamente con ID: " + moto.getTipoMotoId());
        } catch (Exception e) {
            System.out.println("❌ Error al registrar motocicleta: " + e.getMessage());
        }
    }

    private void editarMoto(Scanner sc) {
        int id = readInt(sc, "Ingrese el ID de la moto a editar: ", -1);
        TipoMoto m = tipoMotoService.selectById(id);
        if (m == null) {
            System.out.println("⚠️ Motocicleta no encontrada con ID: " + id);
            return;
        }
        System.out.println("\n✏️ Editando Motocicleta #" + id + " (Presione [ENTER] para mantener el valor actual):");
        m.setMarcaMoto(readString(sc, "Marca [" + m.getMarcaMoto() + "]: ", true, m.getMarcaMoto()));
        m.setModeloMoto(readString(sc, "Modelo [" + m.getModeloMoto() + "]: ", true, m.getModeloMoto()));
        m.setCilindrajeMoto(readString(sc, "Cilindraje [" + m.getCilindrajeMoto() + "]: ", true, m.getCilindrajeMoto()));
        m.setCategoriaMoto(readString(sc, "Categoría [" + m.getCategoriaMoto() + "]: ", true, m.getCategoriaMoto()));
        m.setTipoUso(readString(sc, "Tipo de Uso [" + m.getTipoUso() + "]: ", true, m.getTipoUso()));
        m.setPlaca(readString(sc, "Placa [" + m.getPlaca() + "]: ", true, m.getPlaca()));
        m.setOdometroActual(readInt(sc, "Odómetro actual [" + m.getOdometroActual() + " km]: ", m.getOdometroActual()));

        String changeOwner = readString(sc, "¿Desea cambiar el propietario? (s/n): ", true, "n");
        if (changeOwner.equalsIgnoreCase("s") || changeOwner.equalsIgnoreCase("si")) {
            m.setUser(seleccionarUsuario(sc));
        }

        try {
            tipoMotoService.update(id, m);
            System.out.println("✅ Motocicleta #" + id + " actualizada correctamente.");
        } catch (Exception e) {
            System.out.println("❌ Error al actualizar motocicleta: " + e.getMessage());
        }
    }

    private void eliminarMoto(Scanner sc) {
        int id = readInt(sc, "Ingrese el ID de la moto a eliminar: ", -1);
        TipoMoto m = tipoMotoService.selectById(id);
        if (m == null) {
            System.out.println("⚠️ Motocicleta no encontrada con ID: " + id);
            return;
        }
        String confirm = readString(sc, "¿Está seguro de eliminar la moto con placa '" + m.getPlaca() + "'? (s/n): ", false, "n");
        if (confirm.equalsIgnoreCase("s") || confirm.equalsIgnoreCase("si")) {
            try {
                tipoMotoService.delete(id);
                System.out.println("✅ Motocicleta #" + id + " eliminada con éxito.");
            } catch (Exception e) {
                System.out.println("❌ Error al eliminar moto (puede tener arreglos o estados vinculados): " + e.getMessage());
            }
        } else {
            System.out.println("Operación cancelada.");
        }
    }

    // =========================================================================
    // 3. GESTIÓN DE ESTADO DE LA MOTO / PIEZAS (EstadoMoto)
    // =========================================================================
    private void menuEstadoMoto(Scanner sc) {
        boolean back = false;
        while (!back) {
            System.out.println("\n------------------------------------------------");
            System.out.println("       ⚙️ GESTIÓN DE ESTADO DE PIEZAS (MOTO)    ");
            System.out.println("------------------------------------------------");
            System.out.println("1. Listar estados de piezas registrados");
            System.out.println("2. Consultar estado por ID");
            System.out.println("3. Agregar nuevo estado de piezas");
            System.out.println("4. Editar estado de piezas existente");
            System.out.println("5. Eliminar estado de piezas");
            System.out.println("0. Volver al menú principal");
            int opt = readInt(sc, "Seleccione una opción: ", -1);

            switch (opt) {
                case 1 -> listarEstados();
                case 2 -> {
                    int id = readInt(sc, "Ingrese el ID del estado: ", -1);
                    EstadoMoto em = estadoMotoService.selectById(id);
                    if (em != null) {
                        mostrarEstado(em);
                    } else {
                        System.out.println("⚠️ Estado no encontrado con ID: " + id);
                    }
                }
                case 3 -> agregarEstado(sc);
                case 4 -> editarEstado(sc);
                case 5 -> eliminarEstado(sc);
                case 0 -> back = true;
                default -> System.out.println("❌ Opción inválida.");
            }
        }
    }

    private void listarEstados() {
        List<EstadoMoto> list = estadoMotoService.selectAll();
        System.out.println("\n📋 Estados de Piezas (" + list.size() + " registros):");
        if (list.isEmpty()) {
            System.out.println("  (No hay registros de estado de piezas)");
            return;
        }
        for (EstadoMoto em : list) {
            String motoPlaca = (em.getMoto() != null) ? em.getMoto().getPlaca() : "N/A";
            System.out.printf("  • [ID: %d] Pieza: %s | Estado: %s | Desgaste Estimado: %s km | Moto Placa: %s | Fecha: %s\n",
                    em.getEstadoId(), em.getPiezasMoto(), em.getEstadoPiezas(),
                    em.getKilometrosEstimadosUso() != null ? em.getKilometrosEstimadosUso() : "N/A",
                    motoPlaca, em.getFechaReparacion());
        }
    }

    private void mostrarEstado(EstadoMoto em) {
        System.out.println("\n--- Detalle del Estado de Pieza ---");
        System.out.println("  ID:                   " + em.getEstadoId());
        System.out.println("  Pieza(s):             " + em.getPiezasMoto());
        System.out.println("  Estado:               " + em.getEstadoPiezas());
        System.out.println("  Reparaciones previas: " + em.getReparacionesMoto());
        System.out.println("  Fecha de Reparación:  " + em.getFechaReparacion());
        System.out.println("  Km Estimados de Uso:  " + em.getKilometrosEstimadosUso());
        if (em.getMoto() != null) {
            System.out.println("  Moto Asociada:        [" + em.getMoto().getTipoMotoId() + "] " + em.getMoto().getMarcaMoto() + " (" + em.getMoto().getPlaca() + ")");
        }
    }

    private void agregarEstado(Scanner sc) {
        System.out.println("\n➕ Agregar Nuevo Estado de Piezas:");
        String pieza = readString(sc, "Nombre de la pieza (ej. Pastillas de Freno, Kit de Arrastre, Aceite): ", false, null);
        String estado = readString(sc, "Estado de la pieza (ej. Bueno, Regular, Desgaste crítico): ", false, null);
        String reparaciones = readString(sc, "Detalle o reparaciones realizadas: ", true, "Revisión general");
        LocalDate fecha = readDate(sc, "Fecha de reparación/revisión (AAAA-MM-DD) [Enter para hoy]: ", LocalDate.now());
        int kmEstimados = readInt(sc, "Kilómetros estimados de uso/desgaste (ej. 3000): ", 3000);

        TipoMoto moto = seleccionarMoto(sc);

        EstadoMoto em = new EstadoMoto();
        em.setPiezasMoto(pieza);
        em.setEstadoPiezas(estado);
        em.setReparacionesMoto(reparaciones);
        em.setFechaReparacion(fecha);
        em.setKilometrosEstimadosUso(kmEstimados);
        em.setMoto(moto);

        try {
            estadoMotoService.create(em);
            System.out.println("✅ Estado de pieza guardado con ID: " + em.getEstadoId());
        } catch (Exception e) {
            System.out.println("❌ Error al guardar estado: " + e.getMessage());
        }
    }

    private void editarEstado(Scanner sc) {
        int id = readInt(sc, "Ingrese el ID del estado a editar: ", -1);
        EstadoMoto em = estadoMotoService.selectById(id);
        if (em == null) {
            System.out.println("⚠️ Estado no encontrado con ID: " + id);
            return;
        }
        System.out.println("\n✏️ Editando Estado de Pieza #" + id + " (Presione [ENTER] para mantener el valor actual):");
        em.setPiezasMoto(readString(sc, "Pieza [" + em.getPiezasMoto() + "]: ", true, em.getPiezasMoto()));
        em.setEstadoPiezas(readString(sc, "Estado [" + em.getEstadoPiezas() + "]: ", true, em.getEstadoPiezas()));
        em.setReparacionesMoto(readString(sc, "Reparaciones [" + em.getReparacionesMoto() + "]: ", true, em.getReparacionesMoto()));
        em.setFechaReparacion(readDate(sc, "Fecha [" + em.getFechaReparacion() + "]: ", em.getFechaReparacion()));
        em.setKilometrosEstimadosUso(readInt(sc, "Km Estimados [" + em.getKilometrosEstimadosUso() + "]: ", em.getKilometrosEstimadosUso()));

        String changeMoto = readString(sc, "¿Desea cambiar la moto vinculada? (s/n): ", true, "n");
        if (changeMoto.equalsIgnoreCase("s") || changeMoto.equalsIgnoreCase("si")) {
            em.setMoto(seleccionarMoto(sc));
        }

        try {
            estadoMotoService.update(id, em);
            System.out.println("✅ Estado #" + id + " actualizado correctamente.");
        } catch (Exception e) {
            System.out.println("❌ Error al actualizar estado: " + e.getMessage());
        }
    }

    private void eliminarEstado(Scanner sc) {
        int id = readInt(sc, "Ingrese el ID del estado a eliminar: ", -1);
        EstadoMoto em = estadoMotoService.selectById(id);
        if (em == null) {
            System.out.println("⚠️ Estado no encontrado con ID: " + id);
            return;
        }
        String confirm = readString(sc, "¿Está seguro de eliminar el estado de la pieza '" + em.getPiezasMoto() + "'? (s/n): ", false, "n");
        if (confirm.equalsIgnoreCase("s") || confirm.equalsIgnoreCase("si")) {
            try {
                estadoMotoService.delete(id);
                System.out.println("✅ Estado #" + id + " eliminado con éxito.");
            } catch (Exception e) {
                System.out.println("❌ Error al eliminar estado: " + e.getMessage());
            }
        } else {
            System.out.println("Operación cancelada.");
        }
    }

    // =========================================================================
    // 4. GESTIÓN DE RECORDATORIOS (Recordatorio)
    // =========================================================================
    private void menuRecordatorios(Scanner sc) {
        boolean back = false;
        while (!back) {
            System.out.println("\n------------------------------------------------");
            System.out.println("             ⏰ GESTIÓN DE RECORDATORIOS         ");
            System.out.println("------------------------------------------------");
            System.out.println("1. Listar recordatorios");
            System.out.println("2. Consultar recordatorio por ID");
            System.out.println("3. Agregar nuevo recordatorio");
            System.out.println("4. Editar recordatorio existente");
            System.out.println("5. Eliminar recordatorio");
            System.out.println("0. Volver al menú principal");
            int opt = readInt(sc, "Seleccione una opción: ", -1);

            switch (opt) {
                case 1 -> listarRecordatorios();
                case 2 -> {
                    int id = readInt(sc, "Ingrese el ID del recordatorio: ", -1);
                    Recordatorio r = recordatorioService.selectById(id);
                    if (r != null) {
                        mostrarRecordatorio(r);
                    } else {
                        System.out.println("⚠️ Recordatorio no encontrado con ID: " + id);
                    }
                }
                case 3 -> agregarRecordatorio(sc);
                case 4 -> editarRecordatorio(sc);
                case 5 -> eliminarRecordatorio(sc);
                case 0 -> back = true;
                default -> System.out.println("❌ Opción inválida.");
            }
        }
    }

    private void listarRecordatorios() {
        List<Recordatorio> list = recordatorioService.selectAll();
        System.out.println("\n📋 Lista de Recordatorios (" + list.size() + " registrados):");
        if (list.isEmpty()) {
            System.out.println("  (No hay recordatorios registrados)");
            return;
        }
        for (Recordatorio r : list) {
            String pieza = (r.getEstadoMoto() != null) ? r.getEstadoMoto().getPiezasMoto() : "N/A";
            System.out.printf("  • [ID: %d] Notificación: %s | Estado Repuesto: %s | Pieza Asoc.: %s\n",
                    r.getRecordatorioId(), r.getNotificacionR(), r.getEstadoRepuesto(), pieza);
        }
    }

    private void mostrarRecordatorio(Recordatorio r) {
        System.out.println("\n--- Detalle del Recordatorio ---");
        System.out.println("  ID:             " + r.getRecordatorioId());
        System.out.println("  Notificación:   " + r.getNotificacionR());
        System.out.println("  Estado Repuesto:" + r.getEstadoRepuesto());
        System.out.println("  Descripción:    " + r.getDescripcionRecordatorio());
        if (r.getEstadoMoto() != null) {
            System.out.println("  Estado Pieza:   [" + r.getEstadoMoto().getEstadoId() + "] " + r.getEstadoMoto().getPiezasMoto() + " (" + r.getEstadoMoto().getEstadoPiezas() + ")");
        }
    }

    private void agregarRecordatorio(Scanner sc) {
        System.out.println("\n➕ Agregar Nuevo Recordatorio:");
        String notif = readString(sc, "Título / Notificación (ej. Cambio preventivo de aceite): ", false, null);
        String repuesto = readString(sc, "Estado del repuesto (ej. Pendiente compra, Comprado, En taller): ", false, null);
        String desc = readString(sc, "Descripción detallada del recordatorio: ", true, "");

        EstadoMoto em = seleccionarEstadoMoto(sc);
        if (em == null) {
            System.out.println("⚠️ Debe seleccionar o crear un Estado de Pieza primero.");
            return;
        }

        Recordatorio r = new Recordatorio();
        r.setNotificacionR(notif);
        r.setEstadoRepuesto(repuesto);
        r.setDescripcionRecordatorio(desc);
        r.setEstadoMoto(em);

        try {
            recordatorioService.create(r);
            System.out.println("✅ Recordatorio creado exitosamente con ID: " + r.getRecordatorioId());
        } catch (Exception e) {
            System.out.println("❌ Error al crear recordatorio: " + e.getMessage());
        }
    }

    private void editarRecordatorio(Scanner sc) {
        int id = readInt(sc, "Ingrese el ID del recordatorio a editar: ", -1);
        Recordatorio r = recordatorioService.selectById(id);
        if (r == null) {
            System.out.println("⚠️ Recordatorio no encontrado con ID: " + id);
            return;
        }
        System.out.println("\n✏️ Editando Recordatorio #" + id + " (Presione [ENTER] para mantener el valor actual):");
        r.setNotificacionR(readString(sc, "Notificación [" + r.getNotificacionR() + "]: ", true, r.getNotificacionR()));
        r.setEstadoRepuesto(readString(sc, "Estado Repuesto [" + r.getEstadoRepuesto() + "]: ", true, r.getEstadoRepuesto()));
        r.setDescripcionRecordatorio(readString(sc, "Descripción [" + r.getDescripcionRecordatorio() + "]: ", true, r.getDescripcionRecordatorio()));

        String changeEm = readString(sc, "¿Desea cambiar el estado de pieza vinculado? (s/n): ", true, "n");
        if (changeEm.equalsIgnoreCase("s") || changeEm.equalsIgnoreCase("si")) {
            EstadoMoto nuevoEm = seleccionarEstadoMoto(sc);
            if (nuevoEm != null) r.setEstadoMoto(nuevoEm);
        }

        try {
            recordatorioService.update(id, r);
            System.out.println("✅ Recordatorio #" + id + " actualizado correctamente.");
        } catch (Exception e) {
            System.out.println("❌ Error al actualizar recordatorio: " + e.getMessage());
        }
    }

    private void eliminarRecordatorio(Scanner sc) {
        int id = readInt(sc, "Ingrese el ID del recordatorio a eliminar: ", -1);
        Recordatorio r = recordatorioService.selectById(id);
        if (r == null) {
            System.out.println("⚠️ Recordatorio no encontrado con ID: " + id);
            return;
        }
        String confirm = readString(sc, "¿Está seguro de eliminar el recordatorio '" + r.getNotificacionR() + "'? (s/n): ", false, "n");
        if (confirm.equalsIgnoreCase("s") || confirm.equalsIgnoreCase("si")) {
            try {
                recordatorioService.delete(id);
                System.out.println("✅ Recordatorio #" + id + " eliminado con éxito.");
            } catch (Exception e) {
                System.out.println("❌ Error al eliminar recordatorio: " + e.getMessage());
            }
        } else {
            System.out.println("Operación cancelada.");
        }
    }

    // =========================================================================
    // 5. GESTIÓN DE TALLERES (UbicacionTaller)
    // =========================================================================
    private void menuTalleres(Scanner sc) {
        boolean back = false;
        while (!back) {
            System.out.println("\n------------------------------------------------");
            System.out.println("        🏬 GESTIÓN DE UBICACIÓN DE TALLERES     ");
            System.out.println("------------------------------------------------");
            System.out.println("1. Listar talleres registrados");
            System.out.println("2. Consultar taller por ID");
            System.out.println("3. Agregar nuevo taller");
            System.out.println("4. Editar taller existente");
            System.out.println("5. Eliminar taller");
            System.out.println("0. Volver al menú principal");
            int opt = readInt(sc, "Seleccione una opción: ", -1);

            switch (opt) {
                case 1 -> listarTalleres();
                case 2 -> {
                    int id = readInt(sc, "Ingrese el ID del taller: ", -1);
                    UbicacionTaller t = tallerService.selectById(id);
                    if (t != null) {
                        mostrarTaller(t);
                    } else {
                        System.out.println("⚠️ Taller no encontrado con ID: " + id);
                    }
                }
                case 3 -> agregarTaller(sc);
                case 4 -> editarTaller(sc);
                case 5 -> eliminarTaller(sc);
                case 0 -> back = true;
                default -> System.out.println("❌ Opción inválida.");
            }
        }
    }

    private void listarTalleres() {
        List<UbicacionTaller> list = tallerService.selectAll();
        System.out.println("\n📋 Lista de Talleres (" + list.size() + " registrados):");
        if (list.isEmpty()) {
            System.out.println("  (No hay talleres registrados)");
            return;
        }
        for (UbicacionTaller t : list) {
            System.out.printf("  • [ID: %d] %s, %s | Dir: %s | Mecánico: %s | Tel: %s | Esp: %s\n",
                    t.getTallerId(), t.getMunicipioTaller(), t.getBarrioTaller(), t.getDireccionTaller(),
                    t.getNombreMecanico(), t.getTelefonoTaller(), t.getEspecialidadTaller());
        }
    }

    private void mostrarTaller(UbicacionTaller t) {
        System.out.println("\n--- Detalle del Taller Mecánico ---");
        System.out.println("  ID:           " + t.getTallerId());
        System.out.println("  Municipio:    " + t.getMunicipioTaller());
        System.out.println("  Barrio:       " + t.getBarrioTaller());
        System.out.println("  Dirección:    " + t.getDireccionTaller());
        System.out.println("  Mecánico:     " + t.getNombreMecanico());
        System.out.println("  Especialidad: " + t.getEspecialidadTaller());
        System.out.println("  Teléfono:     " + t.getTelefonoTaller());
    }

    private void agregarTaller(Scanner sc) {
        System.out.println("\n➕ Agregar Nuevo Taller Mecánico:");
        String municipio = readString(sc, "Municipio / Ciudad (ej. Medellín, Bogotá): ", false, null);
        String barrio = readString(sc, "Barrio / Sector: ", true, "");
        String direccion = readString(sc, "Dirección exacta: ", false, null);
        String mecanico = readString(sc, "Nombre del mecánico responsable: ", true, "");
        String especialidad = readString(sc, "Especialidad (ej. Frenos, Motor, Eléctrico): ", true, "Mecánica General");
        String telefono = readString(sc, "Teléfono de contacto: ", true, "");

        UbicacionTaller t = new UbicacionTaller();
        t.setMunicipioTaller(municipio);
        t.setBarrioTaller(barrio);
        t.setDireccionTaller(direccion);
        t.setNombreMecanico(mecanico);
        t.setEspecialidadTaller(especialidad);
        t.setTelefonoTaller(telefono);

        try {
            tallerService.create(t);
            System.out.println("✅ Taller registrado con éxito con ID: " + t.getTallerId());
        } catch (Exception e) {
            System.out.println("❌ Error al registrar taller: " + e.getMessage());
        }
    }

    private void editarTaller(Scanner sc) {
        int id = readInt(sc, "Ingrese el ID del taller a editar: ", -1);
        UbicacionTaller t = tallerService.selectById(id);
        if (t == null) {
            System.out.println("⚠️ Taller no encontrado con ID: " + id);
            return;
        }
        System.out.println("\n✏️ Editando Taller #" + id + " (Presione [ENTER] para mantener el valor actual):");
        t.setMunicipioTaller(readString(sc, "Municipio [" + t.getMunicipioTaller() + "]: ", true, t.getMunicipioTaller()));
        t.setBarrioTaller(readString(sc, "Barrio [" + t.getBarrioTaller() + "]: ", true, t.getBarrioTaller()));
        t.setDireccionTaller(readString(sc, "Dirección [" + t.getDireccionTaller() + "]: ", true, t.getDireccionTaller()));
        t.setNombreMecanico(readString(sc, "Mecánico [" + t.getNombreMecanico() + "]: ", true, t.getNombreMecanico()));
        t.setEspecialidadTaller(readString(sc, "Especialidad [" + t.getEspecialidadTaller() + "]: ", true, t.getEspecialidadTaller()));
        t.setTelefonoTaller(readString(sc, "Teléfono [" + t.getTelefonoTaller() + "]: ", true, t.getTelefonoTaller()));

        try {
            tallerService.update(id, t);
            System.out.println("✅ Taller #" + id + " actualizado correctamente.");
        } catch (Exception e) {
            System.out.println("❌ Error al actualizar taller: " + e.getMessage());
        }
    }

    private void eliminarTaller(Scanner sc) {
        int id = readInt(sc, "Ingrese el ID del taller a eliminar: ", -1);
        UbicacionTaller t = tallerService.selectById(id);
        if (t == null) {
            System.out.println("⚠️ Taller no encontrado con ID: " + id);
            return;
        }
        String confirm = readString(sc, "¿Está seguro de eliminar el taller en '" + t.getDireccionTaller() + "'? (s/n): ", false, "n");
        if (confirm.equalsIgnoreCase("s") || confirm.equalsIgnoreCase("si")) {
            try {
                tallerService.delete(id);
                System.out.println("✅ Taller #" + id + " eliminado con éxito.");
            } catch (Exception e) {
                System.out.println("❌ Error al eliminar taller: " + e.getMessage());
            }
        } else {
            System.out.println("Operación cancelada.");
        }
    }

    // =========================================================================
    // 6. GESTIÓN DE HISTORIAL (Historial)
    // =========================================================================
    private void menuHistorial(Scanner sc) {
        boolean back = false;
        while (!back) {
            System.out.println("\n------------------------------------------------");
            System.out.println("        📋 GESTIÓN DE HISTORIAL DE MANT.        ");
            System.out.println("------------------------------------------------");
            System.out.println("1. Listar historial");
            System.out.println("2. Consultar historial por ID");
            System.out.println("3. Agregar nuevo registro al historial");
            System.out.println("4. Editar registro del historial");
            System.out.println("5. Eliminar registro del historial");
            System.out.println("0. Volver al menú principal");
            int opt = readInt(sc, "Seleccione una opción: ", -1);

            switch (opt) {
                case 1 -> listarHistorial();
                case 2 -> {
                    int id = readInt(sc, "Ingrese el ID del historial: ", -1);
                    Historial h = historialService.selectById(id);
                    if (h != null) {
                        mostrarHistorial(h);
                    } else {
                        System.out.println("⚠️ Historial no encontrado con ID: " + id);
                    }
                }
                case 3 -> agregarHistorial(sc);
                case 4 -> editarHistorial(sc);
                case 5 -> eliminarHistorial(sc);
                case 0 -> back = true;
                default -> System.out.println("❌ Opción inválida.");
            }
        }
    }

    private void listarHistorial() {
        List<Historial> list = historialService.selectAll();
        System.out.println("\n📋 Registros de Historial (" + list.size() + " registrados):");
        if (list.isEmpty()) {
            System.out.println("  (No hay historial registrado)");
            return;
        }
        for (Historial h : list) {
            String tallerNom = (h.getTaller() != null) ? h.getTaller().getNombreMecanico() + " (" + h.getTaller().getMunicipioTaller() + ")" : h.getNomTaller();
            System.out.printf("  • [ID: %d] Fecha: %s | Tipo: %s | Taller: %s | Desc: %s\n",
                    h.getHistorialId(), h.getFechaHistorial(), h.getTipoMantenimiento(), tallerNom, h.getDescripcionMantenimiento());
        }
    }

    private void mostrarHistorial(Historial h) {
        System.out.println("\n--- Detalle del Registro de Historial ---");
        System.out.println("  ID:            " + h.getHistorialId());
        System.out.println("  Fecha:         " + h.getFechaHistorial());
        System.out.println("  Tipo Mantenim.:" + h.getTipoMantenimiento());
        System.out.println("  Nombre Taller: " + h.getNomTaller());
        System.out.println("  Descripción:   " + h.getDescripcionMantenimiento());
        if (h.getRecordatorio() != null) {
            System.out.println("  Recordatorio:  [" + h.getRecordatorio().getRecordatorioId() + "] " + h.getRecordatorio().getNotificacionR());
        }
        if (h.getTaller() != null) {
            System.out.println("  Taller Físico: [" + h.getTaller().getTallerId() + "] Mecánico: " + h.getTaller().getNombreMecanico() + " (" + h.getTaller().getDireccionTaller() + ")");
        }
    }

    private void agregarHistorial(Scanner sc) {
        System.out.println("\n➕ Agregar Registro al Historial:");
        LocalDate fecha = readDate(sc, "Fecha del mantenimiento (AAAA-MM-DD) [Enter para hoy]: ", LocalDate.now());
        String tipo = readString(sc, "Tipo de mantenimiento (ej. Preventivo, Correctivo): ", false, null);
        String desc = readString(sc, "Descripción detallada del mantenimiento: ", true, "");

        Recordatorio rec = seleccionarRecordatorio(sc);
        if (rec == null) {
            System.out.println("⚠️ Debe existir y seleccionar al menos un Recordatorio.");
            return;
        }

        UbicacionTaller taller = seleccionarTaller(sc, true);
        String nomTaller = (taller != null) ? taller.getMunicipioTaller() + " - " + taller.getDireccionTaller()
                : readString(sc, "Nombre del taller o mecánico externo: ", false, "Taller Particular");

        Historial h = new Historial();
        h.setFechaHistorial(fecha);
        h.setTipoMantenimiento(tipo);
        h.setDescripcionMantenimiento(desc);
        h.setNomTaller(nomTaller);
        h.setRecordatorio(rec);
        h.setTaller(taller);

        try {
            historialService.create(h);
            System.out.println("✅ Historial registrado con éxito con ID: " + h.getHistorialId());
        } catch (Exception e) {
            System.out.println("❌ Error al registrar historial: " + e.getMessage());
        }
    }

    private void editarHistorial(Scanner sc) {
        int id = readInt(sc, "Ingrese el ID del historial a editar: ", -1);
        Historial h = historialService.selectById(id);
        if (h == null) {
            System.out.println("⚠️ Registro de historial no encontrado con ID: " + id);
            return;
        }
        System.out.println("\n✏️ Editando Historial #" + id + " (Presione [ENTER] para mantener el valor actual):");
        h.setFechaHistorial(readDate(sc, "Fecha [" + h.getFechaHistorial() + "]: ", h.getFechaHistorial()));
        h.setTipoMantenimiento(readString(sc, "Tipo [" + h.getTipoMantenimiento() + "]: ", true, h.getTipoMantenimiento()));
        h.setNomTaller(readString(sc, "Nombre Taller [" + h.getNomTaller() + "]: ", true, h.getNomTaller()));
        h.setDescripcionMantenimiento(readString(sc, "Descripción [" + h.getDescripcionMantenimiento() + "]: ", true, h.getDescripcionMantenimiento()));

        String changeTaller = readString(sc, "¿Desea cambiar el taller asignado? (s/n): ", true, "n");
        if (changeTaller.equalsIgnoreCase("s") || changeTaller.equalsIgnoreCase("si")) {
            h.setTaller(seleccionarTaller(sc, true));
        }

        try {
            historialService.update(id, h);
            System.out.println("✅ Historial #" + id + " actualizado correctamente.");
        } catch (Exception e) {
            System.out.println("❌ Error al actualizar historial: " + e.getMessage());
        }
    }

    private void eliminarHistorial(Scanner sc) {
        int id = readInt(sc, "Ingrese el ID del historial a eliminar: ", -1);
        Historial h = historialService.selectById(id);
        if (h == null) {
            System.out.println("⚠️ Registro de historial no encontrado con ID: " + id);
            return;
        }
        String confirm = readString(sc, "¿Está seguro de eliminar este registro del historial? (s/n): ", false, "n");
        if (confirm.equalsIgnoreCase("s") || confirm.equalsIgnoreCase("si")) {
            try {
                historialService.delete(id);
                System.out.println("✅ Historial #" + id + " eliminado con éxito.");
            } catch (Exception e) {
                System.out.println("❌ Error al eliminar historial: " + e.getMessage());
            }
        } else {
            System.out.println("Operación cancelada.");
        }
    }

    // =========================================================================
    // 7. GESTIÓN DE ARREGLOS (Arreglos)
    // =========================================================================
    private void menuArreglos(Scanner sc) {
        boolean back = false;
        while (!back) {
            System.out.println("\n------------------------------------------------");
            System.out.println("       🔧 GESTIÓN DE ARREGLOS Y REPARACIONES    ");
            System.out.println("------------------------------------------------");
            System.out.println("1. Listar arreglos realizados");
            System.out.println("2. Consultar arreglo por ID");
            System.out.println("3. Agregar nuevo arreglo");
            System.out.println("4. Editar arreglo existente");
            System.out.println("5. Eliminar arreglo");
            System.out.println("0. Volver al menú principal");
            int opt = readInt(sc, "Seleccione una opción: ", -1);

            switch (opt) {
                case 1 -> listarArreglos();
                case 2 -> {
                    int id = readInt(sc, "Ingrese el ID del arreglo: ", -1);
                    Arreglos a = arreglosService.selectById(id);
                    if (a != null) {
                        mostrarArreglo(a);
                    } else {
                        System.out.println("⚠️ Arreglo no encontrado con ID: " + id);
                    }
                }
                case 3 -> agregarArreglo(sc);
                case 4 -> editarArreglo(sc);
                case 5 -> eliminarArreglo(sc);
                case 0 -> back = true;
                default -> System.out.println("❌ Opción inválida.");
            }
        }
    }

    private void listarArreglos() {
        List<Arreglos> list = arreglosService.selectAll();
        System.out.println("\n📋 Lista de Arreglos (" + list.size() + " registrados):");
        if (list.isEmpty()) {
            System.out.println("  (No hay arreglos registrados)");
            return;
        }
        for (Arreglos a : list) {
            String motoPlaca = (a.getMoto() != null) ? a.getMoto().getPlaca() : "N/A";
            System.out.printf("  • [ID: %d] Fecha: %s | Moto: %s | Estado: %s | Km al momento: %d km | Desc: %s\n",
                    a.getArreglosId(), a.getFechaArreglo(), motoPlaca, a.getEstadoArreglo(),
                    a.getKilometrajeMomento(), a.getDescripcionArreglos());
        }
    }

    private void mostrarArreglo(Arreglos a) {
        System.out.println("\n--- Detalle del Arreglo ---");
        System.out.println("  ID:                  " + a.getArreglosId());
        System.out.println("  Fecha:               " + a.getFechaArreglo());
        System.out.println("  Estado:              " + a.getEstadoArreglo());
        System.out.println("  Km al momento:       " + a.getKilometrajeMomento() + " km");
        System.out.println("  Descripción:         " + a.getDescripcionArreglos());
        if (a.getMoto() != null) {
            System.out.println("  Moto:                [" + a.getMoto().getTipoMotoId() + "] " + a.getMoto().getMarcaMoto() + " (" + a.getMoto().getPlaca() + ")");
        }
    }

    private void agregarArreglo(Scanner sc) {
        System.out.println("\n➕ Registrar Nuevo Arreglo:");
        LocalDate fecha = readDate(sc, "Fecha del arreglo (AAAA-MM-DD) [Enter para hoy]: ", LocalDate.now());
        String desc = readString(sc, "Descripción del trabajo realizado: ", false, null);
        String estado = seleccionarEstadoArreglo(sc);
        int km = readInt(sc, "Kilometraje del vehículo al momento del arreglo: ", 0);

        TipoMoto moto = seleccionarMoto(sc);
        if (moto == null) {
            System.out.println("⚠️ Debe existir al menos una motocicleta registrada.");
            return;
        }

        Arreglos a = new Arreglos();
        a.setFechaArreglo(fecha);
        a.setDescripcionArreglos(desc);
        a.setEstadoArreglo(estado);
        a.setKilometrajeMomento(km);
        a.setMoto(moto);

        try {
            arreglosService.create(a);
            System.out.println("✅ Arreglo registrado exitosamente con ID: " + a.getArreglosId());
        } catch (Exception e) {
            System.out.println("❌ Error al guardar arreglo: " + e.getMessage());
        }
    }

    private void editarArreglo(Scanner sc) {
        int id = readInt(sc, "Ingrese el ID del arreglo a editar: ", -1);
        Arreglos a = arreglosService.selectById(id);
        if (a == null) {
            System.out.println("⚠️ Arreglo no encontrado con ID: " + id);
            return;
        }
        System.out.println("\n✏️ Editando Arreglo #" + id + " (Presione [ENTER] para mantener el valor actual):");
        a.setFechaArreglo(readDate(sc, "Fecha [" + a.getFechaArreglo() + "]: ", a.getFechaArreglo()));
        a.setDescripcionArreglos(readString(sc, "Descripción [" + a.getDescripcionArreglos() + "]: ", true, a.getDescripcionArreglos()));
        a.setKilometrajeMomento(readInt(sc, "Kilometraje [" + a.getKilometrajeMomento() + " km]: ", a.getKilometrajeMomento()));

        String changeEstado = readString(sc, "¿Desea cambiar el estado (" + a.getEstadoArreglo() + ")? (s/n): ", true, "n");
        if (changeEstado.equalsIgnoreCase("s") || changeEstado.equalsIgnoreCase("si")) {
            a.setEstadoArreglo(seleccionarEstadoArreglo(sc));
        }

        String changeMoto = readString(sc, "¿Desea cambiar la moto? (s/n): ", true, "n");
        if (changeMoto.equalsIgnoreCase("s") || changeMoto.equalsIgnoreCase("si")) {
            a.setMoto(seleccionarMoto(sc));
        }

        try {
            arreglosService.update(id, a);
            System.out.println("✅ Arreglo #" + id + " actualizado correctamente.");
        } catch (Exception e) {
            System.out.println("❌ Error al actualizar arreglo: " + e.getMessage());
        }
    }

    private void eliminarArreglo(Scanner sc) {
        int id = readInt(sc, "Ingrese el ID del arreglo a eliminar: ", -1);
        Arreglos a = arreglosService.selectById(id);
        if (a == null) {
            System.out.println("⚠️ Arreglo no encontrado con ID: " + id);
            return;
        }
        String confirm = readString(sc, "¿Está seguro de eliminar el arreglo #" + id + " (" + a.getDescripcionArreglos() + ")? (s/n): ", false, "n");
        if (confirm.equalsIgnoreCase("s") || confirm.equalsIgnoreCase("si")) {
            try {
                arreglosService.delete(id);
                System.out.println("✅ Arreglo #" + id + " eliminado con éxito.");
            } catch (Exception e) {
                System.out.println("❌ Error al eliminar arreglo: " + e.getMessage());
            }
        } else {
            System.out.println("Operación cancelada.");
        }
    }

    private String seleccionarEstadoArreglo(Scanner sc) {
        System.out.println("Seleccione el estado del arreglo:");
        System.out.println("  1. COMPLETADO");
        System.out.println("  2. EN_PROGRESO");
        System.out.println("  3. PENDIENTE");
        System.out.println("  4. CANCELADO");
        int opt = readInt(sc, "Opción [1]: ", 1);
        return switch (opt) {
            case 2 -> EstadoArregloEnum.EN_PROGRESO.name();
            case 3 -> EstadoArregloEnum.PENDIENTE.name();
            case 4 -> EstadoArregloEnum.CANCELADO.name();
            default -> EstadoArregloEnum.COMPLETADO.name();
        };
    }

    // =========================================================================
    // 8. GESTIÓN DE COSTOS DE ARREGLO (CostoArreglo)
    // =========================================================================
    private void menuCostoArreglo(Scanner sc) {
        boolean back = false;
        while (!back) {
            System.out.println("\n------------------------------------------------");
            System.out.println("         💰 GESTIÓN DE COSTOS DE ARREGLO        ");
            System.out.println("------------------------------------------------");
            System.out.println("1. Listar todos los costos registrados");
            System.out.println("2. Consultar costo por ID de Arreglo");
            System.out.println("3. Asignar / Registrar costo a un Arreglo");
            System.out.println("4. Editar costo existente");
            System.out.println("5. Eliminar costo");
            System.out.println("0. Volver al menú principal");
            int opt = readInt(sc, "Seleccione una opción: ", -1);

            switch (opt) {
                case 1 -> listarCostos();
                case 2 -> {
                    int id = readInt(sc, "Ingrese el ID del Arreglo: ", -1);
                    CostoArreglo c = costoArregloService.selectById(id);
                    if (c != null) {
                        mostrarCosto(c);
                    } else {
                        System.out.println("⚠️ No existe costo registrado para el Arreglo #" + id);
                    }
                }
                case 3 -> agregarCosto(sc);
                case 4 -> editarCosto(sc);
                case 5 -> eliminarCosto(sc);
                case 0 -> back = true;
                default -> System.out.println("❌ Opción inválida.");
            }
        }
    }

    private void listarCostos() {
        List<CostoArreglo> list = costoArregloService.selectAll();
        System.out.println("\n📋 Costos Registrados (" + list.size() + " registros):");
        if (list.isEmpty()) {
            System.out.println("  (No hay costos registrados)");
            return;
        }
        for (CostoArreglo c : list) {
            String desc = (c.getArreglos() != null) ? c.getArreglos().getDescripcionArreglos() : "N/A";
            System.out.printf("  • [Arreglo ID: %d] Mano de obra: $%,.2f | Repuestos: $%,.2f | Otros: $%,.2f | TOTAL: $%,.2f | Trabajo: %s\n",
                    c.getArreglosId(), c.getManoDeObra(), c.getCostoRepuestos(), c.getOtrosGastos(), c.getTotalCosto(), desc);
        }
    }

    private void mostrarCosto(CostoArreglo c) {
        System.out.println("\n--- Detalle del Costo del Arreglo ---");
        System.out.println("  ID del Arreglo:       #" + c.getArreglosId());
        System.out.println("  Costo Mano de Obra:   $" + String.format("%,.2f", c.getManoDeObra()));
        System.out.println("  Costo Repuestos:      $" + String.format("%,.2f", c.getCostoRepuestos()));
        System.out.println("  Otros Gastos:         $" + String.format("%,.2f", c.getOtrosGastos()));
        System.out.println("  ---------------------------------------------");
        System.out.println("  TOTAL FINAL:          $" + String.format("%,.2f", c.getTotalCosto()));
        if (c.getArreglos() != null) {
            System.out.println("  Trabajo realizado:    " + c.getArreglos().getDescripcionArreglos());
            if (c.getArreglos().getMoto() != null) {
                System.out.println("  Placa de la moto:     " + c.getArreglos().getMoto().getPlaca());
            }
        }
    }

    private void agregarCosto(Scanner sc) {
        System.out.println("\n➕ Registrar Costo para un Arreglo:");
        Arreglos arreglo = seleccionarArreglo(sc);
        if (arreglo == null) {
            System.out.println("⚠️ Debe registrar primero un Arreglo.");
            return;
        }

        // Verificar si ya tiene costo
        CostoArreglo existente = costoArregloService.selectById(arreglo.getArreglosId());
        if (existente != null) {
            System.out.println("⚠️ El Arreglo #" + arreglo.getArreglosId() + " ya tiene un costo asignado (Total: $" + existente.getTotalCosto() + "). Use la opción de editar.");
            return;
        }

        double manoObra = readDouble(sc, "Valor mano de obra ($): ", 0.0);
        double repuestos = readDouble(sc, "Valor repuestos ($): ", 0.0);
        double otros = readDouble(sc, "Otros gastos ($) [Enter para 0]: ", 0.0);

        CostoArreglo c = new CostoArreglo();
        c.setArreglosId(arreglo.getArreglosId());
        c.setArreglos(arreglo);
        c.setManoDeObra(manoObra);
        c.setCostoRepuestos(repuestos);
        c.setOtrosGastos(otros);

        try {
            costoArregloService.create(c);
            System.out.printf("✅ Costo registrado con éxito. Total calculado: $%,.2f\n", c.getTotalCosto());
        } catch (Exception e) {
            System.out.println("❌ Error al registrar costo: " + e.getMessage());
        }
    }

    private void editarCosto(Scanner sc) {
        int id = readInt(sc, "Ingrese el ID del Arreglo cuyo costo desea editar: ", -1);
        CostoArreglo c = costoArregloService.selectById(id);
        if (c == null) {
            System.out.println("⚠️ No existe costo registrado para el Arreglo #" + id);
            return;
        }
        System.out.println("\n✏️ Editando Costo del Arreglo #" + id + " (Presione [ENTER] para mantener el valor actual):");
        c.setManoDeObra(readDouble(sc, "Mano de Obra [$" + c.getManoDeObra() + "]: ", c.getManoDeObra()));
        c.setCostoRepuestos(readDouble(sc, "Costo Repuestos [$" + c.getCostoRepuestos() + "]: ", c.getCostoRepuestos()));
        c.setOtrosGastos(readDouble(sc, "Otros Gastos [$" + c.getOtrosGastos() + "]: ", c.getOtrosGastos()));

        try {
            costoArregloService.update(id, c);
            System.out.printf("✅ Costo del Arreglo #%d actualizado. Nuevo Total: $%,.2f\n", id, c.getTotalCosto());
        } catch (Exception e) {
            System.out.println("❌ Error al actualizar costo: " + e.getMessage());
        }
    }

    private void eliminarCosto(Scanner sc) {
        int id = readInt(sc, "Ingrese el ID del Arreglo cuyo costo desea eliminar: ", -1);
        CostoArreglo c = costoArregloService.selectById(id);
        if (c == null) {
            System.out.println("⚠️ No existe costo registrado para el Arreglo #" + id);
            return;
        }
        String confirm = readString(sc, "¿Está seguro de eliminar el costo del Arreglo #" + id + "? (s/n): ", false, "n");
        if (confirm.equalsIgnoreCase("s") || confirm.equalsIgnoreCase("si")) {
            try {
                costoArregloService.delete(id);
                System.out.println("✅ Costo eliminado con éxito.");
            } catch (Exception e) {
                System.out.println("❌ Error al eliminar costo: " + e.getMessage());
            }
        } else {
            System.out.println("Operación cancelada.");
        }
    }

    // =========================================================================
    // MÉTODOS DE SELECCIÓN DE ENTIDADES RELACIONADAS
    // =========================================================================
    private User seleccionarUsuario(Scanner sc) {
        List<User> users = userService.selectAll();
        if (users.isEmpty()) {
            System.out.println("⚠️ No hay usuarios registrados.");
            String crear = readString(sc, "¿Desea registrar uno ahora? (s/n): ", false, "n");
            if (crear.equalsIgnoreCase("s") || crear.equalsIgnoreCase("si")) {
                agregarUsuario(sc);
                users = userService.selectAll();
            } else {
                return null;
            }
        }
        System.out.println("\nUsuarios disponibles para asignar:");
        for (User u : users) {
            System.out.println("  [" + u.getUserId() + "] " + u.getName() + " " + u.getLastName() + " (" + u.getEmail() + ")");
        }
        while (true) {
            int id = readInt(sc, "Ingrese el ID del Usuario propietario (o 0 para omitir): ", 0);
            if (id == 0) return null;
            User found = userService.selectById(id);
            if (found != null) return found;
            System.out.println("⚠️ ID no válido. Seleccione uno de la lista.");
        }
    }

    private TipoMoto seleccionarMoto(Scanner sc) {
        List<TipoMoto> motos = tipoMotoService.selectAll();
        if (motos.isEmpty()) {
            System.out.println("⚠️ No hay motocicletas registradas.");
            return null;
        }
        System.out.println("\nMotocicletas disponibles:");
        for (TipoMoto m : motos) {
            System.out.println("  [" + m.getTipoMotoId() + "] Placa: " + m.getPlaca() + " | " + m.getMarcaMoto() + " " + m.getModeloMoto());
        }
        while (true) {
            int id = readInt(sc, "Ingrese el ID de la Motocicleta (o 0 para cancelar): ", 0);
            if (id == 0) return null;
            TipoMoto found = tipoMotoService.selectById(id);
            if (found != null) return found;
            System.out.println("⚠️ ID no válido. Seleccione uno de la lista.");
        }
    }

    private EstadoMoto seleccionarEstadoMoto(Scanner sc) {
        List<EstadoMoto> estados = estadoMotoService.selectAll();
        if (estados.isEmpty()) {
            System.out.println("⚠️ No hay estados de piezas registrados.");
            return null;
        }
        System.out.println("\nEstados de piezas disponibles:");
        for (EstadoMoto em : estados) {
            String placa = (em.getMoto() != null) ? em.getMoto().getPlaca() : "Sin moto";
            System.out.println("  [" + em.getEstadoId() + "] Pieza: " + em.getPiezasMoto() + " (" + em.getEstadoPiezas() + ") | Moto: " + placa);
        }
        while (true) {
            int id = readInt(sc, "Ingrese el ID del Estado de Pieza (o 0 para cancelar): ", 0);
            if (id == 0) return null;
            EstadoMoto found = estadoMotoService.selectById(id);
            if (found != null) return found;
            System.out.println("⚠️ ID no válido. Seleccione uno de la lista.");
        }
    }

    private Recordatorio seleccionarRecordatorio(Scanner sc) {
        List<Recordatorio> list = recordatorioService.selectAll();
        if (list.isEmpty()) {
            System.out.println("⚠️ No hay recordatorios registrados.");
            return null;
        }
        System.out.println("\nRecordatorios disponibles:");
        for (Recordatorio r : list) {
            System.out.println("  [" + r.getRecordatorioId() + "] " + r.getNotificacionR() + " (Repuesto: " + r.getEstadoRepuesto() + ")");
        }
        while (true) {
            int id = readInt(sc, "Ingrese el ID del Recordatorio (o 0 para cancelar): ", 0);
            if (id == 0) return null;
            Recordatorio found = recordatorioService.selectById(id);
            if (found != null) return found;
            System.out.println("⚠️ ID no válido. Seleccione uno de la lista.");
        }
    }

    private UbicacionTaller seleccionarTaller(Scanner sc, boolean opcional) {
        List<UbicacionTaller> list = tallerService.selectAll();
        if (list.isEmpty()) {
            return null;
        }
        System.out.println("\nTalleres disponibles:");
        for (UbicacionTaller t : list) {
            System.out.println("  [" + t.getTallerId() + "] " + t.getMunicipioTaller() + " - " + t.getDireccionTaller() + " (Mecánico: " + t.getNombreMecanico() + ")");
        }
        while (true) {
            int id = readInt(sc, "Ingrese el ID del Taller" + (opcional ? " (o 0 para omitir): " : ": "), 0);
            if (id == 0 && opcional) return null;
            UbicacionTaller found = tallerService.selectById(id);
            if (found != null) return found;
            System.out.println("⚠️ ID no válido. Seleccione uno de la lista.");
        }
    }

    private Arreglos seleccionarArreglo(Scanner sc) {
        List<Arreglos> list = arreglosService.selectAll();
        if (list.isEmpty()) {
            System.out.println("⚠️ No hay arreglos registrados.");
            return null;
        }
        System.out.println("\nArreglos disponibles:");
        for (Arreglos a : list) {
            String placa = (a.getMoto() != null) ? a.getMoto().getPlaca() : "N/A";
            System.out.println("  [" + a.getArreglosId() + "] " + a.getDescripcionArreglos() + " | Moto: " + placa + " (" + a.getEstadoArreglo() + ")");
        }
        while (true) {
            int id = readInt(sc, "Ingrese el ID del Arreglo (o 0 para cancelar): ", 0);
            if (id == 0) return null;
            Arreglos found = arreglosService.selectById(id);
            if (found != null) return found;
            System.out.println("⚠️ ID no válido. Seleccione uno de la lista.");
        }
    }

    // =========================================================================
    // UTILIDADES DE ENTRADA POR CONSOLA CON SCANNER
    // =========================================================================
    private int readInt(Scanner sc, String prompt, int defaultValue) {
        while (true) {
            System.out.print(prompt);
            String line = sc.nextLine().trim();
            if (line.isEmpty()) {
                return defaultValue;
            }
            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("❌ Por favor ingrese un número entero válido.");
            }
        }
    }

    private double readDouble(Scanner sc, String prompt, double defaultValue) {
        while (true) {
            System.out.print(prompt);
            String line = sc.nextLine().trim().replace("$", "").replace(",", ".");
            if (line.isEmpty()) {
                return defaultValue;
            }
            try {
                return Double.parseDouble(line);
            } catch (NumberFormatException e) {
                System.out.println("❌ Por favor ingrese un valor numérico válido.");
            }
        }
    }

    private String readString(Scanner sc, String prompt, boolean allowEmpty, String defaultValue) {
        while (true) {
            System.out.print(prompt);
            String line = sc.nextLine().trim();
            if (line.isEmpty()) {
                if (allowEmpty || defaultValue != null) {
                    return defaultValue != null ? defaultValue : "";
                }
                System.out.println("❌ Este campo no puede estar vacío.");
                continue;
            }
            return line;
        }
    }

    private LocalDate readDate(Scanner sc, String prompt, LocalDate defaultValue) {
        while (true) {
            System.out.print(prompt);
            String line = sc.nextLine().trim();
            if (line.isEmpty()) {
                return defaultValue != null ? defaultValue : LocalDate.now();
            }
            try {
                return LocalDate.parse(line, DATE_FORMATTER);
            } catch (DateTimeParseException e) {
                System.out.println("❌ Formato de fecha inválido. Debe ser AAAA-MM-DD (ejemplo: 2026-10-31).");
            }
        }
    }
}
