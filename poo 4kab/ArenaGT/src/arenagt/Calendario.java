package arenagt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/** El mismo orden se usa para el calendario general y los filtrados. */
public class Calendario implements Iterable<Evento> {
    private final List<Evento> eventos;

    public Calendario(Collection<Evento> eventos) {
        List<Evento> ordenados = new ArrayList<>();
        for (Evento evento : eventos) if (evento.getFecha() != null) ordenados.add(evento);
        Collections.sort(ordenados);
        this.eventos = Collections.unmodifiableList(ordenados);
    }

    @Override
    public Iterator<Evento> iterator() { return eventos.iterator(); }
    public int size() { return eventos.size(); }
}
