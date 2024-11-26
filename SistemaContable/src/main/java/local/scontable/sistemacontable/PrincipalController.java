package local.scontable.sistemacontable;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import local.scontable.sistemacontable.Clases.CambioPanel;
import local.scontable.sistemacontable.ControlMantenimientos.MantenimientoUsuariosController;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class PrincipalController implements Initializable, CambioPanel {

    private PrincipalController panelPadre;
    public PrincipalController panelMenu;


    @FXML
    Button btn_mantenimientos, btn_logout, btn_procesos;
    @FXML
    AnchorPane anpane_padre, pn_menu;
    @FXML
    Label lbl_usuario;
    private String acceso;
    private MantenimientoUsuariosController access = new MantenimientoUsuariosController();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void changePanel_manteniminetos(){
        try {
            cMantenimientos("/Mantenimientos/MantenimientoUsuarios.fxml");
            access.loadMantenimientos(acceso);
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
    public void cMantenimientos(String fxml) throws IOException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxml));
            Pane rgpn = fxmlLoader.load();
            if (fxml.equals("/Mantenimientos/MantenimientoUsuarios.fxml")){
                access = fxmlLoader.getController();
            }
            Object controller = fxmlLoader.getController();
            if (controller instanceof CambioPanel) {
                ((CambioPanel) controller).setPanelPadre(this);
            }
            rgpn.getStylesheets().add(getClass().getResource("/Css/MenuPrincipal.css").toExternalForm());
            rgpn.getStylesheets().add(getClass().getResource("/Css/Mantenimientos.css").toExternalForm());
            CargarPHijo(rgpn);
        }catch (Exception ex){
            System.out.printf(String.valueOf(ex));
        }
    }

    private void CargarPHijo(Node PanelH){

        anpane_padre.getChildren().clear();
        anpane_padre.getChildren().add(PanelH);
    }
    @Override
    public void setPanelPadre(PrincipalController panelPadre) {
        this.panelPadre = panelPadre;
    }

    public void alType(String y){
        this.acceso = y;
        if(acceso.equals("1")){
            btn_procesos.setDisable(false);
        }
        else{
            btn_procesos.setDisable(true);
        }
    }

    public void displayUserName(String user){
        lbl_usuario.setText(user);
    }

    public void gotoMenu(){
        anpane_padre.getChildren().clear();
        anpane_padre.getChildren().add(pn_menu);
    }

    public void LogOut(){
        Stage stage = new Stage();
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(SistemaContableMain.class.getResource("/Login.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            scene.getStylesheets().add("Css/Login.css");
            stage.setTitle("R&P Asociados");
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();

            stage = (Stage) btn_logout.getScene().getWindow();
            stage.close();
        }catch (Exception ex){
            System.out.println(ex);

        }

    }
}



