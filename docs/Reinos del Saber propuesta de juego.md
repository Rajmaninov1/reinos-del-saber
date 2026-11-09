# Reinos del Saber: propuesta de juego

## Presentación del juego

**Nombre:** Reinos del Saber. Resume la idea central del juego. Cada jugador hace crecer su reino con lo que sabe, porque gana territorio respondiendo correctamente.

**Tipo de juego:** educativo y de estrategia por turnos, para niños y adultos. "Por turnos" significa que los jugadores actúan uno a la vez.

**Propósito educativo:** practicar operaciones matemáticas, vocabulario en inglés, sinónimos y antónimos.

**Número de jugadores:** siempre participan 3. Pueden ser de 1 a 3 personas, que se turnan en un mismo computador. Esta modalidad suele llamarse "Hot Seat", porque es como si se pasaran el asiento caliente. Si faltan personas, el computador juega en los puestos restantes; en el juego, esos puestos se llaman CPU.

**Originalidad:** une la conquista de territorio con preguntas de conocimiento, con tres elementos propios: cada persona responde según su nivel, el tema depende del país atacado y, cuanto más territorio tiene un jugador, más acciones gana por turno.

## El juego en pocas palabras

Tres jugadores comparten un mapa dividido en casillas, y cada uno empieza con un país de 27 casillas. En su turno, un jugador elige una casilla enemiga junto a su territorio y responde preguntas para quedarse con ella. Cada jugador tiene una sede, que es su casilla principal, como una capital. Gana quien conquiste primero la sede de otro jugador.

## Alcance

La primera versión del juego incluye lo siguiente:

- Un mapa de 81 casillas, siempre el mismo, con 3 países de 27 casillas unidos en un solo continente.
- Casillas de tierra y de agua.
- Una sola acción posible: atacar una casilla.
- Una sede por jugador, que puede ser aldea, pueblo o ciudad.
- Preguntas con 4 opciones de respuesta.
- 3 niveles de preguntas: fácil, medio y difícil. Cada persona elige el suyo al iniciar la partida.
- Preguntas de traducción entre español e inglés.
- 3 dificultades para la CPU: cuanto más alta es la dificultad, más veces acierta.
- Un tiempo límite para responder, que depende del nivel.
- Partidas que se juegan de principio a fin, sin guardarlas para continuar después.

El juego se desarrollará en Java y usará JavaFX para crear las ventanas y los gráficos.

## Reglas del juego

Cada jugador representa un país y empieza con sus 27 casillas. Al crear la partida, un sorteo define el orden de los turnos y el país de cada jugador. Gana quien conquiste primero una sede rival.

Toda casilla pertenece a un país desde el inicio y la controla el jugador de ese país. Por eso no hay casillas libres: para ganar territorio hay que quitárselo a otro jugador.

### Turno

En cada turno, el jugador debe usar todas sus acciones; al agotarlas, el turno pasa al siguiente jugador. Hacer una acción significa atacar una casilla, y el ataque cuesta 1 acción aunque no logre conquistarla.

La cantidad de acciones depende de cuántas casillas tenga el jugador en total, y ese total también define el tamaño de su sede:

| Tamaño de la sede | Casillas del jugador | Acciones por turno |
| --- | --- | --- |
| Aldea | Hasta 27 | 2 |
| Pueblo | De 28 a 37 | 3 |
| Ciudad | 38 o más | 4 |

Las acciones se cuentan al empezar el turno. Si el jugador conquista casillas durante su turno y cambia de tamaño, las acciones adicionales empiezan a contar en su siguiente turno.

Solo se puede atacar una casilla de otro país que toque, por al menos uno de sus lados, una casilla del país del jugador. Esa zona de contacto entre países se llama **frontera**.

### Cómo se conquista una casilla

Para conquistar una casilla hay que responder bien, una tras otra, tantas preguntas como indique su **Defensa**. Si el jugador falla una pregunta o se le acaba el tiempo, el ataque termina, la casilla no cambia de dueño y la acción se pierde.

| Tipo de casilla | Defensa (aciertos seguidos necesarios) |
| --- | --- |
| Tierra | 1 |
| Agua | 2 |
| Sede | 4 |

### Preguntas

El tema de las preguntas depende del país al que pertenece la casilla atacada:

| País | Tema de las preguntas |
| --- | --- |
| Números | Operaciones matemáticas |
| Idiomas | Vocabulario en inglés |
| Palabras | Sinónimos y antónimos |

Cuando un jugador conquista una casilla, esta pasa a formar parte de su país. Desde el siguiente turno, quien la ataque recibirá preguntas del tema de ese nuevo país.

El nivel de las preguntas depende de quien responde, y cada nivel da un tiempo distinto para contestar:

| Nivel | Tiempo por pregunta |
| --- | --- |
| Fácil | 30 segundos |
| Medio | 20 segundos |
| Difícil | 15 segundos |

Cada intento presenta una pregunta nueva. Si el jugador falla, el juego le muestra la respuesta correcta.

### Final de la partida

La partida termina en el momento en que un jugador conquista una sede rival, y ese jugador gana.

## Imágenes

El diseño previsto de la pantalla de partida muestra el mapa de 81 casillas, resalta con borde blanco las casillas enemigas que se pueden atacar y presenta la pregunta activa. El número de cada casilla resaltada indica cuántos aciertos seguidos se necesitan para conquistarla.

![Pantalla de partida con el mapa de 81 casillas y una pregunta activa](<../imagenes/Pantalla de partida@1x.png>)

El panel de la derecha cambia según lo que esté pasando en el turno.

![Cinco estados del panel de la derecha: sin casilla elegida, vista previa del ataque, últimos segundos para responder, después de fallar y turno de la CPU](<../imagenes/Estados del panel lateral@1x.png>)
