package local.scontable.sistemacontable.ControlMantenimientos;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.binding.Binding;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import local.scontable.sistemacontable.Clases.*;
import local.scontable.sistemacontable.PrincipalController;
import local.scontable.sistemacontable.Clases.ControladorFechaHora;


import javax.crypto.SecretKey;
import java.io.*;
import java.net.URL;
import java.sql.Time;
import java.util.ResourceBundle;

public class MantenimientoCatalogoCuentaInController implements Initializable, CambioPanel {
    @FXML
    TableView<Cuenta> tview_cuenta;
    @FXML
    TableColumn<Cuenta, String> col_nCuenta, col_desCuenta, col_tipo, col_lvl, col_cuentaPadre, col_grupo, col_fCreacion, col_hCreacion, col_DebitoAc, col_CreditoAc, col_Balance;
    @FXML
    Label lbl_date, lbl_hour;
    @FXML
    TextField txtf_nCuenta, txtf_desCuenta, txtf_CuentaPadre;
    @FXML
    ChoiceBox<String> cbox_tipoCuenta, cbox_grupoCuenta, cbox_nivelCuenta;
    @FXML
    Button btn_save, btn_edit, btn_delete;
    private static String archivo = "C:\\ProjectoParcialJava\\SistemaContable\\src\\main\\resources\\Datos\\cuentas.txt";
    private static String archivo1 = "C:\\ProjectoParcialJava\\SistemaContable\\src\\main\\resources\\Datos\\cuentas1.txt";
    private PrincipalController panelPadre;
    private ControladorFechaHora controladorFechaHora = new ControladorFechaHora();
    private Man_CatalogoCuentaEditController Cuenta = new Man_CatalogoCuentaEditController();
    ObservableList<Cuenta> userList = FXCollections.observableArrayList();
    private FilteredList<Cuenta> filteredData;

    public void saveData(){
        int nCuenta, nivelCuenta, CuentaPadre;
        float DebitoAc=0, CreditoAc=0, BalanceCta=0;
        String desCuenta, grupoCuenta, fCreacion, hCreacion;
        boolean tipoCuenta;

        nCuenta =  Integer.parseInt(txtf_nCuenta.getText());
        desCuenta = txtf_desCuenta.getText();
        tipoCuenta = cbox_tipoCuenta.getValue() =="General" ? true : false;
        nivelCuenta = Integer.parseInt(cbox_nivelCuenta.getValue());
        CuentaPadre = txtf_CuentaPadre.getText().isEmpty() ? 0 : Integer.parseInt(txtf_CuentaPadre.getText());
        grupoCuenta = String.valueOf(cbox_grupoCuenta.getValue());
        fCreacion = lbl_date.getText();
        hCreacion = lbl_hour.getText();

        //Construir el registro de la cuenta
        String registro = nCuenta + ";"+ desCuenta + ";" + tipoCuenta + ";" + nivelCuenta + ";" + CuentaPadre
                + ";" + grupoCuenta + ";" + fCreacion + ";" + hCreacion + ";" + DebitoAc + ";" + CreditoAc + ";" + BalanceCta;
        File file = new File(archivo);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("Error al crear el archivo.");
                alert.showAndWait();
            }
        }
        boolean cuentaExiste = false;
        boolean cuentapadreExiste = false;
        boolean noPadre= false;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] datos = linea.split(";");
                String numeroCuentaArchivo = datos[0]; // Convertir a entero para comparar
                if (numeroCuentaArchivo.equals(String.valueOf(nCuenta))) {
                    cuentaExiste = true; // La cuenta ya existe
                }
                if (numeroCuentaArchivo.equals(String.valueOf(CuentaPadre)) && nivelCuenta >0) {
                    cuentapadreExiste = true; // La cuenta padre existe
                }
                if (datos[0].equals(String.valueOf(CuentaPadre)) && datos[2].equals("false")){
                    noPadre=true;
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
        } else if (!cuentapadreExiste && nivelCuenta!=0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Cuenta padre no existe");
            alert.setContentText("El numero de cuenta padre ingresado no existe.");
            alert.showAndWait();
            return;
        } else if (noPadre) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error en la cuenta padre");
            alert.setContentText("Una cuenta tipo detalle no puede ser padre");
            alert.showAndWait();
            return;
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

        reload();
        txtf_nCuenta.setText("");
        txtf_desCuenta.setText("");
        cbox_tipoCuenta.setValue("");
        cbox_nivelCuenta.setValue("");
        txtf_CuentaPadre.setText("");
        cbox_grupoCuenta.setValue("");
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
                String nCuenta = "";
                nCuenta = txtf_nCuenta.getText();
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
                        String numCuenta = datos[0];

                        // Escribir solo las líneas que NO coincidan con el ID a eliminar
                        if (!(numCuenta.trim().equals(nCuenta.trim()))) {
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

                reload();
                txtf_nCuenta.setText("");
                txtf_desCuenta.setText("");
                cbox_tipoCuenta.setValue("");
                cbox_nivelCuenta.setValue(null);
                txtf_CuentaPadre.setText("");
                cbox_grupoCuenta.setValue("");
            } else {

            }
        });


    }

    public void editData(){
        int nCuenta, nivelCuenta=0, CuentaPadre=0;
        float DebitoAc=0, CreditoAc=0, BalanceCta=0;
        String desCuenta="", grupoCuenta="", fCreacion="", hCreacion="";
        boolean tipoCuenta= false;
        boolean noEdit = false;
        nCuenta = Integer.parseInt(txtf_nCuenta.getText());
        File file = new File(archivo);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                System.out.println("Error al crear el archivo: " + e.getMessage());
            }
        }
        boolean cuentaExiste = false;
        boolean esCuentaPadre = false;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String linea;

            while ((linea = reader.readLine()) != null) {
                String[] datos = linea.split(";");
                if (datos.length == 11 && datos[0].equals(String.valueOf(nCuenta))) { // Validar por nombre de usuario
                    desCuenta = datos[1];
                    tipoCuenta = Boolean.parseBoolean(datos[2]);
                    nivelCuenta = Integer.parseInt(datos[3]);
                    CuentaPadre = Integer.parseInt(datos[4]);
                    grupoCuenta = datos[5];
                    fCreacion = datos[6];
                    hCreacion = datos[7];
                    DebitoAc = Float.parseFloat(datos[8]);
                    CreditoAc = Float.parseFloat(datos[9]);
                    BalanceCta = Float.parseFloat(datos[10]);
                    cuentaExiste = true;
                    int numeroCuentaArchivo = Integer.parseInt(datos[4]); // Convertir a entero para comparar
                    if (numeroCuentaArchivo == nCuenta) {
                        esCuentaPadre = true; // La cuenta ya existe
                    }
                    if(BalanceCta >0){
                        noEdit = true;
                    }
                    break;
                }
            }


        } catch (IOException e) {
            System.out.println("Error al leer el archivo: " + e.getMessage());
        }
        // Si el usuario existe, mostrar un mensaje y salir
        if (noEdit){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error al editar la cuenta");
            alert.setContentText("Una cuenta con balance no puede ser modificada");
            alert.showAndWait();
        }
        else if (cuentaExiste) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Mantenimientos/Man_CatalogoCuentaEdit.fxml"));
                Parent root = loader.load();
                Cuenta = loader.getController();
                Stage stage = new Stage();
                stage.setTitle("Editar Usuario");
                Cuenta.getCuenta(nCuenta,desCuenta,tipoCuenta,nivelCuenta,CuentaPadre,grupoCuenta,fCreacion,hCreacion,DebitoAc,CreditoAc,BalanceCta, esCuentaPadre);
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
            alert.setHeaderText("Cuenta no existente");
            alert.setContentText("El numero de cuenta no existe");
            alert.showAndWait();
            return; // Detener el proceso
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        String[] accType = {"General", "Detalle"};
        String[] accLvl = {"0","1","2","3","4","5","6"};
        String[] accGroup = {"Activo", "Pasivo", "Capital", "Ingresos", "Costos", "Gastos"};
        cbox_tipoCuenta.getItems().addAll(accType);
        cbox_nivelCuenta.getItems().addAll(accLvl);
        cbox_grupoCuenta.getItems().addAll(accGroup);


        // Actualizar fecha y hora al iniciar
        controladorFechaHora.updateDateTime(lbl_date, lbl_hour);

        // Configurar una tarea que actualice la hora cada segundo
        Timeline clock = new Timeline(new KeyFrame(Duration.seconds(1), e -> controladorFechaHora.updateDateTime(lbl_date, lbl_hour)));
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();

        btn_save.setDisable(true);
        btn_edit.setDisable(true);
        btn_delete.setDisable(true);
        col_nCuenta.setCellValueFactory(cellData -> cellData.getValue().nCuentaProperty());
        col_desCuenta.setCellValueFactory(cellData -> cellData.getValue().desCuentaProperty());
        col_tipo.setCellValueFactory(cellData -> cellData.getValue().TipoCuentaProperty());
        col_lvl.setCellValueFactory(cellData -> cellData.getValue().LvlCuentaProperty());
        col_cuentaPadre.setCellValueFactory(cellData -> cellData.getValue().CtaPadreProperty());
        col_grupo.setCellValueFactory(cellData -> cellData.getValue().GrpCuentaProperty());
        col_fCreacion.setCellValueFactory(cellData -> cellData.getValue().fCreacionProperty());
        col_hCreacion.setCellValueFactory(cellData -> cellData.getValue().hCreacionProperty());
        col_DebitoAc.setCellValueFactory(cellData -> cellData.getValue().DebitoAcProperty());
        col_CreditoAc.setCellValueFactory(cellData -> cellData.getValue().CreditoAcProperty());
        col_Balance.setCellValueFactory(cellData -> cellData.getValue().BalanceCtaProperty());
        try{
            userList = FXCollections.observableArrayList();
            filteredData = new FilteredList<>(userList,p-> true);
            loadFile(archivo);
            tview_cuenta.setItems(filteredData);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        boolean isCuentaPadreDisabled = txtf_CuentaPadre.isDisabled();
        setupBindings(isCuentaPadreDisabled);
        txtf_CuentaPadre.disableProperty().addListener(((observable, oldValue, newValue) ->
        {
           setupBindings(newValue);
        }));
        // Crear una Binding para validar si todos los campos requeridos están llenos

        BooleanBinding areAllFilled1 = Bindings.createBooleanBinding(()->
                !txtf_nCuenta.getText().trim().isEmpty()
                ,txtf_nCuenta.textProperty()
        );
        btn_edit.disableProperty().bind(areAllFilled1.not());
        btn_delete.disableProperty().bind(areAllFilled1.not());

        txtf_nCuenta.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(
                    ObservableValue<? extends String> observable,
                    String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    txtf_nCuenta.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
        // Vincular el filtro al TextField
        txtf_nCuenta.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(cuenta -> {
                // Si el filtro está vacío, mostrar todos los datos
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                // Comparar cada campo del usuario con el filtro
                if (cuenta.getNroCuenta().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Coincide con el nombre de usuario
                }
                return false; // No hay coincidencias
            });
        });
        cbox_nivelCuenta.valueProperty().addListener((observable, oldValue, newValue) -> {
           if (newValue.equals("0") && newValue != null){
               txtf_CuentaPadre.setDisable(true);
           }
           else {
               txtf_CuentaPadre.setDisable(false);
           }
        });

        txtf_CuentaPadre.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(
                    ObservableValue<? extends String> observable,
                    String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    txtf_CuentaPadre.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });


    }
    private void setupBindings(boolean isCuentaPadreDisabled) {
        // Romper cualquier binding previo en btn_save
        btn_save.disableProperty().unbind();

        // Crear un BooleanBinding basado en el estado de txtf_CuentaPadre
        BooleanBinding areAllFilled;
        if (isCuentaPadreDisabled) {
            // Si txtf_CuentaPadre está desactivado, no validar su contenido
            areAllFilled = Bindings.createBooleanBinding(() ->
                            !txtf_nCuenta.getText().trim().isEmpty() &&
                                    !txtf_desCuenta.getText().trim().isEmpty() &&
                                    cbox_tipoCuenta.getValue() != null &&
                                    cbox_nivelCuenta.getValue() != null &&
                                    cbox_grupoCuenta.getValue() != null,
                    txtf_nCuenta.textProperty(),
                    txtf_desCuenta.textProperty(),
                    cbox_tipoCuenta.valueProperty(),
                    cbox_nivelCuenta.valueProperty(),
                    cbox_grupoCuenta.valueProperty()
            );
        } else {
            // Si txtf_CuentaPadre está activado, validar su contenido también
            areAllFilled = Bindings.createBooleanBinding(() ->
                            !txtf_nCuenta.getText().trim().isEmpty() &&
                                    !txtf_desCuenta.getText().trim().isEmpty() &&
                                    !txtf_CuentaPadre.getText().trim().isEmpty() &&
                                    cbox_tipoCuenta.getValue() != null &&
                                    cbox_nivelCuenta.getValue() != null &&
                                    cbox_grupoCuenta.getValue() != null,
                    txtf_nCuenta.textProperty(),
                    txtf_desCuenta.textProperty(),
                    txtf_CuentaPadre.textProperty(),
                    cbox_tipoCuenta.valueProperty(),
                    cbox_nivelCuenta.valueProperty(),
                    cbox_grupoCuenta.valueProperty()
            );
        }

        // Vincular la propiedad disable del botón al resultado del binding
        btn_save.disableProperty().bind(areAllFilled.not());
    }

    public void returnMenu() throws IOException {
        if (panelPadre != null) {
            panelPadre.cMantenimientos("/Mantenimientos/MantenimientoUsuarios.fxml");
        }
    }

    public void reload(){
        try{
            userList = FXCollections.observableArrayList();
            loadFile(archivo);
            tview_cuenta.setItems(userList);
        }catch (Exception ex){
            System.out.println(ex);
        }
    }

    @Override
    public void setPanelPadre(PrincipalController panelPadre) {
        this.panelPadre = panelPadre;
    }
    //Carga el archivo para el tableview
    private void loadFile(String archivo) throws Exception {
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
                        if (datos.length == 11) {
                            String tCuenta;// Validar formato correcto
                            String cPadre = "";
                            if (datos[2].equals("true")) {
                                tCuenta = "General";
                            } else {
                                tCuenta = "Detalle";
                            }
                            if (!datos[4].equals("0")){
                                cPadre = datos[4];
                            }
                            Cuenta cuenta = new Cuenta(datos[0],datos[1],tCuenta, datos[3], cPadre, datos[5], datos[6], datos[7], datos[8], datos[9], datos[10]);
                            userList.add(cuenta);

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

