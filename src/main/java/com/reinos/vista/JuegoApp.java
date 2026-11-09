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
