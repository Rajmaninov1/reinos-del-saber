# Reinos del Saber: propuesta de juego

## Presentación del juego

**Nombre:** Reinos del Saber. Resume la idea central: cada jugador construye su reino con lo que sabe, porque el territorio se gana respondiendo correctamente.

**Tipo de juego:** educativo de estrategia por turnos, para niños y adultos.

**Propósito educativo:** practicar operaciones matemáticas, vocabulario en inglés, sinónimos y antónimos.

**Número de jugadores:** 3 participantes, de 1 a 3 humanos en un mismo computador; los puestos restantes son CPU.

**Originalidad:** combina la conquista de territorio con retos de conocimiento, y cada jugador responde preguntas de su propio nivel.

## Alcance

La versión mínima es un juego de conquista por turnos para 3 participantes en un mapa fijo de 81 casillas, desarrollado en Java con JavaFX. De 1 a 3 participantes son humanos en un mismo computador (Hot Seat), y los puestos restantes son CPU.

- 3 países de 27 casillas en un solo continente.
- Casillas de tierra y agua.
- Una sola acción: Atacar.
- Una sede por jugador, que puede ser aldea, pueblo o ciudad.
- Preguntas de opción múltiple con 4 respuestas.
- 3 niveles de dificultad elegidos al inicio.
- Traducción entre español e inglés.
- CPU con 3 dificultades.
- Temporizador por nivel.
- Partida de una sola sesión.

## Reglas del juego

Cada jugador representa un país y empieza con las 27 casillas de ese país. Gana quien conquiste primero una sede rival. El orden de los turnos y el país de inicio se sortean al crear la partida.

### Turno

El tamaño de la sede depende del total de casillas del jugador y define sus acciones. La cantidad de acciones se calcula al iniciar el turno y no cambia durante ese turno.

| Tamaño | Casillas del jugador | Acciones por turno |
| --- | --- | --- |
| Aldea | Hasta 27 | 2 |
| Pueblo | 28 a 37 | 3 |
| Ciudad | 38 o más | 4 |

| Acción | Costo | Casilla válida | Efecto |
| --- | --- | --- | --- |
| Atacar | 1 acción | De otro país y en la frontera del tuyo | Serie de retos; si la completas, la casilla es tuya |

**Frontera:** solo se puede atacar una casilla de otro país que toque al menos una casilla del país del jugador. No existen casillas libres: toda casilla pertenece a un país desde el inicio y la controla el jugador de ese país.

### Serie de retos

Atacar pide acertar seguido tantos retos como Defensa tenga la casilla. Un fallo o un tiempo agotado termina la serie y consume la acción.

| Casilla | Defensa |
| --- | --- |
| Tierra | 1 |
| Agua | 2 |
| Sede | 4 |

### Retos

- El tema depende del país al que pertenece la casilla: Números, Idiomas o Palabras. Al conquistarla, pasa al país del conquistador, y desde el siguiente turno sus retos usan ese tema.
- El nivel depende de quien responde, y cada intento recibe un reto nuevo.
- Al fallar, el jugador ve la respuesta correcta.

| Nivel | Tiempo por reto |
| --- | --- |
| Fácil | 30 s |
| Medio | 20 s |
| Difícil | 15 s |

### Final de la partida

La partida termina de inmediato cuando un jugador conquista una sede rival, y ese jugador gana.

## Imágenes

La pantalla de partida muestra el mapa de 81 casillas, las casillas enemigas que se pueden atacar y un reto activo. El panel lateral cambia según el momento del turno.

![Pantalla de partida con el mapa de 81 casillas y un reto activo](<../imagenes/Pantalla de partida@1x.png>)

![Estados del panel lateral: sin casilla elegida, vista previa, últimos segundos, después de fallar y turno de la CPU](<../imagenes/Estados del panel lateral@1x.png>)
