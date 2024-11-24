package local.scontable.sistemacontable.ControlMantenimientos;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import local.scontable.sistemacontable.Clases.CambioPanel;
import local.scontable.sistemacontable.Clases.Usuario;
import local.scontable.sistemacontable.PrincipalController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import javax.swing.event.DocumentListener;
import java.io.*;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class MantenimientoUsuariosInController implements Initializable, CambioPanel {

    @FXML
    TableView<Usuario> tview_users;
    @FXML
    TextField txtf_user, txtf_NameUser, txtf_LastName, txtf_email;
    @FXML
    PasswordField txtf_pass;
    @FXML
    Button btn_save, btn_edit, btn_delete;
    @FXML
    ChoiceBox<String> cbox_LvlUser;
    @FXML
    TableColumn<Usuario, String> col_nUser, col_nUserReal, col_LnUser, col_pass, col_Email, col_LvlUser;
    ObservableList<Usuario> userList = FXCollections.observableArrayList();

    private static String archivo = "C:\\ProjectoParcialJava\\SistemaContable\\src\\main\\resources\\Datos\\usuarios.txt";
    private static String archivo1 = "C:\\ProjectoParcialJava\\SistemaContable\\src\\main\\resources\\Datos\\usuarios1.txt";
    private PrincipalController panelPadre;
    private Man_UsuariosEditController nUsuario = new Man_UsuariosEditController();


    //Guardar los datos en un archivo de texto
    //FALTA ADICIONAR UNA CONDICION (
    public void saveData() {
        String nUser, nUserReal, lnUser, pass, email;
        int lvlUser = 0;
        nUser = txtf_user.getText();
        nUserReal = txtf_NameUser.getText();
        lnUser = txtf_LastName.getText();
        pass = txtf_pass.getText();
        if (String.valueOf(cbox_LvlUser.getValue()).equals("Admin")) {
            lvlUser = 1;
        } else if (String.valueOf(cbox_LvlUser.getValue()).equals("Usuario")) {
            lvlUser = 0;
        }
        if (txtf_email.getText().isEmpty()){
            email = "Sin correo";
        }
        else {
            email = txtf_email.getText();
        }


        // Construir el registro del usuario
        String registro = nUser + ";" + nUserReal + ";" + lnUser + ";" + pass
                + ";" + lvlUser + ";" + email;
        ;
        File file = new File(archivo);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                System.out.println("Error al crear el archivo: " + e.getMessage());
            }
        }

        boolean usuarioExiste = false;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] datos = linea.split(";");
                if (datos.length > 0 && datos[0].equals(nUser)) { // Validar por nombre de usuario
                    usuarioExiste = true;
                    break;
                }
            }
        } catch (IOException e) {
            System.out.println("Error al leer el archivo: " + e.getMessage());
        }

        // Si el usuario existe, mostrar un mensaje y salir
        if (usuarioExiste) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Usuario ya existente");
            alert.setContentText("El nombre de usuario ingresado ya está registrado.");
            alert.showAndWait();
            return; // Detener el proceso
        }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivo, true))) {


                writer.write(registro);
                writer.newLine(); // Agregar un salto de línea al final
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Info");
                alert.setHeaderText("Guardado correctamente");
                alert.setContentText("Usuario guardado correctamente");
                alert.showAndWait();

            } catch (IOException e) {
                Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                alert1.setTitle("Error");
                alert1.setHeaderText("Error al guardar el usuario");
                alert1.setContentText("No se pudo guardar el usuario");
                alert1.showAndWait();
            }

        reload();
        txtf_user.setText("");
        txtf_NameUser.setText("");
        txtf_LastName.setText("");
        txtf_pass.setText("");
        cbox_LvlUser.setValue("");
        txtf_email.setText("");


    }

    public void deleteData() throws IOException {
        Alert del = new Alert(Alert.AlertType.CONFIRMATION);
        del.setTitle("Confirmar");
        del.setContentText("¿Está seguro de eliminar el usuario?");

        del.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                int confirm = 0;
                File original = new File(archivo);
                File temporal = new File(archivo1);
                String nUser = "", nUserReal = "", lnUser = "";
                nUser = txtf_user.getText();
                nUserReal = txtf_NameUser.getText();
                lnUser = txtf_LastName.getText();
                if (!original.exists() && !temporal.exists()) {
                    Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                    alert1.setTitle("Error");
                    alert1.setHeaderText("Error al eliminar el usuario");
                    alert1.setContentText("No hay registros guardados");
                    alert1.showAndWait();
                } else if (original.exists() && !temporal.exists()) {
                    try {
                        temporal.createNewFile();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                try (
                        BufferedReader reader = new BufferedReader(new FileReader(original));
                        BufferedWriter writer = new BufferedWriter(new FileWriter(temporal));
                ) {
                    String linea;

                    while ((linea = reader.readLine()) != null) {
                        // Separar los campos para verificar la condición
                        String[] datos = linea.split(";");
                        String Nombre = datos[0];
                        String NombreReal = datos[1];
                        String Apellido = datos[2];


                        // Escribir solo las líneas que NO coincidan con el ID a eliminar
                        if (!(Nombre.trim().equals(nUser.trim()) && NombreReal.trim().equals(nUserReal.trim()) && Apellido.trim().equals(lnUser.trim()))) {
                            System.out.println("No coincide, escribiendo: " + linea);
                            writer.write(linea);
                            writer.newLine();
                        }
                        else {
                            confirm = 1;
                            System.out.println("Coincide, eliminando: " + linea);
                        }
                    }

                    FileInputStream fis = new FileInputStream(original);
                    fis.close();
                    reader.close();
                    writer.close();

                    if (confirm == 0){
                        Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                        alert1.setTitle("Error");
                        alert1.setHeaderText("Error al eliminar el usuario");
                        alert1.setContentText("El usuario no existe");
                        alert1.showAndWait();
                    }
                    else {
                        // Reemplazar el archivo original por el temporal
                        if (original.delete()) {
                            temporal.renameTo(original);
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Info");
                            alert.setHeaderText("Usuario eliminado correctamente");
                            alert.setContentText("El usuario se ha eliminado correctamente");
                            alert.showAndWait();
                            reload();
                        } else {
                            Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                            alert1.setTitle("Error");
                            alert1.setHeaderText("Error al eliminar el usuario");
                            alert1.setContentText("No se pudo eliminar el usuario");
                            alert1.showAndWait();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }


                txtf_user.setText("");
                txtf_NameUser.setText("");
                txtf_LastName.setText("");
                txtf_pass.setText("");
                cbox_LvlUser.setValue("");
                txtf_email.setText("");
            } else {

            }
        });


    }

    public void editData() throws IOException {
        String nUser, nUserReal="", lnUser="", pass="", email="", lvlUser="";
        nUser = txtf_user.getText();
        File file = new File(archivo);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                System.out.println("Error al crear el archivo: " + e.getMessage());
            }
        }
        boolean usuarioExiste = false;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] datos = linea.split(";");
                if (datos.length == 6 && datos[0].equals(nUser)) { // Validar por nombre de usuario
                    nUserReal = datos[1];
                    lnUser = datos[2];
                    pass = datos[3];
                    if (datos[4].equals("1")){
                        lvlUser = "Admin";
                    }
                    else {
                        lvlUser = "Usuario";
                    }
                    email = datos[5];
                    usuarioExiste = true;
                    break;
                }
            }
        } catch (IOException e) {
            System.out.println("Error al leer el archivo: " + e.getMessage());
        }

        // Si el usuario existe, mostrar un mensaje y salir
        if (usuarioExiste) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Mantenimientos/Man_UsuariosEdit.fxml"));
                Parent root = loader.load();
                nUsuario = loader.getController();
                Stage stage = new Stage();
                stage.setTitle("Editar Usuario");
                nUsuario.getUser(nUser, nUserReal, lnUser, pass, lvlUser, email);
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
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Usuario no existe");
            alert.setContentText("El nombre de usuario ingresado no existe");
            alert.showAndWait();
            return; // Detener el proceso
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        String[] usertype = {"Admin", "Usuario"};
        cbox_LvlUser.getItems().addAll(usertype);
        btn_save.setDisable(true);
        btn_edit.setDisable(true);
        btn_delete.setDisable(true);
        tview_users.setEditable(false);

        col_nUser.setCellValueFactory(cellData -> cellData.getValue().nUserProperty());
        col_nUserReal.setCellValueFactory(cellData -> cellData.getValue().nUserRealProperty());
        col_LnUser.setCellValueFactory(cellData -> cellData.getValue().lnUserProperty());
        col_pass.setCellValueFactory(cellData -> cellData.getValue().passProperty());
        col_LvlUser.setCellValueFactory(cellData -> cellData.getValue().lvlUserProperty());
        col_Email.setCellValueFactory(cellData -> cellData.getValue().emailProperty());

        try{
            userList = FXCollections.observableArrayList();
            loadFile(archivo);
            tview_users.setItems(userList);
        }catch (Exception ex){
            System.out.println(ex);
        }

        //condiciona si los campos estan llenos para activar los botones
        try{
            BooleanBinding areAllFilled = Bindings.createBooleanBinding(() ->
                    !txtf_user.getText().trim().isEmpty() &&
                    !txtf_NameUser.getText().trim().isEmpty() &&
                    !txtf_LastName.getText().trim().isEmpty() &&
                    !txtf_pass.getText().trim().isEmpty() &&
                    cbox_LvlUser.getValue() != null,
                    txtf_user.textProperty()
                    ,txtf_NameUser.textProperty()
                    ,txtf_LastName.textProperty()
                    ,txtf_pass.textProperty()
                    ,cbox_LvlUser.valueProperty()
            );
            btn_save.disableProperty().bind(areAllFilled.not());

            BooleanBinding areAllFilled1 = Bindings.createBooleanBinding(() ->
                     !txtf_user.getText().trim().isEmpty() &&
                     !txtf_NameUser.getText().trim().isEmpty() &&
                     !txtf_LastName.getText().trim().isEmpty(),
                     txtf_user.textProperty()
                    ,txtf_NameUser.textProperty()
                    ,txtf_LastName.textProperty()

            );
            btn_delete.disableProperty().bind(areAllFilled1.not());

            BooleanBinding areAllFilled2 = Bindings.createBooleanBinding(() ->
                    !txtf_user.getText().trim().isEmpty(),
                    txtf_user.textProperty()
            );
            btn_edit.disableProperty().bind(areAllFilled2.not());

        }catch (Exception ex){
            System.out.println(ex);
        }

    }

    //Recarga los datos para el tableview
    public void reload(){
        try{
            userList = FXCollections.observableArrayList();
            loadFile(archivo);
            tview_users.setItems(userList);
        }catch (Exception ex){
            System.out.println(ex);
        }
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

    //Carga el archivo para el tableview
    private void loadFile(String archivo) {

            try{
                File file = new File(archivo);
                if (!file.exists()) {
                    System.out.println("El archivo no existe");
                }
                else {
                    BufferedReader reader = new BufferedReader(new FileReader(archivo));
                    String linea;
                    while ((linea = reader.readLine()) != null) {
                        String[] datos = linea.split(";");
                        try {
                            if (datos.length == 6) {
                                String Lvl_user;// Validar formato correcto
                                if (datos[4].equals("1")) {
                                    Lvl_user = "Admin";
                                } else {
                                    Lvl_user = "Usuario";
                                }
                                Usuario usuario = new Usuario(datos[0], datos[1], datos[2], datos[3], Lvl_user, datos[5]);
                                userList.add(usuario);

                            }
                        } catch (Exception ex) {

                        }

                    }
                    reader.close();

                }


            } catch (IOException e) {
                e.printStackTrace();
            }

    }
}
