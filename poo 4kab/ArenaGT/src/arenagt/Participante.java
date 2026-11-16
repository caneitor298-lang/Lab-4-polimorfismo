package arenagt;

/** Un equipo o cuarteto. Sus puntos se guardan en cada torneo. */
public class Participante {
    private final String codigo;
    private final String nombre;
    private final String empresa;

    public Participante(String codigo, String nombre, String empresa) {
        this.codigo = texto(codigo, "Código").toUpperCase(java.util.Locale.ROOT);
        this.nombre = texto(nombre, "Nombre");
        this.empresa = texto(empresa, "Empresa");
    }

    // Se usa también para limpiar los textos de otros registros.
    static String texto(String valor, String campo) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalArgumentException(campo + " no puede estar vacío.");
        }
        return valor.trim().replaceAll("\\s+", " ");
    }

    public String getCodigo() { return codigo; }
    public String getNombre() { return nombre; }
    public String getEmpresa() { return empresa; }

    @Override
    public String toString() {
        return codigo + " - " + nombre + " (" + empresa + ")";
    }
}
