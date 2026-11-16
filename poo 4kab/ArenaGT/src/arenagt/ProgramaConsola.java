package arenagt;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;

/** Menú de la aplicación. Las reglas de los deportes están en sus propias clases. */
public class ProgramaConsola {
    private final SistemaArena sistema;
    private final Scanner entrada;

    public ProgramaConsola(SistemaArena sistema, Scanner entrada) {
        if (sistema == null || entrada == null) throw new IllegalArgumentException("Falta el sistema o el lector.");
        this.sistema = sistema;
        this.entrada = entrada;
    }

    public static void main(String[] args) {
        SistemaArena sistema = new SistemaArena();
        boolean vacio = args.length == 1 && args[0].equals("--vacio");
        boolean resumen = args.length == 1 && args[0].equals("--resumen");
        if (args.length > 1 || (args.length == 1 && !vacio && !resumen)) {
            System.out.println("Uso: java -cp out arenagt.ProgramaConsola [--vacio | --resumen]");
            return;
        }
        if (!vacio) sistema.cargarDatosIniciales();
        ProgramaConsola programa = new ProgramaConsola(sistema, new Scanner(System.in, "UTF-8"));
        if (resumen) {
            programa.mostrarTorneos();
            for (Torneo torneo : sistema.getTorneos()) programa.mostrarTabla(torneo);
            programa.mostrarReportes();
            programa.mostrarRanking();
        } else programa.ejecutar();
    }

    public void ejecutar() {
        System.out.println("ARENA GT - Organización de torneos");
        System.out.println("Los datos se guardan mientras el programa está abierto.");
        boolean continuar = true;
        while (continuar) {
            System.out.println("\n1. Registrar participante\n2. Crear torneo\n3. Cotizar inscripción");
            System.out.println("4. Inscribir participante\n5. Cerrar inscripciones\n6. Programar o reprogramar evento");
            System.out.println("7. Registrar resultado\n8. Consultar torneos y eventos\n9. Consultar participantes");
            System.out.println("10. Consultar calendario\n11. Tabla de posiciones\n12. Ingresos y premios\n13. Ranking de empresas\n0. Salir");
            try {
                int opcion = leerEntero("Opción: ");
                switch (opcion) {
                    case 1:
                        sistema.registrarParticipante(leerTexto("Código: "), leerTexto("Nombre: "), leerTexto("Empresa: "));
                        System.out.println("Participante registrado.");
                        break;
                    case 2:
                        mostrarDisciplinas();
                        sistema.crearTorneo(leerTexto("Código del torneo: "), leerTexto("Nombre del torneo: "), leerTexto("Código de disciplina: "));
                        System.out.println("Torneo creado. Ya puede inscribir participantes.");
                        break;
                    case 3:
                        mostrarDisciplinas();
                        String disciplina = leerTexto("Código de disciplina: ");
                        int cantidad = leerEntero("Cantidad de participantes: ");
                        System.out.println("Cotización: " + dinero(sistema.cotizar(disciplina, cantidad)));
                        break;
                    case 4:
                        sistema.inscribir(leerTexto("Código del torneo: "), leerTexto("Código del participante: "));
                        System.out.println("Inscripción aceptada y cuota cobrada.");
                        break;
                    case 5:
                        String codigoTorneo = leerTexto("Código del torneo: ");
                        sistema.cerrarInscripciones(codigoTorneo);
                        System.out.println("Inscripciones cerradas. Estos son los eventos generados:");
                        mostrarEventos(sistema.buscarTorneo(codigoTorneo));
                        break;
                    case 6:
                        String codigoEvento = leerTexto("Código del evento: ");
                        LocalDate fecha = leerFecha("Fecha (AAAA-MM-DD): ");
                        int hora = leerEntero("Hora completa (0 a 23): ");
                        String sede = leerTexto("Sede: ");
                        sistema.programarEvento(codigoEvento, fecha, hora, sede);
                        System.out.println("Evento programado.");
                        break;
                    case 7:
                        Torneo torneo = sistema.buscarTorneo(leerTexto("Código del torneo: "));
                        Evento evento = torneo.buscarEvento(leerTexto("Código del evento: "));
                        if (evento.getFecha() == null || evento.estaFinalizado()) {
                            throw new IllegalStateException("Solo se registra resultado a un evento programado sin resultado anterior.");
                        }
                        sistema.registrarResultado(torneo.getCodigo(), evento.getCodigo(), leerResultado(evento));
                        System.out.println("Resultado guardado.");
                        if (torneo.getEstado().equals("Finalizado")) {
                            System.out.println("El torneo finalizó. Campeón: " + torneo.getCampeon().getNombre());
                            System.out.println("Premio entregado: " + dinero(torneo.getPremio()));
                        }
                        break;
                    case 8: mostrarTorneos(); break;
                    case 9:
                        if (sistema.getParticipantes().isEmpty()) System.out.println("No hay participantes registrados.");
                        for (Participante participante : sistema.getParticipantes()) System.out.println(participante);
                        break;
                    case 10: consultarCalendario(); break;
                    case 11: mostrarTabla(sistema.buscarTorneo(leerTexto("Código del torneo: "))); break;
                    case 12: mostrarReportes(); break;
                    case 13: mostrarRanking(); break;
                    case 0: continuar = false; break;
                    default: System.out.println("Esa opción no existe.");
                }
            } catch (IllegalArgumentException | IllegalStateException error) {
                System.out.println("No se pudo realizar la operación: " + error.getMessage());
            } catch (ArithmeticException error) {
                System.out.println("El resultado es demasiado grande. No se cambió la tabla.");
            } catch (NoSuchElementException error) {
                // También permite salir correctamente si se termina la entrada de consola.
                continuar = false;
            }
        }
        System.out.println("Hasta luego.");
    }

    private String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return entrada.nextLine();
    }

    private int leerEntero(String mensaje) {
        while (true) {
            try { return Integer.parseInt(leerTexto(mensaje).trim()); }
            catch (NumberFormatException error) { System.out.println("Escriba un número entero válido."); }
        }
    }

    private LocalDate leerFecha(String mensaje) {
        while (true) {
            try { return LocalDate.parse(leerTexto(mensaje).trim()); }
            catch (DateTimeParseException error) { System.out.println("Fecha inválida. Use AAAA-MM-DD, por ejemplo 2026-12-01."); }
        }
    }

    private Map<String, String> leerResultado(Evento evento) {
        System.out.println("Participantes:");
        for (Participante participante : evento.getParticipantes()) System.out.println("  " + participante);
        System.out.println("Marcadores: enteros no negativos. Tiempo: segundos con punto decimal o DQ.");
        System.out.println("Si no hubo empate en goles, deje vacíos los campos de penales.");
        Map<String, String> datos = new LinkedHashMap<>();
        for (String campo : evento.camposResultado()) datos.put(campo, leerTexto(campo + ": ").trim());
        return datos;
    }

    private void mostrarDisciplinas() {
        for (Map.Entry<String, Disciplina> entrada : sistema.getDisciplinas().entrySet()) {
            Disciplina d = entrada.getValue();
            System.out.println(entrada.getKey() + " - " + d.getNombre() + " | " + d.getMinimo() + " a " + d.getMaximo()
                    + " participantes | " + dinero(d.getCuota()));
        }
    }

    private String dinero(double monto) { return String.format(Locale.US, "Q%.2f", monto); }

    private void mostrarTorneos() {
        if (sistema.getTorneos().isEmpty()) System.out.println("No hay torneos registrados.");
        for (Torneo torneo : sistema.getTorneos()) {
            System.out.println("\n" + torneo.getCodigo() + " - " + torneo.getNombre() + " | " + torneo.getDisciplina().getNombre()
                    + " | " + torneo.getEstado() + " | Inscritos: " + torneo.getParticipantes().size());
            mostrarEventos(torneo);
        }
    }

    private void mostrarEvento(Evento evento) {
        String horario = evento.getFecha() == null ? "Sin programar"
                : evento.getFecha() + " " + String.format(Locale.US, "%02d:00", evento.getHora()) + " | " + evento.getSede();
        StringBuilder nombres = new StringBuilder();
        for (Participante participante : evento.getParticipantes()) {
            if (nombres.length() > 0) nombres.append(" / ");
            nombres.append(participante.getNombre());
        }
        System.out.println(evento.getCodigo() + " | " + evento.getTipo() + " | " + horario + " | " + evento.getEstado());
        System.out.println("  " + nombres);
        if (evento.estaFinalizado()) System.out.println("  Resultado: " + evento.getResultado());
    }

    private void mostrarEventos(Torneo torneo) {
        if (torneo.getEventos().isEmpty()) System.out.println("Todavía no tiene eventos.");
        for (Evento evento : new Calendario(torneo.getEventos())) mostrarEvento(evento);
        for (Evento evento : torneo.getEventos()) if (evento.getFecha() == null) mostrarEvento(evento);
    }

    private void consultarCalendario() {
        System.out.println("1. General\n2. De un torneo\n3. De un participante");
        int opcion = leerEntero("Calendario: ");
        Calendario calendario;
        switch (opcion) {
            case 1: calendario = sistema.calendarioGeneral(); break;
            case 2: calendario = sistema.calendarioTorneo(leerTexto("Código del torneo: ")); break;
            case 3: calendario = sistema.calendarioParticipante(leerTexto("Código del participante: ")); break;
            default: throw new IllegalArgumentException("Opción de calendario inválida.");
        }
        if (calendario.size() == 0) System.out.println("No hay eventos programados.");
        for (Evento evento : calendario) mostrarEvento(evento);
    }

    private void mostrarTabla(Torneo torneo) {
        System.out.println("\nTABLA " + torneo.getCodigo() + " - " + torneo.getNombre());
        System.out.printf("%-6s %-24s %7s %10s %10s%n", "Código", "Participante", "Puntos", "Diferencia", "Victorias");
        List<Posicion> tabla = torneo.getTabla();
        for (Posicion fila : tabla) {
            System.out.printf("%-6s %-24s %7d %10d %10d%n", fila.getParticipante().getCodigo(), fila.getParticipante().getNombre(),
                    fila.getPuntos(), fila.getDiferencia(), fila.getVictorias());
        }
        if (tabla.isEmpty()) System.out.println("Todavía no hay inscritos.");
        if (torneo.getCampeon() != null) {
            System.out.println("Campeón: " + torneo.getCampeon().getNombre() + " | Premio calculado: " + dinero(torneo.getPremio()));
        }
    }

    private void mostrarReportes() {
        System.out.println("\nINGRESOS POR INSCRIPCIONES");
        for (Map.Entry<String, Double> entrada : sistema.getIngresosPorDisciplina().entrySet()) {
            System.out.println(sistema.buscarDisciplina(entrada.getKey()).getNombre() + ": " + dinero(entrada.getValue()));
        }
        System.out.println("Total de ingresos: " + dinero(sistema.getIngresosTotales()));
        System.out.println("PREMIOS ENTREGADOS DURANTE ESTA EJECUCIÓN");
        if (sistema.getPremiosEntregados().isEmpty()) System.out.println("No hay entregas registradas.");
        for (Map.Entry<String, Double> entrada : sistema.getPremiosEntregados().entrySet()) {
            Torneo torneo = sistema.buscarTorneo(entrada.getKey());
            System.out.println(torneo.getCodigo() + " | " + torneo.getCampeon().getNombre() + " | " + dinero(entrada.getValue()));
        }
        System.out.println("Total de premios entregados: " + dinero(sistema.getTotalPremios()));
    }

    private void mostrarRanking() {
        System.out.println("\nRANKING DE EMPRESAS");
        if (sistema.rankingEmpresas().isEmpty()) System.out.println("No hay empresas registradas.");
        for (Map.Entry<String, Integer> entrada : sistema.rankingEmpresas().entrySet()) {
            System.out.println(entrada.getKey() + ": " + entrada.getValue() + " puntos");
        }
    }
}
