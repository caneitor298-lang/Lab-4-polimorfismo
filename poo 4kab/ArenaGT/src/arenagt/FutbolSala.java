package arenagt;

import java.util.List;
import java.util.Map;

public class FutbolSala extends Disciplina {
    public FutbolSala() { super("Fútbol sala", 4, 8, 600); }

    @Override
    public List<Evento> crearEventos(String prefijo, List<Participante> inscritos) {
        validarLista(inscritos);
        return crearEncuentros(prefijo, inscritos);
    }

    @Override
    public List<String> camposResultado(List<Participante> participantes) { return campos(participantes, "goles"); }

    @Override
    public List<Aporte> evaluar(List<Participante> participantes, Map<String, String> datos) {
        validarPareja(participantes);
        // Se revisan solo goles. La subclase agrega la revisión de penales.
        validarCampos(datos, camposResultado(participantes));
        int a = leerMarcador(datos, participantes.get(0).getCodigo() + ".goles");
        int b = leerMarcador(datos, participantes.get(1).getCodigo() + ".goles");
        if (a == b) return aportes(participantes, 1, 1, 0, -1);
        return aportes(participantes, a > b ? 3 : 0, a > b ? 0 : 3, a - b, a > b ? 0 : 1);
    }
}
