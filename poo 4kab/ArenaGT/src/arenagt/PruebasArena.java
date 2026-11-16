package arenagt;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/** Pruebas sin librerías externas. Cada fila incluye entrada, esperado y obtenido. */
public class PruebasArena {
    private static int realizadas;
    private static int fallidas;

    public static void main(String[] args) {
        System.out.println("# Evidencia de pruebas de ArenaGT\n");
        System.out.println("Ejecución automática sobre las clases reales del programa. Cada caso usa sus propios datos o indica la carga inicial.\n");
        System.out.println("| Caso | Regla | Entrada | Esperado | Obtenido | Estado |\n|---|---|---|---|---|---|");
        cuotasYPremios();
        puntuacion();
        rechazos();
        calendarios();
        cargaInicial();
        proteccionYConsola();
        System.out.println("\nTotal: " + realizadas + ". Correctas: " + (realizadas - fallidas) + ". Fallidas: " + fallidas + ".");
        if (fallidas > 0) throw new AssertionError("Hay pruebas fallidas.");
    }

    private static void comprobar(String regla, String entrada, Object esperado, Object obtenido) {
        realizadas++;
        boolean correcto = String.valueOf(esperado).equals(String.valueOf(obtenido));
        if (!correcto) fallidas++;
        System.out.println("| " + realizadas + " | " + celda(regla) + " | " + celda(entrada) + " | " + celda(esperado)
                + " | " + celda(obtenido) + " | " + (correcto ? "OK" : "FALLÓ") + " |");
    }

    private static String celda(Object texto) { return String.valueOf(texto).replace("|", "/").replace("\n", " "); }

    private static void rechazo(SistemaArena sistema, String regla, String entrada, Runnable operacion) {
        String antes = foto(sistema);
        boolean rechazo = false;
        try { operacion.run(); }
        catch (IllegalArgumentException | IllegalStateException | ArithmeticException error) { rechazo = true; }
        comprobar(regla, entrada, "Rechazo y estado completo igual", rechazo && antes.equals(foto(sistema))
                ? "Rechazo y estado completo igual" : "Operación aceptada o cambió el estado");
    }

    // Incluye tablas, resultados, horarios, registros e importes: no solo ingresos.
    private static String foto(SistemaArena sistema) {
        StringBuilder texto = new StringBuilder();
        for (Participante p : sistema.getParticipantes()) texto.append(p.toString());
        texto.append(sistema.getIngresosPorDisciplina()).append(sistema.getPremiosEntregados());
        for (Torneo t : sistema.getTorneos()) {
            texto.append(t.getCodigo()).append(t.getEstado()).append(t.getPremio());
            for (Posicion p : t.getTabla()) texto.append(p.getParticipante().getCodigo()).append(':').append(p.getPuntos())
                    .append(':').append(p.getDiferencia()).append(':').append(p.getVictorias());
            for (Evento e : t.getEventos()) texto.append(e.getCodigo()).append(e.getEstado()).append(e.getFecha())
                    .append(e.getHora()).append(e.getSede()).append(e.getResultado());
        }
        return texto.toString();
    }

    private static SistemaArena base() {
        SistemaArena sistema = new SistemaArena();
        for (int i = 1; i <= 10; i++) {
            sistema.registrarParticipante("P" + i, "Equipo " + i, "Empresa " + ((i + 1) / 2));
        }
        return sistema;
    }

    private static SistemaArena torneo(String deporte, int cantidad) {
        SistemaArena sistema = base();
        sistema.crearTorneo("T", "Copa de prueba", deporte);
        for (int i = 1; i <= cantidad; i++) sistema.inscribir("T", "P" + i);
        sistema.cerrarInscripciones("T");
        return sistema;
    }

    private static Map<String, String> marcador(Evento evento, String a, String b) {
        Map<String, String> datos = new LinkedHashMap<>();
        int i = 0;
        for (String campo : evento.camposResultado()) {
            if (campo.endsWith(".penales")) datos.put(campo, "");
            else if (campo.endsWith(".tiempo")) datos.put(campo, String.valueOf(50 + i++));
            else datos.put(campo, i++ == 0 ? a : b);
        }
        return datos;
    }

    private static Map<String, String> datos(String... clavesYValores) {
        Map<String, String> datos = new LinkedHashMap<>();
        for (int i = 0; i < clavesYValores.length; i += 2) datos.put(clavesYValores[i], clavesYValores[i + 1]);
        return datos;
    }

    private static void cuotasYPremios() {
        String[] deportes = {"FS", "FP", "BC", "VB", "RL"};
        double[] cuotas = {600, 750, 500, 450, 300};
        int[] minimos = {4, 4, 4, 4, 3};
        int[] maximos = {8, 8, 6, 6, 8};
        for (int i = 0; i < deportes.length; i++) {
            String deporte = deportes[i];
            SistemaArena sistema = base();
            comprobar("Cuota " + deporte, deporte, cuotas[i], sistema.buscarDisciplina(deporte).getCuota());
            comprobar("Mínimo " + deporte, deporte, minimos[i], sistema.buscarDisciplina(deporte).getMinimo());
            comprobar("Máximo " + deporte, deporte, maximos[i], sistema.buscarDisciplina(deporte).getMaximo());
            String antes = foto(sistema);
            double cotizacion = sistema.cotizar(deporte, 2);
            comprobar("Cotización " + deporte, "2 participantes", cuotas[i] * 2, cotizacion);
            comprobar("Cotización no modifica", deporte, true, antes.equals(foto(sistema)));
            sistema.crearTorneo("T", "Prueba " + deporte, deporte);
            for (int j = 1; j <= minimos[i]; j++) sistema.inscribir("T", "P" + j);
            comprobar("Cobros " + deporte, minimos[i] + " inscripciones", cuotas[i] * minimos[i], sistema.getIngresosTotales());
            comprobar("Cobros por disciplina " + deporte, deporte, cuotas[i] * minimos[i], sistema.getIngresosPorDisciplina().get(deporte));
            comprobar("Estado inicial", deporte, "En inscripción", sistema.buscarTorneo("T").getEstado());
            sistema.cerrarInscripciones("T");
            Torneo torneo = sistema.buscarTorneo("T");
            comprobar("Cierre " + deporte, "Cerrar con mínimo", "En juego", torneo.getEstado());
            int esperados = deporte.equals("RL") ? 1 : minimos[i] * (minimos[i] - 1) / 2;
            comprobar("Cantidad de eventos " + deporte, minimos[i] + " inscritos", esperados, torneo.getEventos().size());
            int hora = 0;
            for (Evento evento : torneo.getEventos()) {
                sistema.programarEvento(evento.getCodigo(), LocalDate.of(2027, 1, 1), hora++, "Cancha");
                sistema.registrarResultado("T", evento.getCodigo(), marcador(evento, "3", "0"));
            }
            comprobar("Finalización automática " + deporte, "Todos los resultados", "Finalizado", torneo.getEstado());
            comprobar("Premio " + deporte, "50% x cuota x inscritos", cuotas[i] * minimos[i] / 2, torneo.getPremio());
            comprobar("Entrega " + deporte, "Último resultado", torneo.getPremio(), sistema.getTotalPremios());
            comprobar("Campeón " + deporte, "P1 gana sus eventos", "P1", torneo.getCampeon().getCodigo());
            comprobar("Premio único " + deporte, "Torneo terminado", 1, sistema.getPremiosEntregados().size());
            Evento terminado = torneo.getEventos().get(0);
            rechazo(sistema, "No duplicar premio " + deporte, "Repetir resultado", () -> sistema.registrarResultado("T", terminado.getCodigo(), marcador(terminado, "3", "0")));
            rechazo(sistema, "No inscribir en finalizado " + deporte, "P10", () -> sistema.inscribir("T", "P10"));
        }
    }

    private static void evaluar(String nombre, Disciplina disciplina, String campo, String a, String b,
                                String penalesA, String penalesB, String esperado) {
        List<Participante> pareja = base().getParticipantes().subList(0, 2);
        Map<String, String> resultado = datos("P1." + campo, a, "P2." + campo, b);
        if (penalesA != null) resultado.put("P1.penales", penalesA);
        if (penalesB != null) resultado.put("P2.penales", penalesB);
        List<Aporte> aportes = disciplina.evaluar(pareja, resultado);
        String obtenido = "";
        for (Aporte aporte : aportes) obtenido += aporte.getPuntos() + "," + aporte.getDiferencia() + "," + aporte.getVictorias() + ";";
        comprobar(nombre, a + " a " + b + (penalesA == null ? "" : " / penales " + penalesA + " a " + penalesB), esperado, obtenido);
    }

    private static void puntuacion() {
        evaluar("Fútbol victoria local", new FutbolSala(), "goles", "4", "1", null, null, "3,3,1;0,-3,0;");
        evaluar("Fútbol victoria visitante", new FutbolSala(), "goles", "0", "2", null, null, "0,-2,0;3,2,1;");
        evaluar("Fútbol empate con cero", new FutbolSala(), "goles", "0", "0", null, null, "1,0,0;1,0,0;");
        evaluar("Penales local", new FutbolSalaPenales(), "goles", "2", "2", "4", "3", "2,0,1;1,0,0;");
        evaluar("Penales visitante y cero válido", new FutbolSalaPenales(), "goles", "0", "0", "0", "1", "1,0,0;2,0,1;");
        evaluar("Penales sin desempate", new FutbolSalaPenales(), "goles", "3", "1", null, null, "3,2,1;0,-2,0;");
        evaluar("Baloncesto local", new Baloncesto(), "puntos", "80", "75", null, null, "2,5,1;1,-5,0;");
        evaluar("Baloncesto visitante y cero", new Baloncesto(), "puntos", "0", "2", null, null, "1,-2,0;2,2,1;");
        evaluar("Voleibol 3 a 0", new Voleibol(), "sets", "3", "0", null, null, "3,3,1;0,-3,0;");
        evaluar("Voleibol 3 a 1", new Voleibol(), "sets", "3", "1", null, null, "3,2,1;0,-2,0;");
        evaluar("Voleibol 3 a 2", new Voleibol(), "sets", "3", "2", null, null, "2,1,1;1,-1,0;");
        evaluar("Voleibol 2 a 3", new Voleibol(), "sets", "2", "3", null, null, "1,-1,0;2,1,1;");
        List<Participante> ocho = base().getParticipantes().subList(0, 8);
        Map<String, String> tiempos = new LinkedHashMap<>();
        for (int i = 0; i < 8; i++) tiempos.put(ocho.get(i).getCodigo() + ".tiempo", String.valueOf(50 + i));
        List<Aporte> aportes = new Relevos().evaluar(ocho, tiempos);
        String puntos = "";
        boolean diferenciasCero = true;
        for (Aporte aporte : aportes) { puntos += aporte.getPuntos() + ","; diferenciasCero &= aporte.getDiferencia() == 0; }
        comprobar("Ocho lugares de relevos", "50 a 57 segundos", "10,8,6,5,4,3,2,1,", puntos);
        comprobar("Diferencias en relevos", "Ocho tiempos", true, diferenciasCero);
        comprobar("Victoria en relevos", "Mejor tiempo", 1, aportes.get(0).getVictorias());
        comprobar("Segundo no obtiene victoria", "Segundo tiempo", 0, aportes.get(1).getVictorias());
        aportes = new Relevos().evaluar(ocho.subList(0, 3), datos("P1.tiempo", "60", "P2.tiempo", "DQ", "P3.tiempo", "62"));
        comprobar("Descalificación", "P2 DQ", 0, aportes.get(1).getPuntos());
        comprobar("DQ no ocupa lugar", "P3 después de P2 DQ", 8, aportes.get(2).getPuntos());
    }

    private static void rechazos() {
        SistemaArena sistema = base();
        rechazo(sistema, "Código vacío", "Participante código vacío", () -> sistema.registrarParticipante(" ", "Otro", "Otra"));
        rechazo(sistema, "Código repetido", "p1 con espacios", () -> sistema.registrarParticipante(" p1 ", "Otro", "Otra"));
        rechazo(sistema, "Nombre vacío", "Participante sin nombre", () -> sistema.registrarParticipante("N", "", "Otra"));
        rechazo(sistema, "Empresa vacía", "Participante sin empresa", () -> sistema.registrarParticipante("N", "Nombre", ""));
        rechazo(sistema, "Disciplina desconocida", "Crear con XX", () -> sistema.crearTorneo("T", "Copa", "XX"));
        rechazo(sistema, "Cotización cero", "FS, 0", () -> sistema.cotizar("FS", 0));
        rechazo(sistema, "Cotización negativa", "FS, -1", () -> sistema.cotizar("FS", -1));
        sistema.crearTorneo("T", "Copa", "FS");
        rechazo(sistema, "Torneo repetido", "t con espacios", () -> sistema.crearTorneo(" t ", "Otra", "FS"));
        rechazo(sistema, "Torneo sin código", "Código vacío", () -> sistema.crearTorneo("", "Otra", "FS"));
        rechazo(sistema, "Cierre sin mínimo", "T vacío", () -> sistema.cerrarInscripciones("T"));
        rechazo(sistema, "Participante inexistente", "PX", () -> sistema.inscribir("T", "PX"));
        sistema.inscribir("T", "P1");
        rechazo(sistema, "Inscripción duplicada", "P1 otra vez", () -> sistema.inscribir("T", "P1"));
        sistema.inscribir("T", "P2");
        sistema.registrarParticipante("P11", "Tercero", " empresa 1 ");
        rechazo(sistema, "Límite por empresa", "Tercer equipo de Empresa 1", () -> sistema.inscribir("T", "P11"));
        for (int i = 3; i <= 8; i++) sistema.inscribir("T", "P" + i);
        rechazo(sistema, "Cupo máximo", "Noveno inscrito en fútbol", () -> sistema.inscribir("T", "P9"));
        sistema.cerrarInscripciones("T");
        rechazo(sistema, "Cierre repetido", "Cerrar T dos veces", () -> sistema.cerrarInscripciones("T"));
        rechazo(sistema, "Inscripción fuera de estado", "P9 en torneo en juego", () -> sistema.inscribir("T", "P9"));
        Evento e = sistema.buscarTorneo("T").getEventos().get(0);
        rechazo(sistema, "Resultado sin programación", e.getCodigo(), () -> sistema.registrarResultado("T", e.getCodigo(), marcador(e, "1", "0")));
        rechazo(sistema, "Hora negativa", "Hora -1", () -> sistema.programarEvento(e.getCodigo(), LocalDate.of(2027, 1, 1), -1, "Sede"));
        rechazo(sistema, "Hora fuera de rango", "Hora 24", () -> sistema.programarEvento(e.getCodigo(), LocalDate.of(2027, 1, 1), 24, "Sede"));
        rechazo(sistema, "Sede vacía", "Sede sin nombre", () -> sistema.programarEvento(e.getCodigo(), LocalDate.of(2027, 1, 1), 0, " "));
        sistema.programarEvento(e.getCodigo(), LocalDate.of(2027, 1, 1), 0, "Sede");
        rechazo(sistema, "Goles negativos", "-1 a 0", () -> sistema.registrarResultado("T", e.getCodigo(), marcador(e, "-1", "0")));
        rechazo(sistema, "Goles decimales", "1.5 a 0", () -> sistema.registrarResultado("T", e.getCodigo(), marcador(e, "1.5", "0")));
        rechazo(sistema, "Marcador fuera de int", "999999999999 a 0", () -> sistema.registrarResultado("T", e.getCodigo(), marcador(e, "999999999999", "0")));
        rechazo(sistema, "Falta marcador", "Mapa vacío", () -> sistema.registrarResultado("T", e.getCodigo(), Collections.emptyMap()));
        Map<String, String> ajeno = marcador(e, "1", "0"); ajeno.put("PX.goles", "4");
        rechazo(sistema, "Campo de otro participante", "PX.goles", () -> sistema.registrarResultado("T", e.getCodigo(), ajeno));
        sistema.registrarResultado("T", e.getCodigo(), marcador(e, "1", "0"));
        rechazo(sistema, "Resultado repetido", e.getCodigo(), () -> sistema.registrarResultado("T", e.getCodigo(), marcador(e, "2", "0")));
        rechazo(sistema, "Reprogramar finalizado", e.getCodigo(), () -> sistema.programarEvento(e.getCodigo(), LocalDate.of(2027, 1, 2), 0, "Otra"));

        SistemaArena baloncesto = torneo("BC", 4);
        baloncesto.programarEvento("T-E1", LocalDate.of(2027, 1, 1), 9, "Sede");
        Evento b = baloncesto.buscarEvento("T-E1");
        rechazo(baloncesto, "Baloncesto empatado", "80 a 80", () -> baloncesto.registrarResultado("T", "T-E1", marcador(b, "80", "80")));
        SistemaArena volei = torneo("VB", 4);
        volei.programarEvento("T-E1", LocalDate.of(2027, 1, 1), 9, "Sede");
        Evento v = volei.buscarEvento("T-E1");
        for (String[] par : new String[][]{{"2", "1"}, {"3", "3"}, {"4", "2"}, {"3", "-1"}}) {
            rechazo(volei, "Voleibol inválido", par[0] + " a " + par[1], () -> volei.registrarResultado("T", "T-E1", marcador(v, par[0], par[1])));
        }
        SistemaArena penales = torneo("FP", 4);
        penales.programarEvento("T-E1", LocalDate.of(2027, 1, 1), 9, "Sede");
        rechazo(penales, "Empate sin penales", "1 a 1, sin penales", () -> penales.registrarResultado("T", "T-E1", datos("P1.goles", "1", "P2.goles", "1")));
        rechazo(penales, "Penales iguales", "1 a 1, 4 a 4", () -> penales.registrarResultado("T", "T-E1", datos("P1.goles", "1", "P2.goles", "1", "P1.penales", "4", "P2.penales", "4")));
        rechazo(penales, "Penales sin empate de goles", "2 a 1 con penales 4 a 3", () -> penales.registrarResultado("T", "T-E1", datos("P1.goles", "2", "P2.goles", "1", "P1.penales", "4", "P2.penales", "3")));
        SistemaArena relevos = torneo("RL", 3);
        relevos.programarEvento("T-C1", LocalDate.of(2027, 1, 1), 9, "Pista");
        for (String tiempo : new String[]{"0", "-1", "NaN", "Infinity", "", "texto"}) {
            rechazo(relevos, "Tiempo inválido", "P1=" + tiempo, () -> relevos.registrarResultado("T", "T-C1", datos("P1.tiempo", tiempo, "P2.tiempo", "51", "P3.tiempo", "52")));
        }
        rechazo(relevos, "Tiempos numéricamente repetidos", "50.1 y 50.10", () -> relevos.registrarResultado("T", "T-C1", datos("P1.tiempo", "50.1", "P2.tiempo", "50.10", "P3.tiempo", "52")));
        rechazo(relevos, "Error al final de carrera", "Dos tiempos válidos, último repetido", () -> relevos.registrarResultado("T", "T-C1", datos("P1.tiempo", "50", "P2.tiempo", "51", "P3.tiempo", "50")));
        rechazo(relevos, "Falta un tiempo", "Solo P1 y P2", () -> relevos.registrarResultado("T", "T-C1", datos("P1.tiempo", "50", "P2.tiempo", "51")));

        SistemaArena grande = torneo("FS", 4);
        grande.programarEvento("T-E1", LocalDate.of(2027, 1, 1), 1, "Sede");
        grande.programarEvento("T-E2", LocalDate.of(2027, 1, 1), 2, "Sede");
        grande.registrarResultado("T", "T-E1", marcador(grande.buscarEvento("T-E1"), "2147483647", "0"));
        rechazo(grande, "Evitar desbordamiento de diferencia", "Segundo marcador 2147483647 a 0", () -> grande.registrarResultado("T", "T-E2", marcador(grande.buscarEvento("T-E2"), "2147483647", "0")));
    }

    private static void calendarios() {
        SistemaArena sistema = torneo("FS", 4);
        sistema.crearTorneo("R", "Relevos", "RL");
        for (int i = 1; i <= 3; i++) sistema.inscribir("R", "P" + i);
        sistema.cerrarInscripciones("R");
        LocalDate dia = LocalDate.of(2027, 2, 1);
        sistema.programarEvento("T-E1", dia, 9, "Zeta");
        sistema.programarEvento("T-E6", dia, 9, "Alfa");
        sistema.programarEvento("R-C1", dia, 8, "Pista");
        sistema.programarEvento("T-E2", dia.plusDays(1), 0, "Centro");
        String orden = "";
        for (Evento e : sistema.calendarioGeneral()) orden += e.getCodigo() + ",";
        comprobar("Calendario mixto por fecha hora y sede", "Carrera y encuentros; Zeta se programa antes que Alfa", "R-C1,T-E6,T-E1,T-E2,", orden);
        comprobar("Sin programar se omiten", "7 eventos, 4 programados", 4, sistema.calendarioGeneral().size());
        comprobar("Calendario por torneo", "T", 3, sistema.calendarioTorneo("T").size());
        comprobar("Calendario por participante", "P1", 3, sistema.calendarioParticipante("P1").size());
        rechazo(sistema, "Conflicto de sede", "Mover T-E6 a Zeta a las 9", () -> sistema.programarEvento("T-E6", dia, 9, " zeta "));
        rechazo(sistema, "Conflicto de participante entre torneos", "Carrera a las 9 en otra sede", () -> sistema.programarEvento("R-C1", dia, 9, "Otra"));
        sistema.programarEvento("T-E1", dia, 9, "Zeta");
        comprobar("Se excluye el propio evento", "Mismo horario actual", 9, sistema.buscarEvento("T-E1").getHora());
        sistema.programarEvento("T-E1", dia, 10, "Zeta");
        comprobar("Reprogramación válida", "Mover a 10", 10, sistema.buscarEvento("T-E1").getHora());
        comprobar("Reprogramado sigue programado", "Sin resultado", "Programado", sistema.buscarEvento("T-E1").getEstado());
        // La hora 23 también es válida; 0 ya se usó en el día siguiente.
        sistema.programarEvento("T-E1", dia, 23, "Zeta");
        comprobar("Hora límite válida", "23", 23, sistema.buscarEvento("T-E1").getHora());
    }

    private static void cargaInicial() {
        SistemaArena sistema = new SistemaArena();
        sistema.cargarDatosIniciales();
        comprobar("Participantes iniciales", "Carga directa", 8, sistema.getParticipantes().size());
        comprobar("Torneos iniciales", "Cinco disciplinas y dos relevos", 6, sistema.getTorneos().size());
        comprobar("Sin cobros en carga inicial", "Carga directa", 0.0, sistema.getIngresosTotales());
        comprobar("Sin entregas en carga inicial", "Resultados de ejemplo", 0.0, sistema.getTotalPremios());
        int enJuego = 0, programados = 0;
        for (Torneo torneo : sistema.getTorneos()) {
            if (torneo.getEstado().equals("En juego")) enJuego++;
            for (Evento evento : torneo.getEventos()) if (evento.getFecha() != null) programados++;
        }
        comprobar("Torneos en juego", "Carga inicial", 4, enJuego);
        comprobar("Todos los eventos iniciales programados", "Carga inicial", 30, programados);
        List<Posicion> tabla = sistema.buscarTorneo("FS01").getTabla();
        String orden = "";
        for (Posicion fila : tabla) orden += fila.getParticipante().getCodigo() + ",";
        comprobar("Desempates por diferencia y victorias", "FS01", "P05,P03,P04,P01,P02,", orden);
        comprobar("Diferencia decide", "P05 y P03 con cuatro puntos", true, tabla.get(0).getDiferencia() > tabla.get(1).getDiferencia());
        comprobar("Victorias deciden", "P01 y P02 con tres puntos y diferencia cero", true, tabla.get(3).getVictorias() > tabla.get(4).getVictorias());
        comprobar("Ranking suma todos los torneos", "Datos iniciales", "{Ceiba=45, Volcán=34, Quetzal=11, Jaguar=3}", sistema.rankingEmpresas().toString());
        boolean sinConflictos = true;
        List<Evento> eventos = new ArrayList<>();
        for (Evento e : sistema.calendarioGeneral()) eventos.add(e);
        for (int i = 0; i < eventos.size(); i++) {
            for (int j = i + 1; j < eventos.size(); j++) {
                if (eventos.get(i).getFecha().equals(eventos.get(j).getFecha()) && eventos.get(i).getHora() == eventos.get(j).getHora()) sinConflictos = false;
            }
        }
        comprobar("Carga sin conflictos globales", "30 eventos", true, sinConflictos);
        rechazo(sistema, "No repetir carga inicial", "Cargar otra vez", sistema::cargarDatosIniciales);
        SistemaArena nombres = new SistemaArena();
        nombres.registrarParticipante("Z", "Zorro", "Zeta");
        nombres.registrarParticipante("A", "Alfa", "Alfa");
        nombres.registrarParticipante("M", "Mono", "Beta");
        nombres.crearTorneo("N", "Nombres", "FS");
        nombres.inscribir("N", "Z"); nombres.inscribir("N", "A"); nombres.inscribir("N", "M");
        comprobar("Desempate por nombre", "Tres filas en cero", "A", nombres.buscarTorneo("N").getTabla().get(0).getParticipante().getCodigo());
        comprobar("Desempate de empresas por nombre", "Todas con cero", "[Alfa, Beta, Zeta]", nombres.rankingEmpresas().keySet().toString());
    }

    private static void proteccionYConsola() {
        SistemaArena sistema = torneo("FS", 4);
        Posicion copia = sistema.buscarTorneo("T").getTabla().get(0);
        copia.sumar(new Aporte(copia.getParticipante(), 9, 2, 1));
        comprobar("Tabla de consulta independiente", "Modificar una copia", 0, sistema.buscarTorneo("T").getTabla().get(0).getPuntos());
        boolean noPermiteQuitar = false;
        sistema.programarEvento("T-E1", LocalDate.of(2027, 1, 1), 0, "Sede");
        Iterator<Evento> iterador = sistema.calendarioGeneral().iterator();
        iterador.next();
        try { iterador.remove(); } catch (UnsupportedOperationException error) { noPermiteQuitar = true; }
        comprobar("Iterador protege calendario", "iterator.remove", true, noPermiteQuitar);
        PrintStream salida = System.out;
        ByteArrayOutputStream captura = new ByteArrayOutputStream();
        String resultado;
        try {
            System.setOut(new PrintStream(captura, true, StandardCharsets.UTF_8));
            String entrada = "texto\n\n99\n3\nFS\nabc\n0\n9\n0\n";
            new ProgramaConsola(sistema, new Scanner(entrada)).ejecutar();
            resultado = captura.toString(StandardCharsets.UTF_8);
        } finally { System.setOut(salida); }
        comprobar("Consola acepta seguir tras error", "Texto, vacío, opción 99, cantidad inválida, listar, salir", true,
                resultado.contains("Escriba un número entero válido") && resultado.contains("Esa opción no existe")
                        && resultado.contains("La cantidad debe ser positiva") && resultado.contains("Hasta luego."));
        captura.reset();
        try {
            System.setOut(new PrintStream(captura, true, StandardCharsets.UTF_8));
            new ProgramaConsola(sistema, new Scanner("6\nT-E1\n2027-02-30\n2027-02-01\n10\nOtra sede\n0\n")).ejecutar();
            resultado = captura.toString(StandardCharsets.UTF_8);
        } finally { System.setOut(salida); }
        comprobar("Fecha inválida no termina el menú", "30 de febrero y luego fecha válida", true,
                resultado.contains("Fecha inválida") && resultado.contains("Evento programado.") && resultado.contains("Hasta luego."));
        comprobar("Fecha corregida se conserva", "2027-02-01", "2027-02-01", sistema.buscarEvento("T-E1").getFecha().toString());
        captura.reset();
        try {
            System.setOut(new PrintStream(captura, true, StandardCharsets.UTF_8));
            new ProgramaConsola(sistema, new Scanner("")).ejecutar();
            resultado = captura.toString(StandardCharsets.UTF_8);
        } finally { System.setOut(salida); }
        comprobar("Fin de entrada", "Consola sin más líneas", true, resultado.contains("Hasta luego."));
    }
}
