package example.hotel;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.Scanner;

public class Main {

    static String[] hotelNombres = new String[100];
    static String[] tipoAlojamiento = new String[100];
    static String[] ciudades = new String[100];
    static int[] calificaciones = new int[100];
    static double[] precios = new double[100];

    static int[] habitacionHotelID = new int[500];
    static String[] habitacionTipos = new String[500];
    static String[] habitacionCaracteristicas = new String[500];
    static double[] habitacionPrecios = new double[500];
    static boolean[][][] habitacionDisponibilidad = new boolean[500][10][366]; // [Habitación][Año][Día del año]

    static String[] nombresReservas = new String[100];
    static String[] emailsReservas = new String[100];
    static LocalDate[] fechaNacimientoReservas = new LocalDate[100];
    static String[] hotelesReservas = new String[100];
    static String[] habitacionesReservas = new String[100];
    static LocalDate[] fechasInicioReservas = new LocalDate[100];
    static LocalDate[] fechasFinReservas = new LocalDate[100];
    static int[] cantidadAdultosReservas = new int[100];
    static int[] cantidadNiniosReservas = new int[100];
    static int[] cantidadHabitacionesReservas = new int[100];
    static int totalReservas = 0; // Contador de reservas

    public static void main(String[] args) {
        inicializarDatos();
        Scanner scanner = new Scanner(System.in);
        System.out.println("¡Bienvenido a HotelApp!");
        mostrarMenu(scanner);
    }

    /* Metodos de Inicialización de Datos */

    public static void inicializarDatos() {
        inicializarHoteles();
        inicializarHabitaciones();
        inicializarDisponibilidad();
    }

    public static void inicializarHoteles() {
        agregarHotel(0, "Hotel Buenos Aires", "Hotel", "Buenos Aires", 4, 50.0);
        agregarHotel(1, "Hotel Mar del Plata", "Hotel", "Mar del Plata", 3, 60.0);
        agregarHotel(2, "Apartamento Cordoba", "Apartamento", "Cordoba", 2, 40.0);
        agregarHotel(3, "Finca Rosario", "Finca", "Rosario", 5, 100.0);
        agregarHotel(4, "Día de Sol Buenos Aires", "Día de Sol", "Buenos Aires", 4, 70.0);
        agregarHotel(5, "Día de Sol Mar del Plata", "Día de Sol", "Mar del Plata", 3, 80.0);
    }

    public static void agregarHotel(int index, String nombre, String alojamiento, String ciudad, int calificacion, double precio) {
        hotelNombres[index] = nombre;
        tipoAlojamiento[index] = alojamiento;
        ciudades[index] = ciudad;
        calificaciones[index] = calificacion;
        precios[index] = precio;
    }

    public static void inicializarHabitaciones() {
        agregarHabitacion(0, 0, "Single", "2 camas simples, aire acondicionado, WiFi", 50.0);
        agregarHabitacion(1, 0, "Double", "1 cama doble, aire acondicionado, TV", 75.0);
        agregarHabitacion(2, 3, "Suite", "1 cama king size, jacuzzi, TV de pantalla plana", 120.0);
        agregarHabitacion(3, 2, "Single", "2 camas simples, desayuno incluido, WiFi", 55.0);
        agregarHabitacion(4, 4, "Activities", "Piscinas, excursiones y juegos familiares", 150.0);
        agregarHabitacion(5, 5, "Activities", "Spa, actividades al aire libre y recreación", 140.0);

    }

    public static void agregarHabitacion(int index, int hotelID, String tipo, String caracteristicas, double precio) {
        habitacionHotelID[index] = hotelID;
        habitacionTipos[index] = tipo;
        habitacionCaracteristicas[index] = caracteristicas;
        habitacionPrecios[index] = precio;
    }

    public static void inicializarDisponibilidad() {
        for (int i = 0; i < habitacionDisponibilidad.length; i++) {
            for (int j = 0; j < 366; j++) {
                for (int k = 0; k < 10; k++) { // Máximo 10 años distintos de disponibilidad
                    habitacionDisponibilidad[i][k][j] = true;
                }
            }
        }
    }

    public static void mostrarMenu(Scanner scanner) {
        int opcion = 0;
        do {
            System.out.println("\nSeleccione una opción:");
            System.out.println("1. Realizar una reserva");
            System.out.println("2. Actualizar una reserva");
            System.out.println("3. Cancelar una reserva");
            System.out.println("4. Mostrar todas las reservas");
            System.out.println("5. Salir");

            opcion = solicitarNumero(scanner, "Ingrese su opción: ");
            switch (opcion) {
                case 1 -> realizarReserva(scanner);
                case 2 -> actualizarReserva(scanner);
                case 3 -> cancelarReserva(scanner);
                case 4 -> mostrarReservas(); // Nueva opción para mostrar reservas
                case 5 -> System.out.println("Saliendo de la aplicación...");
                default -> System.out.println("Opción inválida.");
            }
        } while (opcion != 5);
    }

    /* Metodos de Creacion de Reserva */
    public static void realizarReserva(Scanner scanner) {
        System.out.println("\n--- Realizar una Reserva ---");

        // Selección de ciudad
        System.out.println("Seleccione una ciudad:");
        String[] ciudadesUnicas = eliminarDuplicados(ciudades);
        int ciudadElegida = listarOpciones(scanner, ciudadesUnicas);

        // Selección de tipo de alojamiento
        System.out.println("Seleccione el tipo de alojamiento:");
        String[] tiposAlojamientoUnicos = eliminarDuplicados(tipoAlojamiento);
        int tipoAlojamientoElegido = listarOpciones(scanner, tiposAlojamientoUnicos);

        // Solicitar cantidad de adultos y ninios
        int cantidadAdultos = solicitarNumero(scanner, "Ingrese la cantidad de adultos: ");
        int cantidadNinios = solicitarNumero(scanner, "Ingrese la cantidad de niños: ");

        // Solicitar cantidad de habitaciones
        int cantidadHabitaciones = solicitarNumero(scanner, "Ingrese la cantidad de habitaciones: ");

        // Solicitar datos de la estadía
        LocalDate fechaInicio = solicitarFecha(scanner, "Ingrese la fecha de ingreso (dd/MM/yyyy): ");
        LocalDate fechaSalida = solicitarFecha(scanner, "Ingrese la fecha de salida (dd/MM/yyyy): ");

        while (!fechaSalida.isAfter(fechaInicio)) {
            System.out.println("La fecha de salida debe ser posterior a la fecha de ingreso.");
            fechaSalida = solicitarFecha(scanner, "Ingrese la fecha de salida nuevamente (dd/MM/yyyy): ");
        }

        // Filtrar hoteles disponibles
        String[] hotelesDisponibles = filtrarHoteles(ciudadElegida, tipoAlojamientoElegido, ciudadesUnicas, tiposAlojamientoUnicos); // Pasar ciudadesUnicas y tiposAlojamientoUnicos como parámetro

        if (hotelesDisponibles.length == 0) {
            System.out.println("No hay hoteles disponibles con las opciones seleccionadas.");
            return;
        }

        System.out.println("Seleccione un hotel disponible:");
        int hotelElegido = listarOpciones(scanner, hotelesDisponibles);

        // Filtrar habitaciones disponibles
        String[] habitaciones = filtrarHabitaciones(hotelElegido, hotelesDisponibles);

        if (habitaciones.length == 0) {
            System.out.println("No hay habitaciones disponibles en el hotel seleccionado.");
            return;
        }

        System.out.println("Seleccione una habitación:");
        int habitacionElegida = listarOpciones(scanner, habitaciones);

        // Cálculo de costos (precio total, ajustes y precio final)
        double precioPorNoche = habitacionPrecios[habitacionElegida];
        long diasEstadia = ChronoUnit.DAYS.between(fechaInicio, fechaSalida);
        double precioTotal = precioPorNoche * diasEstadia;

        double ajuste = calcularDescuento(fechaInicio, fechaSalida, precioPorNoche);
        double precioFinal = precioTotal + (ajuste * diasEstadia);

        // Mostrar el resumen
        System.out.println("\n--- Resumen de la Reserva ---");
        System.out.printf("Hotel: %s%n", hotelNombres[habitacionHotelID[habitacionElegida]]);
        System.out.printf("Habitación: %s - Características: %s%n", habitacionTipos[habitacionElegida], habitacionCaracteristicas[habitacionElegida]);
        System.out.println("Cantidad de habitaciones: " + cantidadHabitaciones);
        System.out.printf("Precio por noche: $%.2f%n", precioPorNoche);
        System.out.printf("Días de estadía: %d%n", diasEstadia);
        System.out.printf("Precio total (antes de ajustes): $%.2f%n", precioTotal);
        System.out.printf("Ajuste por descuento/aumento (por noche): %s $%.2f%n", ajuste > 0 ? "+" : "-", Math.abs(ajuste));
        System.out.printf("Precio final: $%.2f%n", precioFinal);

        // Confirmar la reserva
        System.out.println("\n¿Desea confirmar esta reserva? (Si/No): ");
        String confirmacion = scanner.next();
        if (!confirmacion.equalsIgnoreCase("Si")) {
            System.out.println("Reserva cancelada.");
            return;
        }

        // Solicitar datos personales
        System.out.println("\n--- Complete los datos personales para finalizar la reserva ---");
        System.out.print("Ingrese su Nombre: ");
        scanner.nextLine();  // Limpiar el buffer
        String nombre = scanner.nextLine();

        System.out.print("Ingrese su Apellido: ");
        String apellido = scanner.nextLine();

        System.out.print("Ingrese su Fecha de Nacimiento (dd/MM/yyyy): ");
        LocalDate fechaNacimiento = null;
        while (fechaNacimiento == null) {
            try {
                fechaNacimiento = LocalDate.parse(scanner.nextLine(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            } catch (DateTimeParseException e) {
                System.out.println("Formato de fecha inválido. Intente de nuevo.");
            }
        }

        System.out.print("Ingrese su Email: ");
        String email = scanner.nextLine();

        System.out.print("Ingrese su Nacionalidad: ");
        String nacionalidad = scanner.nextLine();

        System.out.print("Ingrese su Número de Teléfono: ");
        String telefono = scanner.nextLine();

        System.out.print("Ingrese su Hora Aproximada de Llegada (HH:mm): ");
        String horaLlegada = scanner.nextLine();

        // Guardar la reserva
        nombresReservas[totalReservas] = nombre + " " + apellido;
        fechaNacimientoReservas[totalReservas] = fechaNacimiento;
        emailsReservas[totalReservas] = email;
        hotelesReservas[totalReservas] = hotelNombres[habitacionHotelID[habitacionElegida]];
        habitacionesReservas[totalReservas] = habitacionTipos[habitacionElegida];
        fechasInicioReservas[totalReservas] = fechaInicio;
        fechasFinReservas[totalReservas] = fechaSalida;
        cantidadAdultosReservas[totalReservas] = cantidadAdultos;
        cantidadNiniosReservas[totalReservas] = cantidadNinios;
        cantidadHabitacionesReservas[totalReservas] = cantidadHabitaciones;

        totalReservas++;

        System.out.println("\nReserva confirmada con éxito. ¡Gracias por usar HotelApp!");
    }

    public static void cancelarReserva(Scanner scanner) {
        System.out.println("\n--- Cancelar una Reserva ---");

        // Solicitar email y fecha de nacimiento para buscar la reserva
        System.out.print("Ingrese su Email: ");
        String email = scanner.nextLine();

        LocalDate fechaNacimiento = null;
        while (fechaNacimiento == null) {
            System.out.print("Ingrese su Fecha de Nacimiento (dd/MM/yyyy): ");
            try {
                fechaNacimiento = LocalDate.parse(scanner.nextLine(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            } catch (DateTimeParseException e) {
                System.out.println("Formato de fecha inválido. Intente de nuevo.");
            }
        }

        // Buscar la reserva
        int reservaIndex = -1;
        for (int i = 0; i < totalReservas; i++) {
            if (emailsReservas[i] != null && emailsReservas[i].equalsIgnoreCase(email) &&
                    fechaNacimientoReservas[i] != null && fechaNacimientoReservas[i].equals(fechaNacimiento)) {
                reservaIndex = i;
                break;
            }
        }

        // Si la reserva no fue encontrada
        if (reservaIndex == -1) {
            System.out.println("No se encontró ninguna reserva asociada a los datos ingresados.");
            return;
        }

        // Mostrar los detalles de la reserva encontrada
        System.out.println("\nReserva encontrada:");
        mostrarDetallesReserva(reservaIndex);

        // Confirmar la cancelación
        System.out.print("¿Está seguro de que desea cancelar esta reserva? (Si/No): ");
        String confirmacion = scanner.next();
        if (!confirmacion.equalsIgnoreCase("Si")) {
            System.out.println("Cancelación abortada.");
            return;
        }

        // Restablecer la disponibilidad de habitaciones asociadas a la reserva
        restaurarDisponibilidad(reservaIndex);

        // Eliminar la reserva desplazando los datos hacia arriba en los arreglos
        for (int i = reservaIndex; i < totalReservas - 1; i++) {
            nombresReservas[i] = nombresReservas[i + 1];
            fechaNacimientoReservas[i] = fechaNacimientoReservas[i + 1];
            emailsReservas[i] = emailsReservas[i + 1];
            hotelesReservas[i] = hotelesReservas[i + 1];
            habitacionesReservas[i] = habitacionesReservas[i + 1];
            fechasInicioReservas[i] = fechasInicioReservas[i + 1];
            fechasFinReservas[i] = fechasFinReservas[i + 1];
            cantidadAdultosReservas[i] = cantidadAdultosReservas[i + 1];
            cantidadNiniosReservas[i] = cantidadNiniosReservas[i + 1];
            cantidadHabitacionesReservas[i] = cantidadHabitacionesReservas[i + 1];
        }

        // Restar el total de reservas
        totalReservas--;

        System.out.println("La reserva ha sido cancelada exitosamente.");
    }

    public static void restaurarDisponibilidad(int reservaIndex) {
        String hotel = hotelesReservas[reservaIndex];
        String tipoHabitacion = habitacionesReservas[reservaIndex];
        LocalDate inicio = fechasInicioReservas[reservaIndex];
        LocalDate fin = fechasFinReservas[reservaIndex];

        // Encontrar el ID del hotel y la habitación
        int hotelID = -1;
        int habitacionID = -1;

        for (int i = 0; i < hotelNombres.length; i++) {
            if (hotelNombres[i] != null && hotelNombres[i].equals(hotel)) {
                hotelID = i;
                break;
            }
        }

        for (int i = 0; i < habitacionTipos.length; i++) {
            if (habitacionHotelID[i] == hotelID && habitacionTipos[i].equals(tipoHabitacion)) {
                habitacionID = i;
                break;
            }
        }

        if (habitacionID == -1) {
            System.out.println("Error: No se pudo encontrar la habitación asociada a la reserva cancelada.");
            return;
        }

        // Restaurar la disponibilidad
        int añoRelativo = inicio.getYear() - LocalDate.now().getYear();
        for (int dia = inicio.getDayOfYear(); dia <= fin.getDayOfYear(); dia++) {
            habitacionDisponibilidad[habitacionID][añoRelativo][dia] = true;
        }
    }

    public static void realizarBloqueoHabitaciones(int habitacion, LocalDate inicio, LocalDate fin) {
        int año = inicio.getYear() - LocalDate.now().getYear(); // Año relativo
        int diaInicio = inicio.getDayOfYear();
        int diaFin = fin.getDayOfYear();

        for (int dia = diaInicio; dia <= diaFin; dia++) {
            habitacionDisponibilidad[habitacion][año][dia] = false;
        }
    }

    public static String[] eliminarDuplicados(String[] lista) {
        String[] listaUnica = new String[lista.length];
        int indiceUnico = 0;

        for (String item : lista) {
            if (item == null) {
                continue; // Ignorar elementos nulos
            }
            boolean encontrado = false;

            // Comprobar si ya está en la lista única
            for (int i = 0; i < indiceUnico; i++) {
                if (item.equals(listaUnica[i])) {
                    encontrado = true;
                    break;
                }
            }

            // Si no está duplicado, agregarlo a la lista única
            if (!encontrado) {
                listaUnica[indiceUnico++] = item;
            }
        }

        // Crear un array del tamaño exacto con los elementos únicos
        String[] resultado = new String[indiceUnico];
        System.arraycopy(listaUnica, 0, resultado, 0, indiceUnico);
        return resultado;
    }

    public static int listarOpciones(Scanner scanner, String[] opciones) {
        for (int i = 0; i < opciones.length && opciones[i] != null; i++) {
            System.out.printf("%d. %s%n", i + 1, opciones[i]);
        }
        int seleccion;
        do {
            seleccion = solicitarNumero(scanner, "Elija el número correspondiente: ") - 1;
            if (seleccion < 0 || seleccion >= opciones.length) { // Validar que la selección esté dentro del rango
                System.out.println("Opción inválida. Intente nuevamente.");
            }
        } while (seleccion < 0 || seleccion >= opciones.length); // Repetir hasta que la selección sea válida

        return seleccion;
    }

    public static String[] filtrarHoteles(int ciudadElegida, int tipoElegido, String[] ciudadesUnicas, String[] tiposAlojamientoUnicos) { // Agregar parámetro tiposAlojamientoUnicos
        String[] resultado = new String[100];
        int indice = 0;

        // Obtener la ciudad seleccionada del array ciudadesUnicas
        String ciudadSeleccionada = ciudadesUnicas[ciudadElegida];

        // Buscar el índice de la ciudad en el array ciudades
        int indiceCiudad = 0;
        for (int i = 0; i < ciudades.length; i++) {
            if (ciudades[i].equals(ciudadSeleccionada)) {
                indiceCiudad = i;
                break;
            }
        }

        // Obtener el tipo de alojamiento seleccionado del array tiposAlojamientoUnicos
        String tipoSeleccionado = tiposAlojamientoUnicos[tipoElegido];

        // Buscar el índice del tipo de alojamiento en el array tipoAlojamiento
        int indiceTipoAlojamiento = 0;
        for (int i = 0; i < tipoAlojamiento.length; i++) {
            if (tipoAlojamiento[i].equals(tipoSeleccionado)) {
                indiceTipoAlojamiento = i;
                break;
            }
        }

        for (int i = 0; i < hotelNombres.length && hotelNombres[i] != null; i++) {
            // Comparar los índices de ciudad y tipo de alojamiento
            if (ciudades[i].equals(ciudades[indiceCiudad]) && tipoAlojamiento[i].equals(tipoAlojamiento[indiceTipoAlojamiento])) {
                resultado[indice++] = String.format("%s - Precio Base: $%.2f - Calificación: %d estrellas",
                        hotelNombres[i], precios[i], calificaciones[i]);
            }
        }

        System.out.printf("Hoteles disponibles para ciudad: %s, tipo: %s: %d resultado(s)%n", ciudadSeleccionada, tipoSeleccionado, indice);

        // Reducir el tamaño para evitar valores nulos
        String[] hotelesFiltrados = new String[indice];
        System.arraycopy(resultado, 0, hotelesFiltrados, 0, indice);
        return hotelesFiltrados;
    }

    public static String[] filtrarHabitaciones(int hotelElegido, String[] hotelesDisponibles) {
        // Validar si el array hotelesDisponibles es vacío o nulo
        if (hotelesDisponibles == null || hotelesDisponibles.length == 0) {
            System.out.println("No hay hoteles disponibles.");
            return new String[0];
        }

        // Validar que el índice del hotel elegido está dentro de los límites
        if (hotelElegido < 0 || hotelElegido >= hotelesDisponibles.length) {
            System.out.println("Error: Selección de hotel fuera de rango.");
            return new String[0];
        }

        String[] resultado = new String[100]; // Array con resultados filtrados
        int indice = 0;

        // Obtener el nombre del hotel seleccionado
        String hotelSeleccionado = hotelesDisponibles[hotelElegido]; // Índice 0-based del usuario

        // Buscar el índice correspondiente en hotelNombres
        int indiceHotel = -1;
        for (int i = 0; i < hotelNombres.length && hotelNombres[i] != null; i++) {
            if (hotelSeleccionado.contains(hotelNombres[i])) { // Comprobación flexible
                indiceHotel = i;
                break;
            }
        }

        if (indiceHotel == -1) {
            System.out.println("Error: No se encontró el hotel seleccionado en la base de datos.");
            return new String[0];
        }

        // Filtrar habitaciones asociadas al hotel seleccionado
        for (int i = 0; i < habitacionHotelID.length && habitacionTipos[i] != null; i++) {
            // Validar que la habitación pertenece al hotel seleccionado
            if (habitacionHotelID[i] == indiceHotel) {
                // Aplicar reglas adicionales para "Día de Sol"
                if (tipoAlojamiento[indiceHotel].equalsIgnoreCase("Día de Sol")) {
                    if (habitacionTipos[i].equalsIgnoreCase("Activities")) {
                        resultado[indice++] = String.format("%s - Características: %s - Precio: $%.2f",
                                habitacionTipos[i], habitacionCaracteristicas[i], habitacionPrecios[i]);
                    }
                } else {
                    // Habitaciones para otros tipos de alojamiento
                    resultado[indice++] = String.format("%s - Características: %s - Precio: $%.2f",
                            habitacionTipos[i], habitacionCaracteristicas[i], habitacionPrecios[i]);
                }
            }
        }

        if (indice == 0) {
            System.out.println("No hay habitaciones disponibles en el hotel seleccionado.");
            return new String[0];
        }

        // Crear un array ajustado al tamaño de resultados
        String[] habitacionesFiltradas = new String[indice];
        System.arraycopy(resultado, 0, habitacionesFiltradas, 0, indice);
        return habitacionesFiltradas;
    }

    public static double calcularDescuento(LocalDate inicio, LocalDate fin, double precioBase) {
        boolean aumento15 = false;
        boolean aumento10 = false;
        boolean descuento8 = false;

        // Iterar por cada día de la estadía
        LocalDate fecha = inicio;
        while (!fecha.isAfter(fin)) {
            int dia = fecha.getDayOfMonth();

            // Verificar si pertenece a los últimos 5 días del mes
            if (dia >= fecha.lengthOfMonth() - 4) {
                aumento15 = true;
            }

            // Verificar si pertenece al rango del 10 al 15 del mes
            if (dia >= 10 && dia <= 15) {
                aumento10 = true;
            }

            // Verificar si pertenece al rango del 5 al 10 del mes
            if (dia >= 5 && dia < 10) {
                descuento8 = true;
            }

            // Avanzar al próximo día
            fecha = fecha.plusDays(1);
        }

        // Aplicar las reglas de descuento/aumento
        if (aumento15) {
            return precioBase * 0.15; // Aumento del 15%
        } else if (aumento10) {
            return precioBase * 0.10; // Aumento del 10%
        } else if (descuento8) {
            return -precioBase * 0.08; // Descuento del 8%
        }

        // Si no se cumple ninguna condición, no hay ajuste
        return 0;
    }

    public static LocalDate solicitarFecha(Scanner scanner, String mensaje) {
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate fecha = null;
        while (fecha == null) {
            System.out.print(mensaje);
            try {
                fecha = LocalDate.parse(scanner.next(), formato);
            } catch (Exception e) {
                System.out.println("Formato de fecha inválido. Intente de nuevo.");
            }
        }
        return fecha;
    }

    public static int solicitarNumero(Scanner scanner, String mensaje) {
        System.out.print(mensaje);
        while (!scanner.hasNextInt()) {
            System.out.println("Por favor, ingrese un número válido.");
            scanner.next();
        }
        int numero = scanner.nextInt();
        scanner.nextLine(); // Consumir la nueva línea // <-- Corrección aquí
        return numero;
    }

    /* Metodos de Actualización de Reservas */

    public static void actualizarReserva(Scanner scanner) {
        System.out.println("\n--- Actualizar una Reserva ---");

        // Solicitar email y fecha de nacimiento del usuario para la búsqueda
        System.out.print("Ingrese su Email: ");
        String email = scanner.nextLine();

        DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate fechaNacimiento = null;
        while (fechaNacimiento == null) {
            System.out.print("Ingrese su Fecha de Nacimiento (dd/MM/yyyy): ");
            try {
                fechaNacimiento = LocalDate.parse(scanner.nextLine(), formatoFecha);
            } catch (DateTimeParseException e) {
                System.out.println("Formato de fecha inválido. Por favor, intente de nuevo.");
            }
        }

        // Buscar la reserva asociada
        int reservaIndex = -1;
        for (int i = 0; i < totalReservas; i++) {
            if (emailsReservas[i] != null && fechaNacimientoReservas[i] != null &&
                    emailsReservas[i].equalsIgnoreCase(email) &&
                    fechaNacimientoReservas[i].equals(fechaNacimiento)) {
                reservaIndex = i;
                break;
            }
        }

        // Si no se encuentra ninguna reserva
        if (reservaIndex == -1) {
            System.out.println("No se encontró ninguna reserva asociada a los datos ingresados.");
            return;
        }

        // Mostrar los datos de la reserva encontrada
        System.out.println("\nReserva encontrada:");
        mostrarDetallesReserva(reservaIndex);

        // Obtener los hoteles disponibles para la reserva
        String[] hotelesDisponibles = obtenerHotelesDisponiblesParaReserva(reservaIndex);

        // Ofrecer opciones de actualización
        System.out.println("\n¿Qué desea actualizar?");
        System.out.println("1. Fechas de Estadía");
        System.out.println("2. Hotel");
        System.out.println("3. Tipo de Habitación");
        System.out.println("4. Cantidad de Habitaciones");
        System.out.println("5. Datos Personales");
        System.out.println("6. Cancelar");
        while (true) {
            int opcion = solicitarNumero(scanner, "Ingrese su opción: ");
            switch (opcion) {
                case 1 -> actualizarFechas(scanner, reservaIndex);
                case 2 -> actualizarHotel(scanner, reservaIndex);
                case 3 -> actualizarTipoHabitacion(scanner, reservaIndex, hotelesDisponibles);
                case 4 -> actualizarCantidadHabitaciones(scanner, reservaIndex);
                case 5 -> actualizarDatosPersonales(scanner, reservaIndex);
                case 6 -> {
                    System.out.println("Actualización cancelada.");
                    return; // Salir del menú
                }
                default -> System.out.println("Opción inválida. Intente nuevamente.");
            }
        }
    }

    public static String[] obtenerHotelesDisponiblesParaReserva(int reservaIndex) {
        String ciudadReserva = null;
        String tipoAlojamientoReserva = null;

        for (int i = 0; i < hotelNombres.length; i++) {
            if (hotelNombres[i] != null && hotelNombres[i].equals(hotelesReservas[reservaIndex])) {
                ciudadReserva = ciudades[i];
                tipoAlojamientoReserva = tipoAlojamiento[i];
                break;
            }
        }

        if (ciudadReserva == null || tipoAlojamientoReserva == null) {
            System.out.println("Error: No se pudieron recuperar los datos del hotel asociado a la reserva.");
            return new String[0]; // Devolver un array vacío si hay un error
        }

        // Filtrar los hoteles disponibles basados en la ciudad y tipo de alojamiento
        String[] hotelesDisponibles = new String[100];
        int indice = 0;
        for (int i = 0; i < hotelNombres.length && hotelNombres[i] != null; i++) {
            if (ciudades[i].equals(ciudadReserva) && tipoAlojamiento[i].equals(tipoAlojamientoReserva)) {
                hotelesDisponibles[indice++] = hotelNombres[i];
            }
        }

        // Reducir el tamaño del array de hoteles disponibles
        String[] hotelesFiltrados = new String[indice];
        System.arraycopy(hotelesDisponibles, 0, hotelesFiltrados, 0, indice);

        return hotelesFiltrados;
    }

    public static void mostrarDetallesReserva(int reservaIndex) {
        System.out.println("Detalles de la Reserva:");
        System.out.printf("Nombre: %s%n", nombresReservas[reservaIndex]);
        System.out.printf("Email: %s%n", emailsReservas[reservaIndex]);
        System.out.printf("Fecha de Nacimiento: %s%n", fechaNacimientoReservas[reservaIndex]);
        System.out.printf("Hotel: %s%n", hotelesReservas[reservaIndex]);
        System.out.printf("Tipo de Habitación: %s%n", habitacionesReservas[reservaIndex]);
        System.out.printf("Fecha Inicio: %s%n", fechasInicioReservas[reservaIndex]);
        System.out.printf("Fecha Fin: %s%n", fechasFinReservas[reservaIndex]);
        System.out.printf("Cantidad de Habitaciones: %d%n", cantidadHabitacionesReservas[reservaIndex]);
        System.out.printf("Cantidad de Adultos: %d%n", cantidadAdultosReservas[reservaIndex]);
        System.out.printf("Cantidad de Niños: %d%n", cantidadNiniosReservas[reservaIndex]);
    }

    public static void actualizarFechas(Scanner scanner, int reservaIndex) {
        System.out.println("\n--- Actualizar Fechas de Estadía ---");

        // Solicitar nuevas fechas
        LocalDate nuevaFechaInicio = solicitarFecha(scanner, "Ingrese la nueva fecha de ingreso (dd/MM/yyyy): ");
        LocalDate nuevaFechaFin = solicitarFecha(scanner, "Ingrese la nueva fecha de salida (dd/MM/yyyy): ");

        // Validar que la fecha de salida sea posterior a la fecha de ingreso
        while (!nuevaFechaFin.isAfter(nuevaFechaInicio)) {
            System.out.println("La fecha de salida debe ser posterior a la fecha de ingreso.");
            nuevaFechaFin = solicitarFecha(scanner, "Ingrese la fecha de salida nuevamente (dd/MM/yyyy): ");
        }

        // Actualizar las fechas en la reserva
        fechasInicioReservas[reservaIndex] = nuevaFechaInicio;
        fechasFinReservas[reservaIndex] = nuevaFechaFin;
        System.out.println("Fechas de estadía actualizadas exitosamente.");
    }

    public static void actualizarCantidadHabitaciones(Scanner scanner, int reservaIndex) {
        System.out.println("\n--- Actualizar Cantidad de Habitaciones ---");

        int nuevaCantidadHabitaciones = solicitarNumero(scanner, "Ingrese la nueva cantidad de habitaciones: ");
        if (nuevaCantidadHabitaciones <= 0) {
            System.out.println("La cantidad de habitaciones debe ser mayor a 0.");
        } else {
            cantidadHabitacionesReservas[reservaIndex] = nuevaCantidadHabitaciones;
            System.out.println("Cantidad de habitaciones actualizada exitosamente a: " + nuevaCantidadHabitaciones);
        }
    }

    public static void actualizarTipoHabitacion(Scanner scanner, int reservaIndex, String[] hotelesDisponibles) { // Agregar parámetro hotelesDisponibles
        System.out.println("\n--- Actualizar Tipo de Habitación ---");

        String hotelActual = hotelesReservas[reservaIndex];

        int hotelID = -1;
        for (int i = 0; i < hotelNombres.length; i++) {
            if (hotelNombres[i] != null && hotelNombres[i].equals(hotelActual)) {
                hotelID = i;
                break;
            }
        }

        if (hotelID == -1) {
            System.out.println("Error: No se encontró el hotel asociado a la reserva actual.");
            return;
        }

        // Filtrar las habitaciones disponibles en el hotel seleccionado
        String[] habitacionesDisponibles = filtrarHabitaciones(hotelID, hotelesDisponibles);

        if (habitacionesDisponibles.length == 0) {
            System.out.println("No hay habitaciones disponibles en el hotel seleccionado.");
            return;
        }

        // Mostrar opciones de habitaciones disponibles
        System.out.println("Seleccione una nueva habitación:");
        int habitacionElegida = listarOpciones(scanner, habitacionesDisponibles);

        // Actualizar el tipo de habitación en la reserva
        habitacionesReservas[reservaIndex] = habitacionesDisponibles[habitacionElegida];
        System.out.println("El tipo de habitación ha sido actualizado a: " + habitacionesReservas[reservaIndex]);
    }

    public static void actualizarHotel(Scanner scanner, int reservaIndex) {
        System.out.println("\n--- Actualizar Hotel ---");

        // Obtener los detalles del hotel actual
        String ciudadReserva = null;
        String tipoAlojamientoReserva = null;

        for (int i = 0; i < hotelNombres.length; i++) {
            if (hotelNombres[i] != null && hotelNombres[i].equals(hotelesReservas[reservaIndex])) {
                ciudadReserva = ciudades[i];
                tipoAlojamientoReserva = tipoAlojamiento[i];
                break;
            }
        }

        // Verificar que se encontraron los detalles del hotel actual
        if (ciudadReserva == null || tipoAlojamientoReserva == null) {
            System.out.println("Error: No se pudieron recuperar los datos del hotel asociado a la reserva.");
            return;
        }

        // Filtrar los hoteles disponibles basados en la ciudad y tipo de alojamiento
        String[] hotelesDisponibles = new String[100];
        int indice = 0;
        for (int i = 0; i < hotelNombres.length && hotelNombres[i] != null; i++) {
            if (ciudades[i].equals(ciudadReserva) && tipoAlojamiento[i].equals(tipoAlojamientoReserva)) {
                hotelesDisponibles[indice++] = hotelNombres[i];
            }
        }

        // Verificar si hay hoteles disponibles
        if (indice == 0) {
            System.out.println("No hay hoteles disponibles en la misma ciudad y tipo de alojamiento.");
            return;
        }

        // Reducir el tamaño del array de hoteles disponibles
        String[] hotelesFiltrados = new String[indice];
        System.arraycopy(hotelesDisponibles, 0, hotelesFiltrados, 0, indice);

        // Mostrar opciones de hoteles disponibles
        System.out.println("Seleccione un nuevo hotel:");
        int hotelElegido = listarOpciones(scanner, hotelesFiltrados);

        // Actualizar el hotel en la reserva
        hotelesReservas[reservaIndex] = hotelesFiltrados[hotelElegido];
        System.out.println("Hotel actualizado exitosamente a: " + hotelesReservas[reservaIndex]);
    }

    public static void actualizarDatosPersonales(Scanner scanner, int reservaIndex) {
        System.out.println("\n--- Actualizar Datos Personales ---");

        System.out.print("Ingrese su nuevo nombre: ");
        scanner.nextLine(); // Limpiar el buffer
        String nuevoNombre = scanner.nextLine();

        System.out.print("Ingrese su nuevo email: ");
        String nuevoEmail = scanner.nextLine();

        System.out.println("Ingrese su fecha de nacimiento (dd/MM/yyyy): ");
        LocalDate nuevaFechaNacimiento = null;
        while (nuevaFechaNacimiento == null) {
            try {
                nuevaFechaNacimiento = LocalDate.parse(scanner.next(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            } catch (DateTimeParseException e) {
                System.out.println("Formato de fecha inválido. Intente de nuevo.");
            }
        }

        // Actualizar los datos en la reserva
        nombresReservas[reservaIndex] = nuevoNombre;
        emailsReservas[reservaIndex] = nuevoEmail;
        fechaNacimientoReservas[reservaIndex] = nuevaFechaNacimiento;
        System.out.println("Datos personales actualizados exitosamente.");
    }

    public static void mostrarReservas() {
        System.out.println("\n--- Todas las Reservas ---");
        if (totalReservas == 0) {
            System.out.println("No hay reservas registradas.");
            return;
        }

        for (int i = 0; i < totalReservas; i++) {
            System.out.printf("Reserva #%d%n", i + 1);
            System.out.printf("Nombre: %s%n", nombresReservas[i]);
            System.out.printf("Email: %s%n", emailsReservas[i]);
            System.out.printf("Fecha de Nacimiento: %s%n", fechaNacimientoReservas[i]);
            System.out.printf("Hotel: %s%n", hotelesReservas[i]);
            System.out.printf("Tipo de Habitación: %s%n", habitacionesReservas[i]);
            System.out.printf("Fecha Inicio: %s%n", fechasInicioReservas[i]);
            System.out.printf("Fecha Fin: %s%n", fechasFinReservas[i]);
            System.out.printf("Cantidad de Habitaciones: %d%n", cantidadHabitacionesReservas[i]);
            System.out.printf("Cantidad de Adultos: %d%n", cantidadAdultosReservas[i]);
            System.out.printf("Cantidad de Niños: %d%n", cantidadNiniosReservas[i]);
            System.out.println();
        }
    }
}