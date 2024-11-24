package local.scontable.sistemacontable.ControlMantenimientos;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;

import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;

public class Man_UsuariosEditController implements Initializable {
    private String nUser;
    private static String archivo = "C:\\ProjectoParcialJava\\SistemaContable\\src\\main\\resources\\Datos\\usuarios.txt";
    private static String archivo1 = "C:\\ProjectoParcialJava\\SistemaContable\\src\\main\\resources\\Datos\\usuarios1.txt";

    @FXML
    TextField txtf_user, txtf_pass, txtf_NameUser, txtf_LastName, txtf_LvlUser, txtf_email,
            txtf_NameUserEdit, txtf_LastNameEdit, txtf_passEdit, txtf_emailEdit;
    @FXML
    Button btn_save, btn_cancel;
    @FXML
    ComboBox cbox_LvlUser;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        txtf_user.setText(nUser);
        String[] usertype = {"Admin", "Usuario"};
        cbox_LvlUser.getItems().addAll(usertype);
        btn_save.setDisable(false);

        BooleanBinding areAllFilled = Bindings.createBooleanBinding(() ->
                !txtf_NameUserEdit.getText().trim().isEmpty() ||
                !txtf_LastNameEdit.getText().trim().isEmpty() ||
                !txtf_passEdit.getText().trim().isEmpty() ||
                !txtf_emailEdit.getText().trim().isEmpty() ||
                cbox_LvlUser.getValue() != null
                ,txtf_NameUserEdit.textProperty()
                ,txtf_LastNameEdit.textProperty()
                ,txtf_passEdit.textProperty()
                ,cbox_LvlUser.valueProperty()
                ,txtf_emailEdit.textProperty()
        );
        btn_save.disableProperty().bind(areAllFilled.not());
    }

    public void SaveChanges(){
        File original = new File(archivo);
        File temporal = new File(archivo1);

        try (BufferedReader reader = new BufferedReader(new FileReader(original));
             BufferedWriter writer = new BufferedWriter(new FileWriter(temporal)))
        {
            String linea;

            while ((linea = reader.readLine()) != null) {
                String[] datos = linea.split(";");
                if (datos.length == 6 && datos[0].equals(nUser)) { // Usuario encontrado
                    // Usar los datos existentes si no se editaron
                    String nUser = datos[0]; // Campo clave, no editable
                    String nUserReal = txtf_NameUserEdit.getText().isEmpty() ? datos[1] : txtf_NameUserEdit.getText();
                    String lnUser = txtf_LastNameEdit.getText().isEmpty() ? datos[2] : txtf_LastNameEdit.getText();
                    String pass = txtf_passEdit.getText().isEmpty() ? datos[3] : txtf_passEdit.getText();
                    String lvlUser = String.valueOf(cbox_LvlUser.getValue()) == null ? datos[4] : (String.valueOf(cbox_LvlUser.getValue()).equals("Admin") ? "1" : "0");
                    String email = txtf_emailEdit.getText().isEmpty() ? datos[5] : txtf_emailEdit.getText();

                    // Escribir los datos editados en el archivo temporal
                    writer.write(nUser + ";" + nUserReal + ";" + lnUser + ";" + pass + ";" + lvlUser + ";" + email);
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
                alert.setHeaderText("Usuario editado correctamente");
                alert.setContentText("Los campos se han editado correctamente");
                alert.showAndWait();
            } else {
                Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                alert1.setTitle("Error");
                alert1.setHeaderText("Error al editar el usuario");
                alert1.setContentText("No se pudo editar los campos el usuario");
                alert1.showAndWait();
            }
            txtf_NameUserEdit.setText("");
            txtf_LastNameEdit.setText("");
            txtf_pass.setText("");
            cbox_LvlUser.setValue("");
            txtf_emailEdit.setText("");
        }catch (Exception ex){
            System.out.println(ex);
        }
    }

    public void getUser(String nUser, String nUserReal,String lnUser, String pass, String lvlUser, String email){
        this.nUser = nUser;
        txtf_user.setText(nUser);
        txtf_NameUser.setText(nUserReal);
        txtf_LastName.setText(lnUser);
        txtf_pass.setText(pass);
        txtf_LvlUser.setText(lvlUser);
        txtf_email.setText(email);
    }
    public void Cancel(){
        Stage stage = (Stage) btn_cancel.getScene().getWindow();
        stage.close();
    }
}
