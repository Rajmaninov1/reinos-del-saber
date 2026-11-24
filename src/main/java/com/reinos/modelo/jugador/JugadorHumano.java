package com.reinos.modelo.jugador;

import com.reinos.modelo.retos.Nivel;

/**
 * Jugador controlado por una persona.
 * <p>
 * Responde preguntas del nivel que eligió al configurar la partida.
 * Jugador Humano hereda los atributos y métodos de Jugador
 * </p>
 */
public class JugadorHumano extends Jugador {

    /**
     * Nivel de las preguntas que recibe este jugador.
     */
    private final Nivel nivel;

    /**
     * Crea un jugador humano.
     *
     * @param nombre nombre del jugador
     * @param nivel  nivel de sus preguntas
     * @throws IllegalArgumentException si el nombre está vacío
     */
    public JugadorHumano(String nombre, Nivel nivel) {
        super(nombre);
        this.nivel = nivel;
    }

    /**
     * Devuelve el nivel de las preguntas del jugador.
     *
     * @return el nivel elegido
     */
    public Nivel getNivel() {
        return nivel;
    }

    /**
     * Indica que este jugador es una persona.
     *
     * @return siempre {@code true}
     */
    @Override
    public boolean esHumano() {
        return true;
    }
}
