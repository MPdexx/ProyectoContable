package local.scontable.sistemacontable.Clases;

import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Cuenta {
    private final StringProperty nroCuenta;
    private final StringProperty desCuenta;
    private final StringProperty tipoCuenta;
    private final StringProperty lvlCuenta;
    private final StringProperty ctaPadre;
    private final StringProperty grpCuenta;
    private final StringProperty fCreacion;
    private final StringProperty hCreacion;
    private final StringProperty DebitoAc;
    private final StringProperty CreditoAc;
    private final FloatProperty BalanceCta;

    public Cuenta(String nroCuenta1, String desCuenta1, String tipoCuenta1, String lvlCuenta1, String ctaPadre1, String grpCuenta1, String fCreacion1, String hCreacion1, String DebitoAc1, String CreditoAc1, float BalanceCta1){
        this.nroCuenta = new SimpleStringProperty(nroCuenta1);
        this.desCuenta = new SimpleStringProperty(desCuenta1);
        this.tipoCuenta = new SimpleStringProperty(tipoCuenta1);
        this.lvlCuenta = new SimpleStringProperty(lvlCuenta1);
        this.ctaPadre = new SimpleStringProperty(ctaPadre1);
        this.grpCuenta = new SimpleStringProperty(grpCuenta1);
        this.fCreacion = new SimpleStringProperty(fCreacion1);
        this.hCreacion = new SimpleStringProperty(hCreacion1);
        this.DebitoAc = new SimpleStringProperty(DebitoAc1);
        this.CreditoAc = new SimpleStringProperty(CreditoAc1);
        this.BalanceCta = new SimpleFloatProperty(BalanceCta1);
    }

    public String getNroCuenta(){
        return nroCuenta.get();
    }
    public void setNroCuenta(String nroCuenta1){
        this.nroCuenta.set(nroCuenta1);
    }
    public StringProperty nCuentaProperty(){
        return nroCuenta;
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

    public String getTipoCuenta(){
        return tipoCuenta.get();
    }
    public void setTipoCuenta(String tipoCuenta1){
        this.tipoCuenta.set(tipoCuenta1);
    }
    public StringProperty TipoCuentaProperty(){
        return tipoCuenta;
    }

    public String getLvlCuenta(){
        return lvlCuenta.get();
    }
    public void setLvlCuenta(String lvlCuenta1){
        this.lvlCuenta.set(lvlCuenta1);
    }
    public StringProperty LvlCuentaProperty(){
        return lvlCuenta;
    }

    public String getCtaPadre(){
        return ctaPadre.get();
    }
    public void setCtaPadre(String ctaPadre1){
        this.ctaPadre.set(ctaPadre1);
    }
    public StringProperty CtaPadreProperty(){
        return ctaPadre;
    }

    public String getGrpCuenta(){
        return grpCuenta.get();
    }
    public void setGrpCuenta(String grpCuenta1){
        this.grpCuenta.set(grpCuenta1);
    }
    public StringProperty GrpCuentaProperty(){
        return grpCuenta;
    }

    public String getFCreacion(){
        return fCreacion.get();
    }
    public void setfCreacion(String fCreacion1){
        this.fCreacion.set(fCreacion1);
    }
    public StringProperty fCreacionProperty(){
        return fCreacion;
    }

    public String getHCreacion(){
        return hCreacion.get();
    }
    public void sethCreacion(String hCreacion1){
        this.hCreacion.set(hCreacion1);
    }
    public StringProperty hCreacionProperty(){
        return hCreacion;
    }

    public String getDebitoAc(){
        return DebitoAc.get();
    }
    public void setDebitoAc(String debitoAc1){
        this.DebitoAc.set(debitoAc1);
    }
    public StringProperty DebitoAcProperty(){
        return DebitoAc;
    }

    public String getCreditoAc(){
        return CreditoAc.get();
    }
    public void setCreditoAc(String CreditoAc1){
        this.CreditoAc.set(CreditoAc1);
    }
    public StringProperty CreditoAcProperty(){
        return CreditoAc;
    }

    public float getBalanceCta(){
        return BalanceCta.get();
    }
    public void setBalanceCta(float BalanceCta1){
        this.BalanceCta.set(BalanceCta1);
    }
    public FloatProperty BalanceCtaProperty(){
        return BalanceCta;
    }

}

