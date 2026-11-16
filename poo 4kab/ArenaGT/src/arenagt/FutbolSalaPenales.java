package arenagt;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FutbolSalaPenales extends FutbolSala {
    public FutbolSalaPenales() { super(); }

    @Override public String getNombre() { return "Fútbol sala con penales"; }
    @Override public double getCuota() { return super.getCuota() + 150; }

    @Override
    public List<String> camposResultado(List<Participante> participantes) {
        List<String> campos = new ArrayList<>(super.camposResultado(participantes));
        campos.addAll(campos(participantes, "penales"));
        return campos;
    }

    @Override
    public List<Aporte> evaluar(List<Participante> participantes, Map<String, String> datos) {
        validarPareja(participantes);
        validarCampos(datos, camposResultado(participantes));
        String codigoA = participantes.get(0).getCodigo();
        String codigoB = participantes.get(1).getCodigo();
        int golesA = leerMarcador(datos, codigoA + ".goles");
        int golesB = leerMarcador(datos, codigoB + ".goles");
        if (golesA != golesB) {
            String penalesA = datos.get(codigoA + ".penales");
            String penalesB = datos.get(codigoB + ".penales");
            if ((penalesA != null && !penalesA.trim().isEmpty())
                    || (penalesB != null && !penalesB.trim().isEmpty())) {
                throw new IllegalArgumentException("Sin empate en goles no se registran penales.");
            }
            return super.evaluar(participantes, datos);
        }
        int penalesA = leerMarcador(datos, codigoA + ".penales");
        int penalesB = leerMarcador(datos, codigoB + ".penales");
        if (penalesA == penalesB) throw new IllegalArgumentException("Los penales no pueden quedar empatados.");
        return aportes(participantes, penalesA > penalesB ? 2 : 1, penalesA > penalesB ? 1 : 2,
                0, penalesA > penalesB ? 0 : 1);
    }
}
