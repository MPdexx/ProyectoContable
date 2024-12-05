package local.scontable.sistemacontable.Clases;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Detalle {
    private final StringProperty nDocumento;
    private final StringProperty secDocumento;
    private final StringProperty cuentaContable;
    private final StringProperty comentario;
    private final StringProperty vDebito;
    private final StringProperty vCredito;


    // Constructor
    public Detalle(String nDocumento1, String secDocumento1, String cuentaContable1,
                   String  vDebito1, String  vCredito1, String comentario1) {
        this.nDocumento = new SimpleStringProperty(nDocumento1);
        this.secDocumento = new SimpleStringProperty(secDocumento1);
        this.cuentaContable = new SimpleStringProperty(cuentaContable1);
        this.comentario = new SimpleStringProperty(comentario1);
        this.vDebito = new SimpleStringProperty(vDebito1);
        this.vCredito = new SimpleStringProperty(vCredito1);

    }

    // Getters
    public String getNDocumento() {
        return nDocumento.get();
    }
    public StringProperty getnDocumentoProperty(){
        return nDocumento;
    }

    public String getSecDocumento() {
        return secDocumento.get();
    }
    public StringProperty getSecDocumentoProperty(){
        return secDocumento;
    }

    public String getCuentaContable() {
        return cuentaContable.get();
    }
    public StringProperty getCuentaContableProperty(){
        return cuentaContable;
    }

    public String getComentario() {
        return comentario.get();
    }
    public StringProperty getComentarioProperty(){
        return  comentario;
    }

    public String getVDebito() {
        return vDebito.get();
    }
    public StringProperty getVDebitoProperty(){
        return vDebito;
    }

    public String getVCredito() {
        return vCredito.get();
    }
    public StringProperty getVCreditoProperty(){
        return vCredito;
    }

}