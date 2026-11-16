package arenagt;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/** Controla las inscripciones y la tabla de un torneo. */
public class Torneo {
    private final String codigo;
    private final String nombre;
    private final Disciplina disciplina;
    private String estado = "En inscripción";
    private Map<String, Posicion> posiciones = new LinkedHashMap<>();
    private List<Evento> eventos = new ArrayList<>();
    private double premio;

    public Torneo(String codigo, String nombre, Disciplina disciplina) {
        this.codigo = Participante.texto(codigo, "Código del torneo").toUpperCase(java.util.Locale.ROOT);
        this.nombre = Participante.texto(nombre, "Nombre del torneo");
        if (disciplina == null) throw new IllegalArgumentException("Falta la disciplina.");
        this.disciplina = disciplina;
    }

    public double inscribir(Participante participante) {
        if (!estado.equals("En inscripción")) throw new IllegalStateException("El torneo ya cerró sus inscripciones.");
        disciplina.validarInscripcion(getParticipantes(), participante);
        posiciones.put(participante.getCodigo(), new Posicion(participante));
        return disciplina.getCuota();
    }

    public void cerrarInscripciones() {
        if (!estado.equals("En inscripción")) throw new IllegalStateException("Las inscripciones ya están cerradas.");
        if (posiciones.size() < disciplina.getMinimo()) {
            throw new IllegalArgumentException("Se necesitan al menos " + disciplina.getMinimo() + " participantes.");
        }
        List<Evento> nuevosEventos = disciplina.crearEventos(codigo, getParticipantes());
        if (nuevosEventos == null || nuevosEventos.isEmpty()) throw new IllegalArgumentException("No se generaron eventos.");
        Set<String> codigos = new HashSet<>();
        for (Evento evento : nuevosEventos) {
            if (evento == null || !codigos.add(evento.getCodigo())) throw new IllegalArgumentException("Eventos repetidos o nulos.");
            for (Participante participante : evento.getParticipantes()) {
                if (!posiciones.containsKey(participante.getCodigo())) throw new IllegalArgumentException("Evento con participante ajeno.");
            }
        }
        eventos = new ArrayList<>(nuevosEventos);
        estado = "En juego";
    }

    /** Retorna un premio positivo solamente cuando este resultado termina el torneo. */
    public double registrarResultado(String codigoEvento, Map<String, String> datos) {
        if (!estado.equals("En juego")) throw new IllegalStateException("El torneo no está en juego.");
        Evento evento = buscarEvento(codigoEvento);
        List<Aporte> aportes = evento.evaluarResultado(datos);
        if (aportes == null || aportes.size() != evento.getParticipantes().size()) {
            throw new IllegalArgumentException("El resultado no incluye a todos los participantes.");
        }

        // Trabajamos sobre copias. Si algo falla, la tabla original sigue igual.
        Map<String, Posicion> nuevaTabla = new LinkedHashMap<>();
        for (Map.Entry<String, Posicion> fila : posiciones.entrySet()) nuevaTabla.put(fila.getKey(), fila.getValue().copia());
        Set<String> esperados = new HashSet<>();
        for (Participante participante : evento.getParticipantes()) esperados.add(participante.getCodigo());
        Set<String> recibidos = new HashSet<>();
        for (Aporte aporte : aportes) {
            if (aporte == null) throw new IllegalArgumentException("Aporte nulo.");
            String codigoParticipante = aporte.getParticipante().getCodigo();
            if (!esperados.contains(codigoParticipante) || !recibidos.add(codigoParticipante)) {
                throw new IllegalArgumentException("Aportes repetidos o de otro evento.");
            }
            nuevaTabla.get(codigoParticipante).sumar(aporte);
        }
        if (!recibidos.equals(esperados)) throw new IllegalArgumentException("Faltan aportes.");

        evento.confirmarResultado(datos);
        posiciones = nuevaTabla;
        return finalizarSiCorresponde();
    }

    private double finalizarSiCorresponde() {
        for (Evento evento : eventos) if (!evento.estaFinalizado()) return 0;
        estado = "Finalizado";
        premio = disciplina.getCuota() * posiciones.size() * 0.50;
        return premio;
    }

    // El cargador inicial usa este método, sin inscripciones cobradas.
    void cargarInscritosDemo(List<Participante> participantes) {
        if (!posiciones.isEmpty() || !estado.equals("En inscripción")) throw new IllegalStateException("El torneo no está vacío.");
        List<Participante> revisados = new ArrayList<>();
        Map<String, Posicion> nuevasPosiciones = new LinkedHashMap<>();
        for (Participante participante : participantes) {
            disciplina.validarInscripcion(revisados, participante);
            revisados.add(participante);
            nuevasPosiciones.put(participante.getCodigo(), new Posicion(participante));
        }
        posiciones = nuevasPosiciones;
    }

    public Evento buscarEvento(String codigoEvento) {
        String buscado = Participante.texto(codigoEvento, "Código del evento");
        for (Evento evento : eventos) if (evento.getCodigo().equalsIgnoreCase(buscado)) return evento;
        throw new IllegalArgumentException("No existe ese evento en el torneo.");
    }

    public List<Posicion> getTabla() {
        List<Posicion> tabla = new ArrayList<>();
        for (Posicion posicion : posiciones.values()) tabla.add(posicion.copia());
        Collections.sort(tabla);
        return Collections.unmodifiableList(tabla);
    }

    public List<Participante> getParticipantes() {
        List<Participante> inscritos = new ArrayList<>();
        for (Posicion posicion : posiciones.values()) inscritos.add(posicion.getParticipante());
        return Collections.unmodifiableList(inscritos);
    }

    public List<Evento> getEventos() { return Collections.unmodifiableList(eventos); }
    public String getCodigo() { return codigo; }
    public String getNombre() { return nombre; }
    public Disciplina getDisciplina() { return disciplina; }
    public String getEstado() { return estado; }
    public double getPremio() { return premio; }
    public Participante getCampeon() { return estado.equals("Finalizado") ? getTabla().get(0).getParticipante() : null; }
}
