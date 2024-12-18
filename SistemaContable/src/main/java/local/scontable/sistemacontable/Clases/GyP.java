package local.scontable.sistemacontable.Clases;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class GyP {
    private final StringProperty nCuenta;
    private final StringProperty desCuenta;
    private final StringProperty grupo;
    private final DoubleProperty debitoAc;
    private final DoubleProperty creditoAc;
    private final DoubleProperty balance;

    public GyP(String nCuenta, String desCuenta, String grupo,double debitoAc, double creditoAc, double balance) {
        this.nCuenta = new SimpleStringProperty(nCuenta);
        this.desCuenta = new SimpleStringProperty(desCuenta);
        this.grupo = new SimpleStringProperty(grupo);
        this.debitoAc = new SimpleDoubleProperty(debitoAc);
        this.creditoAc = new SimpleDoubleProperty(creditoAc);
        this.balance = new SimpleDoubleProperty(balance);
    }

    // Getters
    public String getNroCuenta(){
        return nCuenta.get();
    }
    public void setNroCuenta(String nroCuenta1){
        this.nCuenta.set(nroCuenta1);
    }
    public StringProperty nCuentaProperty(){
        return nCuenta;
    }

    public String getDesCuenta(){
        return desCuenta.get();
    }
    public void setDesCuenta(String desCuenta1){
        this.desCuenta.set(desCuenta1);
    }
    public StringProperty desCuentaProperty(){
        return desCuenta;
    }

    public String getGrupo(){
        return grupo.get();
    }
    public void setGrupo(String desCuenta1){
        this.grupo.set(desCuenta1);
    }
    public StringProperty GrupoProperty(){
        return grupo;
    }

    public double getDebitoAc(){
        return debitoAc.get();
    }
    public void setDebitoAc(double debitoAc1){
        this.debitoAc.set(debitoAc1);
    }
    public DoubleProperty DebitoAcProperty(){
        return debitoAc;
    }

    public double getCreditoAc(){
        return creditoAc.get();
    }
    public void setCreditoAc(double CreditoAc1){
        this.creditoAc.set(CreditoAc1);
    }
    public DoubleProperty CreditoAcProperty(){
        return creditoAc;
    }

    public double getBalance(){
        return balance.get();
    }
    public void setBalance(double BalanceCta1){
        this.balance.set(BalanceCta1);
    }
    public DoubleProperty BalanceProperty(){
        return balance;
    }

}
