package arenagt;

import java.util.List;
import java.util.Map;

public class Baloncesto extends Disciplina {
    public Baloncesto() { super("Baloncesto", 4, 6, 500); }

    @Override
    public List<Evento> crearEventos(String prefijo, List<Participante> inscritos) {
        validarLista(inscritos);
        return crearEncuentros(prefijo, inscritos);
    }

    @Override
    public List<String> camposResultado(List<Participante> participantes) { return campos(participantes, "puntos"); }

    @Override
    public List<Aporte> evaluar(List<Participante> participantes, Map<String, String> datos) {
        validarPareja(participantes);
        validarCampos(datos, camposResultado(participantes));
        int a = leerMarcador(datos, participantes.get(0).getCodigo() + ".puntos");
        int b = leerMarcador(datos, participantes.get(1).getCodigo() + ".puntos");
        if (a == b) throw new IllegalArgumentException("En baloncesto no se permiten empates.");
        return aportes(participantes, a > b ? 2 : 1, a > b ? 1 : 2, a - b, a > b ? 0 : 1);
    }
}
