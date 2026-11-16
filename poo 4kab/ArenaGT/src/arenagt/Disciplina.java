package arenagt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/** Cada deporte completa sus reglas; el torneo usa esta clase general. */
public abstract class Disciplina {
    private final String nombre;
    private final int minimo;
    private final int maximo;
    private final double cuota;

    protected Disciplina(String nombre, int minimo, int maximo, double cuota) {
        this.nombre = Participante.texto(nombre, "Disciplina");
        if (minimo < 1 || maximo < minimo || !Double.isFinite(cuota) || cuota <= 0) {
            throw new IllegalArgumentException("Cupos o cuota no válidos.");
        }
        this.minimo = minimo;
        this.maximo = maximo;
        this.cuota = cuota;
    }

    public String getNombre() { return nombre; }
    public int getMinimo() { return minimo; }
    public int getMaximo() { return maximo; }
    public double getCuota() { return cuota; }

    public double cotizar(int cantidad) {
        if (cantidad <= 0) throw new IllegalArgumentException("La cantidad debe ser positiva.");
        return cantidad * getCuota();
    }

    public void validarInscripcion(List<Participante> inscritos, Participante candidato) {
        if (candidato == null) throw new IllegalArgumentException("Falta el participante.");
        if (inscritos.size() >= maximo) throw new IllegalArgumentException("El torneo ya alcanzó su cupo máximo.");
        int mismaEmpresa = 0;
        for (Participante participante : inscritos) {
            if (participante.getCodigo().equals(candidato.getCodigo())) {
                throw new IllegalArgumentException("El participante ya está inscrito.");
            }
            if (participante.getEmpresa().equalsIgnoreCase(candidato.getEmpresa())) mismaEmpresa++;
        }
        if (mismaEmpresa >= 2) throw new IllegalArgumentException("Esa empresa ya tiene dos participantes inscritos.");
    }

    protected List<Evento> crearEncuentros(String prefijo, List<Participante> inscritos) {
        List<Evento> eventos = new ArrayList<>();
        int numero = 1;
        for (int i = 0; i < inscritos.size(); i++) {
            for (int j = i + 1; j < inscritos.size(); j++) {
                eventos.add(new Encuentro(prefijo + "-E" + numero++, inscritos.get(i), inscritos.get(j), this));
            }
        }
        return eventos;
    }

    protected List<String> campos(List<Participante> participantes, String nombreCampo) {
        List<String> campos = new ArrayList<>();
        for (Participante participante : participantes) campos.add(participante.getCodigo() + "." + nombreCampo);
        return campos;
    }

    protected void validarCampos(Map<String, String> datos, List<String> permitidos) {
        if (datos == null) throw new IllegalArgumentException("Faltan los datos del resultado.");
        for (Map.Entry<String, String> campo : datos.entrySet()) {
            if (!permitidos.contains(campo.getKey()) || campo.getValue() == null) {
                throw new IllegalArgumentException("Campo no válido: " + campo.getKey());
            }
        }
    }

    protected void validarPareja(List<Participante> participantes) {
        if (participantes == null || participantes.size() != 2 || participantes.get(0) == null
                || participantes.get(1) == null
                || participantes.get(0).getCodigo().equals(participantes.get(1).getCodigo())) {
            throw new IllegalArgumentException("Se necesitan dos participantes diferentes.");
        }
    }

    protected int leerMarcador(Map<String, String> datos, String clave) {
        String texto = datos.get(clave);
        try {
            if (texto == null || !texto.trim().matches("[0-9]+")) throw new NumberFormatException();
            return Integer.parseInt(texto.trim());
        } catch (NumberFormatException error) {
            throw new IllegalArgumentException(clave + " debe ser un entero no negativo dentro del rango de int.");
        }
    }

    protected List<Aporte> aportes(List<Participante> participantes, int puntosA, int puntosB,
                                  int diferencia, int ganador) {
        return Arrays.asList(new Aporte(participantes.get(0), puntosA, diferencia, ganador == 0 ? 1 : 0),
                new Aporte(participantes.get(1), puntosB, -diferencia, ganador == 1 ? 1 : 0));
    }

    protected void validarLista(List<Participante> participantes) {
        if (participantes == null || participantes.size() < minimo || participantes.size() > maximo) {
            throw new IllegalArgumentException("La cantidad de participantes no corresponde a la disciplina.");
        }
        Set<String> codigos = new HashSet<>();
        for (Participante p : participantes) {
            if (p == null || !codigos.add(p.getCodigo())) throw new IllegalArgumentException("Participantes repetidos o nulos.");
        }
    }

    public abstract List<Evento> crearEventos(String prefijo, List<Participante> inscritos);
    public abstract List<String> camposResultado(List<Participante> participantes);
    public abstract List<Aporte> evaluar(List<Participante> participantes, Map<String, String> datos);
}
