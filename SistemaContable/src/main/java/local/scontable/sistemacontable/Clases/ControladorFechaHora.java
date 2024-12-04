package local.scontable.sistemacontable.Clases;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.util.Duration;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;


public class ControladorFechaHora {
    private String currentDate;
    private String currentHour;


    public void updateDateTime(Label lbl_date, Label lbl_hour) {
        // Obtener fecha y hora actuales
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();

        // Formatear la fecha y la hora
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm:ss a");

        currentDate = date.format(dateFormatter); // Guardar la fecha en una variable
        currentHour = time.format(timeFormatter); // Guardar la hora en una variable

        // Mostrar en los labels
        lbl_date.setText(currentDate);
        lbl_hour.setText(currentHour);
    }

    // Métodos para obtener la fecha y hora desde otras partes de tu aplicación
    public String getCurrentDate() {
        return currentDate;
    }

    public String getCurrentHour() {
        return currentHour;
    }
}

