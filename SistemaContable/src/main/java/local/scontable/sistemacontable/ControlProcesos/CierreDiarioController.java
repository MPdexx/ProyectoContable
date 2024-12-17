package local.scontable.sistemacontable.ControlProcesos;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import local.scontable.sistemacontable.Clases.Cuenta;

import java.io.*;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class CierreDiarioController implements Initializable {
    @FXML
    DatePicker date_inicio, date_final;
    @FXML
    Button btn_save;
    private int cont;
    private static String archivo = "C:\\ProjectoParcialJava\\SistemaContable\\src\\main\\resources\\Datos\\cuentas.txt";
    private static String archivo1 = "C:\\ProjectoParcialJava\\SistemaContable\\src\\main\\resources\\Datos\\cuentas1.txt";
    private static String archivoC = "C:\\ProjectoParcialJava\\SistemaContable\\src\\main\\resources\\Datos\\cabecera.txt";
    private static String archivoC1 = "C:\\ProjectoParcialJava\\SistemaContable\\src\\main\\resources\\Datos\\cabecera1.txt";
    private static String archivoD = "C:\\ProjectoParcialJava\\SistemaContable\\src\\main\\resources\\Datos\\detalle.txt";
    private Map<String, Cuenta> cuentas = new HashMap<>();
    //private boolean isSaving = false; // Bandera para evitar múltiples guardados

    public void procesar(){
        if(cont>=1){
            cargarCuentas(archivo);
        }
        // Leer las fechas seleccionadas
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate fechaInicio = date_inicio.getValue();
        LocalDate fechaFinal = date_final.getValue();

        // Validar si la fecha inicial es mayor a la fecha final
        if (fechaInicio.isAfter(fechaFinal)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error de fechas");
            alert.setContentText("La fecha inicial no puede ser mayor que la fecha final.");
            alert.showAndWait();
            return;
        }

        if (fechaInicio == null || fechaFinal == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Fechas inválidas");
            alert.setContentText("Debe seleccionar ambas fechas para procesar el cierre diario.");
            alert.showAndWait();
            return;
        }

        // Convertir fechas a un formato legible si es necesario
        String fechaInicioStr = fechaInicio.format(formatter);
        String fechaFinalStr = fechaFinal.format(formatter);

        // Procesar transacciones en el rango de fechas
        procesarTransaccionesPorFechas(fechaInicioStr, fechaFinalStr);

    }

    private void procesarTransaccionesPorFechas(String fechaInicio, String fechaFinal) {
        File archivoCabecera = new File(archivoC);
        File archivoTemporal = new File(archivoC1);

        try (BufferedReader readerCabecera = new BufferedReader(new FileReader(archivoCabecera));
             BufferedWriter writerCabecera = new BufferedWriter(new FileWriter(archivoTemporal))) {

            String linea;
            LocalDate fechaHoy = LocalDate.now(); // Fecha de actualización actual
            String fechaHoyStr = fechaHoy.toString();

            while ((linea = readerCabecera.readLine()) != null) {
                String[] datosCabecera = linea.split(";");

                if (datosCabecera.length < 8) { // Verifica que la línea tenga todas las columnas
                    System.out.println("Línea inválida en cabecera: " + linea);
                    writerCabecera.write(linea);
                    writerCabecera.newLine();
                    continue;
                }

                String fechaTransaccion = datosCabecera[1]; // Campo 1: Fecha del documento
                String numeroDocumento = datosCabecera[0]; // Campo 0: Número de documento
                boolean statusActualizacion = Boolean.parseBoolean(datosCabecera[7]); // Campo 7: Status de actualización

                // Verificar si la transacción está en el rango de fechas
                if (fechaTransaccion.compareTo(fechaInicio) >= 0 && fechaTransaccion.compareTo(fechaFinal) <= 0 && !statusActualizacion) {
                    System.out.println("Procesando transacción: " + numeroDocumento);

                    // Procesar el detalle relacionado
                    procesarDetallePorDocumento(new File(archivoD), numeroDocumento);

                    // Actualizar columnas de la cabecera
                    datosCabecera[6] = fechaHoyStr; // Columna 6: Fecha de actualización
                    datosCabecera[7] = "true";     // Columna 7: Status de actualización
                    cont++;
                }

                // Reescribir la línea actualizada en el archivo temporal
                writerCabecera.write(String.join(";", datosCabecera));
                writerCabecera.newLine();
            }
            if (cont==0){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Aviso");
                alert.setContentText("Todas las transacciones en el rango de fecha\nhan sido actualizadas");
                alert.showAndWait();
                return;
            }
            cuentas.forEach((clave, cuenta) -> {
                cuenta.setDebitoAc(0);
                cuenta.setCreditoAc(0);
                cuenta.setBalanceCta(0);
            });
        } catch (IOException e) {
            System.out.println("Error al procesar la tabla cabecera: " + e.getMessage());
        }

        // Reemplazar el archivo original con el temporal
        if (archivoCabecera.delete()) {
            if (!archivoTemporal.renameTo(archivoCabecera)) {
                System.out.println("Error al renombrar el archivo temporal.");
            }
        } else {
            System.out.println("Error al eliminar el archivo original.");
        }
    }

    private void procesarDetallePorDocumento(File archivoDetalle, String numeroDocumento) {
        try (BufferedReader readerDetalle = new BufferedReader(new FileReader(archivoDetalle))) {
            String linea;

            while ((linea = readerDetalle.readLine()) != null) {
                String[] datosDetalle = linea.split(";");
                String nDoc = datosDetalle[0]; // Campo 0 es el número de documento
                String cuentaContable = datosDetalle[2]; // Campo 2 es la cuenta contable
                float debito = Float.parseFloat(datosDetalle[3]); // Campo 3 indica débito
                float credito = Float.parseFloat(datosDetalle[4]); //Campo 4 indica credito

                System.out.println(cuentaContable);
                if (nDoc.equals(numeroDocumento)) {
                    // Actualizar el balance de la cuenta contable
                    actualizarBalances(cuentaContable, debito, credito);
                }
            }

        } catch (IOException e) {
            System.out.println("Error al leer la tabla detalle: " + e.getMessage());
        }
    }

    private void actualizarBalances(String numeroCuenta, float debito, float credito) throws IOException {
        Cuenta cuenta = cuentas.get(numeroCuenta);

        while (cuenta != null) {
            float balanceActual = cuenta.getBalanceCta();

            // Determinar el origen de la cuenta
            String origen = cuenta.getGrpCuenta().toLowerCase(); // Activo, pasivo, capital, etc.

            // Actualizar balance según el origen
            switch (origen) {
                case "activo":
                case "costos":
                case "gastos":
                    if (debito > 0) {
                        balanceActual += debito; // Suma para débito
                    } else  {
                        balanceActual -= credito; // Resta para crédito
                    }
                    break;

                case "pasivo":
                case "capital":
                case "ingresos":
                    if (debito > 0) {
                        balanceActual -= debito; // Resta para débito
                    } else  {
                        balanceActual += credito; // Suma para crédito
                    }
                    break;
            }

            // Actualizar acumulados
            if (debito > 0) {
                cuenta.setDebitoAc(cuenta.getDebitoAc() + debito);
            } else  {
                cuenta.setCreditoAc(cuenta.getCreditoAc() + credito);
            }

            // Guardar el balance actualizado
            cuenta.setBalanceCta(balanceActual);

            // Propagar a la cuenta padre
            cuenta = cuentas.get(cuenta.getCtaPadre());

        }
        guardarCuentas(archivo,archivo1);
    }

    private void guardarCuentas(String archivo, String archivo1) throws IOException {
        File original = new File(archivo);
        File temporal = new File(archivo1);
       /* if (isSaving) {
            System.out.println("Guardar cuentas ya está en proceso. Ignorando esta ejecución.");
            return; // Salir si ya se está ejecutando
        }

        isSaving = true;*/

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(temporal))) {
            for (Cuenta cuenta : cuentas.values()) {
                // Validar datos de la cuenta antes de escribir
                if (cuenta.getNroCuenta() == null || cuenta.getDesCuenta() == null) {
                    System.out.println("Datos inválidos encontrados. Saltando cuenta: " + cuenta.getNroCuenta());
                    continue; // Saltar cuentas con datos inválidos
                }

                String linea = String.format("%s;%s;%s;%s;%s;%s;%s;%s;%.2f;%.2f;%.2f",
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

    public void returnMenu(){

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btn_save.setDisable(true);
        cargarCuentas(archivo);
        BooleanBinding areAllFilled = Bindings.createBooleanBinding(() ->
                date_inicio.getValue() != null &&
                        date_final.getValue() != null
                ,date_inicio.valueProperty()
                ,date_final.valueProperty()

        );
        btn_save.disableProperty().bind(areAllFilled.not());
    }
}
