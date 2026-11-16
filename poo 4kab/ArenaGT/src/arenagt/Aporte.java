package arenagt;

/** Lo que un resultado agrega a la tabla de un participante. */
public class Aporte {
    private final Participante participante;
    private final int puntos;
    private final int diferencia;
    private final int victorias;

    public Aporte(Participante participante, int puntos, int diferencia, int victorias) {
        if (participante == null || puntos < 0 || victorias < 0 || victorias > 1) {
            throw new IllegalArgumentException("El aporte no es válido.");
        }
        this.participante = participante;
        this.puntos = puntos;
        this.diferencia = diferencia;
        this.victorias = victorias;
    }

    public Participante getParticipante() { return participante; }
    public int getPuntos() { return puntos; }
    public int getDiferencia() { return diferencia; }
    public int getVictorias() { return victorias; }
}
