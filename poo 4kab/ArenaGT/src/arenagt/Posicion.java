package arenagt;

/** Una fila de la tabla. Comparable permite ordenar sin repetir los criterios. */
public class Posicion implements Comparable<Posicion> {
    private final Participante participante;
    private int puntos;
    private int diferencia;
    private int victorias;

    public Posicion(Participante participante) {
        if (participante == null) throw new IllegalArgumentException("Falta participante.");
        this.participante = participante;
    }

    void sumar(Aporte aporte) {
        if (!participante.getCodigo().equals(aporte.getParticipante().getCodigo())) {
            throw new IllegalArgumentException("El aporte pertenece a otro participante.");
        }
        // Primero se calculan los tres valores; así un desbordamiento no cambia la fila.
        int nuevosPuntos = Math.addExact(puntos, aporte.getPuntos());
        int nuevaDiferencia = Math.addExact(diferencia, aporte.getDiferencia());
        int nuevasVictorias = Math.addExact(victorias, aporte.getVictorias());
        puntos = nuevosPuntos;
        diferencia = nuevaDiferencia;
        victorias = nuevasVictorias;
    }

    public Posicion copia() {
        Posicion copia = new Posicion(participante);
        copia.puntos = puntos;
        copia.diferencia = diferencia;
        copia.victorias = victorias;
        return copia;
    }

    public Participante getParticipante() { return participante; }
    public int getPuntos() { return puntos; }
    public int getDiferencia() { return diferencia; }
    public int getVictorias() { return victorias; }

    @Override
    public int compareTo(Posicion otra) {
        int orden = Integer.compare(otra.puntos, puntos);
        if (orden == 0) orden = Integer.compare(otra.diferencia, diferencia);
        if (orden == 0) orden = Integer.compare(otra.victorias, victorias);
        if (orden == 0) orden = participante.getNombre().compareToIgnoreCase(otra.participante.getNombre());
        if (orden == 0) orden = participante.getCodigo().compareTo(otra.participante.getCodigo());
        return orden;
    }
}
