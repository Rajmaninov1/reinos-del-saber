package com.reinos.modelo.mapa;

/**
 * Casilla hexagonal del mapa.
 * <p>
 * Toda casilla pertenece a un país desde el inicio. Su país cambia cuando
 * otro jugador la conquista; su posición, su terreno y si es sede no
 * cambian nunca.
 * </p>
 */
public class Casilla {

    /**
     * Defensa de una sede, sin importar su terreno.
     */
    public static final int DEFENSA_SEDE = 4;

    /**
     * Posición de la casilla en el mapa.
     */
    private final Coordenada coordenada;

    /**
     * País al que pertenece actualmente la casilla.
     */
    private Pais pais;

    /**
     * Terreno de la casilla, que define su Defensa base.
     */
    private final Terreno terreno;

    /**
     * Indica si la casilla es la sede de su país original.
     */
    private final boolean esSede;

    /**
     * Crea una casilla.
     *
     * @param coordenada posición de la casilla
     * @param pais       país inicial de la casilla
     * @param terreno    terreno de la casilla
     * @param esSede     {@code true} si la casilla es una sede
     */
    public Casilla(Coordenada coordenada, Pais pais, Terreno terreno, boolean esSede) {
        this.coordenada = coordenada;
        this.pais = pais;
        this.terreno = terreno;
        this.esSede = esSede;
    }

    /**
     * Cambia el país de la casilla al del jugador que la conquistó.
     *
     * @param nuevoPais país del jugador que conquista la casilla
     * @throws IllegalArgumentException si la casilla ya pertenece a ese país
     */
    public void conquistar(Pais nuevoPais) {
        if (nuevoPais == pais) {
            throw new IllegalArgumentException("La casilla ya pertenece a " + nuevoPais);
        }

        else if (nuevoPais == null) {
            throw new IllegalArgumentException("El país no puede ser nulo");
        }
        pais = nuevoPais;
    }

    /**
     * Calcula la Defensa de la casilla.
     * <p>
     * Una sede siempre tiene Defensa {@value #DEFENSA_SEDE}; las demás
     * casillas usan la Defensa base de su terreno.
     * </p>
     *
     * @return aciertos seguidos necesarios para conquistar la casilla
     */
    public int getDefensa() {
        return esSede ? DEFENSA_SEDE : terreno.getDefensaBase();
    }

    /**
     * Devuelve la posición de la casilla.
     *
     * @return la coordenada de la casilla
     */
    public Coordenada getCoordenada() {
        return coordenada;
    }

    /**
     * Devuelve el país al que pertenece actualmente la casilla.
     *
     * @return el país actual de la casilla
     */
    public Pais getPais() {
        return pais;
    }

    /**
     * Devuelve el terreno de la casilla.
     *
     * @return el terreno de la casilla
     */
    public Terreno getTerreno() {
        return terreno;
    }

    /**
     * Indica si la casilla es una sede.
     *
     * @return {@code true} si la casilla es una sede
     */
    public boolean esSede() {
        return esSede;
    }

    /**
     * Describe la casilla para mensajes de error y depuración.
     *
     * @return texto con la coordenada, el país, el terreno y si es sede
     */
    @Override
    public String toString() {
        return "Casilla" + coordenada + " " + pais + " " + terreno + (esSede ? " sede" : "");
    }
}
