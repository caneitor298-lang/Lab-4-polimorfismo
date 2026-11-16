package arenagt;

import java.time.LocalDate;

/** Contrato para un objeto al que se le puede asignar un horario. */
public interface Programable {
    void programar(LocalDate fecha, int hora, String sede);
    LocalDate getFecha();
    int getHora();
    String getSede();
}
