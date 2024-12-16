package local.scontable.sistemacontable.ControlConsultas;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import local.scontable.sistemacontable.Clases.CambioPanel;
import local.scontable.sistemacontable.Clases.Cuenta;
import local.scontable.sistemacontable.PrincipalController;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class PorCatalogoController implements Initializable, CambioPanel {
    private PrincipalController panelPadre;
    @FXML
    TableView<Cuenta> tview_cuenta;
    @FXML
    TableColumn<Cuenta, String> col_nCuenta, col_desCuenta, col_tipo, col_lvl, col_cuentaPadre, col_grupo, col_fCreacion, col_hCreacion;
    @FXML
    TableColumn<Cuenta, Float> col_Balance, col_DebitoAc, col_CreditoAc;
    @FXML
    TextField txtf_descripcion, txtf_nCuenta, txtf_cuentaPadre;
    @FXML
    DatePicker date_fCreacion;
    @FXML
    Label lbl_fCreacion, lbl_lvl, lbl_tipo, lbl_descripcion;
    @FXML
    ChoiceBox<String> cbox_filtro, cbox_grupo, cbox_lvl, cbox_tipo;
    @FXML
    Button btn_clean;
    ObservableList<Cuenta> userList = FXCollections.observableArrayList();
    private FilteredList<Cuenta> filteredData;
    private static String archivo = "C:\\ProjectoParcialJava\\SistemaContable\\src\\main\\resources\\Datos\\cuentas.txt";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        lbl_descripcion.setVisible(false);
        txtf_descripcion.setVisible(false);
        lbl_fCreacion.setVisible(false);
        date_fCreacion.setVisible(false);
        lbl_lvl.setVisible(false);
        cbox_lvl.setVisible(false);
        lbl_tipo.setVisible(false);
        cbox_tipo.setVisible(false);

        String[] filtro= {"Descripcion", "Tipo", "Fecha de creacion","Nivel"};
        String[] grupo = {"Activo", "Pasivo", "Capital", "Ingresos", "Costos", "Gastos"};
        String[] lvl = {"0","1","2","3","4","5","6"};
        String[] tipo = {"General", "Detalle"};
        cbox_filtro.getItems().addAll(filtro);
        cbox_grupo.getItems().addAll(grupo);
        cbox_lvl.getItems().addAll(lvl);
        cbox_tipo.getItems().addAll(tipo);

        col_nCuenta.setCellValueFactory(cellData -> cellData.getValue().nCuentaProperty());
        col_desCuenta.setCellValueFactory(cellData -> cellData.getValue().desCuentaProperty());
        col_tipo.setCellValueFactory(cellData -> cellData.getValue().TipoCuentaProperty());
        col_lvl.setCellValueFactory(cellData -> cellData.getValue().LvlCuentaProperty());
        col_cuentaPadre.setCellValueFactory(cellData -> cellData.getValue().CtaPadreProperty());
        col_grupo.setCellValueFactory(cellData -> cellData.getValue().GrpCuentaProperty());
        col_fCreacion.setCellValueFactory(cellData -> cellData.getValue().fCreacionProperty());
        col_hCreacion.setCellValueFactory(cellData -> cellData.getValue().hCreacionProperty());
        col_DebitoAc.setCellValueFactory(cellData -> cellData.getValue().DebitoAcProperty().asObject());
        col_CreditoAc.setCellValueFactory(cellData -> cellData.getValue().CreditoAcProperty().asObject());
        col_Balance.setCellValueFactory(cellData -> cellData.getValue().BalanceCtaProperty().asObject());
        try{
            userList = FXCollections.observableArrayList();
            filteredData = new FilteredList<>(userList, p-> true);
            loadFile(archivo);
            tview_cuenta.setItems(filteredData);
        }catch (Exception ex){
            ex.printStackTrace();
        }

        //Configuracion para filtrara datos
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

        txtf_cuentaPadre.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(
                    ObservableValue<? extends String> observable,
                    String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    txtf_cuentaPadre.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
        // Vincular el filtro al TextField
        txtf_cuentaPadre.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(cuenta -> {
                // Si el filtro está vacío, mostrar todos los datos
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                // Comparar cada campo del usuario con el filtro
                if (cuenta.getCtaPadre().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Coincide con el nombre de usuario
                }
                return false; // No hay coincidencias
            });
        });

        cbox_grupo.valueProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(cuenta -> {
                // Si el filtro está vacío, mostrar todos los datos
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                // Comparar cada campo del usuario con el filtro
                if (cuenta.getGrpCuenta().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Coincide con el nombre de usuario
                }
                return false; // No hay coincidencias
            });
        });

        cbox_filtro.valueProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.equals("Descripcion")){
                visible(newValue);
                txtf_descripcion.textProperty().addListener((observable1, oldValue1, newValue1) -> {
                    filteredData.setPredicate(cuenta -> {
                        // Si el filtro está vacío, mostrar todos los datos
                        if (newValue1 == null || newValue1.isEmpty()) {
                            return true;
                        }

                        String lowerCaseFilter = newValue1.toLowerCase();

                        // Comparar cada campo del usuario con el filtro
                        if (cuenta.getDesCuenta().toLowerCase().contains(lowerCaseFilter)) {
                            return true; // Coincide con el nombre de usuario
                        }
                        return false; // No hay coincidencias
                    });
                });
            } else if (newValue.equals("Tipo")) {
                visible(newValue);
                cbox_tipo.valueProperty().addListener((observable1, oldValue1, newValue1) -> {
                    filteredData.setPredicate(cuenta -> {
                        // Si el filtro está vacío, mostrar todos los datos

                        if (newValue1 == null || newValue1.isEmpty()) {
                            return true;
                        }
                        String lowerCaseFilter = newValue1.toLowerCase();

                        // Comparar cada campo del usuario con el filtro
                        if (cuenta.getTipoCuenta().toLowerCase().contains(lowerCaseFilter)) {
                            return true; // Coincide con el nombre de usuario
                        }
                        return false; // No hay coincidencias
                    });
                });
            } else if (newValue.equals("Fecha de creacion")) {
                visible(newValue);
                date_fCreacion.valueProperty().addListener((observable1, oldValue1, newValue1) -> {
                    filteredData.setPredicate(cuenta -> {
                        // Si el filtro está vacío, mostrar todos los datos
                        if (newValue1 == null) {
                            return true;
                        }

                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                        LocalDate fechaInicio = date_fCreacion.getValue();
                        String lowerCaseFilter = fechaInicio.format(formatter);

                        // Comparar cada campo del usuario con el filtro
                        if (cuenta.getFCreacion().toLowerCase().contains(lowerCaseFilter)) {
                            return true; // Coincide con el nombre de usuario
                        }
                        return false; // No hay coincidencias
                    });
                });
            } else if (newValue.equals("Nivel")) {
                visible(newValue);
                cbox_lvl.valueProperty().addListener((observable1, oldValue1, newValue1) -> {
                    filteredData.setPredicate(cuenta -> {
                        // Si el filtro está vacío, mostrar todos los datos
                        if (newValue1 == null || newValue1.isEmpty()) {
                            return true;
                        }

                        String lowerCaseFilter = newValue1.toLowerCase();

                        // Comparar cada campo del usuario con el filtro
                        if (cuenta.getLvlCuenta().toLowerCase().contains(lowerCaseFilter)) {
                            return true; // Coincide con el nombre de usuario
                        }
                        return false; // No hay coincidencias
                    });
                });
            }

        });
    }

    public void visible(String opc){
        switch (opc){
            case "Descripcion":
                lbl_lvl.setVisible(false);
                cbox_lvl.setVisible(false);
                cbox_lvl.setValue("");
                lbl_fCreacion.setVisible(false);
                date_fCreacion.setVisible(false);
                date_fCreacion.setValue(null);
                lbl_tipo.setVisible(false);
                cbox_tipo.setVisible(false);
                cbox_tipo.setValue("");
                lbl_descripcion.setVisible(true);
                txtf_descripcion.setVisible(true);
                txtf_descripcion.setText("");
                break;

            case "Tipo":
                lbl_lvl.setVisible(false);
                cbox_lvl.setVisible(false);
                cbox_lvl.setValue("");
                lbl_fCreacion.setVisible(false);
                date_fCreacion.setVisible(false);
                date_fCreacion.setValue(null);
                lbl_tipo.setVisible(true);
                cbox_tipo.setVisible(true);
                cbox_tipo.setValue("");
                lbl_descripcion.setVisible(false);
                txtf_descripcion.setVisible(false);
                txtf_descripcion.setText("");
                    break;

            case "Fecha de creacion":
                lbl_lvl.setVisible(false);
                cbox_lvl.setVisible(false);
                cbox_lvl.setValue("");
                lbl_fCreacion.setVisible(true);
                date_fCreacion.setVisible(true);
                date_fCreacion.setValue(null);
                lbl_tipo.setVisible(false);
                cbox_tipo.setVisible(false);
                cbox_tipo.setValue("");
                lbl_descripcion.setVisible(false);
                txtf_descripcion.setVisible(false);
                txtf_descripcion.setText("");
                break;

            case "Nivel":
                lbl_lvl.setVisible(true);
                cbox_lvl.setVisible(true);
                cbox_lvl.setValue("");
                lbl_fCreacion.setVisible(false);
                date_fCreacion.setVisible(false);
                date_fCreacion.setValue(null);
                lbl_tipo.setVisible(false);
                cbox_tipo.setVisible(false);
                cbox_tipo.setValue("");
                lbl_descripcion.setVisible(false);
                txtf_descripcion.setVisible(false);
                txtf_descripcion.setText("");
                break;
        }
    }

    public void limpiar(){
        txtf_nCuenta.setText("");
        txtf_cuentaPadre.setText("");
        cbox_grupo.setValue("");
        cbox_lvl.setValue("");
        cbox_tipo.setValue("");
        date_fCreacion.setValue(null);
        txtf_descripcion.setText("");
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
                            Cuenta cuenta = new Cuenta(datos[0],datos[1],tCuenta, datos[3], cPadre, datos[5], datos[6], datos[7], Float.parseFloat(datos[8]), Float.parseFloat(datos[9]),Float.parseFloat(datos[10]));
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


    public void returnMenu() throws IOException {
        if (panelPadre != null) {
            panelPadre.cMantenimientos("/Consultas/ConsultaGen.fxml");
        }
    }
    @Override
    public void setPanelPadre(PrincipalController panelPadre) {
        this.panelPadre = panelPadre;
    }
}
