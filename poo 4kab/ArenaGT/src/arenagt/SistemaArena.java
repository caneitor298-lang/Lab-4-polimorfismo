package arenagt;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/** Coordina las operaciones del menú y los reportes. */
public class SistemaArena {
    private final Map<String, Participante> participantes = new LinkedHashMap<>();
    private final Map<String, Torneo> torneos = new LinkedHashMap<>();
    private final Map<String, Disciplina> disciplinas = new LinkedHashMap<>();
    private final Map<String, Double> ingresos = new LinkedHashMap<>();
    private final Map<String, Double> premios = new LinkedHashMap<>();

    public SistemaArena() {
        disciplinas.put("FS", new FutbolSala());
        disciplinas.put("FP", new FutbolSalaPenales());
        disciplinas.put("BC", new Baloncesto());
        disciplinas.put("VB", new Voleibol());
        disciplinas.put("RL", new Relevos());
        for (String codigo : disciplinas.keySet()) ingresos.put(codigo, 0.0);
    }

    private String codigo(String texto) {
        return Participante.texto(texto, "Código").toUpperCase(java.util.Locale.ROOT);
    }

    public void registrarParticipante(String codigo, String nombre, String empresa) {
        String clave = codigo(codigo);
        if (participantes.containsKey(clave)) throw new IllegalArgumentException("El código de participante ya existe.");
        String empresaLimpia = Participante.texto(empresa, "Empresa");
        for (Participante registrado : participantes.values()) {
            if (registrado.getEmpresa().equalsIgnoreCase(empresaLimpia)) empresaLimpia = registrado.getEmpresa();
        }
        Participante nuevo = new Participante(clave, nombre, empresaLimpia);
        participantes.put(clave, nuevo);
    }

    public void crearTorneo(String codigo, String nombre, String codigoDisciplina) {
        String clave = codigo(codigo);
        if (torneos.containsKey(clave)) throw new IllegalArgumentException("El código de torneo ya existe.");
        Torneo nuevo = new Torneo(clave, nombre, buscarDisciplina(codigoDisciplina));
        torneos.put(clave, nuevo);
    }

    public Disciplina buscarDisciplina(String codigoDisciplina) {
        Disciplina disciplina = disciplinas.get(codigo(codigoDisciplina));
        if (disciplina == null) throw new IllegalArgumentException("No existe esa disciplina.");
        return disciplina;
    }

    public Participante buscarParticipante(String codigoParticipante) {
        Participante participante = participantes.get(codigo(codigoParticipante));
        if (participante == null) throw new IllegalArgumentException("No existe ese participante.");
        return participante;
    }

    public Torneo buscarTorneo(String codigoTorneo) {
        Torneo torneo = torneos.get(codigo(codigoTorneo));
        if (torneo == null) throw new IllegalArgumentException("No existe ese torneo.");
        return torneo;
    }

    public Evento buscarEvento(String codigoEvento) {
        String buscado = codigo(codigoEvento);
        for (Torneo torneo : torneos.values()) {
            for (Evento evento : torneo.getEventos()) if (evento.getCodigo().equals(buscado)) return evento;
        }
        throw new IllegalArgumentException("No existe ese evento.");
    }

    public double cotizar(String codigoDisciplina, int cantidad) {
        return buscarDisciplina(codigoDisciplina).cotizar(cantidad);
    }

    public void inscribir(String codigoTorneo, String codigoParticipante) {
        Torneo torneo = buscarTorneo(codigoTorneo);
        Participante participante = buscarParticipante(codigoParticipante);
        String codigoDisciplina = "";
        for (Map.Entry<String, Disciplina> entrada : disciplinas.entrySet()) {
            if (entrada.getValue() == torneo.getDisciplina()) codigoDisciplina = entrada.getKey();
        }
        // Solo sumamos dinero cuando inscribir terminó correctamente.
        double cuota = torneo.inscribir(participante);
        ingresos.put(codigoDisciplina, ingresos.get(codigoDisciplina) + cuota);
    }

    public void cerrarInscripciones(String codigoTorneo) { buscarTorneo(codigoTorneo).cerrarInscripciones(); }

    public void programarEvento(String codigoEvento, LocalDate fecha, int hora, String sede) {
        Evento evento = buscarEvento(codigoEvento);
        String sedeLimpia = Participante.texto(sede, "Sede");
        if (fecha == null || hora < 0 || hora > 23) throw new IllegalArgumentException("Fecha inválida u hora fuera de 0 a 23.");
        if (evento.estaFinalizado()) throw new IllegalStateException("El evento ya tiene resultado.");
        // Se revisan TODOS los torneos y se excluye el evento que se está cambiando.
        for (Evento otro : todosLosEventos()) {
            if (otro == evento || !fecha.equals(otro.getFecha()) || hora != otro.getHora()) continue;
            if (sedeLimpia.equalsIgnoreCase(otro.getSede())) throw new IllegalArgumentException("La sede ya está ocupada en esa fecha y hora.");
            for (Participante a : evento.getParticipantes()) {
                for (Participante b : otro.getParticipantes()) {
                    if (a.getCodigo().equals(b.getCodigo())) {
                        throw new IllegalArgumentException("Conflicto: " + a.getNombre() + " ya tiene otro evento en esa fecha y hora.");
                    }
                }
            }
        }
        evento.programar(fecha, hora, sedeLimpia);
    }

    public void registrarResultado(String codigoTorneo, String codigoEvento, Map<String, String> datos) {
        Torneo torneo = buscarTorneo(codigoTorneo);
        double premio = torneo.registrarResultado(codigoEvento, datos);
        if (premio > 0) premios.put(torneo.getCodigo(), premio);
    }

    private List<Evento> todosLosEventos() {
        List<Evento> eventos = new ArrayList<>();
        for (Torneo torneo : torneos.values()) eventos.addAll(torneo.getEventos());
        return eventos;
    }

    public Calendario calendarioGeneral() { return new Calendario(todosLosEventos()); }
    public Calendario calendarioTorneo(String codigoTorneo) { return new Calendario(buscarTorneo(codigoTorneo).getEventos()); }

    public Calendario calendarioParticipante(String codigoParticipante) {
        Participante participante = buscarParticipante(codigoParticipante);
        List<Evento> eventos = new ArrayList<>();
        for (Evento evento : todosLosEventos()) {
            for (Participante inscrito : evento.getParticipantes()) {
                if (participante.getCodigo().equals(inscrito.getCodigo())) { eventos.add(evento); break; }
            }
        }
        return new Calendario(eventos);
    }

    public Map<String, Integer> rankingEmpresas() {
        Map<String, Integer> totales = new LinkedHashMap<>();
        for (Participante participante : participantes.values()) totales.put(participante.getEmpresa(), 0);
        for (Torneo torneo : torneos.values()) {
            for (Posicion fila : torneo.getTabla()) {
                String empresa = fila.getParticipante().getEmpresa();
                totales.put(empresa, Math.addExact(totales.get(empresa), fila.getPuntos()));
            }
        }
        List<String> empresas = new ArrayList<>(totales.keySet());
        empresas.sort((a, b) -> {
            int orden = Integer.compare(totales.get(b), totales.get(a));
            if (orden == 0) orden = a.compareToIgnoreCase(b);
            return orden;
        });
        Map<String, Integer> ranking = new LinkedHashMap<>();
        for (String empresa : empresas) ranking.put(empresa, totales.get(empresa));
        return Collections.unmodifiableMap(ranking);
    }

    public List<Torneo> getTorneos() { return Collections.unmodifiableList(new ArrayList<>(torneos.values())); }
    public List<Participante> getParticipantes() { return Collections.unmodifiableList(new ArrayList<>(participantes.values())); }
    public Map<String, Disciplina> getDisciplinas() { return Collections.unmodifiableMap(disciplinas); }
    public Map<String, Double> getIngresosPorDisciplina() { return Collections.unmodifiableMap(new LinkedHashMap<>(ingresos)); }
    public Map<String, Double> getPremiosEntregados() { return Collections.unmodifiableMap(new LinkedHashMap<>(premios)); }
    public double getIngresosTotales() {
        double total = 0;
        for (double monto : ingresos.values()) total += monto;
        return total;
    }
    public double getTotalPremios() {
        double total = 0;
        for (double monto : premios.values()) total += monto;
        return total;
    }

    /** Carga directa de ejemplo. Se prepara todo antes de modificar el sistema. */
    public void cargarDatosIniciales() {
        if (!participantes.isEmpty() || !torneos.isEmpty()) throw new IllegalStateException("La carga inicial requiere un sistema vacío.");
        List<Participante> nuevos = Arrays.asList(
                new Participante("P01", "Ceiba Norte", "Ceiba"), new Participante("P02", "Ceiba Sur", "Ceiba"),
                new Participante("P03", "Volcán Azul", "Volcán"), new Participante("P04", "Volcán Rojo", "Volcán"),
                new Participante("P05", "Quetzal Uno", "Quetzal"), new Participante("P06", "Quetzal Dos", "Quetzal"),
                new Participante("P07", "Jaguar Centro", "Jaguar"), new Participante("P08", "Jaguar Este", "Jaguar"));
        List<Torneo> nuevosTorneos = new ArrayList<>();
        String[] codigos = {"FS01", "FP01", "BC01", "VB01", "RL01", "RL02"};
        String[] deportes = {"FS", "FP", "BC", "VB", "RL", "RL"};
        int[] cantidades = {5, 4, 4, 4, 8, 3};
        LocalDateTime horario = LocalDateTime.of(2026, 12, 1, 8, 0);
        for (int i = 0; i < codigos.length; i++) {
            Torneo torneo = new Torneo(codigos[i], "Copa de ejemplo " + codigos[i], buscarDisciplina(deportes[i]));
            torneo.cargarInscritosDemo(nuevos.subList(0, cantidades[i]));
            torneo.cerrarInscripciones();
            for (Evento evento : torneo.getEventos()) {
                evento.programar(horario.toLocalDate(), horario.getHour(), "Sede Central");
                horario = horario.plusHours(1);
            }
            nuevosTorneos.add(torneo);
        }
        Torneo futbol = nuevosTorneos.get(0);
        resultadoDemo(futbol, "P01", "P03", "goles", "2", "0");
        resultadoDemo(futbol, "P01", "P04", "goles", "0", "1");
        resultadoDemo(futbol, "P01", "P05", "goles", "0", "1");
        resultadoDemo(futbol, "P02", "P03", "goles", "0", "0");
        resultadoDemo(futbol, "P02", "P04", "goles", "0", "0");
        resultadoDemo(futbol, "P02", "P05", "goles", "0", "0");
        resultadoDemo(futbol, "P03", "P04", "goles", "2", "0");
        Map<String, String> penales = new LinkedHashMap<>();
        penales.put("P01.goles", "2"); penales.put("P02.goles", "2");
        penales.put("P01.penales", "4"); penales.put("P02.penales", "3");
        nuevosTorneos.get(1).registrarResultado("FP01-E1", penales);
        resultadoDemo(nuevosTorneos.get(1), "P03", "P04", "goles", "3", "1");
        resultadoDemo(nuevosTorneos.get(2), "P01", "P02", "puntos", "80", "75");
        resultadoDemo(nuevosTorneos.get(3), "P01", "P02", "sets", "3", "0");
        resultadoDemo(nuevosTorneos.get(3), "P03", "P04", "sets", "3", "1");
        resultadoDemo(nuevosTorneos.get(3), "P01", "P03", "sets", "3", "2");
        Map<String, String> carrera = new LinkedHashMap<>();
        String[] tiempos = {"50.1", "51.9", "52.4", "55.0", "56.0", "57.0", "58.0", "59.0"};
        for (int i = 0; i < nuevos.size(); i++) carrera.put(nuevos.get(i).getCodigo() + ".tiempo", tiempos[i]);
        nuevosTorneos.get(4).registrarResultado("RL01-C1", carrera);
        Map<String, String> descalificacion = new LinkedHashMap<>();
        descalificacion.put("P01.tiempo", "60.0"); descalificacion.put("P02.tiempo", "DQ"); descalificacion.put("P03.tiempo", "62.0");
        nuevosTorneos.get(5).registrarResultado("RL02-C1", descalificacion);

        // Un horario distinto por evento evita conflictos de sede y participante.
        Set<String> codigosEventos = new HashSet<>();
        for (Torneo torneo : nuevosTorneos) {
            for (Evento evento : torneo.getEventos()) {
                if (!codigosEventos.add(evento.getCodigo())) throw new IllegalArgumentException("Código de evento repetido en la carga.");
            }
        }
        for (Participante participante : nuevos) participantes.put(participante.getCodigo(), participante);
        for (Torneo torneo : nuevosTorneos) torneos.put(torneo.getCodigo(), torneo);
        // No se modifica ingresos ni premios: estos son resultados de ejemplo.
    }

    private void resultadoDemo(Torneo torneo, String codigoA, String codigoB, String campo, String a, String b) {
        for (Evento evento : torneo.getEventos()) {
            if (evento.getParticipantes().get(0).getCodigo().equals(codigoA)
                    && evento.getParticipantes().get(1).getCodigo().equals(codigoB)) {
                Map<String, String> resultado = new LinkedHashMap<>();
                resultado.put(codigoA + "." + campo, a);
                resultado.put(codigoB + "." + campo, b);
                torneo.registrarResultado(evento.getCodigo(), resultado);
                return;
            }
        }
        throw new IllegalArgumentException("No se encontró la pareja del ejemplo.");
    }
}
