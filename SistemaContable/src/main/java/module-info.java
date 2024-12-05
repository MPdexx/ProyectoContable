module local.scontable.sistemacontable {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.sql;


    opens local.scontable.sistemacontable to javafx.fxml;
    exports local.scontable.sistemacontable;
    exports local.scontable.sistemacontable.ControlMantenimientos;
    exports local.scontable.sistemacontable.ControlMovimientos to javafx.fxml;
    opens local.scontable.sistemacontable.ControlMantenimientos to javafx.fxml;
    opens local.scontable.sistemacontable.ControlMovimientos to javafx.fxml;
}