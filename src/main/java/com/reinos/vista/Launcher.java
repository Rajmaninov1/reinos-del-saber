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
