package local.scontable.sistemacontable.ControlConsultas;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import local.scontable.sistemacontable.Clases.CambioPanel;
import local.scontable.sistemacontable.Clases.GastosGenerales;
import local.scontable.sistemacontable.Clases.GyP;
import local.scontable.sistemacontable.PrincipalController;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class EstadoGyPController implements Initializable, CambioPanel {
    private PrincipalController panelPadre;
    @FXML
    TableView<GyP> tview_cuenta;
    @FXML
    TableColumn<GyP,String> col_nCuenta, col_descripcion, col_grupo;
    @FXML
    TableColumn<GyP, Double> col_creditoAc, col_debitoAc, col_balance;
    @FXML
    Label lbl_ganancias, lbl_perdidas, lbl_neto;
    @FXML
    ChoiceBox<String> cbox_filtro;
    @FXML
    PieChart pchart_GyP;
    ObservableList<GyP> userList = FXCollections.observableArrayList();
    private FilteredList<GyP> filteredData;
    private static String archivo = "C:\\ProjectoParcialJava\\SistemaContable\\src\\main\\resources\\Datos\\cuentas.txt";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        String[] grupo = {"Ingresos", "Gastos"};
        cbox_filtro.getItems().addAll(grupo);

        col_nCuenta.setCellValueFactory(cellData -> cellData.getValue().nCuentaProperty());
        col_descripcion.setCellValueFactory(cellData -> cellData.getValue().desCuentaProperty());
        col_grupo.setCellValueFactory(cellData -> cellData.getValue().GrupoProperty());
        col_debitoAc.setCellValueFactory(cellData -> cellData.getValue().DebitoAcProperty().asObject());
        col_creditoAc.setCellValueFactory(cellData -> cellData.getValue().CreditoAcProperty().asObject());
        col_balance.setCellValueFactory(cellData -> cellData.getValue().BalanceProperty().asObject());
        try{
            userList = FXCollections.observableArrayList();
            filteredData = new FilteredList<>(userList, p-> true);
            loadFile(archivo);
            tview_cuenta.setItems(filteredData);
        }catch (Exception ex){
            System.out.println(ex);
        }

        cbox_filtro.valueProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(gyP -> {
                // Si el filtro está vacío, mostrar todos los datos
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                // Comparar cada campo del usuario con el filtro
                if (gyP.getGrupo().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Coincide con el nombre de usuario
                }
                return false; // No hay coincidencias
            });
        });
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
                double totalG=0, totalP=0;
                while ((linea = reader.readLine()) != null) {
                    String[] datos = linea.split(";");
                    try {
                        if (datos.length == 11) {
                            String nCuenta;
                            String desCuenta;
                            Double debitoAc;
                            Double creditoAc;
                            Double balance;
                            String grupo = datos[5];
                            if (grupo.equalsIgnoreCase("gastos") || grupo.equalsIgnoreCase("ingresos")){
                                nCuenta = datos[0];
                                desCuenta = datos[1];
                                debitoAc = Double.parseDouble(datos[8]);
                                creditoAc = Double.parseDouble(datos[9]);
                                balance = Double.parseDouble(datos[10]);
                                totalG+= grupo.equalsIgnoreCase("ingresos") ? balance : 0;
                                totalP+= grupo.equalsIgnoreCase("gastos") ? balance : 0;
                                GyP gyp = new GyP(nCuenta, desCuenta, grupo,debitoAc, creditoAc, balance);
                                userList.add(gyp);
                            }
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                lbl_ganancias.setText(String.valueOf(totalG));
                lbl_perdidas.setText(String.valueOf(totalP));
                lbl_neto.setText(String.valueOf(totalG-totalP));
                reader.close();
                // Crear los datos para el PieChart
                ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                        new PieChart.Data("Ganancias", totalG),
                        new PieChart.Data("Pérdidas", totalP)
                );

                // Asignar los datos al PieChart
                pchart_GyP.setData(pieChartData);
                for (PieChart.Data data : pchart_GyP.getData()) {
                    data.nameProperty().set(data.getName() + " (" + String.format("%.2f%%", (data.getPieValue() / (totalG + totalP)) * 100) + ")");
                }
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
