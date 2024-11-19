package local.scontable.sistemacontable.Clases;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Usuario {
    private final StringProperty nUser;
    private final StringProperty nUserReal;
    private final StringProperty lnUser;
    private final StringProperty pass;
    private final StringProperty lvlUser;
    private final StringProperty email;

    public Usuario(String nUser, String nUserReal, String lnUser, String pass, String lvlUser, String email) {
        this.nUser = new SimpleStringProperty(nUser);
        this.nUserReal = new SimpleStringProperty(nUserReal);
        this.lnUser = new SimpleStringProperty(lnUser);
        this.pass = new SimpleStringProperty(pass);
        this.lvlUser = new SimpleStringProperty(lvlUser);
        this.email = new SimpleStringProperty(email);
    }

    // Métodos Getter y Setter para las propiedades
    public String getNUser() {
        return nUser.get();
    }

    public void setNUser(String nUser) {
        this.nUser.set(nUser);
    }

    public StringProperty nUserProperty() {
        return nUser;
    }

    public String getNUserReal() {
        return nUserReal.get();
    }

    public void setNUserReal(String nUserReal) {
        this.nUserReal.set(nUserReal);
    }

    public StringProperty nUserRealProperty() {
        return nUserReal;
    }

    public String getLnUser() {
        return lnUser.get();
    }

    public void setLnUser(String lnUser) {
        this.lnUser.set(lnUser);
    }

    public StringProperty lnUserProperty() {
        return lnUser;
    }

    public String getPass() {
        return pass.get();
    }

    public void setPass(String pass) {
        this.pass.set(pass);
    }

    public StringProperty passProperty() {
        return pass;
    }

    public String getLvlUser() {
        return lvlUser.get();
    }

    public void setLvlUser(String lvlUser) {
        this.lvlUser.set(lvlUser);
    }

    public StringProperty lvlUserProperty() {
        return lvlUser;
    }

    public String getEmail() {
        return email.get();
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public StringProperty emailProperty() {
        return email;
    }
}