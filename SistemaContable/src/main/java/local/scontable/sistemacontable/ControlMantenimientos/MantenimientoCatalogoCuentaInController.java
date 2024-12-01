package local.scontable.sistemacontable.ControlMantenimientos;

import javafx.beans.binding.Binding;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import local.scontable.sistemacontable.Clases.CambioPanel;
import local.scontable.sistemacontable.PrincipalController;


import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;

public class MantenimientoCatalogoCuentaInController implements Initializable, CambioPanel {
    @FXML
    TextField txtf_nCuenta, txtf_desCuenta, txtf_CuentaPadre;
    @FXML
    ChoiceBox<String> cbox_tipoCuenta, cbox_grupoCuenta;
    @FXML
    ChoiceBox<Integer> cbox_nivelCuenta;
    @FXML
    Button btn_save, btn_edit, btn_delete;
    private static String archivo = "C:\\ProjectoParcialJava\\SistemaContable\\src\\main\\resources\\Datos\\cuentas.txt";
    private static String archivo1 = "C:\\ProjectoParcialJava\\SistemaContable\\src\\main\\resources\\Datos\\cuentas1.txt";
    private PrincipalController panelPadre;

    public void saveData(){
        int nCuenta, nivelCuenta, CuentaPadre;
        String desCuenta, grupoCuenta;
        boolean tipoCuenta;

        nCuenta =  Integer.parseInt(txtf_nCuenta.getText());
        desCuenta = txtf_desCuenta.getText();
        tipoCuenta = String.valueOf(cbox_tipoCuenta).equals("General") ? true : false;
        nivelCuenta = cbox_nivelCuenta.getValue();
        CuentaPadre = Integer.parseInt(txtf_CuentaPadre.getText());
        grupoCuenta = String.valueOf(cbox_grupoCuenta.getValue());

        //Construir el registro de la cuenta
        String registro = nCuenta + ";"+ desCuenta + ";" + tipoCuenta + ";" + nivelCuenta + ";" + CuentaPadre
                + ";" + grupoCuenta;
        File file = new File(archivo);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("Error al creal el archivo.");
                alert.showAndWait();
            }
        }
        boolean cuentaExiste = false;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] datos = linea.split(";");
                if (datos.length > 0 && datos[0].equals(nCuenta)) { // Validar por nombre de usuario
                    cuentaExiste = true;
                    break;
                }
            }
        } catch (IOException e) {
            System.out.println("Error al leer el archivo: " + e.getMessage());
        }
        if (cuentaExiste) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Cuenta ya Existente");
            alert.setContentText("El numero de cuenta ingresado ya está registrado.");
            alert.showAndWait();
            return; // Detener el proceso
        }

        //Guardar los datos
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivo, true))) {
            writer.write(registro);
            writer.newLine(); // Agregar un salto de línea al final
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Info");
            alert.setHeaderText("Guardado correctamente");
            alert.setContentText("Datos de cuenta guardado correctamente");
            alert.showAndWait();

        } catch (IOException e) {
            Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
            alert1.setTitle("Error");
            alert1.setHeaderText("Error al guardar los datos");
            alert1.setContentText("No se pudo guardar los datos de la cuenta");
            alert1.showAndWait();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        String[] accType = {"General", "Detalle"};
        Integer[] accLvl = {1,2,3,4,5,6};
        String[] accGroup = {"Activo", "Pasivo", "Capital", "Ingresos", "Costos", "Gastos"};
        cbox_tipoCuenta.getItems().addAll(accType);
        cbox_nivelCuenta.getItems().addAll(accLvl);
        cbox_grupoCuenta.getItems().addAll(accGroup);

        btn_save.setDisable(true);
        btn_edit.setDisable(true);
        btn_delete.setDisable(true);
        BooleanBinding areAllFilled = Bindings.createBooleanBinding(()->
                !txtf_nCuenta.getText().trim().isEmpty() &&
                !txtf_desCuenta.getText().trim().isEmpty() &&
                !txtf_CuentaPadre.getText().trim().isEmpty() &&
                cbox_tipoCuenta.getValue() != null &&
                cbox_nivelCuenta.getValue() != null &&
                cbox_grupoCuenta.getValue() != null
                ,txtf_nCuenta.textProperty()
                ,txtf_desCuenta.textProperty()
                ,txtf_CuentaPadre.textProperty()
                ,cbox_tipoCuenta.valueProperty()
                ,cbox_nivelCuenta.valueProperty()
                ,cbox_grupoCuenta.valueProperty()
                );
        btn_save.disableProperty().bind(areAllFilled.not());

        BooleanBinding areAllFilled1 = Bindings.createBooleanBinding(()->
                !txtf_nCuenta.getText().trim().isEmpty()
                ,txtf_nCuenta.textProperty()
        );
        btn_edit.disableProperty().bind(areAllFilled1.not());
        btn_delete.disableProperty().bind(areAllFilled1.not());
    }

    public void returnMenu() throws IOException {
        if (panelPadre != null) {
            panelPadre.cMantenimientos("/Mantenimientos/MantenimientoUsuarios.fxml");
        }
    }
    @Override
    public void setPanelPadre(PrincipalController panelPadre) {
        this.panelPadre = panelPadre;
    }
}
