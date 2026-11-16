package arenagt;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Relevos extends Disciplina {
    public Relevos() { super("Relevos", 3, 8, 300); }

    @Override
    public List<Evento> crearEventos(String prefijo, List<Participante> inscritos) {
        validarLista(inscritos);
        return Collections.singletonList(new Carrera(prefijo + "-C1", inscritos, this));
    }

    @Override
    public List<String> camposResultado(List<Participante> participantes) { return campos(participantes, "tiempo"); }

    @Override
    public List<Aporte> evaluar(List<Participante> participantes, Map<String, String> datos) {
        validarLista(participantes);
        validarCampos(datos, camposResultado(participantes));
        List<Participante> conTiempo = new ArrayList<>();
        Map<String, Double> tiempos = new HashMap<>();
        Set<Double> usados = new HashSet<>();
        // Se validan todos los tiempos antes de crear los aportes.
        for (Participante participante : participantes) {
            String clave = participante.getCodigo() + ".tiempo";
            String texto = datos.get(clave);
            if (texto == null) throw new IllegalArgumentException("Falta " + clave + ".");
            texto = texto.trim();
            if (texto.equalsIgnoreCase("DQ")) continue;
            double tiempo;
            try {
                if (!texto.matches("[0-9]+(\\.[0-9]+)?")) throw new NumberFormatException();
                tiempo = Double.parseDouble(texto);
            } catch (NumberFormatException error) {
                throw new IllegalArgumentException(clave + " debe ser un tiempo positivo en segundos o DQ.");
            }
            if (!Double.isFinite(tiempo) || tiempo <= 0) throw new IllegalArgumentException("El tiempo debe ser positivo y finito.");
            if (!usados.add(tiempo)) throw new IllegalArgumentException("Los tiempos de relevos no pueden repetirse.");
            tiempos.put(participante.getCodigo(), tiempo);
            conTiempo.add(participante);
        }
        conTiempo.sort((a, b) -> Double.compare(tiempos.get(a.getCodigo()), tiempos.get(b.getCodigo())));
        int[] puntosPorLugar = {10, 8, 6, 5, 4, 3, 2, 1};
        List<Aporte> aportes = new ArrayList<>();
        for (Participante participante : participantes) {
            int lugar = conTiempo.indexOf(participante);
            int puntos = lugar == -1 ? 0 : puntosPorLugar[lugar];
            aportes.add(new Aporte(participante, puntos, 0, lugar == 0 ? 1 : 0));
        }
        return aportes;
    }
}
