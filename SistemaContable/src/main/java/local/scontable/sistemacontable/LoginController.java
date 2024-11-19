package local.scontable.sistemacontable;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML
    Label lbl_err;
    @FXML
    TextField txtf_user, txtf_pass;
    @FXML
    Button btn_login;
    private String archivo="C:\\ProjectoParcialJava\\SistemaContable\\src\\main\\resources\\Datos\\usuarios.txt";
    private PrincipalController acceso = new PrincipalController();
    private PrincipalController displayName = new PrincipalController();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        lbl_err.setVisible(false);

        BooleanBinding validar = Bindings.createBooleanBinding(() ->
                !txtf_user.getText().trim().isEmpty() &&
                !txtf_pass.getText().trim().isEmpty(),
                txtf_user.textProperty(),
                txtf_pass.textProperty()
        );
        btn_login.disableProperty().bind(validar.not());
    }

    public void Login(){
        String username = txtf_user.getText().trim();
        String password = txtf_pass.getText().trim();


        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            String linea;
            boolean usuarioEncontrado = false;

            while ((linea = reader.readLine()) != null) {
                String[] datos = linea.split(";");
                if (datos.length == 6) {
                    String nUser = datos[0].trim();
                    String pass = datos[3].trim();
                    String nivelAcceso = datos[4].trim();

                    // Validar usuario y contraseña
                    if (nUser.equals(username) && pass.equals(password)) {
                        usuarioEncontrado = true;

                        if (nivelAcceso.equals("1")) { // Es admin
                            System.out.println("1");
                           openPrincipal();
                            acceso.loadMantenimientos("1");
                            displayName.displayUserName(nUser);
                        } else {
                            System.out.println("0");
                            openPrincipal();
                            acceso.loadMantenimientos("0");
                        }
                        Stage stage = (Stage) btn_login.getScene().getWindow();
                        stage.close();
                        break;

                    }
                }
            }

            if (!usuarioEncontrado) {
                lbl_err.setVisible(true);
            }
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error de lectura");
            alert.setContentText("No se pudo acceder al archivo de usuarios.");
            alert.showAndWait();
        }

    }

    public void openPrincipal() throws IOException {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Principal.fxml"));
            Parent root = loader.load();
            acceso = loader.getController();
            displayName = loader.getController();
            Stage stage = new Stage();
            stage.setTitle("Sistema Contable");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error al abrir la ventana");
            alert.setContentText("No se pudo cargar la ventana de mantenimiento.");
            alert.showAndWait();
        }
    }

}
