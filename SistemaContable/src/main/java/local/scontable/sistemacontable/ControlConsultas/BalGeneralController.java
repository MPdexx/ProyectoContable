package local.scontable.sistemacontable.ControlConsultas;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import local.scontable.sistemacontable.Clases.BalanzaGeneral;
import local.scontable.sistemacontable.Clases.CambioPanel;
import local.scontable.sistemacontable.Clases.Cuenta;
import local.scontable.sistemacontable.PrincipalController;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class BalGeneralController implements Initializable, CambioPanel {
    private PrincipalController panelPadre;
    @FXML
    TableView<BalanzaGeneral> tview_bal;
    @FXML
    TableColumn<BalanzaGeneral,String> col_grupo;
    @FXML
    TableColumn<BalanzaGeneral,Double> col_debitoT, col_creditoT,col_total;
    ObservableList<BalanzaGeneral> balanceGeneralList = FXCollections.observableArrayList();
    private static String archivo = "C:\\ProjectoParcialJava\\SistemaContable\\src\\main\\resources\\Datos\\cuentas.txt";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Configurar columnas
        col_grupo.setCellValueFactory(cellData -> cellData.getValue().grupoCuentaProperty());
        col_debitoT.setCellValueFactory(cellData -> cellData.getValue().totalDebitosProperty().asObject());
        col_creditoT.setCellValueFactory(cellData -> cellData.getValue().totalCreditosProperty().asObject());
        col_total.setCellValueFactory(cellData -> cellData.getValue().balanceProperty().asObject());

        // Vincular la lista con el TableView
        tview_bal.setItems(balanceGeneralList);


        try {
            File file = new File(archivo);
            loadFile(file);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void loadFile(File archivo) throws Exception {
        double totalDebitosActivos = 0, totalCreditosActivos = 0;
        double totalDebitosPasivos = 0, totalCreditosPasivos = 0;
        double totalDebitosCapital = 0, totalCreditosCapital = 0;
        if (!archivo.exists()){
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] datos = linea.split(";");
                if (datos.length < 6) continue;

                String grupoCuenta = datos[5]; // Supongamos que el grupo está en la columna 6 (índice 5)
                double debito = Double.parseDouble(datos[8]); // Débito
                double credito = Double.parseDouble(datos[9]); // Crédito

                // Sumar los débitos y créditos según el grupo
                switch (grupoCuenta.toLowerCase()) {
                    case "activo":
                        totalDebitosActivos += debito;
                        totalCreditosActivos += credito;
                        break;
                    case "pasivo":
                        totalDebitosPasivos += debito;
                        totalCreditosPasivos += credito;
                        break;
                    case "capital":
                        totalDebitosCapital += debito;
                        totalCreditosCapital += credito;
                        break;
                }
            }
        } catch (IOException e) {
            System.out.println("Error al leer el archivo: " + e.getMessage());
        }

        // Limpiar la lista del TableView
        balanceGeneralList.clear();

        // Agregar las filas por grupo
        balanceGeneralList.add(new BalanzaGeneral("Activo", totalDebitosActivos, totalCreditosActivos));
        balanceGeneralList.add(new BalanzaGeneral("Pasivo", totalDebitosPasivos, totalCreditosPasivos));
        balanceGeneralList.add(new BalanzaGeneral("Capital", totalDebitosCapital, totalCreditosCapital));
    }

    public void returnMenu() throws IOException {
        if (panelPadre != null) {
            panelPadre.cMantenimientos("/Consultas/BalanzaSelect.fxml");
        }
    }

    @Override
    public void setPanelPadre(PrincipalController panelPadre) {
        this.panelPadre = panelPadre;
    }
}
