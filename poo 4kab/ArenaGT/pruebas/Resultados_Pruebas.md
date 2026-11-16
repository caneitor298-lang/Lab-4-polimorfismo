# Evidencia de pruebas de ArenaGT

Ejecución automática sobre las clases reales del programa. Cada caso usa sus propios datos o indica la carga inicial.

| Caso | Regla | Entrada | Esperado | Obtenido | Estado |
|---|---|---|---|---|---|
| 1 | Cuota FS | FS | 600.0 | 600.0 | OK |
| 2 | Mínimo FS | FS | 4 | 4 | OK |
| 3 | Máximo FS | FS | 8 | 8 | OK |
| 4 | Cotización FS | 2 participantes | 1200.0 | 1200.0 | OK |
| 5 | Cotización no modifica | FS | true | true | OK |
| 6 | Cobros FS | 4 inscripciones | 2400.0 | 2400.0 | OK |
| 7 | Cobros por disciplina FS | FS | 2400.0 | 2400.0 | OK |
| 8 | Estado inicial | FS | En inscripción | En inscripción | OK |
| 9 | Cierre FS | Cerrar con mínimo | En juego | En juego | OK |
| 10 | Cantidad de eventos FS | 4 inscritos | 6 | 6 | OK |
| 11 | Finalización automática FS | Todos los resultados | Finalizado | Finalizado | OK |
| 12 | Premio FS | 50% x cuota x inscritos | 1200.0 | 1200.0 | OK |
| 13 | Entrega FS | Último resultado | 1200.0 | 1200.0 | OK |
| 14 | Campeón FS | P1 gana sus eventos | P1 | P1 | OK |
| 15 | Premio único FS | Torneo terminado | 1 | 1 | OK |
| 16 | No duplicar premio FS | Repetir resultado | Rechazo y estado completo igual | Rechazo y estado completo igual | OK |
| 17 | No inscribir en finalizado FS | P10 | Rechazo y estado completo igual | Rechazo y estado completo igual | OK |
| 18 | Cuota FP | FP | 750.0 | 750.0 | OK |
| 19 | Mínimo FP | FP | 4 | 4 | OK |
| 20 | Máximo FP | FP | 8 | 8 | OK |
| 21 | Cotización FP | 2 participantes | 1500.0 | 1500.0 | OK |
| 22 | Cotización no modifica | FP | true | true | OK |
| 23 | Cobros FP | 4 inscripciones | 3000.0 | 3000.0 | OK |
| 24 | Cobros por disciplina FP | FP | 3000.0 | 3000.0 | OK |
| 25 | Estado inicial | FP | En inscripción | En inscripción | OK |
| 26 | Cierre FP | Cerrar con mínimo | En juego | En juego | OK |
| 27 | Cantidad de eventos FP | 4 inscritos | 6 | 6 | OK |
| 28 | Finalización automática FP | Todos los resultados | Finalizado | Finalizado | OK |
| 29 | Premio FP | 50% x cuota x inscritos | 1500.0 | 1500.0 | OK |
| 30 | Entrega FP | Último resultado | 1500.0 | 1500.0 | OK |
| 31 | Campeón FP | P1 gana sus eventos | P1 | P1 | OK |
| 32 | Premio único FP | Torneo terminado | 1 | 1 | OK |
| 33 | No duplicar premio FP | Repetir resultado | Rechazo y estado completo igual | Rechazo y estado completo igual | OK |
| 34 | No inscribir en finalizado FP | P10 | Rechazo y estado completo igual | Rechazo y estado completo igual | OK |
| 35 | Cuota BC | BC | 500.0 | 500.0 | OK |
| 36 | Mínimo BC | BC | 4 | 4 | OK |
| 37 | Máximo BC | BC | 6 | 6 | OK |
| 38 | Cotización BC | 2 participantes | 1000.0 | 1000.0 | OK |
| 39 | Cotización no modifica | BC | true | true | OK |
| 40 | Cobros BC | 4 inscripciones | 2000.0 | 2000.0 | OK |
| 41 | Cobros por disciplina BC | BC | 2000.0 | 2000.0 | OK |
| 42 | Estado inicial | BC | En inscripción | En inscripción | OK |
| 43 | Cierre BC | Cerrar con mínimo | En juego | En juego | OK |
| 44 | Cantidad de eventos BC | 4 inscritos | 6 | 6 | OK |
| 45 | Finalización automática BC | Todos los resultados | Finalizado | Finalizado | OK |
| 46 | Premio BC | 50% x cuota x inscritos | 1000.0 | 1000.0 | OK |
| 47 | Entrega BC | Último resultado | 1000.0 | 1000.0 | OK |
| 48 | Campeón BC | P1 gana sus eventos | P1 | P1 | OK |
| 49 | Premio único BC | Torneo terminado | 1 | 1 | OK |
| 50 | No duplicar premio BC | Repetir resultado | Rechazo y estado completo igual | Rechazo y estado completo igual | OK |
| 51 | No inscribir en finalizado BC | P10 | Rechazo y estado completo igual | Rechazo y estado completo igual | OK |
| 52 | Cuota VB | VB | 450.0 | 450.0 | OK |
| 53 | Mínimo VB | VB | 4 | 4 | OK |
| 54 | Máximo VB | VB | 6 | 6 | OK |
| 55 | Cotización VB | 2 participantes | 900.0 | 900.0 | OK |
| 56 | Cotización no modifica | VB | true | true | OK |
| 57 | Cobros VB | 4 inscripciones | 1800.0 | 1800.0 | OK |
| 58 | Cobros por disciplina VB | VB | 1800.0 | 1800.0 | OK |
| 59 | Estado inicial | VB | En inscripción | En inscripción | OK |
| 60 | Cierre VB | Cerrar con mínimo | En juego | En juego | OK |
| 61 | Cantidad de eventos VB | 4 inscritos | 6 | 6 | OK |
| 62 | Finalización automática VB | Todos los resultados | Finalizado | Finalizado | OK |
| 63 | Premio VB | 50% x cuota x inscritos | 900.0 | 900.0 | OK |
| 64 | Entrega VB | Último resultado | 900.0 | 900.0 | OK |
| 65 | Campeón VB | P1 gana sus eventos | P1 | P1 | OK |
| 66 | Premio único VB | Torneo terminado | 1 | 1 | OK |
| 67 | No duplicar premio VB | Repetir resultado | Rechazo y estado completo igual | Rechazo y estado completo igual | OK |
| 68 | No inscribir en finalizado VB | P10 | Rechazo y estado completo igual | Rechazo y estado completo igual | OK |
| 69 | Cuota RL | RL | 300.0 | 300.0 | OK |
| 70 | Mínimo RL | RL | 3 | 3 | OK |
| 71 | Máximo RL | RL | 8 | 8 | OK |
| 72 | Cotización RL | 2 participantes | 600.0 | 600.0 | OK |
| 73 | Cotización no modifica | RL | true | true | OK |
| 74 | Cobros RL | 3 inscripciones | 900.0 | 900.0 | OK |
| 75 | Cobros por disciplina RL | RL | 900.0 | 900.0 | OK |
| 76 | Estado inicial | RL | En inscripción | En inscripción | OK |
| 77 | Cierre RL | Cerrar con mínimo | En juego | En juego | OK |
| 78 | Cantidad de eventos RL | 3 inscritos | 1 | 1 | OK |
| 79 | Finalización automática RL | Todos los resultados | Finalizado | Finalizado | OK |
| 80 | Premio RL | 50% x cuota x inscritos | 450.0 | 450.0 | OK |
| 81 | Entrega RL | Último resultado | 450.0 | 450.0 | OK |
| 82 | Campeón RL | P1 gana sus eventos | P1 | P1 | OK |
| 83 | Premio único RL | Torneo terminado | 1 | 1 | OK |
| 84 | No duplicar premio RL | Repetir resultado | Rechazo y estado completo igual | Rechazo y estado completo igual | OK |
| 85 | No inscribir en finalizado RL | P10 | Rechazo y estado completo igual | Rechazo y estado completo igual | OK |
| 86 | Fútbol victoria local | 4 a 1 | 3,3,1;0,-3,0; | 3,3,1;0,-3,0; | OK |
| 87 | Fútbol victoria visitante | 0 a 2 | 0,-2,0;3,2,1; | 0,-2,0;3,2,1; | OK |
| 88 | Fútbol empate con cero | 0 a 0 | 1,0,0;1,0,0; | 1,0,0;1,0,0; | OK |
| 89 | Penales local | 2 a 2 / penales 4 a 3 | 2,0,1;1,0,0; | 2,0,1;1,0,0; | OK |
| 90 | Penales visitante y cero válido | 0 a 0 / penales 0 a 1 | 1,0,0;2,0,1; | 1,0,0;2,0,1; | OK |
| 91 | Penales sin desempate | 3 a 1 | 3,2,1;0,-2,0; | 3,2,1;0,-2,0; | OK |
| 92 | Baloncesto local | 80 a 75 | 2,5,1;1,-5,0; | 2,5,1;1,-5,0; | OK |
| 93 | Baloncesto visitante y cero | 0 a 2 | 1,-2,0;2,2,1; | 1,-2,0;2,2,1; | OK |
| 94 | Voleibol 3 a 0 | 3 a 0 | 3,3,1;0,-3,0; | 3,3,1;0,-3,0; | OK |
| 95 | Voleibol 3 a 1 | 3 a 1 | 3,2,1;0,-2,0; | 3,2,1;0,-2,0; | OK |
| 96 | Voleibol 3 a 2 | 3 a 2 | 2,1,1;1,-1,0; | 2,1,1;1,-1,0; | OK |
| 97 | Voleibol 2 a 3 | 2 a 3 | 1,-1,0;2,1,1; | 1,-1,0;2,1,1; | OK |
| 98 | Ocho lugares de relevos | 50 a 57 segundos | 10,8,6,5,4,3,2,1, | 10,8,6,5,4,3,2,1, | OK |
| 99 | Diferencias en relevos | Ocho tiempos | true | true | OK |
| 100 | Victoria en relevos | Mejor tiempo | 1 | 1 | OK |
| 101 | Segundo no obtiene victoria | Segundo tiempo | 0 | 0 | OK |
| 102 | Descalificación | P2 DQ | 0 | 0 | OK |
| 103 | DQ no ocupa lugar | P3 después de P2 DQ | 8 | 8 | OK |
| 104 | Código vacío | Participante código vacío | Rechazo y estado completo igual | Rechazo y estado completo igual | OK |
| 105 | Código repetido | p1 con espacios | Rechazo y estado completo igual | Rechazo y estado completo igual | OK |
| 106 | Nombre vacío | Participante sin nombre | Rechazo y estado completo igual | Rechazo y estado completo igual | OK |
| 107 | Empresa vacía | Participante sin empresa | Rechazo y estado completo igual | Rechazo y estado completo igual | OK |
| 108 | Disciplina desconocida | Crear con XX | Rechazo y estado completo igual | Rechazo y estado completo igual | OK |
| 109 | Cotización cero | FS, 0 | Rechazo y estado completo igual | Rechazo y estado completo igual | OK |
| 110 | Cotización negativa | FS, -1 | Rechazo y estado completo igual | Rechazo y estado completo igual | OK |
| 111 | Torneo repetido | t con espacios | Rechazo y estado completo igual | Rechazo y estado completo igual | OK |
| 112 | Torneo sin código | Código vacío | Rechazo y estado completo igual | Rechazo y estado completo igual | OK |
| 113 | Cierre sin mínimo | T vacío | Rechazo y estado completo igual | Rechazo y estado completo igual | OK |
| 114 | Participante inexistente | PX | Rechazo y estado completo igual | Rechazo y estado completo igual | OK |
| 115 | Inscripción duplicada | P1 otra vez | Rechazo y estado completo igual | Rechazo y estado completo igual | OK |
| 116 | Límite por empresa | Tercer equipo de Empresa 1 | Rechazo y estado completo igual | Rechazo y estado completo igual | OK |
| 117 | Cupo máximo | Noveno inscrito en fútbol | Rechazo y estado completo igual | Rechazo y estado completo igual | OK |
| 118 | Cierre repetido | Cerrar T dos veces | Rechazo y estado completo igual | Rechazo y estado completo igual | OK |
| 119 | Inscripción fuera de estado | P9 en torneo en juego | Rechazo y estado completo igual | Rechazo y estado completo igual | OK |
| 120 | Resultado sin programación | T-E1 | Rechazo y estado completo igual | Rechazo y estado completo igual | OK |
| 121 | Hora negativa | Hora -1 | Rechazo y estado completo igual | Rechazo y estado completo igual | OK |
| 122 | Hora fuera de rango | Hora 24 | Rechazo y estado completo igual | Rechazo y estado completo igual | OK |
| 123 | Sede vacía | Sede sin nombre | Rechazo y estado completo igual | Rechazo y estado completo igual | OK |
| 124 | Goles negativos | -1 a 0 | Rechazo y estado completo igual | Rechazo y estado completo igual | OK |
| 125 | Goles decimales | 1.5 a 0 | Rechazo y estado completo igual | Rechazo y estado completo igual | OK |
| 126 | Marcador fuera de int | 999999999999 a 0 | Rechazo y estado completo igual | Rechazo y estado completo igual | OK |
| 127 | Falta marcador | Mapa vacío | Rechazo y estado completo igual | Rechazo y estado completo igual | OK |
| 128 | Campo de otro participante | PX.goles | Rechazo y estado completo igual | Rechazo y estado completo igual | OK |
| 129 | Resultado repetido | T-E1 | Rechazo y estado completo igual | Rechazo y estado completo igual | OK |
| 130 | Reprogramar finalizado | T-E1 | Rechazo y estado completo igual | Rechazo y estado completo igual | OK |
| 131 | Baloncesto empatado | 80 a 80 | Rechazo y estado completo igual | Rechazo y estado completo igual | OK |
| 132 | Voleibol inválido | 2 a 1 | Rechazo y estado completo igual | Rechazo y estado completo igual | OK |
| 133 | Voleibol inválido | 3 a 3 | Rechazo y estado completo igual | Rechazo y estado completo igual | OK |
| 134 | Voleibol inválido | 4 a 2 | Rechazo y estado completo igual | Rechazo y estado completo igual | OK |
| 135 | Voleibol inválido | 3 a -1 | Rechazo y estado completo igual | Rechazo y estado completo igual | OK |
| 136 | Empate sin penales | 1 a 1, sin penales | Rechazo y estado completo igual | Rechazo y estado completo igual | OK |
| 137 | Penales iguales | 1 a 1, 4 a 4 | Rechazo y estado completo igual | Rechazo y estado completo igual | OK |
| 138 | Penales sin empate de goles | 2 a 1 con penales 4 a 3 | Rechazo y estado completo igual | Rechazo y estado completo igual | OK |
| 139 | Tiempo inválido | P1=0 | Rechazo y estado completo igual | Rechazo y estado completo igual | OK |
| 140 | Tiempo inválido | P1=-1 | Rechazo y estado completo igual | Rechazo y estado completo igual | OK |
| 141 | Tiempo inválido | P1=NaN | Rechazo y estado completo igual | Rechazo y estado completo igual | OK |
| 142 | Tiempo inválido | P1=Infinity | Rechazo y estado completo igual | Rechazo y estado completo igual | OK |
| 143 | Tiempo inválido | P1= | Rechazo y estado completo igual | Rechazo y estado completo igual | OK |
| 144 | Tiempo inválido | P1=texto | Rechazo y estado completo igual | Rechazo y estado completo igual | OK |
| 145 | Tiempos numéricamente repetidos | 50.1 y 50.10 | Rechazo y estado completo igual | Rechazo y estado completo igual | OK |
| 146 | Error al final de carrera | Dos tiempos válidos, último repetido | Rechazo y estado completo igual | Rechazo y estado completo igual | OK |
| 147 | Falta un tiempo | Solo P1 y P2 | Rechazo y estado completo igual | Rechazo y estado completo igual | OK |
| 148 | Evitar desbordamiento de diferencia | Segundo marcador 2147483647 a 0 | Rechazo y estado completo igual | Rechazo y estado completo igual | OK |
| 149 | Calendario mixto por fecha hora y sede | Carrera y encuentros; Zeta se programa antes que Alfa | R-C1,T-E6,T-E1,T-E2, | R-C1,T-E6,T-E1,T-E2, | OK |
| 150 | Sin programar se omiten | 7 eventos, 4 programados | 4 | 4 | OK |
| 151 | Calendario por torneo | T | 3 | 3 | OK |
| 152 | Calendario por participante | P1 | 3 | 3 | OK |
| 153 | Conflicto de sede | Mover T-E6 a Zeta a las 9 | Rechazo y estado completo igual | Rechazo y estado completo igual | OK |
| 154 | Conflicto de participante entre torneos | Carrera a las 9 en otra sede | Rechazo y estado completo igual | Rechazo y estado completo igual | OK |
| 155 | Se excluye el propio evento | Mismo horario actual | 9 | 9 | OK |
| 156 | Reprogramación válida | Mover a 10 | 10 | 10 | OK |
| 157 | Reprogramado sigue programado | Sin resultado | Programado | Programado | OK |
| 158 | Hora límite válida | 23 | 23 | 23 | OK |
| 159 | Participantes iniciales | Carga directa | 8 | 8 | OK |
| 160 | Torneos iniciales | Cinco disciplinas y dos relevos | 6 | 6 | OK |
| 161 | Sin cobros en carga inicial | Carga directa | 0.0 | 0.0 | OK |
| 162 | Sin entregas en carga inicial | Resultados de ejemplo | 0.0 | 0.0 | OK |
| 163 | Torneos en juego | Carga inicial | 4 | 4 | OK |
| 164 | Todos los eventos iniciales programados | Carga inicial | 30 | 30 | OK |
| 165 | Desempates por diferencia y victorias | FS01 | P05,P03,P04,P01,P02, | P05,P03,P04,P01,P02, | OK |
| 166 | Diferencia decide | P05 y P03 con cuatro puntos | true | true | OK |
| 167 | Victorias deciden | P01 y P02 con tres puntos y diferencia cero | true | true | OK |
| 168 | Ranking suma todos los torneos | Datos iniciales | {Ceiba=45, Volcán=34, Quetzal=11, Jaguar=3} | {Ceiba=45, Volcán=34, Quetzal=11, Jaguar=3} | OK |
| 169 | Carga sin conflictos globales | 30 eventos | true | true | OK |
| 170 | No repetir carga inicial | Cargar otra vez | Rechazo y estado completo igual | Rechazo y estado completo igual | OK |
| 171 | Desempate por nombre | Tres filas en cero | A | A | OK |
| 172 | Desempate de empresas por nombre | Todas con cero | [Alfa, Beta, Zeta] | [Alfa, Beta, Zeta] | OK |
| 173 | Tabla de consulta independiente | Modificar una copia | 0 | 0 | OK |
| 174 | Iterador protege calendario | iterator.remove | true | true | OK |
| 175 | Consola acepta seguir tras error | Texto, vacío, opción 99, cantidad inválida, listar, salir | true | true | OK |
| 176 | Fecha inválida no termina el menú | 30 de febrero y luego fecha válida | true | true | OK |
| 177 | Fecha corregida se conserva | 2027-02-01 | 2027-02-01 | 2027-02-01 | OK |
| 178 | Fin de entrada | Consola sin más líneas | true | true | OK |

Total: 178. Correctas: 178. Fallidas: 0.
