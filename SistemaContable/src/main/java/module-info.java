module local.scontable.sistemacontable {
    requires javafx.controls;
    requires javafx.fxml;


    opens local.scontable.sistemacontable to javafx.fxml;
    exports local.scontable.sistemacontable;
}