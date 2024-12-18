package local.scontable.sistemacontable.ControlConsultas;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import local.scontable.sistemacontable.Clases.CambioPanel;
import local.scontable.sistemacontable.Clases.Cuenta;
import local.scontable.sistemacontable.Clases.Movimiento;
import local.scontable.sistemacontable.PrincipalController;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class PorTransController implements Initializable, CambioPanel {
    private PrincipalController panelPadre;
    @FXML
    TableView<Movimiento> tview_trans;
    @FXML
    TableColumn<Movimiento, String> col_nDocumento, col_fecha, col_tipo, col_descripcion, col_hechoPor, col_fActualizacion, col_estadoActualizacion;
    @FXML
    TableColumn<Movimiento, Double> col_monto;
    @FXML
    DatePicker date_fCreacion, date_fInicio, date_fFinal;
    @FXML
    TextField txtf_nDocumento;
    @FXML
    ChoiceBox<String> cbox_filtro, cbox_tipo;
    @FXML
    Label lbl_fCreacion, lbl_fInicio, lbl_fFinal, lbl_nDocumento, lbl_tipoDocumento;
    @FXML
    Button btn_return, btn_clean;
    ObservableList<Movimiento> docList = FXCollections.observableArrayList();
    private FilteredList<Movimiento> filteredData;
    private static String archivoC = "C:\\ProjectoParcialJava\\SistemaContable\\src\\main\\resources\\Datos\\cabecera.txt";


    public void returnMenu() throws IOException {
        if (panelPadre != null) {
            panelPadre.cMantenimientos("/Consultas/ConsultaGen.fxml");
        }
    }
    public void limpiar(){

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        String[] filtro = {"Fecha", "Rango de fechas", "Numero documento", "Tipo documento"};
        String[] docType = {"Factura", "Notas de débito", "Notas de crédito", "Doc. Interno"};
        cbox_filtro.getItems().addAll(filtro);
        date_fCreacion.setVisible(false);
        lbl_fCreacion.setVisible(false);
        date_fInicio.setVisible(false);
        date_fFinal.setVisible(false);
        lbl_fInicio.setVisible(false);
        lbl_fFinal.setVisible(false);
        lbl_tipoDocumento.setVisible(false);
        cbox_tipo.getItems().addAll(docType);
        cbox_tipo.setVisible(false);
        txtf_nDocumento.setVisible(false);
        lbl_nDocumento.setVisible(false);

        col_nDocumento.setCellValueFactory(cellData -> cellData.getValue().numeroDocumentoProperty());
        col_fecha.setCellValueFactory(cellData -> cellData.getValue().fechaDocumentoProperty());
        col_tipo.setCellValueFactory(cellData -> cellData.getValue().tipoDocumentoProperty());
        col_descripcion.setCellValueFactory(cellData -> cellData.getValue().descripcionDocumentoProperty());
        col_hechoPor.setCellValueFactory(cellData -> cellData.getValue().hechoPorProperty());
        col_monto.setCellValueFactory(cellData -> cellData.getValue().montoTransaccionProperty().asObject());
        col_fActualizacion.setCellValueFactory(cellData -> cellData.getValue().fechaActualizacionProperty());
        col_estadoActualizacion.setCellValueFactory(cellData -> cellData.getValue().statusActualizacionProperty());
        try{
            docList = FXCollections.observableArrayList();
            filteredData = new FilteredList<>(docList, p-> true);
            loadFile(archivoC);
            tview_trans.setItems(filteredData);
        }catch (Exception ex){
            ex.printStackTrace();
        }

        // Vincular el filtro
        cbox_filtro.valueProperty().addListener((observable, oldValue, newValue) -> {

            if(newValue.equals("Fecha") || String.valueOf(cbox_filtro.getValue()).equals("Fecha")){
                visible(newValue);
                date_fCreacion.valueProperty().addListener((observable1, oldValue1, newValue1) -> {
                    filteredData.setPredicate(movimiento -> {
                        // Si el filtro está vacío, mostrar todos los datos
                        if (newValue1 == null) {
                            return true;
                        }

                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                        LocalDate fechaInicio = newValue1;
                        String lowerCaseFilter = fechaInicio.format(formatter);

                        // Comparar cada campo del usuario con el filtro
                        return movimiento.getFechaDocumento().toLowerCase().contains(lowerCaseFilter);
                    });
                });
            } else if (newValue.equals("Rango de fechas")) {
                visible(newValue);

                // Listener para la fecha inicial
                date_fInicio.valueProperty().addListener((observable1, oldValue1, newValue1) -> {
                    aplicarFiltroRangoFechas();
                });

                // Listener para la fecha final
                date_fFinal.valueProperty().addListener((observable1, oldValue1, newValue1) -> {
                    aplicarFiltroRangoFechas();
                });

            } else if (newValue.equals("Numero documento")) {
                visible(newValue);
                txtf_nDocumento.textProperty().addListener((observable1, oldValue1, newValue1) -> {
                    filteredData.setPredicate(movimiento -> {
                        // Si el filtro está vacío, mostrar todos los datos
                        if (newValue1 == null || newValue1.isEmpty()) {
                            return true;
                        }

                        String lowerCaseFilter = newValue1.toLowerCase();

                        return movimiento.getNumeroDocumento().toLowerCase().contains(lowerCaseFilter);
                    });
                });
            } else if (newValue.equals("Tipo documento")) {
                visible(newValue);
                cbox_tipo.valueProperty().addListener((observable1, oldValue1, newValue1) -> {
                    filteredData.setPredicate(movimiento -> {
                        // Si el filtro está vacío, mostrar todos los datos
                        if (newValue1 == null || newValue1.isEmpty()) {
                            return true;
                        }

                        String lowerCaseFilter = newValue1.toLowerCase();

                        // Comparar cada campo del usuario con el filtro
                        if (movimiento.getTipoDocumento().toLowerCase().contains(lowerCaseFilter)) {
                            return true; // Coincide con el nombre de usuario
                        }
                        return false; // No hay coincidencias
                    });
                });
            }

        });

    }

    // Metodo para aplicar el filtro por rango de fechas
    private void aplicarFiltroRangoFechas() {
        LocalDate fechaInicio = date_fInicio.getValue();
        LocalDate fechaFinal = date_fFinal.getValue();

        // Validar que ambas fechas estén seleccionadas
        if (fechaInicio == null || fechaFinal == null) {
            filteredData.setPredicate(null); // Mostrar todos los datos
            return;
        }

        // Validar que la fecha inicial no sea mayor que la fecha final
        if (fechaInicio.isAfter(fechaFinal)) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Rango de fechas inválido");
            alert.setContentText("La fecha inicial no puede ser mayor que la fecha final.");
            alert.showAndWait();
            filteredData.setPredicate(null); // Limpiar el filtro
            return;
        }

        // Aplicar el filtro por rango de fechas
        filteredData.setPredicate(movimiento -> {
            LocalDate fechaMovimiento = LocalDate.parse(movimiento.getFechaDocumento(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            return (fechaMovimiento.isEqual(fechaInicio) || fechaMovimiento.isAfter(fechaInicio))
                    && (fechaMovimiento.isEqual(fechaFinal) || fechaMovimiento.isBefore(fechaFinal));
        });
    }

    public void visible(String opc){
        switch (opc){
            case "Fecha":
                lbl_fCreacion.setVisible(true);
                date_fCreacion.setVisible(true);
                date_fCreacion.setValue(null);
                date_fInicio.setVisible(false);
                date_fFinal.setValue(null);
                date_fFinal.setVisible(false);
                lbl_fInicio.setVisible(false);
                date_fInicio.setValue(null);
                lbl_fFinal.setVisible(false);
                cbox_tipo.setVisible(false);
                cbox_tipo.setValue("");
                lbl_tipoDocumento.setVisible(false);
                txtf_nDocumento.setVisible(false);
                txtf_nDocumento.setText("");
                lbl_nDocumento.setVisible(false);
                break;

            case "Rango de fechas":
                lbl_fCreacion.setVisible(false);
                date_fCreacion.setVisible(false);
                date_fCreacion.setValue(null);
                date_fInicio.setVisible(true);
                date_fFinal.setValue(null);
                date_fFinal.setVisible(true);
                lbl_fInicio.setVisible(true);
                date_fInicio.setValue(null);
                lbl_fFinal.setVisible(true);
                cbox_tipo.setVisible(false);
                cbox_tipo.setValue("");
                lbl_tipoDocumento.setVisible(false);
                txtf_nDocumento.setVisible(false);
                txtf_nDocumento.setText("");
                lbl_nDocumento.setVisible(false);
                break;


            case "Numero documento":
                lbl_fCreacion.setVisible(false);
                date_fCreacion.setVisible(false);
                date_fCreacion.setValue(null);
                date_fInicio.setVisible(false);
                date_fFinal.setValue(null);
                date_fFinal.setVisible(false);
                lbl_fInicio.setVisible(false);
                date_fInicio.setValue(null);
                lbl_fFinal.setVisible(false);
                cbox_tipo.setVisible(false);
                cbox_tipo.setValue("");
                lbl_tipoDocumento.setVisible(false);
                txtf_nDocumento.setVisible(true);
                txtf_nDocumento.setText("");
                lbl_nDocumento.setVisible(true);
                break;


            case "Tipo documento":
                lbl_fCreacion.setVisible(false);
                date_fCreacion.setVisible(false);
                date_fCreacion.setValue(null);
                date_fInicio.setVisible(false);
                date_fFinal.setValue(null);
                date_fFinal.setVisible(false);
                lbl_fInicio.setVisible(false);
                date_fInicio.setValue(null);
                lbl_fFinal.setVisible(false);
                cbox_tipo.setVisible(true);
                cbox_tipo.setValue("");
                lbl_tipoDocumento.setVisible(true);
                txtf_nDocumento.setVisible(false);
                txtf_nDocumento.setText("");
                lbl_nDocumento.setVisible(false);
                break;

        }
    }

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
                        if (datos.length == 8) {
                            String eActualizacion;// Validar formato correcto
                            System.out.println(datos[6]);
                            String fActualizacion = "";
                            if (datos[7].equals("true")) {
                                eActualizacion = "Actualizado";
                            } else {
                                eActualizacion = "Sin actualizar";
                            }
                            Movimiento movimiento = new Movimiento(datos[0],datos[1], datos[2], datos[3], datos[4], Double.parseDouble(datos[5]), datos[6], eActualizacion);
                            docList.add(movimiento);

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

    @Override
    public void setPanelPadre(PrincipalController panelPadre) {
        this.panelPadre = panelPadre;
    }
}
