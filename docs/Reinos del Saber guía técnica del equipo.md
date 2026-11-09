# Reinos del Saber: guía técnica del equipo

Esta guía define cómo se implementa el juego y en qué orden se construye. Las primeras secciones describen el diseño, y [Plan de implementación](#plan-de-implementación) reparte ese trabajo entre las entregas del módulo. La presentación, el alcance y las reglas están en Reinos del Saber: propuesta de juego.

## Stack y estructura

El proyecto usa Java 21, OpenJFX 21, JUnit 5 y Maven, sin librerías adicionales. La regla de arquitectura es que ninguna clase de `modelo` importe `javafx.*`.

| Herramienta | Uso | Configuración clave |
| --- | --- | --- |
| Java 21 | Lenguaje | `maven.compiler.release` = 21 |
| OpenJFX 21 | Interfaz | `javafx-controls` como dependencia |
| Maven | Construcción | `javafx-maven-plugin`; se ejecuta con `mvn javafx:run` |
| JUnit 5 | Pruebas | `junit-jupiter` y Surefire; `mvn test` |
| Javadoc | Documentación | `maven-javadoc-plugin` con `<show>private</show>` |

La interfaz se construye por código, sin FXML, y el proyecto no usa `module-info.java`. El proyecto se construye, se prueba y se ejecuta con comandos de Maven desde la terminal, y no depende de la configuración de ningún editor.

```
src/main/java/com/reinos/
  modelo/mapa/        Coordenada, Pais, Terreno, Casilla, Mapa, CargadorMapa
  modelo/retos/       Nivel, Reto, BancoRetos
  modelo/jugador/     Jugador, JugadorHumano, JugadorCPU, DificultadCPU,
                      EstrategiaCPU, EstrategiaAvance
  modelo/partida/     ConfiguracionPartida, Partida, EstadoPartida,
                      SerieRetos, TamanoCiudad
  vista/              Launcher, JuegoApp, ConfiguracionView, PartidaController,
                      MapaView, PanelRetoView, PanelEstadoView,
                      PantallaFinalView
src/main/resources/
  mapa.txt
  retos/preguntas.csv
src/test/java/com/reinos/  Una clase de prueba por clase con lógica
```

### Configuración inicial

Estos archivos crean la base del proyecto. La configuración se probó con Java 21.0.13 y Maven 3.10.0.

`pom.xml`, en la raíz del proyecto:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>

  <groupId>com.reinos</groupId>
  <artifactId>reinos-del-saber</artifactId>
  <version>1.0-SNAPSHOT</version>
  <packaging>jar</packaging>

  <properties>
    <maven.compiler.release>21</maven.compiler.release>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    <javafx.version>21.0.13</javafx.version>
    <junit.version>5.14.4</junit.version>
  </properties>

  <dependencies>
    <dependency>
      <groupId>org.openjfx</groupId>
      <artifactId>javafx-controls</artifactId>
      <version>${javafx.version}</version>
    </dependency>
    <dependency>
      <groupId>org.junit.jupiter</groupId>
      <artifactId>junit-jupiter</artifactId>
      <version>${junit.version}</version>
      <scope>test</scope>
    </dependency>
  </dependencies>

  <build>
    <plugins>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-compiler-plugin</artifactId>
        <version>3.16.0</version>
      </plugin>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-surefire-plugin</artifactId>
        <version>3.6.1</version>
      </plugin>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-javadoc-plugin</artifactId>
        <version>3.12.0</version>
        <configuration>
          <show>private</show>
        </configuration>
      </plugin>
      <plugin>
        <groupId>org.openjfx</groupId>
        <artifactId>javafx-maven-plugin</artifactId>
        <version>0.0.8</version>
        <configuration>
          <mainClass>com.reinos.vista.Launcher</mainClass>
        </configuration>
      </plugin>
    </plugins>
  </build>
</project>
```

`src/main/java/com/reinos/vista/Launcher.java`:

```java
package com.reinos.vista;

import javafx.application.Application;

/** Punto de entrada de la aplicación. */
public final class Launcher {
    private Launcher() { }

    /**
     * Inicia la aplicación.
     *
     * @param args argumentos de la línea de comandos
     */
    public static void main(String[] args) {
        Application.launch(JuegoApp.class, args);
    }
}
```

`src/main/java/com/reinos/vista/JuegoApp.java`:

```java
package com.reinos.vista;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/** Ventana principal del juego. */
public class JuegoApp extends Application {
    @Override
    public void start(Stage ventana) {
        ventana.setScene(new Scene(new StackPane(new Label("Reinos del Saber")), 1280, 800));
        ventana.setTitle("Reinos del Saber");
        ventana.show();
    }
}
```

`mvn javafx:run` inicia el juego a través de `Launcher`, que no debe extender `Application`: como el proyecto no usa `module-info.java`, esa separación evita el error `JavaFX runtime components are missing`. `JuegoApp` es un punto de partida, y su contenido se reemplaza al construir las pantallas.

## Modelo

El modelo tiene 20 tipos en 4 paquetes y se puede ejecutar y probar sin abrir la interfaz. `Partida` y `BancoRetos` reciben un `RandomGenerator` en su constructor, para que las pruebas den resultados repetibles.

### modelo.mapa

| Tipo | Atributos | Métodos principales |
| --- | --- | --- |
| `Coordenada` (record) | `q`, `r` | `vecinos()` |
| `Pais` (enum) | `NUMEROS`, `IDIOMAS`, `PALABRAS` | Ninguno |
| `Terreno` (enum) | `TIERRA(1)`, `AGUA(2)` | `getDefensaBase()` |
| `Casilla` | `coordenada`, `pais`, `terreno`, `esSede` | `conquistar(pais)`, `getDefensa()` |
| `Mapa` | `Map<Coordenada, Casilla>` | `getCasilla(c)`, `adyacentes(c)`, `casillasDePais(pais)`, `esFrontera(c, pais)`, `distancia(desde, hasta)` (BFS) |
| `CargadorMapa` | Ninguno | `cargar(InputStream)` |

### modelo.retos

| Tipo | Atributos | Métodos principales |
| --- | --- | --- |
| `Nivel` (enum) | `FACIL(30)`, `MEDIO(20)`, `DIFICIL(15)` | `getSegundos()` |
| `Reto` (record) | `pais`, `nivel`, `enunciado`, `opciones`, `indiceCorrecto` | `esCorrecta(indice)`, `getRespuestaCorrecta()`, `barajado(random)` |
| `BancoRetos` | `retos`, `ultimo`, `random` | `cargar(InputStream)`, `agregar(reto)`, `obtener(pais, nivel)` |

### modelo.jugador

| Tipo | Atributos | Métodos principales |
| --- | --- | --- |
| `Jugador` (abstracta) | `nombre`, `pais`, `sede` | `esHumano()` |
| `JugadorHumano` | `nivel` | `getNivel()` |
| `JugadorCPU` | `dificultad`, `estrategia` | `simularRespuesta(random)`, `elegirCasilla(partida)` |
| `DificultadCPU` (enum) | `FACIL(0.5)`, `MEDIA(0.7)`, `DIFICIL(0.9)` | `getProbabilidadAcierto()` |
| `EstrategiaCPU` (interfaz) | Ninguno | `elegirCasilla(partida, jugador)` |
| `EstrategiaAvance` | Ninguno | Elige la casilla según la prioridad definida |

### modelo.partida

| Tipo | Atributos | Métodos principales |
| --- | --- | --- |
| `ConfiguracionPartida` | Humanos, niveles, dificultad de CPU | `validar()`: exige datos completos y nombres sin repetir |
| `Partida` | Mapa, jugadores, ronda, turno, estado, acciones restantes, ganador | `iniciar()`, `atacar(casilla)`, `responder(indice)`, `tiempoAgotado()`, `ejecutarAccionCPU()`, `casillasAtacables()`, `getRetoActual()`, `getGanador()`, `casillasDe(jugador)` |
| `EstadoPartida` (enum) | Estados del flujo | Ninguno |
| `SerieRetos` | `casilla`, `requeridos`, `aciertos`, `retoActual` | `registrarAcierto()`, `estaCompleta()` |
| `TamanoCiudad` (enum) | `ALDEA(2)`, `PUEBLO(3)`, `CIUDAD(4)` | `desde(casillas)`, `getAcciones()` |

Los retos no se guardan por casilla. `Partida` pide uno nuevo a `BancoRetos` en cada intento, según el nivel de quien responde. Los puestos de CPU se llaman `CPU 1` y `CPU 2`, y esos nombres quedan reservados.

## Flujo de la partida

`Partida` funciona como una máquina de 4 estados, y los métodos que modifican la partida rechazan con `IllegalStateException` las llamadas que no correspondan al estado actual.

| Estado | Cuándo aplica | Qué lo cambia |
| --- | --- | --- |
| `ELIGIENDO_CASILLA` | Turno de un humano, sin ataque en curso | `atacar(casilla)` pasa a `RESPONDIENDO_RETO` |
| `RESPONDIENDO_RETO` | Serie de retos en curso | Un acierto con la serie incompleta mantiene el estado con un reto nuevo; si la serie termina, finaliza la acción |
| `TURNO_CPU` | Turno de la CPU | `ejecutarAccionCPU()` resuelve una acción completa y la finaliza |
| `FINALIZADA` | Una sede fue conquistada | Es el estado final |

Al finalizar una acción, si se conquistó una sede, la partida pasa a `FINALIZADA`. Si no, y quedan acciones, el jugador sigue en su turno. Si no quedan, el turno pasa al siguiente jugador: a `ELIGIENDO_CASILLA` si es humano o a `TURNO_CPU` si es CPU. `iniciar()` aplica esa misma regla al primer jugador sorteado.

El cambio de turno es directo y no tiene pantalla intermedia, porque ninguna información es secreta: el panel muestra de quién es el turno, y el reto y el tiempo solo empiezan al pulsar Comenzar.

`PartidaController` es el único que llama a `Partida`. Después de cada llamada consulta su estado y ejecuta `refrescar()`, y así la interfaz se actualiza sin que el modelo conozca JavaFX. La excepción es un fallo o un tiempo agotado: el modelo ya pudo pasar al siguiente turno, así que el controlador espera a que el jugador pulse Continuar para ejecutar `refrescar()` y, si corresponde, agendar a la CPU.

## Retos y datos

Cada fila de `retos/preguntas.csv` es una pregunta completa, con su enunciado, la respuesta correcta y 3 opciones incorrectas. Todos los temas usan el mismo formato y el mismo código, así que agregar preguntas no requiere programar. El mínimo de contenido es de 20 filas por combinación de país y nivel, es decir, 180 filas en total, y se completa para la entrega 3.

```csv
pais;nivel;enunciado;correcta;incorrecta1;incorrecta2;incorrecta3
NUMEROS;FACIL;¿Cuánto es 7 + 5?;12;9;16;20
IDIOMAS;FACIL;¿Cómo se dice “perro” en inglés?;dog;cat;bird;fish
IDIOMAS;FACIL;¿Qué significa “dog” en español?;perro;gato;pájaro;pez
PALABRAS;FACIL;¿Cuál es un sinónimo de “feliz”?;alegre;triste;mesa;nube
```

El archivo se lee en UTF-8 y el `;` queda reservado como separador. Los enunciados usan comillas tipográficas (“ ”), porque las rectas se alteran al guardar el archivo desde Excel. Las filas de Idiomas deben cubrir los dos sentidos de la traducción.

`BancoRetos` elige los retos al azar. Una pregunta puede repetirse durante la partida, pero no dos veces seguidas si hay otra disponible, para cumplir la regla de que cada intento reciba un reto nuevo. Para eso guarda la última fila entregada. Cada vez que entrega un reto, baraja las opciones para que la correcta cambie de posición.

| Momento | Qué se valida |
| --- | --- |
| Al crear el `Reto` | Datos completos, exactamente 4 opciones sin repetir e índice correcto válido |
| Al llamar a `agregar(reto)` | Que no exista otra pregunta con el mismo país, nivel y enunciado |
| Al leer cada línea en `cargar(...)` | Omite el encabezado y las líneas vacías, y recorta los espacios de cada campo; exige 7 campos y valores válidos de `pais` y `nivel`; si una fila falla, el error indica su número de línea |
| Al terminar `cargar(...)` | Al menos una pregunta para cada una de las 9 combinaciones de país y nivel |
| En `mvn test` | Una prueba carga el `preguntas.csv` real, así que una fila inválida rompe la construcción |

La validación detecta errores de formato, pero no de contenido: quien escribe la fila comprueba que su nivel sea el adecuado y que ninguna opción incorrecta también sea válida.

### Nivel de dificultad

Quien escribe la fila asigna la columna `nivel` según lo que se pregunta, lo parecidas que son las opciones incorrectas y el tiempo disponible.

| Tema | Fácil | Medio | Difícil |
| --- | --- | --- | --- |
| Números | Suma y resta hasta 20, sin negativos; tablas del 2, 5 y 10 | Suma y resta hasta 100, sin negativos; tablas del 2 al 9 | Suma y resta hasta 1.000, sin negativos; dos cifras por una: 47 × 6 |
| Idiomas | Palabras cotidianas: perro → dog | Palabras frecuentes: ventana → window | Palabras poco comunes: umbral → threshold |
| Palabras | Vocabulario básico: sinónimo de feliz | Vocabulario general: antónimo de generoso | Vocabulario amplio: sinónimo de efímero |

En los niveles de Idiomas se puede usar como referencia el MCER: A1-A2 para Fácil, B1-B2 para Medio y C1 en adelante para Difícil.

| Nivel | Opciones incorrectas | Ejemplo |
| --- | --- | --- |
| Fácil | Claramente distintas, se descartan rápido | `alegre` frente a `mesa`, `nube`, `rápido` |
| Medio | De la misma categoría, hay que conocer la palabra | `dog` frente a `cat`, `bird`, `fish` |
| Difícil | Parecidas o basadas en errores típicos | `282` frente a `242` (olvidar la llevada), `272`, `292` |

En Palabras Difícil, las opciones pueden estar relacionadas, pero nunca ser sinónimos reales. En Números Difícil, conviene que terminen en la misma cifra, para que no se pueda acertar calculando solo las unidades.

Quien sepa la respuesta debería poder contestar en menos de la mitad del tiempo del nivel. Si tarda más, la pregunta se baja de nivel o se acorta su enunciado. Si al jugar una pregunta parece mal ubicada, basta con cambiar su `nivel` en el CSV.

### Mapa

`mapa.txt` define las 81 casillas como una cuadrícula de texto. Las filas impares se desplazan media casilla a la derecha.

```
. . n n n n i i i i . .
. 1 n n N n i I i i 2 .
n n n n N n i I i i i i
n n n N N n i I I i i i
. n n n n n i i i i i .
. . . n p p p p i . . .
. . . p p p p p p . . .
. . p p P P p p p . . .
. . . p p P P p . . . .
. . . . p p p . . . . .
. . . . p 3 . . . . . .
```

| Símbolo | Significado |
| --- | --- |
| `n`, `i`, `p` | Tierra de Números, Idiomas o Palabras |
| `N`, `I`, `P` | Agua de Números, Idiomas o Palabras |
| `1`, `2`, `3` | Sede en tierra de Números, Idiomas o Palabras |
| `.` | Sin casilla |

Cada fila y columna se convierte con `q = columna - (fila - (fila & 1)) / 2` y `r = fila`. En pantalla, `x = lado * sqrt(3) * (q + r / 2.0)` y `y = lado * 1.5 * r`.

## Interfaz JavaFX

La aplicación tiene 3 pantallas en una sola ventana de 1280 × 800, y `PartidaController` es el único punto que conecta la interfaz con `Partida`.

| Pantalla | Contenido | Comportamiento |
| --- | --- | --- |
| Configuración | Número de humanos (1 a 3), nombre y nivel de cada uno, dificultad de la CPU si hay algún puesto de CPU | Deshabilita Iniciar si faltan datos o hay nombres repetidos |
| Partida | Mapa con leyenda, nombre de quien tiene el turno, ronda, acciones restantes y progreso de cada jugador hacia el siguiente tamaño | Resalta las casillas enemigas de la frontera |
| Fin | Ganador y casillas por jugador | Permite volver a Configuración |

En el mapa, cada casilla es un `Polygon` dentro de `MapaView`:

| Elemento | Cómo se ve |
| --- | --- |
| País | Color del país al que pertenece: naranja, verde o morado |
| Agua | Tono oscuro del color del país, con ondas |
| Sede | Icono según el tamaño, borde resaltado y nombre del jugador |
| Defensa | Número de 1 a 4, solo en casillas atacables |

Las casillas atacables llevan borde blanco, y la casilla elegida, borde dorado. La casilla no muestra la pregunta.

El panel lateral `PanelRetoView` tiene 5 estados:

| Estado | Contenido |
| --- | --- |
| Sin casilla | Instrucción de elegir una casilla con borde blanco |
| Vista previa | País, terreno, aciertos necesarios, tiempo por reto y botones Cancelar y Comenzar |
| Reto | Enunciado, 4 opciones numeradas, avance de la serie y barra de tiempo |
| Después de fallar | Opción correcta en verde, opción elegida en rojo si la hubo y botón Continuar |
| Turno de CPU | Mensaje “CPU ataca…” |

Cancelar no consume la acción. Comenzar llama a `atacar(casilla)` e inicia el temporizador. Las teclas 1 a 4 equivalen a las opciones, y la barra de tiempo se vuelve roja en los últimos 5 segundos.

Si la respuesta es correcta, la partida sigue de inmediato con el siguiente reto de la serie o con la casilla conquistada. Si hay un fallo o se agota el tiempo, el panel pasa a Después de fallar y espera a que el jugador pulse Continuar. `responder(indice)` devuelve si hubo acierto, y `PartidaController` guarda el reto actual antes de llamarlo para poder mostrar la respuesta correcta.

El temporizador usa un `Timeline` que se reinicia con cada reto, y al llegar a cero llama a `tiempoAgotado()`.

Los clics en el mapa solo se atienden en Sin casilla y Vista previa, y las opciones y las teclas 1 a 4, solo en Reto, porque `Partida` rechaza las llamadas fuera de estado. Además, el panel ignora las respuestas durante los primeros 300 ms de cada reto: como el siguiente aparece de inmediato, un doble clic o una tecla repetida lo contestaría sin querer.

Cuando `PartidaController` encuentra el estado `TURNO_CPU`, agenda `ejecutarAccionCPU()` con un `PauseTransition` de 1.200 ms, con la casilla atacada resaltada en el mapa. Nunca se usa `Thread.sleep` en el hilo de JavaFX.

### Cómo armar las pantallas

La aplicación usa una sola `Scene`, y `JuegoApp` cambia de pantalla reemplazando su raíz. La ventana no cambia de tamaño, porque el mapa usa medidas fijas. Cada pantalla recibe `JuegoApp` para poder pedir el cambio a la siguiente.

```java
public class JuegoApp extends Application {
    private Scene escena;

    @Override
    public void start(Stage ventana) {
        escena = new Scene(new ConfiguracionView(this), 1280, 800);
        ventana.setScene(escena);
        ventana.setTitle("Reinos del Saber");
        ventana.setResizable(false);
        ventana.show();
    }

    public void mostrar(Parent pantalla) {
        escena.setRoot(pantalla);
    }
}
```

`PartidaController` arma la pantalla de partida con un `BorderPane`, siguiendo el [diseño de la pantalla](<../imagenes/Pantalla de partida@1x.png>):

| Zona | Contenedor | Contenido |
| --- | --- | --- |
| Arriba | `HBox` | Título a la izquierda y ronda a la derecha, separados por una `Region` con `HBox.setHgrow(region, Priority.ALWAYS)` |
| Centro | `MapaView`, que extiende `Pane` | Casillas y leyenda; `Pane` permite colocar cada elemento en coordenadas exactas |
| Derecha | `VBox` de 365 px de ancho | `PanelEstadoView` con el turno, las acciones y el crecimiento; debajo, `PanelRetoView` con `VBox.setVgrow(panelReto, Priority.ALWAYS)` para ocupar el resto del alto |

Dentro de `PanelRetoView`, las 4 opciones van en un `GridPane` de 2 columnas y 2 filas.

Cada casilla se dibuja con `lado = 38` y `margen = 12`. El centro se calcula con las fórmulas de la sección [Mapa](#mapa), sumando `lado * sqrt(3) / 2 + margen` a `x` y `lado + margen` a `y`, para que las casillas del borde no queden cortadas. Con ese centro se calculan los 6 vértices:

```java
private Polygon crearHexagono(double centroX, double centroY, double lado) {
    Polygon hexagono = new Polygon();
    for (int i = 0; i < 6; i++) {
        double angulo = Math.toRadians(60 * i + 30);
        hexagono.getPoints().addAll(
                centroX + lado * Math.cos(angulo),
                centroY + lado * Math.sin(angulo));
    }
    return hexagono;
}
```

## CPU

La CPU elige siempre la casilla que más la acerque a una sede rival. No lee preguntas: cada reto se resuelve con `random.nextDouble() < p`, donde `p` es su probabilidad de acierto (0,5, 0,7 o 0,9).

La prioridad para elegir la casilla es:

1. La casilla con menor distancia BFS, sobre las casillas del mapa, a la sede rival más próxima. Una sede rival atacable tiene distancia 0, así que siempre se elige primero.
2. Si hay empate, la de menor Defensa; si sigue, la de menor `q` y luego menor `r`.

La dificultad cambia solo `p`: la lógica de decisión es la misma en las tres. Estos valores son un punto de partida y deben ajustarse jugando.

## Pruebas y documentación

La entrega 2 exige el modelo con pruebas JUnit y Javadoc, así que cada clase con lógica tiene su clase de prueba. Estos son los casos mínimos:

| Clase | Casos de prueba |
| --- | --- |
| `Coordenada` | Tiene 6 vecinos |
| `CargadorMapa` | Carga 81 casillas, 27 por país, 12 de agua y 3 sedes; cada sede está a 5 ataques o más de un enemigo; rechaza símbolos desconocidos |
| `Mapa` | Todas las casillas forman un solo continente; devuelve adyacentes correctos; `esFrontera` es correcta; `distancia` rodea los huecos del mapa |
| `Casilla` | La Defensa depende del terreno o la sede; conquistar cambia el país de la casilla |
| `Reto` | Exige datos completos, 4 opciones únicas y un índice válido; identifica la respuesta correcta; `barajado` conserva la respuesta correcta |
| `TamanoCiudad` | Límites 27/28 y 37/38 |
| `SerieRetos` | Exige tantos aciertos como Defensa |
| `Partida` | Un fallo consume la acción y termina la serie; el tiempo agotado equivale a fallo; `responder` indica si hubo acierto |
| `Partida` | Las acciones se calculan al iniciar el turno; al agotarlas, el turno pasa directamente al siguiente jugador; la ronda avanza después del tercer participante |
| `Partida` | Conquistar una sede termina la partida y declara ganador al atacante |
| `Partida` | `iniciar()` sortea el orden y los países con un generador controlado; los métodos rechazan las llamadas fuera de estado; `ejecutarAccionCPU()` resuelve una acción completa y puede terminar la partida |
| `EstrategiaAvance` | Prefiere una sede alcanzable; respeta el desempate; avanza hacia la sede rival más próxima; no elige casillas inválidas |
| `BancoRetos` | Omite el encabezado; rechaza filas mal formadas indicando la línea; rechaza preguntas repetidas; exige las 9 combinaciones de país y nivel; entrega retos del país y nivel pedidos; no entrega la misma pregunta dos veces seguidas; el `preguntas.csv` real carga sin errores |
| `ConfiguracionPartida` | Acepta de 1 a 3 humanos con nombre y nivel; rechaza datos incompletos, nombres repetidos y nombres reservados de CPU |
| `JugadorCPU` | Con un generador aleatorio controlado, acierta o falla según su probabilidad |
| `Partida` | Atacar rechaza casillas propias o sin frontera con el país del jugador |
| `Partida` | Una partida completa con respuestas simuladas llega de `iniciar()` a `FINALIZADA` |

## Riesgos de implementación

El mayor riesgo está en coordinar la partida con la interfaz, por lo que la máquina de estados de `Partida` debe estar terminada y probada con JUnit antes de conectar JavaFX.

| Punto | Dificultad | Riesgo | Cómo reducirlo |
| --- | --- | --- | --- |
| Sincronizar partida e interfaz | Alta | Vista previa, serie, temporizador, respuesta y cambio de turno ocurren en secuencia | El modelo es la única fuente de verdad; `PartidaController` ejecuta `refrescar()` después de cada llamada a `Partida` e ignora clics y teclas durante el turno de la CPU |
| Choque entre tiempo y respuesta | Alta | El tiempo puede agotarse justo al responder con clic o tecla, y contar doble o saltarse un reto; un doble clic puede contestar el reto siguiente | Detener el `Timeline` antes de procesar; atender entradas solo en el estado que las admite; ignorar respuestas durante los primeros 300 ms de cada reto |
| Turno de la CPU | Media-alta | Encadena varias acciones con pausas de 1.200 ms y puede ganar en medio de la secuencia | `ejecutarAccionCPU()` resuelve una acción completa; el controlador agenda la siguiente solo si la partida sigue |
| Decisión de la CPU | Media | La distancia hexagonal ignora los huecos del mapa | Calcular la distancia a la sede con BFS sobre las casillas del mapa |
| Mapa hexagonal | Media | Un error en las filas desplazadas rompe vecinos y frontera | Empezar por `Coordenada` y `Mapa` con JUnit, y hacer pronto el mapa clicable |
| Actualizar la vista | Media | Cada conquista cambia color, frontera, tamaño, acciones y crecimiento | Un solo método `refrescar()` redibuja desde el estado del modelo |
| Contenido de las preguntas | Media | Una opción incorrecta también puede ser válida, o una pregunta puede quedar en el nivel equivocado | Aplicar las reglas de nivel; `BancoRetos` valida el formato |

El contenido de las 180 filas, el Javadoc de cada miembro, los diagramas y la presentación son poco complejos, pero requieren tiempo. Conviene avanzarlos desde el inicio.

## Plan de implementación

El plan sigue las 3 entregas del enunciado. Cada una indica qué se exige, qué hay que construir y en qué orden, y cuándo se considera cerrada. Las fechas exactas deben confirmarse en la plataforma.

| Entrega | Semana | Qué se sube | Trabajo principal |
| --- | --- | --- | --- |
| 1 | 3 | Propuesta en PDF | Cerrar la propuesta y preparar el entorno |
| 2 | 5 | Código del modelo, pruebas JUnit y Javadoc | Construir toda la lógica del juego, sin interfaz |
| 3 y sustentación | 7 y 8 | Código completo, diagramas de clases y presentación | Construir la interfaz y preparar la sustentación |

Durante todo el proyecto:

- Cada clase del modelo se escribe junto con su prueba y su Javadoc, no al final.
- `mvn test` debe pasar antes de compartir cambios con el grupo.
- Los problemas importantes y su solución se anotan en `docs/problemas.md`, porque la presentación final debe explicarlos.

### Entrega 1: propuesta (semana 3)

El enunciado pide un PDF con el nombre y el porqué de su elección, la descripción detallada, el tipo de juego, el número de jugadores, las reglas e imágenes. El tutor decide si el juego es viable.

- [ ] Revisar que la propuesta cubra todos los puntos exigidos.
- [ ] Exportar la propuesta a PDF y comprobar que incluya las 2 imágenes.
- [ ] Enviar el PDF al tutor.
- [ ] Instalar el JDK 21 y Maven en cada equipo, siguiendo el README.
- [ ] Crear la base del proyecto con [Configuración inicial](#configuración-inicial) y comprobar que `mvn javafx:run` abra la ventana en cada equipo.

**Se cierra cuando** el PDF esté enviado y las observaciones del tutor estén incorporadas en la propuesta y en esta guía. Mientras llega su respuesta, se puede avanzar con el mapa y los retos.

### Entrega 2: modelo (semana 5)

El enunciado pide el código de todas las clases con lógica de negocio, sus pruebas JUnit, un comentario de múltiples líneas en cada miembro y los archivos Javadoc generados. La interfaz no forma parte de esta entrega.

| Orden | Trabajo | Clases y archivos | Depende de | Listo cuando |
| --- | --- | --- | --- | --- |
| 1 | Mapa | `Coordenada`, `Pais`, `Terreno`, `Casilla`, `Mapa`, `CargadorMapa`, `mapa.txt` | Base del proyecto | Pasan las pruebas de carga, vecinos, frontera y distancia |
| 2 | Retos | `Nivel`, `Reto`, `BancoRetos` | `Pais` | Pasan todas las validaciones del banco |
| 3 | Preguntas | `preguntas.csv` | Formato del paso 2 | Hay al menos una pregunta por cada combinación de país y nivel, y la prueba del archivo real pasa |
| 4 | Jugadores y reglas de apoyo | `Jugador`, `JugadorHumano`, `DificultadCPU`, `ConfiguracionPartida`, `TamanoCiudad`, `SerieRetos` | Pasos 1 y 2 | Pasan sus pruebas |
| 5 | Partida | `EstadoPartida`, `Partida` | Pasos 1, 2 y 4 | Funcionan ataque, series, acciones, turnos y final de partida |
| 6 | CPU | `EstrategiaCPU`, `EstrategiaAvance`, `JugadorCPU`, `ejecutarAccionCPU()` | Paso 5 | Una partida completa con respuestas simuladas llega a `FINALIZADA` |

Los pasos 1, 2 y 3 se pueden hacer al mismo tiempo. `Partida` es el trabajo más delicado, así que conviene empezarla apenas estén listas sus dependencias. Como adelanto que no se entrega, un `MapaView` clicable permite detectar pronto errores en las coordenadas.

Antes de subir la entrega:

- [ ] Cada clase, atributo, constante de enumeración y método del modelo tiene su comentario `/** ... */`, incluidos los privados.
- [ ] `mvn test` pasa y ninguna clase de `modelo` importa JavaFX.
- [ ] `mvn javadoc:javadoc` genera la documentación en `target/reports/apidocs/`.
- [ ] El archivo subido incluye `pom.xml`, `src/main/java/com/reinos/modelo/`, `src/main/resources/`, `src/test/` y la carpeta del Javadoc.

**Se cierra cuando** se pueda jugar una partida completa desde las pruebas, sin abrir la interfaz.

### Entrega 3 y sustentación (semanas 7 y 8)

El enunciado pide el código completo del modelo y de la interfaz, sus diagramas de clases y una presentación con el juego, los detalles de implementación a alto nivel, las tecnologías usadas, cómo se usaron, los problemas encontrados y cómo se resolvieron.

| Orden | Trabajo | Clases | Depende de | Listo cuando |
| --- | --- | --- | --- | --- |
| 1 | Configuración | `JuegoApp`, `ConfiguracionView` | Modelo cerrado | Se crea una partida válida con 1, 2 o 3 humanos |
| 2 | Mapa | `MapaView` | Paso 1 | Se ven las 81 casillas con sus países, sedes y Defensa |
| 3 | Conexión con el modelo | `PartidaController`, `PanelEstadoView` | Paso 2 | Elegir una casilla, actualizar con `refrescar()` y mostrar el turno funcionan |
| 4 | Retos | `PanelRetoView` | Paso 3 | Funcionan los 5 estados del panel, el temporizador, las teclas y la protección contra doble clic |
| 5 | Turno de la CPU | `PartidaController` | Paso 4 | La CPU juega sus acciones con pausas y sin bloquear la ventana |
| 6 | Final | `PantallaFinalView` | Paso 5 | Se muestra el ganador y se puede volver a Configuración |
| 7 | Ajustes y preguntas | `preguntas.csv` y toda la aplicación | Paso 6 | Se cumplen los criterios de aceptación y el banco tiene las 180 filas |

Los pasos 3 a 5 concentran los riesgos altos, así que conviene hacerlos primero con una interfaz básica y dejar los detalles visuales para el final.

La versión final está terminada cuando se pueda jugar una partida completa, de la configuración a la pantalla final, sin errores.

- [ ] Se puede iniciar con 1, 2 o 3 humanos, y los puestos restantes son CPU.
- [ ] El mapa muestra 81 casillas, sus países y la Defensa de las casillas atacables.
- [ ] Solo se pueden atacar casillas enemigas de la frontera.
- [ ] Elegir una casilla muestra una vista previa; la acción y el tiempo empiezan al pulsar Comenzar.
- [ ] Las teclas 1 a 4 responden los retos.
- [ ] Cada intento presenta un reto nuevo del país de la casilla y del nivel de quien responde.
- [ ] Un acierto completa o avanza la serie; un fallo o tiempo agotado la termina.
- [ ] El número de acciones cambia con aldea, pueblo y ciudad.
- [ ] Conquistar una sede termina la partida y da la victoria al atacante.
- [ ] La CPU juega sola y sin bloquear la ventana.
- [ ] La partida termina al conquistar una sede y muestra el ganador.
- [ ] `mvn test` pasa y el modelo no importa JavaFX.
- [ ] El Javadoc cubre todos los miembros del modelo.

Antes de subir la entrega:

- [ ] Los diagramas de clases del modelo y de la interfaz coinciden con el código final.
- [ ] La presentación cubre el juego, la implementación a alto nivel, las tecnologías, cómo se usaron y los problemas de `docs/problemas.md`.
- [ ] Alguien que no haya configurado el proyecto lo instala y ejecuta siguiendo solo el README.
- [ ] El archivo subido incluye el código completo, `pom.xml`, los recursos, las pruebas y los diagramas.
- [ ] El grupo ensayó la sustentación con una partida en vivo.

**Se cierra cuando** todo esté subido a la plataforma y la sustentación esté presentada.
