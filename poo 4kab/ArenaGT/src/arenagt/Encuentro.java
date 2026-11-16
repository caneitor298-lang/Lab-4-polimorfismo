package arenagt;

import java.util.Arrays;

/** Un encuentro siempre tiene dos participantes diferentes. */
public class Encuentro extends Evento {
    public Encuentro(String codigo, Participante local, Participante visitante, Disciplina disciplina) {
        super(codigo, Arrays.asList(local, visitante), disciplina);
    }

    @Override
    public String getTipo() { return "Encuentro"; }
}
