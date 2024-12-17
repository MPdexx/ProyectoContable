package local.scontable.sistemacontable.Clases;

public class CuentaResumen {
    private final String numeroCuenta;
    private double debitos;
    private double creditos;

    public CuentaResumen(String numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
        this.debitos = 0;
        this.creditos = 0;
    }

    public void addDebito(double monto) {
        this.debitos += monto;
    }

    public void addCredito(double monto) {
        this.creditos += monto;
    }

    public String getNumeroCuenta() {
        return numeroCuenta;
    }

    public double getDebitos() {
        return debitos;
    }

    public double getCreditos() {
        return creditos;
    }
}

