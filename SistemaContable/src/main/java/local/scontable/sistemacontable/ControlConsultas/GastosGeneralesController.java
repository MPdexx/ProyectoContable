package local.scontable.sistemacontable.ControlConsultas;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import local.scontable.sistemacontable.Clases.CambioPanel;
import local.scontable.sistemacontable.Clases.Cuenta;
import local.scontable.sistemacontable.Clases.GastosGenerales;
import local.scontable.sistemacontable.PrincipalController;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static java.lang.String.valueOf;

public class GastosGeneralesController implements Initializable, CambioPanel {
    private PrincipalController panelPadre;
    @FXML
    TableView<GastosGenerales> tview_gastos;
    @FXML
    TableColumn<GastosGenerales,String> col_nCuenta, col_descripcion;
    @FXML
    TableColumn<GastosGenerales, Double> col_creditoAc, col_debitoAc, col_balance;
    @FXML
    Label lbl_total;
    ObservableList<GastosGenerales> userList = FXCollections.observableArrayList();
    private static String archivo = "C:\\ProjectoParcialJava\\SistemaContable\\src\\main\\resources\\Datos\\cuentas.txt";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        col_nCuenta.setCellValueFactory(cellData -> cellData.getValue().nCuentaProperty());
        col_descripcion.setCellValueFactory(cellData -> cellData.getValue().desCuentaProperty());
        col_debitoAc.setCellValueFactory(cellData -> cellData.getValue().DebitoAcProperty().asObject());
        col_creditoAc.setCellValueFactory(cellData -> cellData.getValue().CreditoAcProperty().asObject());
        col_balance.setCellValueFactory(cellData -> cellData.getValue().BalanceProperty().asObject());
        try{
            loadFile(archivo);
            tview_gastos.setItems(userList);
        }catch (Exception ex){
            System.out.println(ex);
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
                double total=0;
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
                            if (grupo.equalsIgnoreCase("gastos")){
                                nCuenta = datos[0];
                                desCuenta = datos[1];
                                debitoAc = Double.parseDouble(datos[8]);
                                creditoAc = Double.parseDouble(datos[9]);
                                balance = Double.parseDouble(datos[10]);
                                total+= balance;
                                GastosGenerales gast = new GastosGenerales(nCuenta, desCuenta, debitoAc, creditoAc, balance);
                                userList.add(gast);
                            }
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                lbl_total.setText("Total de gastos: "+ total);
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
