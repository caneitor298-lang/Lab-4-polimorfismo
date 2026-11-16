package arenagt;

import java.util.List;

/** Una carrera reúne a todos los cuartetos del torneo de relevos. */
public class Carrera extends Evento {
    public Carrera(String codigo, List<Participante> participantes, Disciplina disciplina) {
        super(codigo, participantes, disciplina);
        if (participantes.size() < 3 || participantes.size() > 8) {
            throw new IllegalArgumentException("La carrera necesita de 3 a 8 participantes.");
        }
    }

    @Override
    public String getTipo() { return "Carrera"; }
}
