module local.scontable.sistemacontable {
    requires javafx.controls;
    requires javafx.fxml;


    opens local.scontable.sistemacontable to javafx.fxml;
    exports local.scontable.sistemacontable;
    exports local.scontable.sistemacontable.ControlMantenimientos;
    opens local.scontable.sistemacontable.ControlMantenimientos to javafx.fxml;
}