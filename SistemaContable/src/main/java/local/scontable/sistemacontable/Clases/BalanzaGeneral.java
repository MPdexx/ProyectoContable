package local.scontable.sistemacontable.Clases;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class BalanzaGeneral {
    private final StringProperty grupoCuenta;
    private final DoubleProperty totalDebitos;
    private final DoubleProperty totalCreditos;
    private final DoubleProperty balance;

    public BalanzaGeneral(String grupoCuenta, double totalDebitos, double totalCreditos) {
        this.grupoCuenta = new SimpleStringProperty(grupoCuenta);
        this.totalDebitos = new SimpleDoubleProperty(totalDebitos);
        this.totalCreditos = new SimpleDoubleProperty(totalCreditos);
        if (grupoCuenta.equalsIgnoreCase("activo")){
            // Balance = Total Débitos - Total Créditos
            this.balance = new SimpleDoubleProperty(totalDebitos - totalCreditos);
        }
        else{
            // Balance = Total Débitos - Total Créditos
            this.balance = new SimpleDoubleProperty(totalCreditos - totalDebitos);
        }

    }

    // Getters
    public String getGrupoCuenta() { return grupoCuenta.get(); }
    public double getTotalDebitos() { return totalDebitos.get(); }
    public double getTotalCreditos() { return totalCreditos.get(); }
    public double getBalance() { return balance.get(); }

    // Property Methods
    public StringProperty grupoCuentaProperty() { return grupoCuenta; }
    public DoubleProperty totalDebitosProperty() { return totalDebitos; }
    public DoubleProperty totalCreditosProperty() { return totalCreditos; }
    public DoubleProperty balanceProperty() { return balance; }
}
