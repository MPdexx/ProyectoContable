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
import javafx.util.Duration;
import local.scontable.sistemacontable.Clases.ControladorFechaHora;
import local.scontable.sistemacontable.Clases.Cuenta;
import local.scontable.sistemacontable.Clases.Detalle;

import java.io.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;


public class PanelMovimientosController implements Initializable {
    @FXML
    TableView<Detalle> tview_detalle;
    @FXML
    TableColumn<Detalle, String> col_NumeroCuenta, col_SecuenciaDocumento, col_CuentaContable, col_Debito, col_Credito, col_Comentario;
    @FXML
    TextField txtf_nDocumento, txtf_desDocumento, txtf_Monto, txtf_Usuario, txtf_Secuencia, txtf_Cuenta,
    txtf_Debito, txtf_Credito, txtf_Comentario;
    @FXML
    ChoiceBox<String> cbox_tipoDocumento;
    @FXML
    Button btn_save, btn_cancel, btn_add;
    @FXML
    Label lbl_date, lbl_hour;
    private static String archivo = "C:\\ProjectoParcialJava\\SistemaContable\\src\\main\\resources\\Datos\\cuentas.txt";
    private static String archivoC = "C:\\ProjectoParcialJava\\SistemaContable\\src\\main\\resources\\Datos\\cabecera.txt";
    private static String archivoD = "C:\\ProjectoParcialJava\\SistemaContable\\src\\main\\resources\\Datos\\detalle.txt";
    private static String archivoD1 = "C:\\ProjectoParcialJava\\SistemaContable\\src\\main\\resources\\Datos\\detalle1.txt";
    private static String archivo1 = "C:\\ProjectoParcialJava\\SistemaContable\\src\\main\\resources\\Datos\\cuentas1.txt";
    private String usuario;
    private int cont=0;
    private ControladorFechaHora controladorFechaHora = new ControladorFechaHora();
    ObservableList<Detalle> listaDetalle = FXCollections.observableArrayList();
    private Map<String, Cuenta> cuentas = new HashMap<>();
    private boolean isSaving = false; // Bandera para evitar múltiples guardados
    private boolean isEditing = false;

    public void addData(){
        String nDocumento, secDocumento, cuentaContable, comentario = "";
        float vDebito=0, vCredito=0, monto=0;
        nDocumento = txtf_nDocumento.getText();
        secDocumento = txtf_Secuencia.getText();
        cuentaContable = txtf_Cuenta.getText();
        vDebito = txtf_Debito.getText().isEmpty() ? 0 : Float.parseFloat(txtf_Debito.getText());
        vCredito = txtf_Credito.getText().isEmpty() ? 0 : Float.parseFloat(txtf_Credito.getText());
        comentario = txtf_Comentario.getText().isEmpty() ? "Sin comentarios" : txtf_Comentario.getText();
        File file = new File(archivo);
        File file1 = new File(archivoD);
        File file2 = new File(archivoC);
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
            Alert del = new Alert(Alert.AlertType.CONFIRMATION);
            del.setTitle("Confirmar");
            del.setContentText("Este documento existe, ¿desea traerlo?");
            del.showAndWait().ifPresent(response ->{
                if (response == ButtonType.OK){
                    try (BufferedReader reader = new BufferedReader(new FileReader(file2))) {
                        String linea;
                        while ((linea = reader.readLine()) != null) {

                            String[] datos = linea.split(";");
                            String nroDocument = datos[0];
                            if (nDocumento.equalsIgnoreCase(nroDocument)){
                            String docType = datos[2];
                            String desDoc = datos[3];
                            String mont = datos[5];
                            cbox_tipoDocumento.setValue(docType);
                            txtf_desDocumento.setText(desDoc);
                            txtf_Monto.setText(mont);
                            }

                        }
                    } catch (IOException e) {
                        System.out.println("Error al leer el archivo: " + e.getMessage());
                    }
                    try (BufferedReader reader = new BufferedReader(new FileReader(file1))) {
                        String linea;
                        while ((linea = reader.readLine()) != null) {

                            String[] datos = linea.split(";");
                            String nroDocument = datos[0];
                            if (nDocumento.equalsIgnoreCase(nroDocument)){
                                String Account = datos[2];
                                txtf_Cuenta.setText(Account);
                            }

                        }
                    } catch (IOException e) {
                        System.out.println("Error al leer el archivo: " + e.getMessage());
                    }
                    txtf_nDocumento.setDisable(true);
                    txtf_Cuenta.setDisable(true);
                    cont++;
                    reload();
                    isEditing = true;
                    return;
                }
            });

        } else {
            String registro= nDocumento+ ";" + secDocumento + ";" + cuentaContable +";" + vDebito + ";" + vCredito +";" + comentario;
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivoD, true))) {

                writer.write(registro);
                writer.newLine(); // Agregar un salto de línea al final
                txtf_nDocumento.setDisable(true);
                txtf_Secuencia.setText("");
                txtf_Cuenta.setDisable(true);
                txtf_Debito.setText("");
                txtf_Credito.setText("");
                txtf_Comentario.setText("");
                cont+=1;

            } catch (IOException e) {
                Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                alert1.setTitle("Error");
                alert1.setHeaderText("Error al guardar los datos");
                alert1.setContentText("No se pudo guardar los datos");
                alert1.showAndWait();
            }

        }
        float totalDebito = 0;
        float totalCredito = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(archivoD))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] datos = linea.split(";"); // Asume que los datos están separados por comas
                if (datos.length >= 6 && datos[0].equals(nDocumento)) {
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
        if (totalDebito > totalCredito){
            txtf_Monto.setText(String.valueOf(totalDebito));
        }
        else {
            txtf_Monto.setText(String.valueOf(totalCredito));
        }
        reload();
        if (cont>=2){
            btn_save.setDisable(false);
        }

    }

    public void SaveData() throws IOException {
        String nDocumento, fDocumento, tipoDocumento, desDocumento, HechoPor, fActualizacion="";
        float Monto;
        boolean StatusActualizacion = false;
        nDocumento = txtf_nDocumento.getText();
        fDocumento = lbl_date.getText();
        tipoDocumento = String.valueOf(cbox_tipoDocumento.getValue());
        desDocumento = txtf_desDocumento.getText();
        HechoPor = txtf_Usuario.getText();
        String cuentaContable = txtf_Cuenta.getText();
        Monto = Float.parseFloat(txtf_Monto.getText());
        float totalDebito = 0;
        float totalCredito = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(archivoD))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] datos = linea.split(";"); // Asume que los datos están separados por comas
                if (datos.length >= 6 && datos[0].equals(nDocumento)) {
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
        if (totalDebito == totalCredito && totalDebito>0 && totalCredito>0){
            String registro = nDocumento + ";" + fDocumento + ";" + tipoDocumento + ";" + desDocumento + ";" + HechoPor + ";" + Monto + ";" + fActualizacion + ";" + StatusActualizacion;
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivoC, true))) {
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
            String nCuenta = "";

            File original = new File(archivo);
            try (BufferedReader reader = new BufferedReader(new FileReader(original))) {
                String linea;

                while ((linea = reader.readLine()) != null) {
                    String[] datos = linea.split(";"); // Asume que los datos están separados por comas
                    if (datos[0].equals(cuentaContable)){
                        nCuenta =  datos[0];
                    }
                }
                reader.close();
                FileInputStream fis = new FileInputStream(original);
                fis.close();


            } catch (IOException e) {
               e.printStackTrace();
            }
                // Actualizar balances
                actualizarBalances(cuentaContable, tipoDocumento, Monto);

                Cuenta cuenta = cuentas.get(nCuenta);
                while (cuenta !=null){
                    cuenta.setBalanceCta(cuenta.getBalanceCta() + Monto);
                    cuenta = cuentas.get(cuenta.getCtaPadre());
                }
                guardarCuentas(archivo,archivo1);
                isSaving = false;

        }
        else {
            mostrarAlerta("Error","La suma de todos los créditos \ny débitos deben ser el mismo");
            return;
        }
        txtf_nDocumento.setDisable(false);
        txtf_nDocumento.setText("");
        txtf_Secuencia.setText("");
        txtf_Cuenta.setDisable(false);
        txtf_Cuenta.setText("");
        txtf_Debito.setText("");
        txtf_Credito.setText("");
        txtf_Comentario.setText("");
        txtf_Monto.setText("");
        txtf_desDocumento.setText("");
        cbox_tipoDocumento.setValue("");
        reload();
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
        cargarCuentas(archivo);

        btn_save.setDisable(true);
        btn_add.setDisable(true);
        String[] docType = {"Ventas", "Compras", "Notas de débito", "Notas de crédito"};
        cbox_tipoDocumento.getItems().addAll(docType);

        BooleanBinding areAllFilled1 = Bindings.createBooleanBinding(() ->
                        !txtf_Secuencia.getText().trim().isEmpty() &&
                                !txtf_nDocumento.getText().trim().isEmpty() &&
                                !txtf_Cuenta.getText().trim().isEmpty() &&
                                cbox_tipoDocumento.getValue() !=null &&
                                !txtf_desDocumento.getText().trim().isEmpty() &&
                                !txtf_Debito.getText().trim().isEmpty() ||
                                !txtf_Credito.getText().trim().isEmpty()
                ,txtf_Secuencia.textProperty()
                ,txtf_Cuenta.textProperty()
                ,txtf_Debito.textProperty()
                ,txtf_Credito.textProperty()
                ,txtf_nDocumento.textProperty()
                ,cbox_tipoDocumento.valueProperty()
                ,txtf_desDocumento.textProperty()
        );
        btn_add.disableProperty().bind(areAllFilled1.not());

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

    private void guardarCuentas(String archivo, String archivo1) throws IOException {
        File original = new File(archivo);
        File temporal = new File(archivo1);
        if (isSaving) {
            System.out.println("Guardar cuentas ya está en proceso. Ignorando esta ejecución.");
            return; // Salir si ya se está ejecutando
        }

        isSaving = true;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(temporal))) {
            for (Cuenta cuenta : cuentas.values()) {
                // Validar datos de la cuenta antes de escribir
                if (cuenta.getNroCuenta() == null || cuenta.getDesCuenta() == null) {
                    System.out.println("Datos inválidos encontrados. Saltando cuenta: " + cuenta.getNroCuenta());
                    continue; // Saltar cuentas con datos inválidos
                }

                String linea = String.format("%s;%s;%s;%s;%s;%s;%s;%s;%s;%s;%.2f",
                        cuenta.getNroCuenta(),
                        cuenta.getDesCuenta() != null ? cuenta.getDesCuenta() : "",
                        cuenta.getTipoCuenta(),
                        cuenta.getLvlCuenta() != null ? cuenta.getLvlCuenta() : "",
                        cuenta.getCtaPadre() != null ? cuenta.getCtaPadre() : "0",
                        cuenta.getGrpCuenta() != null ? cuenta.getGrpCuenta() : "",
                        cuenta.getFCreacion() != null ? cuenta.getFCreacion() : "",
                        cuenta.getHCreacion() != null ? cuenta.getHCreacion() : "",
                        cuenta.getDebitoAc(),
                        cuenta.getCreditoAc(),
                        cuenta.getBalanceCta());
                writer.write(linea);
                writer.newLine();
                System.out.println("Línea escrita: " + linea);
            }
            System.out.println("Archivo temporal generado correctamente.");
            FileInputStream fis = new FileInputStream(original);
            fis.close();

        } catch (IOException e) {
            System.out.println("Error al guardar las cuentas: " + e.getMessage());
        }

        // Verificar reemplazo del archivo
        if (original.delete()) {
            temporal.renameTo(original);
        } else {
            System.out.println("Error al eliminar el archivo original.");
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
                        if (txtf_nDocumento.getText().equals(datos[0])) {
                            Detalle detalle = new Detalle(datos[0],datos[1],datos[2], datos[3], datos[4], datos[5]);
                            listaDetalle.add(detalle);
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

    public void Cancel(){
        Alert del = new Alert(Alert.AlertType.CONFIRMATION);
        del.setTitle("Confirmar");
        del.setContentText("¿Está seguro que quieres cancelar?");
        del.showAndWait().ifPresent(response ->{
            if (response == ButtonType.OK){
                File original = new File(archivoD);
                File temporal = new File(archivoD1);
                if (!isEditing && original.exists()){
                    try(BufferedReader reader = new BufferedReader(new FileReader(original));
                        BufferedWriter writer = new BufferedWriter(new FileWriter(temporal)))
                    {
                        String linea;

                        while ((linea = reader.readLine()) != null) {
                            // Separar los campos para verificar la condición
                            String[] datos = linea.split(";");
                            if (datos.length >=1){
                                String nDoc = datos[0];
                                // Escribir solo las líneas que NO coincidan con el ID a eliminar
                                if (!txtf_nDocumento.getText().equalsIgnoreCase(nDoc)) {
                                    writer.write(linea);
                                    writer.newLine();
                                }
                            }

                        }
                        FileInputStream fis = new FileInputStream(original);
                        fis.close();
                        reader.close();
                        writer.close();

                        if (original.delete()){
                            temporal.renameTo(original);
                        }

                    }catch (Exception ex){
                        ex.printStackTrace();
                    }
                }
                txtf_nDocumento.setDisable(false);
                txtf_nDocumento.setText("");
                txtf_desDocumento.setText("");
                cbox_tipoDocumento.setValue("");
                txtf_Secuencia.setText("");
                txtf_Cuenta.setDisable(false);
                txtf_Monto.setText("");
                txtf_Cuenta.setText("");
                txtf_Debito.setText("");
                txtf_Credito.setText("");
                txtf_Comentario.setText("");
                reload();
            }
        });
    }

    //Metodo para cargar cuentas desde un archivo
    public void cargarCuentas(String arc) {
        try (BufferedReader reader = new BufferedReader(new FileReader(arc))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] datos = linea.split(";");
                if (datos.length >= 11) {
                    // Buscar cuenta padre en el mapa
                    Cuenta cuenta = new Cuenta(datos[0],datos[1],datos[2], datos[3], datos[4], datos[5], datos[6], datos[7], datos[8], datos[9], Float.parseFloat(datos[10]));
                    // Añadir cuenta al mapa
                    cuentas.put(datos[0], cuenta);
                }
            }
        } catch (IOException e) {
            System.out.println("Error al cargar las cuentas: " + e.getMessage());
        }
        cuentas.forEach((clave, cuenta) -> {
            System.out.println("Cuenta: " + clave);
            System.out.println("Número: " + cuenta.getNroCuenta());
            System.out.println("Descripción: " + cuenta.getDesCuenta());
            System.out.println("Tipo: " + cuenta.getTipoCuenta());
            System.out.println("Padre: " + cuenta.getCtaPadre());
            System.out.println("Balance: " + cuenta.getBalanceCta());
            System.out.println("--------------------------");
        });
    }

    private void actualizarBalances(String cuentaContable, String tipoDocumento, float monto) throws IOException {
        Cuenta cuenta = cuentas.get(cuentaContable);

        if (cuenta == null) {
            System.out.println("Cuenta contable no encontrada: " + cuentaContable);
            return;
        }

        while (cuenta != null) {
            // Determinar el tipo de movimiento
            boolean esDebito = tipoDocumento.equals("Ventas") || tipoDocumento.equals("Notas de débito");
            float balanceActual = cuenta.getBalanceCta();

            // Manejar el tipo de cuenta
            if (cuenta.getTipoCuenta().equals("true")) {
                // Lógica para cuentas detalle
                if (esDebito) {
                    balanceActual += monto; // Suma para débito
                } else {
                    balanceActual -= monto; // Resta para crédito
                }
            } else {
                // Lógica para cuentas generales
                if (!esDebito) {
                    balanceActual += monto; // resta para débito
                } else  {
                    balanceActual -= monto; // Suma para crédito
                }
            }

            // Actualizar el balance en el mapa y en la cuenta
            cuenta.setBalanceCta(balanceActual);
            cuentas.put(cuenta.getNroCuenta(), cuenta); // Asegurar que se actualice en el mapa

            // Pasar a la cuenta padre
            cuenta = cuentas.get(cuenta.getCtaPadre());
        }

        // Guardar los cambios al archivo
        guardarCuentas(archivo,archivo1);
    }
}
