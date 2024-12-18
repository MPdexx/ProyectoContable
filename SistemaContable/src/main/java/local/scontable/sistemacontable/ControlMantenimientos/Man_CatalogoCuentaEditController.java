package local.scontable.sistemacontable.ControlMantenimientos;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import local.scontable.sistemacontable.Clases.CambioPanel;
import local.scontable.sistemacontable.Clases.SharedController;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;

public class Man_CatalogoCuentaEditController implements Initializable {
    private String nCuenta;
    private boolean esCuentaPadre;
    @FXML
    TextField txtf_nCuenta, txtf_desCuenta, txtf_Tipo, txtf_Lvl, txtf_CuentaPadre, txtf_Grupo, txtf_fCreacion, txtf_hCreacion, txtf_DebitoAc, txtf_CreditoAc, txtf_BalanceAc;
    @FXML
    TextField txtf_desCuentaEdit, txtf_cuentaPadreEdit;
    @FXML
    Button btn_save, btn_cancel;
    @FXML
    ChoiceBox<String> cbox_tipo, cbox_Lvl, cbox_grupo;
    private static String archivo = "C:\\ProjectoParcialJava\\SistemaContable\\src\\main\\resources\\Datos\\cuentas.txt";
    private static String archivo1 = "C:\\ProjectoParcialJava\\SistemaContable\\src\\main\\resources\\Datos\\cuentas1.txt";

    public void getCuenta(int nroCuenta1, String desCuenta1, boolean tipoCuenta1, int lvlCuenta1, int ctaPadre1, String grpCuenta1, String fCreacion1, String hCreacion1, float DebitoAc1, float CreditoAc1, float BalanceCta1, boolean esCPadre){
        this.txtf_nCuenta.setText(String.valueOf(nroCuenta1));
        this.txtf_desCuenta.setText(desCuenta1);
        String tipo = String.valueOf(tipoCuenta1).equals("true") ? "General" : "Detalle";
        this.txtf_Tipo.setText(tipo);
        this.txtf_Lvl.setText(String.valueOf(lvlCuenta1));
        this.txtf_CuentaPadre.setText(String.valueOf(ctaPadre1));
        this.txtf_Grupo.setText(grpCuenta1);
        this.txtf_fCreacion.setText(fCreacion1);
        this.txtf_hCreacion.setText(hCreacion1);
        this.txtf_DebitoAc.setText(String.valueOf(DebitoAc1));
        this.txtf_CreditoAc.setText(String.valueOf(CreditoAc1));
        this.txtf_BalanceAc.setText(String.valueOf(BalanceCta1));
        this.esCuentaPadre = esCPadre;
        this.txtf_desCuentaEdit.setText(desCuenta1);
        this.txtf_CuentaPadre.setText(String.valueOf(ctaPadre1));
        if (esCuentaPadre){
            cbox_tipo.setDisable(true);
        }
    }

    public void SaveChanges(){
        File original = new File(archivo);
        File temporal = new File(archivo1);

        try (BufferedReader reader = new BufferedReader(new FileReader(original));
             BufferedWriter writer = new BufferedWriter(new FileWriter(temporal)))
        {
            String linea;
            nCuenta = txtf_nCuenta.getText();
            System.out.println(nCuenta);
            while ((linea = reader.readLine()) != null) {
                String[] datos = linea.split(";");
                if (datos[0].equals(nCuenta)) { // Usuario encontrado
                    // Usar los datos existentes si no se editaron
                    int nCuenta = Integer.parseInt(datos[0]); // Campo clave, no editable
                    String desCuenta = txtf_desCuentaEdit.getText().isEmpty() ? datos[1] : txtf_desCuentaEdit.getText();
                    boolean tipo = cbox_tipo.getValue() == null ? Boolean.parseBoolean(datos[2]) : Boolean.valueOf(cbox_tipo.getValue().equals("General")) ? true : false;
                    int lvl = cbox_Lvl.getValue() == null ? Integer.parseInt(datos[3]) : Integer.parseInt(cbox_Lvl.getValue());
                    int cuentaPadre = txtf_CuentaPadre.getText().isEmpty() ? Integer.parseInt(datos[4]) : Integer.parseInt(txtf_CuentaPadre.getText());
                    String grupo = cbox_grupo.getValue() == null ? datos[5] : cbox_grupo.getValue();

                    // Escribir los datos editados en el archivo temporal
                    writer.write(nCuenta + ";" + desCuenta + ";" + tipo + ";" + lvl + ";" + cuentaPadre + ";" + grupo + ";" + datos[6] + ";" + datos[7] + ";" + datos[8] + ";" + datos[9] + ";" + datos[10]);
                } else {
                    // Escribir las líneas que no coincidan sin cambios
                    writer.write(linea);
                }
                writer.newLine();
            }
            FileInputStream fis = new FileInputStream(original);
            fis.close();
            reader.close();
            writer.close();

            if (original.delete()) {
                temporal.renameTo(original);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Info");
                alert.setHeaderText("Cuenta editada correctamente");
                alert.setContentText("Los campos se han editado correctamente");
                alert.showAndWait();
                MantenimientoCatalogoCuentaInController receptor = SharedController.getInstance().getPestañaReceptoraController();
                if (receptor != null) {
                    receptor.reload();
                }
                Stage stage = (Stage) btn_save.getScene().getWindow();
                stage.close();
            } else {
                Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                alert1.setTitle("Error");
                alert1.setHeaderText("Error al editar la cuenta");
                alert1.setContentText("No se pudo editar los campos de la cuenta");
                alert1.showAndWait();
            }

        }catch (Exception ex){
            System.out.println(ex);
        }
    }
    public void Cancel(){
        Stage stage = (Stage) btn_cancel.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (esCuentaPadre){
            txtf_Tipo.setDisable(true);
        }
        String[] accType = {"General", "Detalle"};
        String[] accLvl = {"0","1","2","3","4","5","6"};
        String[] accGroup = {"Activo", "Pasivo", "Capital", "Ingresos", "Costos", "Gastos"};
        cbox_tipo.getItems().addAll(accType);
        cbox_Lvl.getItems().addAll(accLvl);
        cbox_grupo.getItems().addAll(accGroup);

    }
}
