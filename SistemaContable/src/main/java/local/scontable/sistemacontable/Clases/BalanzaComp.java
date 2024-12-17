package local.scontable.sistemacontable.Clases;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class BalanzaComp {
    private final StringProperty nroCuenta;
    private final StringProperty desCuenta;
    private final DoubleProperty debitoAc;
    private final DoubleProperty creditoAc;
    private final DoubleProperty saldoInicial;
    private final DoubleProperty movimientosDebito;
    private final DoubleProperty movimientosCredito;
    private final DoubleProperty saldoFinal;


    public BalanzaComp(String nroCuenta, String desCuenta,
                       double movimientosDebito, double movimientosCredito, Double debitoAc, Double creditoAc, String grupo) {
        double saldoInicial=0;
        this.nroCuenta = new SimpleStringProperty(nroCuenta);
        this.desCuenta = new SimpleStringProperty(desCuenta);
        this.debitoAc = new SimpleDoubleProperty(debitoAc);
        this.creditoAc = new SimpleDoubleProperty(creditoAc);
        switch (grupo){
            case "activo":
            case "costos":
            case "gastos":
                saldoInicial = debitoAc-creditoAc;
                break;

            case "pasivo":
            case "capital":
            case "ingresos":
                saldoInicial = creditoAc-debitoAc;
                break;
        }
        this.saldoInicial = new SimpleDoubleProperty(saldoInicial);
        this.movimientosDebito = new SimpleDoubleProperty(movimientosDebito);
        this.movimientosCredito = new SimpleDoubleProperty(movimientosCredito);
        this.saldoFinal = new SimpleDoubleProperty(saldoInicial + movimientosDebito - movimientosCredito);
    }

    // Getters
    public String getNroCuenta() {
        return nroCuenta.get();
    }
    public StringProperty getNroCuentaProperty(){
        return nroCuenta;
    }

    public String getDesCuenta() {
        return desCuenta.get();
    }
    public StringProperty getDesCuentaProperty(){
        return desCuenta;
    }

    public double getDebitoAc(){
        return debitoAc.get();
    }
    public DoubleProperty getDebitoAcProperty(){
        return debitoAc;
    }

    public double getCreditoAc(){
        return creditoAc.get();
    }
    public DoubleProperty getCreditoAcProperty(){
        return creditoAc;
    }

    public double getSaldoInicial() {
        return saldoInicial.get();
    }
    public DoubleProperty getSaldoInicialProperty(){
        return saldoInicial;
    }

    public double getMovimientosDebito() {
        return movimientosDebito.get();
    }
    public DoubleProperty getMovimientosDebitoProperty(){
        return movimientosDebito;
    }

    public double getMovimientosCredito() {
        return movimientosCredito.get();
    }
    public DoubleProperty getMovimientosCreditoProperty(){
        return movimientosCredito;
    }

    public double getSaldoFinal() {
        return saldoFinal.get();
    }
    public DoubleProperty getSaldoFinalProperty(){
        return saldoInicial;
    }
}

