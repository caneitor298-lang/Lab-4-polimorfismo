package arenagt;

import java.util.List;
import java.util.Map;

public class Voleibol extends Disciplina {
    public Voleibol() { super("Voleibol", 4, 6, 450); }

    @Override
    public List<Evento> crearEventos(String prefijo, List<Participante> inscritos) {
        validarLista(inscritos);
        return crearEncuentros(prefijo, inscritos);
    }

    @Override
    public List<String> camposResultado(List<Participante> participantes) { return campos(participantes, "sets"); }

    @Override
    public List<Aporte> evaluar(List<Participante> participantes, Map<String, String> datos) {
        validarPareja(participantes);
        validarCampos(datos, camposResultado(participantes));
        int a = leerMarcador(datos, participantes.get(0).getCodigo() + ".sets");
        int b = leerMarcador(datos, participantes.get(1).getCodigo() + ".sets");
        if (!((a == 3 && b < 3) || (b == 3 && a < 3))) {
            throw new IllegalArgumentException("El ganador debe tener 3 sets y el perdedor 0, 1 o 2.");
        }
        int puntosGanador = Math.min(a, b) == 2 ? 2 : 3;
        int puntosPerdedor = Math.min(a, b) == 2 ? 1 : 0;
        return aportes(participantes, a > b ? puntosGanador : puntosPerdedor,
                a > b ? puntosPerdedor : puntosGanador, a - b, a > b ? 0 : 1);
    }
}
