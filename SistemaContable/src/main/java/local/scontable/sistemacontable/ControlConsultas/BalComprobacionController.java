package local.scontable.sistemacontable.ControlConsultas;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import local.scontable.sistemacontable.Clases.BalanzaComp;
import local.scontable.sistemacontable.Clases.CambioPanel;
import local.scontable.sistemacontable.Clases.Cuenta;
import local.scontable.sistemacontable.Clases.CuentaResumen;
import local.scontable.sistemacontable.PrincipalController;

import java.io.*;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class BalComprobacionController implements Initializable, CambioPanel {
    private PrincipalController panelPadre;
    @FXML
    TableView<BalanzaComp> tview_bal;
    @FXML
    TableColumn<BalanzaComp, String> col_nCuenta, col_descripcion;
    @FXML
    TableColumn<BalanzaComp,Double> col_debitoAc, col_creditoAc, col_saldoIni, col_movDebito, col_movCredito, col_saldoFinal;
    @FXML
    DatePicker date_fInicial, date_fFinal;
    private static String archivo = "C:\\ProjectoParcialJava\\SistemaContable\\src\\main\\resources\\Datos\\cuentas.txt";
    private static String archivoD = "C:\\ProjectoParcialJava\\SistemaContable\\src\\main\\resources\\Datos\\detalle.txt";
    private static String archivoC = "C:\\ProjectoParcialJava\\SistemaContable\\src\\main\\resources\\Datos\\cabecera.txt";
    private Map<String, Cuenta> cuentas = new HashMap<>();
    private ObservableList<BalanzaComp> balanzaList = FXCollections.observableArrayList();

    public void procesar() {

        LocalDate fechaInicio = date_fInicial.getValue();
        LocalDate fechaFinal = date_fFinal.getValue();

        if (fechaInicio.isAfter(fechaFinal)) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Rango de fechas inválido");
            alert.setContentText("La fecha inicial no puede ser mayor que la fecha final.");
            alert.showAndWait();
            return;
        }
        File archivoCabecera = new File(archivoC);
        File archivoDetalle = new File(archivoD);

        // Filtrar transacciones por rango de fechas
        Set<String> documentosFiltrados = filtrarDocumentosPorFecha(fechaInicio, fechaFinal, archivoCabecera);

        // Calcular débitos, créditos y saldos iniciales
        Map<String, double[]> resumenCuentas = calcularMovimientos(documentosFiltrados, archivoDetalle);

        // Generar la lista de la balanza
        balanzaList.clear();
        for (Map.Entry<String, double[]> entry : resumenCuentas.entrySet()) {
            String numeroCuenta = entry.getKey();
            double movimientosDebito = entry.getValue()[1];
            double movimientosCredito = entry.getValue()[2];
            String desCuenta="";
            double debitoAc=0;
            double creditoAc=0;
            String grupo="";
            try(BufferedReader reader1 = new BufferedReader(new FileReader(archivo))){
                String line;
                while ((line = reader1.readLine()) != null){
                    String[] data = line.split(";");

                    String nCuenta = data[0];
                    if (nCuenta.equals(numeroCuenta)){
                        desCuenta = data[1];
                        debitoAc = Double.parseDouble(data[8]);
                        creditoAc = Double.parseDouble(data[9]);
                        grupo = data[5].toLowerCase();
                    }
                }
            }catch (Exception ex){
                ex.printStackTrace();
            }

            balanzaList.add(new BalanzaComp(numeroCuenta, desCuenta, movimientosDebito, movimientosCredito, debitoAc, creditoAc, grupo));
        }

        tview_bal.setItems(balanzaList);

    }

    private Set<String> filtrarDocumentosPorFecha(LocalDate fechaInicio, LocalDate fechaFinal, File archivoCabecera) {
        Set<String> documentosFiltrados = new HashSet<>();
        if(!archivoCabecera.exists()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("No hay datos");
            alert.showAndWait();
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(archivoCabecera))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] datos = linea.split(";");

                String numeroDocumento = datos[0];
                LocalDate fechaDocumento = LocalDate.parse(datos[1], DateTimeFormatter.ofPattern("dd/MM/yyyy"));

                if (!fechaDocumento.isBefore(fechaInicio) && !fechaDocumento.isAfter(fechaFinal)) {
                    documentosFiltrados.add(numeroDocumento);
                }
            }
        } catch (IOException e) {
            System.out.println("Error al leer la cabecera: " + e.getMessage());
        }
        return documentosFiltrados;
    }
    private Map<String, double[]> calcularMovimientos(Set<String> documentosFiltrados, File archivoDetalle) {
        Map<String, double[]> resumenCuentas = new HashMap<>();
        if(!archivoDetalle.exists()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("No hay datos");
            alert.showAndWait();
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(archivoDetalle))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] datos = linea.split(";");

                String numeroDocumento = datos[0];
                String cuentaContable = datos[2];
                double debito = Double.parseDouble(datos[3]);
                double credito = Double.parseDouble(datos[4]);

                if (documentosFiltrados.contains(numeroDocumento)) {
                    double[] valores = resumenCuentas.getOrDefault(cuentaContable, new double[3]);
                    valores[1] += debito;  // Movimientos débito
                    valores[2] += credito; // Movimientos crédito
                    resumenCuentas.put(cuentaContable, valores);
                }
            }
        } catch (IOException e) {
            System.out.println("Error al leer detalle: " + e.getMessage());
        }
        return resumenCuentas;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        col_nCuenta.setCellValueFactory(cellData -> cellData.getValue().getNroCuentaProperty());
        col_descripcion.setCellValueFactory(cellData -> cellData.getValue().getDesCuentaProperty());
        col_debitoAc.setCellValueFactory(cellData -> cellData.getValue().getDebitoAcProperty().asObject());
        col_creditoAc.setCellValueFactory(cellData -> cellData.getValue().getCreditoAcProperty().asObject());
        col_saldoIni.setCellValueFactory(cellData -> cellData.getValue().getSaldoInicialProperty().asObject());
        col_movDebito.setCellValueFactory(cellData -> cellData.getValue().getMovimientosDebitoProperty().asObject());
        col_movCredito.setCellValueFactory(cellData -> cellData.getValue().getMovimientosCreditoProperty().asObject());
        col_saldoFinal.setCellValueFactory(cellData -> cellData.getValue().getSaldoFinalProperty().asObject());

    }

    public void cargarCuentas(String arc) {
        try (BufferedReader reader = new BufferedReader(new FileReader(arc))) {
            int cuentasCargadas = 0;
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] datos = linea.split(";");
                if (datos.length >= 11) {
                    // Buscar cuenta padre en el mapa
                    Cuenta cuenta = new Cuenta(datos[0],datos[1],datos[2], datos[3], datos[4], datos[5], datos[6], datos[7], Float.parseFloat(datos[8]), Float.parseFloat(datos[9]), Float.parseFloat(datos[10]));
                    // Añadir cuenta al mapa
                    cuentas.put(datos[0], cuenta);
                    cuentasCargadas++;
                }
            }
            System.out.println("Total de cuentas cargadas: " + cuentasCargadas);
        } catch (IOException e) {
            System.out.println("Error al cargar las cuentas: " + e.getMessage());
        }
        /*cuentas.forEach((clave, cuenta) -> {
            System.out.println("Cuenta: " + clave);
            System.out.println("Número: " + cuenta.getNroCuenta());
            System.out.println("Descripción: " + cuenta.getDesCuenta());
            System.out.println("Tipo: " + cuenta.getTipoCuenta());
            System.out.println("Padre: " + cuenta.getCtaPadre());
            System.out.println("Balance: " + cuenta.getBalanceCta());
            System.out.println("--------------------------");
        });*/
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
