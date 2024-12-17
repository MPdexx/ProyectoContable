package local.scontable.sistemacontable.Clases;
import javafx.beans.property.*;

public class Movimiento {

    // Propiedades del archivo cabecera
    private final StringProperty numeroDocumento;    // Campo 1: Número de Documento
    private final StringProperty fechaDocumento;     // Campo 2: Fecha Documento
    private final StringProperty tipoDocumento;      // Campo 3: Tipo Documento
    private final StringProperty descripcionDocumento; // Campo 4: Descripción Documento
    private final StringProperty hechoPor;           // Campo 5: Hecho por
    private final DoubleProperty montoTransaccion;   // Campo 6: Monto Transacción
    private final StringProperty fechaActualizacion; // Campo 7: Fecha de Actualización
    private final StringProperty statusActualizacion; // Campo 8: Status de Actualización

    // Constructor
    public Movimiento(String numeroDocumento, String fechaDocumento, String tipoDocumento,
                      String descripcionDocumento, String hechoPor, double montoTransaccion,
                      String fechaActualizacion, String statusActualizacion) {
        this.numeroDocumento = new SimpleStringProperty(numeroDocumento);
        this.fechaDocumento = new SimpleStringProperty(fechaDocumento);
        this.tipoDocumento = new SimpleStringProperty(tipoDocumento);
        this.descripcionDocumento = new SimpleStringProperty(descripcionDocumento);
        this.hechoPor = new SimpleStringProperty(hechoPor);
        this.montoTransaccion = new SimpleDoubleProperty(montoTransaccion);
        this.fechaActualizacion = new SimpleStringProperty(fechaActualizacion);
        this.statusActualizacion = new SimpleStringProperty(statusActualizacion);
    }

    // Getters
    public String getNumeroDocumento() {
        return numeroDocumento.get();
    }

    public String getFechaDocumento() {
        return fechaDocumento.get();
    }

    public String getTipoDocumento() {
        return tipoDocumento.get();
    }

    public String getDescripcionDocumento() {
        return descripcionDocumento.get();
    }

    public String getHechoPor() {
        return hechoPor.get();
    }

    public double getMontoTransaccion() {
        return montoTransaccion.get();
    }

    public String getFechaActualizacion() {
        return fechaActualizacion.get();
    }

    public String isStatusActualizacion() {
        return statusActualizacion.get();
    }

    // Setters
    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento.set(numeroDocumento);
    }

    public void setFechaDocumento(String fechaDocumento) {
        this.fechaDocumento.set(fechaDocumento);
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento.set(tipoDocumento);
    }

    public void setDescripcionDocumento(String descripcionDocumento) {
        this.descripcionDocumento.set(descripcionDocumento);
    }

    public void setHechoPor(String hechoPor) {
        this.hechoPor.set(hechoPor);
    }

    public void setMontoTransaccion(double montoTransaccion) {
        this.montoTransaccion.set(montoTransaccion);
    }

    public void setFechaActualizacion(String fechaActualizacion) {
        this.fechaActualizacion.set(fechaActualizacion);
    }

    public void setStatusActualizacion(String statusActualizacion) {
        this.statusActualizacion.set(statusActualizacion);
    }

    // Property Methods (para integrarse con TableView o bindings)
    public StringProperty numeroDocumentoProperty() {
        return numeroDocumento;
    }

    public StringProperty fechaDocumentoProperty() {
        return fechaDocumento;
    }

    public StringProperty tipoDocumentoProperty() {
        return tipoDocumento;
    }

    public StringProperty descripcionDocumentoProperty() {
        return descripcionDocumento;
    }

    public StringProperty hechoPorProperty() {
        return hechoPor;
    }

    public DoubleProperty montoTransaccionProperty() {
        return montoTransaccion;
    }

    public StringProperty fechaActualizacionProperty() {
        return fechaActualizacion;
    }

    public StringProperty statusActualizacionProperty() {
        return statusActualizacion;
    }
}

