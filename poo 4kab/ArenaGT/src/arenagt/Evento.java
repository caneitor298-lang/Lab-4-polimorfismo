package arenagt;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Datos compartidos por encuentros y carreras. */
public abstract class Evento implements Programable, Comparable<Evento> {
    private final String codigo;
    private final List<Participante> participantes;
    private final Disciplina disciplina;
    private LocalDate fecha;
    private int hora = -1;
    private String sede = "";
    private Map<String, String> resultado;

    protected Evento(String codigo, List<Participante> participantes, Disciplina disciplina) {
        this.codigo = Participante.texto(codigo, "Código del evento").toUpperCase(java.util.Locale.ROOT);
        if (disciplina == null || participantes == null || participantes.isEmpty()) {
            throw new IllegalArgumentException("Faltan disciplina o participantes.");
        }
        HashSet<String> codigos = new HashSet<>();
        for (Participante participante : participantes) {
            if (participante == null || !codigos.add(participante.getCodigo())) {
                throw new IllegalArgumentException("Participantes nulos o repetidos en el evento.");
            }
        }
        this.participantes = Collections.unmodifiableList(new ArrayList<>(participantes));
        this.disciplina = disciplina;
    }

    @Override
    public void programar(LocalDate fecha, int hora, String sede) {
        if (estaFinalizado()) throw new IllegalStateException("Un evento finalizado no se reprograma.");
        if (fecha == null || hora < 0 || hora > 23) {
            throw new IllegalArgumentException("La fecha debe existir y la hora debe estar entre 0 y 23.");
        }
        String nuevaSede = Participante.texto(sede, "Sede");
        this.fecha = fecha;
        this.hora = hora;
        this.sede = nuevaSede;
    }

    public List<String> camposResultado() {
        return disciplina.camposResultado(participantes);
    }

    public List<Aporte> evaluarResultado(Map<String, String> datos) {
        if (fecha == null) throw new IllegalStateException("Primero debe programar el evento.");
        if (estaFinalizado()) throw new IllegalStateException("El evento ya tiene resultado.");
        return disciplina.evaluar(participantes, datos);
    }

    // Solo Torneo confirma después de validar y preparar todos los acumulados.
    void confirmarResultado(Map<String, String> datos) {
        if (fecha == null || estaFinalizado()) throw new IllegalStateException("No se puede confirmar el resultado.");
        if (datos == null) throw new IllegalArgumentException("Falta el resultado.");
        resultado = Collections.unmodifiableMap(new LinkedHashMap<>(datos));
    }

    public abstract String getTipo();
    public String getCodigo() { return codigo; }
    public List<Participante> getParticipantes() { return participantes; }
    @Override public LocalDate getFecha() { return fecha; }
    @Override public int getHora() { return hora; }
    @Override public String getSede() { return sede; }
    public boolean estaFinalizado() { return resultado != null; }
    public Map<String, String> getResultado() { return resultado == null ? Collections.emptyMap() : resultado; }

    public String getEstado() {
        if (estaFinalizado()) return "Finalizado";
        return fecha == null ? "Sin programar" : "Programado";
    }

    @Override
    public int compareTo(Evento otro) {
        if (fecha == null && otro.fecha != null) return 1;
        if (fecha != null && otro.fecha == null) return -1;
        int orden = 0;
        if (fecha != null) {
            orden = fecha.compareTo(otro.fecha);
            if (orden == 0) orden = Integer.compare(hora, otro.hora);
            if (orden == 0) orden = sede.compareToIgnoreCase(otro.sede);
        }
        if (orden == 0) orden = codigo.compareTo(otro.codigo);
        return orden;
    }
}
