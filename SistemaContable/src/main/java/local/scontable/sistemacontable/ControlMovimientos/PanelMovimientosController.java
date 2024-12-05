package local.scontable.sistemacontable.ControlMovimientos;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Duration;
import local.scontable.sistemacontable.Clases.ControladorFechaHora;
import local.scontable.sistemacontable.Clases.Cuenta;
import local.scontable.sistemacontable.Clases.Detalle;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;


public class PanelMovimientosController implements Initializable {
    @FXML
    TableView<Detalle> tview_detalle;
    @FXML
    TableColumn<Detalle, String> col_NumeroCuenta, col_SecuenciaDocumento, col_CuentaContable, col_Debito, col_Credito, col_Comentario;
    @FXML
    TextField txtf_nDocumento, txtf_tipoDocumento, txtf_desDocumento, txtf_Monto, txtf_Usuario, txtf_Secuencia, txtf_Cuenta,
    txtf_Debito, txtf_Credito, txtf_Comentario;
    @FXML
    Button btn_save, btn_cancel, btn_add;
    @FXML
    Label lbl_date, lbl_hour;
    private static String archivo = "C:\\ProjectoParcialJava\\SistemaContable\\src\\main\\resources\\Datos\\cuentas.txt";
    private static String archivoC = "C:\\ProjectoParcialJava\\SistemaContable\\src\\main\\resources\\Datos\\cabecera.txt";
    private static String archivoD = "C:\\ProjectoParcialJava\\SistemaContable\\src\\main\\resources\\Datos\\detalle.txt";
    private String usuario;
    private ControladorFechaHora controladorFechaHora = new ControladorFechaHora();
    ObservableList<Detalle> listaDetalle = FXCollections.observableArrayList();

    public void addData(){
        String nDocumento, secDocumento, cuentaContable, comentario = "";
        float vDebito=0, vCredito=0, monto=0;
        nDocumento = txtf_nDocumento.getText();
        secDocumento = txtf_Secuencia.getText();
        cuentaContable = txtf_Cuenta.getText();
        vDebito = txtf_Debito.getText().isEmpty() ? 0 : Float.parseFloat(txtf_Debito.getText());
        vCredito = txtf_Credito.getText().isEmpty() ? 0 : Float.parseFloat(txtf_Credito.getText());
        comentario = txtf_Comentario.getText().isEmpty() ? "" : txtf_Comentario.getText();

        File file = new File(archivo);
        File file1 = new File(archivoD);
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
        boolean docExiste = false;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] datos = linea.split(";");
                String  numeroCuentaArchivo = datos[0]; // Convertir a entero para comparar
                String tipoCuenta = datos[2];
                if (numeroCuentaArchivo.equals(cuentaContable)) {
                    cuentaExiste = true; // La cuenta ya existe
                }
                if(numeroCuentaArchivo.equals(cuentaContable) && tipoCuenta.equals("true")){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Tipo de cuenta no válido");
                    alert.setContentText("Solo se pueden hacer transacciones a cuentas tipo detalle");
                    alert.showAndWait();
                    return; // Detener el proceso
                }
            }
        } catch (IOException e) {
            System.out.println("Error al leer el archivo: " + e.getMessage());
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file1))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] datos = linea.split(";");
                String  numeroDocumento = datos[0]; // Convertir a entero para comparar
                if (numeroDocumento.equals(nDocumento) && !txtf_nDocumento.isDisable()) {
                    docExiste = true; // La cuenta ya existe
                }
            }
        } catch (IOException e) {
            System.out.println("Error al leer el archivo: " + e.getMessage());
        }
        if (!cuentaExiste) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Cuenta inexistente");
            alert.setContentText("El numero de cuenta ingresado no existe");
            alert.showAndWait();
            return; // Detener el proceso
        } else if (docExiste) {


        } else {
            String registro= nDocumento+ ";" + secDocumento + ";" + cuentaContable +";" + vDebito + ";" + vCredito +";" + comentario;
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivoD, true))) {


                writer.write(registro);
                writer.newLine(); // Agregar un salto de línea al final
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Info");
                alert.setHeaderText("Guardado correctamente");
                alert.setContentText("Datos guardados correctamente");
                alert.showAndWait();
                reload();
                if (vDebito > vCredito){
                    txtf_Monto.setText(String.valueOf(vDebito));
                }
                else {
                    txtf_Monto.setText(String.valueOf(vCredito));
                }
                txtf_nDocumento.setDisable(true);
                txtf_Secuencia.setText("");
                txtf_Cuenta.setText("");
                txtf_Debito.setText("");
                txtf_Credito.setText("");
                txtf_Comentario.setText("");
                reload();
            } catch (IOException e) {
                Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                alert1.setTitle("Error");
                alert1.setHeaderText("Error al guardar los datos");
                alert1.setContentText("No se pudo guardar los datos");
                alert1.showAndWait();
            }

        }

    }

    public void SaveData(){
        String nDocumento, fDocumento, tipoDocumento, desDocumento, HechoPor, fActualizacion="";
        double Monto;
        boolean StatusActualizacion = false;
        nDocumento = txtf_nDocumento.getText();
        fDocumento = lbl_date.getText();
        tipoDocumento = txtf_tipoDocumento.getText();
        desDocumento = txtf_desDocumento.getText();
        HechoPor = txtf_Usuario.getText();

        float totalDebito = 0;
        float totalCredito = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] datos = linea.split(","); // Asume que los datos están separados por comas
                if (datos.length >= 6) {
                    // Parsear los campos necesarios
                    float debito = Float.parseFloat(datos[3]); // vDebito
                    float credito = Float.parseFloat(datos[4]); // vCredito

                    // Sumar los valores
                    totalDebito += debito;
                    totalCredito += credito;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (totalDebito == totalCredito){
            String registro = nDocumento + ";" + fDocumento + ";" + tipoDocumento + ";" + desDocumento + ";" + HechoPor + ";" + fActualizacion + ";" + StatusActualizacion;
            File file = new File(archivoC);

        }
        else {
            mostrarAlerta("Error","La suma de todos los créditos \ny débitos deben ser el mismo");
        }
    }
    public void setUsername(String username){
        this.txtf_Usuario.setText(username);
    }

    public void returnMenu(){

    }
    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btn_save.setDisable(true);
        btn_add.setDisable(true);

        BooleanBinding areAllFilled = Bindings.createBooleanBinding(() ->
                        !txtf_nDocumento.getText().trim().isEmpty() &&
                                !txtf_tipoDocumento.getText().trim().isEmpty() &&
                                !txtf_desDocumento.getText().trim().isEmpty()
                ,txtf_nDocumento.textProperty()
                ,txtf_tipoDocumento.textProperty()
                ,txtf_desDocumento.textProperty()
        );
        btn_save.disableProperty().bind(areAllFilled.not());

        BooleanBinding areAllFilled1 = Bindings.createBooleanBinding(() ->
                        !txtf_Secuencia.getText().trim().isEmpty() &&
                                !txtf_Cuenta.getText().trim().isEmpty() ||
                                !txtf_Debito.getText().trim().isEmpty() ||
                                !txtf_Credito.getText().trim().isEmpty()
                ,txtf_Secuencia.textProperty()
                ,txtf_Cuenta.textProperty()
                ,txtf_Debito.textProperty()
                ,txtf_Credito.textProperty()
        );
        btn_add.disableProperty().bind(areAllFilled.not());

        // Actualizar fecha y hora al iniciar
        controladorFechaHora.updateDateTime(lbl_date, lbl_hour);

        // Configurar una tarea que actualice la hora cada segundo
        Timeline clock = new Timeline(new KeyFrame(Duration.seconds(1), e -> controladorFechaHora.updateDateTime(lbl_date, lbl_hour)));
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();

        col_NumeroCuenta.setCellValueFactory(cellData ->cellData.getValue().getnDocumentoProperty());
        col_SecuenciaDocumento.setCellValueFactory(cellData ->cellData.getValue().getSecDocumentoProperty());
        col_CuentaContable.setCellValueFactory(cellData ->cellData.getValue().getCuentaContableProperty());
        col_Debito.setCellValueFactory(cellData ->cellData.getValue().getVDebitoProperty());
        col_Credito.setCellValueFactory(cellData ->cellData.getValue().getVCreditoProperty());
        col_Comentario.setCellValueFactory(cellData ->cellData.getValue().getComentarioProperty());

        //Listener del textfield Debito, para que si esta lleno el de credito no se pueda llenar
        txtf_Debito.textProperty().addListener(((observable, oldValue, newValue) ->{
            if (newValue != null && !newValue.isEmpty()) {
                txtf_Credito.setDisable(true);
            }
            else {
                txtf_Credito.setDisable(false);
            }
        }
        ));
        //Listener para que solo se puedan introducir numeros
        txtf_Debito.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(
                    ObservableValue<? extends String> observable,
                    String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    txtf_Debito.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        //Listener del textfield Credito, para que si esta lleno el de debito no se pueda llenar
        txtf_Credito.textProperty().addListener((observable, oldValue, newValue)->{
            if (newValue != null && !newValue.isEmpty()){
                txtf_Debito.setDisable(true);
            }
            else{
                txtf_Debito.setDisable(false);
            }
        });
        //Listener para que solo se puedan introducir numeros
        txtf_Credito.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(
                    ObservableValue<? extends String> observable,
                    String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    txtf_Credito.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        txtf_Cuenta.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(
                    ObservableValue<? extends String> observable,
                    String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    txtf_Cuenta.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
        try{
            listaDetalle = FXCollections.observableArrayList();
            loadFile(archivoD);
            tview_detalle.setItems(listaDetalle);
        }catch (Exception ex){
            System.out.println(ex);
        }
    }

    public void reload(){
        try{
            listaDetalle = FXCollections.observableArrayList();
            loadFile(archivoD);
            tview_detalle.setItems(listaDetalle);
        }catch (Exception ex){
            System.out.println(ex);
        }
    }

    //Carga el archivo para el tableview
    private void loadFile(String arc) throws Exception {
        try{
            File file = new File(arc);
            if (!file.exists()) {
                System.out.println("El archivo no existe");
            }
            else {
                BufferedReader reader = new BufferedReader(new FileReader(arc));
                String linea;
                while ((linea = reader.readLine()) != null) {
                    String[] datos = linea.split(";");
                    try {
                        if (txtf_nDocumento.getText().trim().equals(datos[0])) {
                            Detalle detalle = new Detalle(datos[0],datos[1],datos[2], datos[3], datos[4], datos[5]);
                            listaDetalle.add(detalle);
                            System.out.println("Datos procesados: " + datos);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                reader.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
